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
    
    Circle[] chartLegend;

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
    }
    private void generatePopulationPlot() {
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("X Axis");
        yAxis.setLabel("Y Axis");

        // Create scatter chart
        this.tsnePlot = new ScatterChart<>(xAxis, yAxis);
        tsnePlot.setTitle("Dynamic Scatter Plot Example");

        dynamicGraphs.getChildren().add(this.tsnePlot);
        optimization.addPopulationObserver(()->{
            VectorOperations[] vector = optimization.getPopulation();
            double[][] coordinates = TsneVector.generateTsneCoordinates(vector);
            updatePopulationPlot(coordinates);
        });
    }
    private void updatePopulationPlot(double[][] coordinates){
        if(this.tsnePlot.getData().isEmpty()){ //First initialization of series
            prepareLegendOfPlot(coordinates);
            int i = 1;
            for (double[] coordinate: coordinates){
                double x = coordinate[0];
                double y = coordinate[1];
                XYChart.Series<Number,Number> seria = new XYChart.Series<Number,Number>();
                seria.setName(String.format("Unit %d", i));
                XYChart.Data<Number,Number> data = new XYChart.Data<>(x,y);

                data.setNode(chartLegend[i-1]);
                seria.getData().add(data);
                Platform.runLater(()->{
                    this.tsnePlot.getData().add(seria);
                });
                i++;
            }
        }else{ //
            if(coordinates.length != tsnePlot.getData().size()){ //Standars way of adding values to series
                throw new NegativeArraySizeException("Długośc serii nie odpowiada długości tablicy współrzednych");
            }
            for(int i = 0 ; i < coordinates.length; i++){
                double x = coordinates[i][0];
                double y = coordinates[i][1];
                XYChart.Data<Number,Number> data = new XYChart.Data<>(x,y);
                //Circle circle = new Circle(3);
                data.setNode(chartLegend[i]);
                int finalI = i;
                Platform.runLater(()->{
                    tsnePlot.getData().get(finalI).getData().add(data);
                });

            }
        }
    }

    private void prepareLegendOfPlot(double[][] coordinates) {
        Color[] colors = new Color[coordinates.length];
        this.chartLegend = new Circle[coordinates.length];
        for (int j = 0; j < colors.length; j++) {
            double hue = (double) j / colors.length; // Odcień koloru
            colors[j] = Color.hsb(hue * 360, 1.0, 1.0); // Tworzenie koloru na podstawie odcienia
            chartLegend[j] = new Circle(3,colors[j]);
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
        optimization.setRunning(true);
        executorService.execute(optimization);
        LOGGER.log(Level.INFO, "Optymalizacja została uruchomiona");
        generatePopulationPlot();
        //generatePlots();
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