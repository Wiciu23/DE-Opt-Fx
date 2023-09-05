package com.witek.deoptfx.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.logging.*;

public class Optimization implements Runnable {
    private final ArrayList<ValueObserver> bestSolutionObservers = new ArrayList<>();
    private final ArrayList<ValueObserver> populationObservers = new ArrayList<>();

    private final ArrayList<ValueObserver> bestVectorObservers = new ArrayList<>();

    private final ArrayList<ValueObserver> isPauedObservers = new ArrayList<>();

    private volatile boolean isRunning = false;

    private volatile boolean isPaused = false;
    private Boolean isRandomDistribution = false;

    public boolean isPaused() {
        return isPaused;
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
    }

    public boolean isRandomModule() {
        return isRandomModule;
    }

    public void setRandomModule(boolean randomModule) {
        isRandomModule = randomModule;
    }

    private boolean isRandomModule = false;
    private static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private double bestSolution = Double.MAX_VALUE;

    public VectorOperations getBestVector() {
        return bestVector;
    }

    private boolean isInRange(int index, double currentCoordinate){
        return currentCoordinate >= optimizationParameters[index].getLowerBound() && currentCoordinate <= optimizationParameters[index].getUpperBound();
    }

    private boolean isInOptimize(int index){
        return optimizationParameters[index].isOptimize();
    }

    private VectorOperations bestVector;
    private OptimizationFunction objectiveFunction;

    public OptimizationParameter[] getOptimizationParameters() {
        return optimizationParameters;
    }

    private OptimizationParameter[] optimizationParameters;
    private VectorLockable[] population;

    public void setTargetEpochCount(int targetEpochCount) {
        this.targetEpochCount = targetEpochCount;
    }
    public void addSolutionObserver(ValueObserver observer){
        this.bestSolutionObservers.add(observer);
    }

    public void addPopulationObserver(ValueObserver observer){
        this.populationObservers.add(observer);
    }

    public void addBestVectorObserver(ValueObserver observer) {this.bestVectorObservers.add(observer);}

    public void notifyBestVectorObservers() throws IOException {
        for (ValueObserver observer: bestVectorObservers) {
            observer.update();
        }
    }

    public void notifySolutionObservers() throws IOException {
        for (ValueObserver observer: bestSolutionObservers) {
            observer.update();
        }
    }

    public void notifyPopulationObservers() throws IOException {
        for (ValueObserver observer: populationObservers) {
            observer.update();
        }
    }

    private Double targetErrorValue = 0.1;

    private Integer targetEpochCount;

    private double F; //scaling factor

    private double CR; //crossover constatn

    public double getF() {
        return F;
    }

    public void setF(double f) {
        F = f;
    }

    public double getCR() {
        return CR;
    }

    public void setCR(double CR) {
        this.CR = CR;
    }

    public void setRunning(boolean b) {
        this.isRunning = b;
    }
    public boolean isRunning() {
        return isRunning;
    }

    public void setTargetErrorValue(double targetErrorValue) {
        this.targetErrorValue = targetErrorValue;
    }

    public VectorLockable[] getPopulation() {
        return population;
    }

    public void setPopulation(VectorLockable[] population) {
        this.population = population;
    }
    //offspring + mutacje będa tworzone w iteracjach optymalizacji
    public Optimization(OptimizationFunction objectiveFunction, VectorLockable[] population, OptimizationParameter[] optimizationParameters) {
        this.objectiveFunction = objectiveFunction;
        this.population = population;
    }

    public Optimization() {
    }

    public void setObjectiveFunction(OptimizationFunction objectiveFunction) {
        this.objectiveFunction = objectiveFunction;
    }

    public Optimization(OptimizationParameter[] optimizationParameters, int population, OptimizationFunction objectiveFunction, double scalingFactor, double crossOverConstant) {
        this.optimizationParameters = optimizationParameters;
        generatePopulation(population);
        this.objectiveFunction = objectiveFunction;
        this.F = scalingFactor;
        this.CR = crossOverConstant;
    }

    public double getBestSolution() {
        return bestSolution;
    }

