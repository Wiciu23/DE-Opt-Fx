package com.witek.deoptfx.model.function;

import com.witek.deoptfx.model.OptimizationFunction;

public class Ackley implements OptimizationFunction {
    @Override
    public double optimize(double[] arguments) {
        double phrase = 0;
        double sum1 = 0;
        double sum2 = 0;
        double n = arguments.length;

        for(int i = 0 ; i < n ; i++){
            double x = arguments[i];
            sum1 += Math.pow(x,2);
            sum2 += Math.cos(2*Math.PI*x);
        }
        sum1 = sum1/n;
        sum2 = sum2/n;
        phrase = -20 * Math.exp(-0.2*sum1) - Math.exp(sum2) + 20 + Math.E;
        return phrase;
    }

    @Override
    public String toString(){
        return "Ackley";
    }
}
