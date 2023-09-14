package com.witek.deoptfx.automate;

import com.witek.deoptfx.model.*;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.logging.Level;

public class Automat {
    Optimization optimization;
    ArrayList<Double> CR = new ArrayList<>();
    ArrayList<Double> F = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        OptimizationParameter[] objParams = OptimizeParametersFactory.getOptimizeParameters("Rastrigin");
        OptimizationFunction objFunction = OptimizeFunctionFactory.getOptimizeFunction(3);
        //Optimization opt = new Optimization(objParams, 15, objFunction, 0.4, 0.6);


        /*System.out.println(String.format("==== F = %f | CR = %f ====", 0.6, 0.75));
        opt.setF(0.6);
        opt.setCR(0.75);
        opt.setTargetErrorValue(0.01);
        opt.setTargetEpochCount(1000);
        opt.setRunning(true);
        opt.run();*/
            for (double i = 0.0; i < 1.0; i += 0.1) {
                for (double j = 0.0; j < 1.0; j += 0.1) {
                    System.out.println(String.format("==== F = %f | CR = %f ====", i, j));
                    double F = i;
                    double CR = j;
                    Optimization opt = new Optimization(objParams, 10 ,objFunction ,F ,CR);
                    prepareDataFile(opt);
                    opt.setTargetErrorValue(0.01);
                    opt.setTargetEpochCount(1000);
                    opt.setRunning(true);
                    opt.run();
                    opt.setCounter(0);
                }
            }
    }

    private static void prepareDataFile(Optimization opt) throws IOException {

        String initialRow = "bestEval,BestVector";
        String fileName = String.format("IterationsOfFunction");
        CSVWriter writer = new CSVWriter(fileName);
        //writer.write(initialRow);
        /*opt.addSolutionObserver(()->{
            double bestEval = opt.getBestSolution();
            String bestVector = opt.getBestVector().toString();
            String row = String.format("%.5f,%s",bestEval,bestVector);
            writer.write(row);
        });*/
        opt.addCounterObserver(()->{
            double CR = opt.getCR();
            double F = opt.getF();
            int iterations = opt.getCounter();
            String row = String.format("CR,%f,F,%f,Iterations,%d",F,CR,iterations);
            writer.write(row);
        });
    }
}


