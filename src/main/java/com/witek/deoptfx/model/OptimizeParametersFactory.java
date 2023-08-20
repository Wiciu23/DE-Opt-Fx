package com.witek.deoptfx.model;

public class OptimizeParametersFactory {
    public static OptimizationParameter[] getOptimizeParameters(int type) {
        if (type == 1) {
            OptimizationParameter[] parameters = new OptimizationParameter[13];
            double[][] ranges = {
                    {0.05 * 0.001, 0.15 * 0.001},//1
                    {15000, 22000},//2
                    {50E3, 100E3},//3
                    {3E10 * 0.01, 3E10 * 0.09},//4
                    {1E3 * 100.0, 1E3 * 150.0},//5
                    {1.5, 2.5},//6
                    {1,0, 10.0},//7
                    {0.0, 10.0},//8
                    {0.0, 10.0},//{0.05, 0.25},//9
                    {0.0, 1.0},//10
                    {1E13 * 0.0, 1E13 * 1.0},//11
                    {1E13 * 0.0, 1E13 * 0.1},//{1E13 * 0.00001, 1E13 * 0.00009},
                    {0.01, 0.09}
            };
            for (int i = 0; i < parameters.length; i++) {
                double lowerBound = ranges[i][0];
                double upperBound = ranges[i][1];
                parameters[i] = new OptimizationParameter(lowerBound,upperBound);
            }
            return parameters;
        } else
            return null;

    }
}

