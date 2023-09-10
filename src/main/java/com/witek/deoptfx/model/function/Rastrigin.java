package com.witek.deoptfx.model.function;

import com.witek.deoptfx.model.OptimizationFunction;

public class Rastrigin implements OptimizationFunction {
    @Override
    public double optimize(double[] arguments) {
        double sum = 0;
        for(int i = 0; i < arguments.length ; i++){
            double x = arguments[i];
            double phrase = Math.pow(x,2) - 10*Math.cos(2*Math.PI*x) + 10;
            sum += phrase;
        }
        return sum;
    }

    @Override
    public String toString(){
        return "Rastrigin";
    }
}
