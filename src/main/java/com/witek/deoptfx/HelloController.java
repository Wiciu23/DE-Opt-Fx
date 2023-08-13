package com.witek.deoptfx;

import com.witek.deoptfx.model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
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

    private ComboBox<OptimizationFunction> comboBoxObjFunc;
    ExecutorService executorService = Executors.newCachedThreadPool();
    ArrayList<OptimizationFunction> functions = new ArrayList<>();
    public VBox dynamicFields;
    public TextField F;
    public TextField CR;
    public TextField population;
    public Label bestEval;
    @FXML
    private Label welcomeText;
    Optimization optimization;

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
        double epochsStop = Double.parseDouble(epochsStopCondition.getText());
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
    }

    public void onStopOptButtonClick(ActionEvent actionEvent) {
        optimization.setRunning(false);
        LOGGER.log(Level.INFO,"Optymalizacja zostaje zatrzymana");
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