    @Override
    public void run(){
        double bestOptSolution = bestSolution;
        int counter = 0;
        //DODAĆ TUTAJ WARUNEK ZŁOŻONY JAKO FUNKCJA, KTÓRA ZWRACA BOOLEAN
        //while((bestOptSolution > targetErrorValue || targetEpochCount < counter) && isRunning) {
        while(shouldStop(bestOptSolution,counter)) {
            if(isPaused) break;
            {
                    VectorOperations[] mutated = new VectorOperations[population.length];
                    for (int i = 0; i < population.length; i++) {
                        VectorLockable ancestor = population[i];
                        int jRand = (int) rand(0, ancestor.length());
                        VectorOperations[] randoms = drawUniqueIndividuals(3, ancestor);
                        VectorOperations r1 = randoms[0];
                        VectorOperations r2 = randoms[1];
                        VectorOperations r3 = randoms[2];
                        mutated[i] = mutate(ancestor, F, CR, jRand, r1, r2, r3);
                        replace(population[i], mutated[i]);
                    }
                    bestOptSolution = foundBestOptSolution(population);
                    counter++;
                    LOGGER.log(Level.INFO, "Iteration: " + counter);
                try {
                    notifySolutionObservers();
                    notifyPopulationObservers();
                    notifyBestVectorObservers();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                    LOGGER.log(Level.INFO, "Best global vector: " + bestVector + " FUNCTION VALUE: " + bestOptSolution);

                }
        }
    }
    boolean shouldStop(Double currentErrorValue, Integer currentEpochCount){
        if(!this.isRunning){
            return false;
        }
        boolean targetErrorValuePart = this.targetErrorValue != null && currentErrorValue > this.targetErrorValue;
        boolean targetEpochCountPart = this.targetEpochCount != null && currentEpochCount < this.targetEpochCount;

        return targetErrorValuePart || targetEpochCountPart;
    }
    private double foundBestOptSolution(VectorOperations[] vectors){
        for (VectorOperations vector: vectors) {
            double valueOfObjFunction = objectiveFunction.optimize(vector.getCordinates());
            if(this.bestSolution > valueOfObjFunction){
                this.bestSolution = valueOfObjFunction;
                this.bestVector = vector;
                LOGGER.log(Level.INFO,"----| Best founded solution: " + bestSolution + " |----");
                LOGGER.log(Level.INFO,"Best founded vector: " + vector);
            }
        }

        return bestSolution;
    }
    public void generatePopulation(int amount){
        if(optimizationParameters == null) throw new NullPointerException("Wymagany jest obiekt zmienności parametrów optymalizacji");
        population = new VectorLockable[amount];
        for(int i = 0; i < amount ; i++){
            population[i] = new VectorLockable(new Vector(generateRandomArrayOfDoubles(optimizationParameters)),optimizationParameters);
            LOGGER.log(Level.INFO, ((i + 1) + " of " + amount + " population generated"));
        }
    }
    private double rand(double lowerBound, double upperBound){
        Random rand = new Random();
        return lowerBound + (upperBound - lowerBound)*(rand.nextDouble());
    }
    private double[] generateRandomArrayOfDoubles(OptimizationParameter[] parameters){
        double[] array = new double[parameters.length];
        for(int i = 0 ; i < array.length ; i++){
            array[i] = rand(parameters[i].getLowerBound(), parameters[i].getUpperBound());
        }
        return array;
    }
    private VectorOperations[] drawUniqueIndividuals(int amountOfIndividuals, VectorOperations uniqueIndividual){
        if(amountOfIndividuals >= population.length){
            throw new NegativeArraySizeException("Population should be bigger to draw " + amountOfIndividuals + " unique individuals");
        }
        ArrayList<VectorOperations> individuals = new ArrayList<>(Arrays.asList(this.population));
        int endIndex = individuals.size() - 1 ;
        VectorOperations[] drawIndividuals = new VectorOperations[amountOfIndividuals];
        int correctDrawn = 0;
        int i = 0;
        while(correctDrawn < amountOfIndividuals){
            int index = (int) rand(0,endIndex);
            if( !individuals.get(index).equals(uniqueIndividual)){
                drawIndividuals[i] = individuals.get(index);
                individuals.remove(index);
                i++;
                correctDrawn++;
            }else {
                individuals.remove(index);
            }
            endIndex = individuals.size() - 1;
        }
        return drawIndividuals;
    }
    //SPRAWDZIĆ CZY TUTAJ LEPIEJ ZWRACAĆ TYP INTERFEJSU CZY VECTORLOCKABLE?!
    public VectorOperations mutate(VectorOperations vector, double F ,double CR, int jRand, VectorOperations r1, VectorOperations r2, VectorOperations r3){
        if(vector.length() != r1.length() && vector.length() != r2.length() && vector.length() != r3.length()){
            throw new NegativeArraySizeException("Vectors have to be the same lenght!");
        }
        double[] coordinates = vector.getCordinates();
        double[] mutated = vector.getCordinates().clone();
        Random rand = new Random();
        VectorOperations mutVector;
        for(int i = 0 ; i < coordinates.length ; i++) {
            double muatedCoordinate;
            if (((rand.nextDouble() < CR) || (i == jRand)) && isInOptimize(i)) { //CLASSIC MUTATION (CROSS-OVER)
                muatedCoordinate = r3.getVectorComponent(i) + (F * (r1.getVectorComponent(i) - r2.getVectorComponent(i)));
                if(isInRange(i,muatedCoordinate)){
                    mutated[i] = muatedCoordinate;
                } else if (isRandomModule) { //RANDOMLY SHUFFLE IF MUTATE COORDINATE IS OUT OF RANGE
                    LOGGER.log(Level.INFO, String.format("COORDINTAE IS OUT OF SCOPE: %f.0 [VECTOR %d]", muatedCoordinate, i));
                    double savedMutatedCoordinate = mutated[i];
                    muatedCoordinate = rand(optimizationParameters[i].getLowerBound(), optimizationParameters[i].getUpperBound());
                    mutated[i] = muatedCoordinate;
                    checkIfMutatedIsBetter(coordinates, mutated, i, muatedCoordinate, savedMutatedCoordinate);
                }
            } if(isRandomDistribution && isInOptimize(i)){ //INSTANT RANDOM DISTRIBUTION
                double savedMutatedCoordinate = mutated[i];
                muatedCoordinate = rand(optimizationParameters[i].getLowerBound(), optimizationParameters[i].getUpperBound());
                checkIfMutatedIsBetter(coordinates, mutated, i, muatedCoordinate, savedMutatedCoordinate);
            }
        }
        mutVector = new VectorLockable(new Vector(mutated), this.optimizationParameters);
        //mutVector.set(mutated);
        return mutVector;
    }
    private void checkIfMutatedIsBetter(double[] coordinates, double[] mutated, int i, double muatedCoordinate, double savedMutatedCoordinate) {
        if (objectiveFunction.optimize(mutated) < objectiveFunction.optimize(coordinates)) {
            mutated[i] = muatedCoordinate;
            LOGGER.log(Level.INFO, String.format("RANDOMLY SHUFFELED COORDIANTE IS BETTER %f.0 better < %f.0 :  [Coordinate %d]", coordinates[i], mutated[i], i));
        } else {
            mutated[i] = savedMutatedCoordinate;
            LOGGER.log(Level.INFO, String.format("RANDOMLY SHUFFELED COORDIANTE IS WORSE %f.0 > than %f.0 :  [Coordinate %d]", coordinates[i], muatedCoordinate, i));
        }
    }
    void replace(VectorOperations vector, VectorOperations mutated){
        double[] vectorCoordinates = vector.getCordinates();
        double[] mutatedCoordinates = mutated.getCordinates();
        //LOGGER.log(Level.INFO,"Vector: " + Arrays.toString(vectorCoordinates));
        //LOGGER.log(Level.INFO,"Mutated Vector: " + Arrays.toString(mutatedCoordinates));
        double evalMutVec = objectiveFunction.optimize(mutatedCoordinates);
        double evalVec = objectiveFunction.optimize(vectorCoordinates);
        if(evalMutVec < evalVec || Double.isNaN(evalVec)){
            vector.set(mutatedCoordinates);
            LOGGER.log(Level.INFO, String.format("%-60s %E < %E","Vector has been swapped, better solution was founded: ",evalMutVec, evalVec ));
        }else LOGGER.log(Level.INFO, String.format("%-60s %E > %E","Vector has not been swapped, better solution was not founded: ", evalMutVec, evalVec ));
    }
    public void setRandomDistributionModule(boolean b) {
        this.isRandomDistribution = b;
    }
}
