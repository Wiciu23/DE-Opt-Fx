package com.witek.deoptfx.model;
import java.io.IOException;
import java.util.logging.*;

public class Main {

    private static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    public static void main(String[] args){
        Optimization optimization;
        OptimizationParameter[] optParams = OptimizeParametersFactory.getOptimizeParameters("Objective function of dislocation density");
        OptimizationFunction optFunction = OptimizeFunctionFactory.getOptimizeFunction(1);

        try {
            FileHandler fileHandler = new FileHandler("app.log");
            LOGGER.addHandler(fileHandler);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Nie udało się skonfigurować zapisu do pliku.", e);
        }
        LOGGER.log(Level.INFO,"Log test");


        try{
            optimization = new Optimization(optParams,10,optFunction, 0.75, 0.5 );
            optimization.setTargetErrorValue(0.001);
           //optimization.generatePopulation(5);
            optimization.run();
            LOGGER.log(Level.INFO,"Optymalizacja została zakończona");
        }catch (Exception e){
            e.printStackTrace();
        }
    }




    private void startOptimization(){

    }
}
