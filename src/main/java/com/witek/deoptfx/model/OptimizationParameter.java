package com.witek.deoptfx.model;

public class OptimizationParameter{
    private double lowerBound;
    private double upperBound;
    private double stepSize; //Uzależnić
    private boolean optimize = true;

    public double getStepSize() {
        return stepSize;
    }

    public void setStepSize(double stepSize) {
        this.stepSize = stepSize;
    }

    public OptimizationParameter(){
    }


    public OptimizationParameter(double lowerBound, double upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    public void setLowerBound(double lowerBound) {
        this.lowerBound = lowerBound;
    }

    public void setUpperBound(double upperBound) {
        this.upperBound = upperBound;
    }

    public void setOptimize(boolean optimize) {
        this.optimize = optimize;
    }

    public double getLowerBound() {
        return lowerBound;
    }

    public double getUpperBound() {
        return upperBound;
    }

    public boolean isOptimize() {
        return optimize;
    }

}
