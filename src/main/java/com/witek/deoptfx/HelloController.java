package com.witek.deoptfx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.util.logging.Level;
import java.util.logging.Logger;

public class HelloController {
    private static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    @FXML
    public void initialize(){
        LOGGER.log(Level.INFO,"Inicjalizacja Optymalizacji...");

    }
    public TextField F;
    public TextField CR;
    public TextField population;
    public Label bestEval;
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


    public void onStartOptButtonClick(ActionEvent actionEvent) {
    }

    public void onStopOptButtonClick(ActionEvent actionEvent) {
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
}