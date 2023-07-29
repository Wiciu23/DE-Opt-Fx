package com.witek.deoptfx.model;

public interface VectorOperations {
    double[] getCordinates();

    double getVectorComponent(int index);

    void set(double[] _a);

    void add(VectorOperations v);

    void sub(VectorOperations v);

    void mul(double s);

    void div(VectorOperations v);

    void normalize();

    int length();

    VectorOperations clone ();
}
