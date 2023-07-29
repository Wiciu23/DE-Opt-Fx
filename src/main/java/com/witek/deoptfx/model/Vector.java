package com.witek.deoptfx.model;/*
package PSO;

/**
 * Can represent a position as well as a velocity.
 */

import java.util.Arrays;

/**
 * Can represent a position as well as a velocity.
 */
class Vector implements VectorOperations {

    private double a[];
    private double limit = Double.MAX_VALUE;

    Vector (int length) {
        this( new double[length]);
    }

    Vector (double _a[]) {
        a = new double[_a.length];
        for (int i = 0; i < _a.length; i++){
            a[i] = _a[i];
        }
    }

    @Override
    public double[] getCordinates(){
        double cordinates[] = a;
        return cordinates;
    }
    @Override
    public double getVectorComponent(int index) {
        return a[index];
    }

    @Override
    public void set(double[] _a) {
        System.arraycopy(_a, 0, this.a, 0, _a.length);
    }



    @Override
    public void add(VectorOperations v) {
        for (int i = 0; i < v.length(); i++){
            this.a[i] += v.getVectorComponent(i);
        }
        limit();
        System.out.println("SIEMAAA TO NIE DZIALA");
    }

    @Override
    public void sub(VectorOperations v) {
        for (int i = 0; i < v.length(); i++){
            this.a[i] -= v.getVectorComponent(i);
        }
        limit();
    }

    @Override
    public void mul(double s) {
        for (int i = 0; i < a.length; i++){
            this.a[i] *= s;
        }
        limit();
    }

    @Override
    public void div(VectorOperations v) {
        for (int i = 0; i < v.length(); i++){
            this.a[i] /= v.getVectorComponent(i);
        }
        limit();
    }

    @Override
    public void normalize() {
        double m = mag();
        if (m > 0) for (int i = 0; i < a.length; i++){
            this.a[i] /= m;
        }
    }

    @Override
    public int length() {
        return a.length;
    }

    private double mag () {
        return Math.sqrt(Arrays.stream(a).sum());
    }

    void limit (double l) {
        limit = l;
        limit();
    }

    protected void limit () {
        double m = mag();
        if (m > limit) {
            double ratio = m / limit;
            for (int i = 0; i < a.length; i++){
                this.a[i] /= ratio;
            }
        }
    }

    @Override
    public VectorOperations clone () {
        return new Vector(a);
    }

    public String toString () {
        return Arrays.toString(a);
    }

}





/*
class Vector {

    private double x, y, z;
    private double limit = Double.MAX_VALUE;

    Vector () {
        this(0, 0, 0);
    }

    Vector (double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    double getX () {
        return x;
    }

    double getY () {
        return y;
    }

    double getZ () {
        return z;
    }

    void set (double x, double y, double z) {
        setX(x);
        setY(y);
        setZ(z);
    }

    private void setX (double x) {
        this.x = x;
    }

    private void setY (double y) {
        this.y = y;
    }

    private void setZ (double z) {
        this.z = z;
    }

    void add (Vector v) {
        x += v.x;
        y += v.y;
        z += v.z;
        limit();
    }

    void sub (Vector v) {
        x -= v.x;
        y -= v.y;
        z -= v.z;
        limit();
    }

    void mul (double s) {
        x *= s;
        y *= s;
        z *= s;
        limit();
    }

    void div (double s) {
        x /= s;
        y /= s;
        z /= s;
        limit();
    }

    void normalize () {
        double m = mag();
        if (m > 0) {
            x /= m;
            y /= m;
            z /= m;
        }
    }

    private double mag () {
        return Math.sqrt(x*x + y*y);
    }

    void limit (double l) {
        limit = l;
        limit();
    }

    private void limit () {
        double m = mag();
        if (m > limit) {
            double ratio = m / limit;
            x /= ratio;
            y /= ratio;
        }
    }

    public Vector clone () {
        return new Vector(x, y, z);
    }

    public String toString () {
        return "(" + x + ", " + y + ", " + z + ")";
    }

}
*/