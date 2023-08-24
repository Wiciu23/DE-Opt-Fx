package com.witek.deoptfx;

import com.witek.deoptfx.model.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HelloController {
    private static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    public TextField epochsStopCondition;
    public TextField solutionStopCondition;
    public VBox dynamicGraphs;
    public TextField perplexity;
    public TextField eta;
    public TextField iterations;
    public GridPane buttonsGridPane;
    public Button pauseButton;
    private ComboBox<OptimizationFunction> comboBoxObjFunc;
    ExecutorService executorService = Executors.newCachedThreadPool();
    ArrayList<OptimizationFunction> functions = new ArrayList<>();
    public VBox dynamicFields;
    public TextField F;
    public TextField CR;
    public TextField population;
    public Label bestEval;
    Optimization optimization;
    ScatterChart<Number,Number> tsnePlot;
    FunctionPlot functionPlot;

    private GridPane objFuncPlots;
    
    Color[] chartLegend;

    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @FXML
    public Button startButton;

    @FXML
    public Button stopButton;

    @FXML
    public void initialize(){
        LOGGER.log(Level.INFO,"Inicjalizacja Optymalizacji...");
        //dodanie funkcji optymalizacji do listy
        functions.add(OptimizeFunctionFactory.getOptimizeFunction(1));
        //generowanie selectboxa na podstawie listy
        generateFunctionDropList();
        stopButton.setDisable(true);
        //podpiecie wykresu do optymalizacji
        //generatePopulationPlot();
        buttonsGridPane.add(startButton,1,0);
        buttonsGridPane.add(stopButton,2,0);
        buttonsGridPane.add(pauseButton,3,0);
        addRandomModuleCheckBox();
        addTotalRandomCheckBox();
    }

    private void addRandomModuleCheckBox() {
        CheckBox checkBox = new CheckBox("Random module");
        checkBox.setSelected(false);
        checkBox.setOnAction(event ->{
            if(checkBox.isSelected()){
                optimization.setRandomModule(true);
                LOGGER.log(Level.INFO,"RANDOM MODULE TURN ON");
            }else {
                optimization.setRandomModule(false);
                LOGGER.log(Level.INFO,"RANDOM MODULE TURN OFF");
            }
        });
        buttonsGridPane.add(checkBox,4,0);
    }

    private void addTotalRandomCheckBox() {
        CheckBox checkBox = new CheckBox("Random distribution module");
        checkBox.setSelected(false);
        checkBox.setOnAction(event ->{
            if(checkBox.isSelected()){
                optimization.setRandomDistributionModule(true);
                LOGGER.log(Level.INFO,"RANDOM MODULE TURN ON");
            }else {
                optimization.setRandomDistributionModule(false);
                LOGGER.log(Level.INFO,"RANDOM MODULE TURN OFF");
            }
        });
        buttonsGridPane.add(checkBox,5,0);
    }

    public void addCoefficientCheckBoxes(){
        OptimizationParameter[] parameters = optimization.getOptimizationParameters();
        GridPane coefficients = new GridPane();
        for (int i = 0; i < parameters.length ; i ++) {
            OptimizationParameter parameter = parameters[i];
            CheckBox checkBox = new CheckBox("param" + i);
            checkBox.setSelected(true);
            int finalI = i;
            checkBox.setOnAction(event ->{
                if(checkBox.isSelected()){
                    parameter.setOptimize(true);
                    System.out.println("param" + finalI + " został właczony");
                }else {
                    parameter.setOptimize(false);
                    System.out.println("param" + finalI + " został wyłaczony");
                }
            });
            coefficients.add(checkBox,i,0);
        }
        Platform.runLater(()->{
            dynamicFields.getChildren().add(coefficients);
        });
    }
    private void generatePopulationPlot() {
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("X Axis");
        yAxis.setLabel("Y Axis");

        int perpexity = Integer.parseInt(this.perplexity.getText());
        int eta = Integer.parseInt(this.eta.getText());
        int iterations = Integer.parseInt(this.iterations.getText());

        // Create scatter chart
        this.tsnePlot = new ScatterChart<>(xAxis, yAxis);
        tsnePlot.setTitle("t-SNE vector dim reduction");

        dynamicGraphs.getChildren().add(this.tsnePlot);
        optimization.addPopulationObserver(()->{
            VectorOperations[] vector = optimization.getPopulation();
            double[][] coordinates = TsneVector.generateTsneCoordinates(vector,perpexity,eta,iterations);
            updatePopulationPlot(coordinates);
        });
    }
    private void updatePopulationPlot(double[][] coordinates){
        if(this.tsnePlot.getData().isEmpty()){ //First initialization of series
            prepareLegendOfPlot(coordinates);
            for(int i = 0 ; i < coordinates.length ; i++){
                double x = coordinates[i][0];
                double y = coordinates[i][1];
                XYChart.Series<Number,Number> seria = new XYChart.Series<Number,Number>();
                seria.setName(String.format("Unit %d", i + 1));
                XYChart.Data<Number,Number> data = new XYChart.Data<>(x,y);
                data.setNode(new Circle(3, chartLegend[i]));
                seria.getData().add(data);

                Platform.runLater(()->{
                    this.tsnePlot.getData().add(seria);
                });
            }
        }else{
            if(coordinates.length != tsnePlot.getData().size()){ //Standars way of adding values to series
                throw new NegativeArraySizeException("Długośc serii nie odpowiada długości tablicy współrzednych");
            }
            for(int i = 0 ; i < coordinates.length; i++){
                double x = coordinates[i][0];
                double y = coordinates[i][1];
                XYChart.Data<Number,Number> data = new XYChart.Data<>(x,y);
                //Circle circle = new Circle(3);
                //data.setNode(circle);
                data.setNode(new Circle(3, chartLegend[i]));
                int finalI = i;
                Platform.runLater(()->{
                    tsnePlot.getData().get(finalI).getData().add(data);
                });

            }
        }
    }

    private void prepareLegendOfPlot(double[][] coordinates) {
        this.chartLegend = new Color[coordinates.length];
        for (int j = 0; j < chartLegend.length; j++) {
            double hue = (double) j / chartLegend.length; // Odcień koloru
            chartLegend[j] = Color.hsb(hue * 360, 1.0, 1.0); // Tworzenie koloru na podstawie odcienia
        }
    }
    private void generateFunctionDropList() {
        ComboBox<OptimizationFunction> comboBoxObjFunc = new ComboBox<>();
        this.comboBoxObjFunc = comboBoxObjFunc;
        comboBoxObjFunc.getItems().addAll(this.functions);
        dynamicFields.getChildren().add(comboBoxObjFunc);
    }

    public void onStartOptButtonClick(ActionEvent actionEvent) {
        if (executorService.isShutdown()) {
            LOGGER.log(Level.INFO,"RERUNNING EXECUTOR");
            executorService = Executors.newCachedThreadPool();
        }
        startButton.setDisable(true);
        stopButton.setDisable(false);
        int population =  Integer.parseInt(this.population.getText());
        double F = Double.parseDouble(this.F.getText());
        double CR = Double.parseDouble(this.CR.getText());
        int epochsStop = Integer.parseInt(epochsStopCondition.getText());
        double solutionStop = Double.parseDouble(solutionStopCondition.getText());
        OptimizationParameter[] objParams = OptimizeParametersFactory.getOptimizeParameters(1);
        OptimizationFunction objFunction = comboBoxObjFunc.getValue();
        optimization = new Optimization(objParams,population,objFunction,F,CR);
        optimization.setTargetEpochCount(epochsStop);
        optimization.setTargetErrorValue(solutionStop);
        LOGGER.log(Level.INFO, "Parametry zostały wczytane, nastepuje uruchomienie optymalizacji");
        LOGGER.log(Level.INFO, "Przygotowanie wykresow funkcji...");
        setupObjectiveFunctionPlot();
        optimization.setRunning(true);
        executorService.execute(optimization);
        LOGGER.log(Level.INFO, "Optymalizacja została uruchomiona");

        //generatePopulationPlot();
        //generatePlots();
        setupBestSolutionText();
        addCoefficientCheckBoxes();
        optimization.addBestVectorObserver(()-> {
            //double[] vector = {1.2521226511541942E-4, 21186.985087346766, 94376.12424881992, 2.2558555594080133E9, 128881.08375253416, 2.4822558526892475, 0.0, 0.452, 0.20307949021769203, 0.409, 0.0, 4.2E8, 0.07618196420073034};
            //functionPlot.updatePlots(vector);
            functionPlot.updatePlots(optimization.getBestVector().getCordinates());

        });
    }

    private void setupObjectiveFunctionPlot() {
        if (comboBoxObjFunc.getValue().toString().equalsIgnoreCase("Objective function of dislocation density")) {
            FunctionMgr objFunction = (FunctionMgr) comboBoxObjFunc.getValue();
            FunctionPlot functionPlot = new FunctionPlot(objFunction.getDataTable());
            this.functionPlot = functionPlot;
            functionPlot.initialize();
            Platform.runLater(()->{
                dynamicGraphs.getChildren().add(functionPlot.getGrid());
            });

        }
    }

    private void setupBestSolutionText() {
        this.optimization.addSolutionObserver(() ->{
            double bestSolution = this.optimization.getBestSolution();
            Platform.runLater(()->{
                bestEval.setText(Double.toString(bestSolution));
            });
        });
    }

    private void generatePlots() {
        optimization.addSolutionObserver(()->{
            double bestSoluton = optimization.getBestSolution();
            System.out.printf("UWAGA NOWE ROZWIAZANIE JAKO OBSERWATOR: %f%n", bestSoluton);
        });
    }

    public void onStopOptButtonClick(ActionEvent actionEvent) {
        optimization.setRunning(false);
        LOGGER.log(Level.INFO,"Optymalizacja zostaje zatrzymana");
        startButton.setDisable(false);
        stopButton.setDisable(true);
        if (executorService.isShutdown()) {
            LOGGER.log(Level.INFO,"EXECUTOR SHUTDOWNED");
            startButton.setDisable(false);
            stopButton.setDisable(true);
        }
    }
    public void onPauseOptButtonClick(ActionEvent actionEvent) {
        if(!optimization.isPaused()){
            optimization.setPaused(true);
            Platform.runLater(()->{
                pauseButton.setText("Optimization Paused");
            });
            LOGGER.log(Level.INFO,"Optymalizacja zostaje spauzowana");
        }else{
            optimization.setPaused(false);
            executorService.execute(optimization);
            Platform.runLater(()->{
                pauseButton.setText("Pause Optimizaton");
            });
            LOGGER.log(Level.INFO,"Optymalizacja zostaje wznowiona");
        }
    }

    public void onCRMouseClicked(MouseEvent mouseEvent) {
        String defaultText = "CR (cross-over constant)";
        if(CR.getText().equals(defaultText)){
            CR.clear();
        }
    }
    public void onCRMouseExited(MouseEvent mouseEvent) {
        String defaultText = "CR (cross-over constant)";
        if(CR.getText().isEmpty()){
            CR.setText(defaultText);
        }
    }
    public void onFMouseClicked(MouseEvent mouseEvent) {
        String defaultText = "F (scaling factor)";
        if(F.getText().equals(defaultText)){
            F.clear();
        }
    }
    public void onFMouseExited(MouseEvent mouseEvent) {
        String defaultText = "F (scaling factor)";
        if(F.getText().isEmpty()){
            F.setText(defaultText);
        }
    }
    public void onPopulationMouseClicked(MouseEvent mouseEvent) {
        String defaultText = "Population Size";
        if(population.getText().equals(defaultText)){
            population.clear();
        }
    }
    public void onPopulationMouseExited(MouseEvent mouseEvent) {
        String defaultText = "Population Size";
        if(population.getText().isEmpty()){
            population.setText(defaultText);
        }
    }

    public void onEpochsStopConditionMouseClicked(MouseEvent mouseEvent) {
        String defaultText = "Epochs stop condition";
        if(epochsStopCondition.getText().equals(defaultText)){
            epochsStopCondition.clear();
        }
    }

    public void onEpochsStopConditionMouseExited(MouseEvent mouseEvent) {
        String defaultText = "Epochs stop condition";
        if(epochsStopCondition.getText().isEmpty()){
            epochsStopCondition.setText(defaultText);
        }
    }

    public void onSolutionStopConditionMouseClicked(MouseEvent mouseEvent) {
        String defaultText = "Value obj. fun stop condition";
        if(solutionStopCondition.getText().equals(defaultText)){
            solutionStopCondition.clear();
        }
    }

    public void onSolutionStopConditionMouseExited(MouseEvent mouseEvent) {
        String defaultText = "Value obj. fun stop condition";
        if(solutionStopCondition.getText().isEmpty()){
            solutionStopCondition.setText(defaultText);
        }
    }
}