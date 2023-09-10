package com.witek.deoptfx.model;

public class Rosenbrocks implements OptimizationFunction{

    @Override
    public double optimize(double[] arguments) {
        double sum = 0.0;
        for (int i = 0; i < arguments.length - 1; i++) {
            double xi = arguments[i];
            double xiPlus1 = arguments[i + 1];
            double term1 = xiPlus1 - xi * xi;
            double term2 = 1.0 - xi;
            sum += 100.0 * term1 * term1 + term2 * term2;
        }
        return sum;
    }

    @Override
    public String toString(){
        return "RosenBrock";
    }
}
