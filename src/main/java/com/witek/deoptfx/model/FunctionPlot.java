package com.witek.deoptfx.model;

import javafx.application.Platform;
import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FunctionPlot {
    private static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    ObjectProperties[] dataTable;
    GridPane grid = new GridPane();

    ArrayList<LineChart> charts = new ArrayList<>();
    public void updatePlots(double[] a){
        System.out.println("TEST");
    }

    public FunctionPlot(ObjectProperties[] dataTable) {
        this.dataTable = dataTable;
    }

    public GridPane getGrid() {
        return grid;
    }

    public void initialize(){
        for (ObjectProperties data: dataTable) {
            NumberAxis xAxis = new NumberAxis();
            NumberAxis yAxis = new NumberAxis();
            xAxis.setLabel("Time [s]");
            yAxis.setLabel("Density");
            String tile = String.format("Dens. dislocation, Temp %.0f, dot_epsilon %.0f", data.temperature, data.dot_epsilon);
            LineChart<Number,Number> linePlot = new LineChart<>(xAxis,yAxis);
            linePlot.setTitle(tile);
            charts.add(linePlot);
            XYChart.Series<Number,Number> seria = new XYChart.Series<>();
            String seriesName = String.format("T%.0fEps%.0f",data.temperature, data.dot_epsilon);
            seria.setName(seriesName);
            for(int i = 0; i < data.epsilon.length-10; i+=50){
                double x = data.epsilon[i];
                double y = data.sigma[i];
                XYChart.Data<Number, Number> point = new XYChart.Data<>(x, y);
                seria.getData().add(point);
            }
            seria.getNode().setStr
            linePlot.getData().add(seria);
            System.out.println("DUPA");
        }
        addPlotsToGrid();
        LOGGER.log(Level.INFO,"Inicjalizacja wykresów zakończona...");
    }
    private void addPlotsToGrid(){
        int row = 0;
        int col = 0;
        for (LineChart chart: charts) {
            int finalCol = col;
            int finalRow = row;
            Platform.runLater(()->{
                grid.add(chart, finalCol, finalRow);
            });
            col++;
            if(col >2){
                col = 0;
                row++;
            }
        }
    }
}

