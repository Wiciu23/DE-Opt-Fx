package com.witek.deoptfx.model;
import java.security.SecureRandom;
import java.util.Random;
import java.util.logging.*;

public class Optimization {
    private static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private double bestValue = Double.MAX_VALUE;
    private OptimizationFunction objectiveFunction;
    private OptimizationParameter[] optimizationParameters;
    private Vector[] population;
    //offspring + mutacje będa tworzone w iteracjach optymalizacji
    public Optimization(OptimizationFunction objectiveFunction, Vector[] population, OptimizationParameter[] optimizationParameters) {
        this.objectiveFunction = objectiveFunction;
        this.population = population;
    }

    public void setObjectiveFunction(OptimizationFunction objectiveFunction) {
        this.objectiveFunction = objectiveFunction;
    }

    public Optimization(OptimizationParameter[] optimizationParameters, int population) {
        this.optimizationParameters = optimizationParameters;
        generatePopulation(population);
    }

    public void generatePopulation(int amount){
        if(optimizationParameters != null) throw new NullPointerException("Wymagany jest obiekt zmienności parametrów optymalizacji");
        population = new Vector[amount];
        for(int i = 0; i < amount ; i++){
            assert optimizationParameters != null; //DO SPRAWDZENIA
            population[i] = new Vector(generateRandomArrayOfDoubles(optimizationParameters));
            LOGGER.log(Level.INFO, ((i + 1) + " " + of + " " + amount + " population generated"));
        }
    }

    private double rand(double lowerBound, double upperBound){
        Random rand = new Random();
        return lowerBound + upperBound*(rand.nextDouble());
    }
    private double[] generateRandomArrayOfDoubles(OptimizationParameter[] parameters){
        double[] array = new double[parameters.length];
        for(int i = 0 ; i < array.length ; i++){
            array[i] = rand(parameters[i].getLowerBound(), parameters[i].getUpperBound());
        }
        return array;
    }
}
