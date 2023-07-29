package com.witek.deoptfx.model;

public class VectorLockable implements VectorOperations{

    private Vector vector;
    private OptimizationParameter[] params;

    public VectorLockable(Vector vector, OptimizationParameter[] params) {
        this.vector = vector;
        this.params = params;
    }

    @Override
    public double[] getCordinates() {
        return vector.getCordinates();
    }

    @Override
    public double getVectorComponent(int index) {
        return vector.getVectorComponent(index);
    }

    @Override
    public void set(double[] _a) {
        vector.set(_a);
    }

    @Override
    public void add(VectorOperations v) {
        double[] a = vector.getCordinates();
        for(int i = 0; i < a.length ; i++){
            if(params[i].isOptimize()){
                a[i] += v.getVectorComponent(i);
            }
        }
        vector.set(a);
        vector.limit();
    }

    @Override
    public void sub(VectorOperations v) {
        double[] a = vector.getCordinates();
        for(int i = 0; i < a.length ; i++){
            if(params[i].isOptimize()){
                a[i] -= v.getVectorComponent(i);
            }
        }
        vector.set(a);
        vector.limit();
    }

    @Override
    public void mul(double s) {
        double[] a = vector.getCordinates();
        for(int i = 0; i < a.length ; i++){
            if(params[i].isOptimize()){
                a[i] *= s;
            }
        }
        vector.set(a);
        vector.limit();
    }

    @Override
    public void div(VectorOperations v) {
        double[] a = vector.getCordinates();
        for(int i = 0; i < a.length ; i++){
            if(params[i].isOptimize()){
                a[i] /= v.getVectorComponent(i);
            }
        }
        vector.set(a);
        vector.limit();
    }

    @Override
    public void normalize() {
        vector.normalize();
    }

    @Override
    public int length() {
        return vector.length();
    }

    @Override
    public VectorOperations clone() {
        return new VectorLockable(vector,params);
    }

    @Override
    public String toString() {
        return vector.toString();
    }
}
