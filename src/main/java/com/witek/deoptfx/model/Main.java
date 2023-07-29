package com.witek.deoptfx.model;
import java.io.IOException;
import java.util.logging.*;

public class Main {

    private static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    public static void main(String[] args){
        try {
            FileHandler fileHandler = new FileHandler("app.log");
            LOGGER.addHandler(fileHandler);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Nie udało się skonfigurować zapisu do pliku.", e);
        }
        LOGGER.log(Level.INFO,"Log test");


        try{
            //tworzenie obiektu optymalizacji
        }catch (Exception e){
            e.printStackTrace();
        }
    }




    private void startOptimization(){

    }
}
