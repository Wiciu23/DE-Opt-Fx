package com.witek.deoptfx.model;

import com.witek.deoptfx.model.function.Ackley;
import com.witek.deoptfx.model.function.Rastrigin;

public class OptimizeFunctionFactory {
    public static OptimizationFunction getOptimizeFunction(int type){
        if(type == 1){
            OptimizationFunction function = new FunctionMgr();
            return function;
        }else if(type == 2){
            OptimizationFunction function = new Rosenbrocks();
            return function;
        }else if(type == 3){
            OptimizationFunction function = new Rastrigin();
            return function;
        }else if(type == 4){
            OptimizationFunction function = new Ackley();
            return function;
        }else

            return null;
    }
}
