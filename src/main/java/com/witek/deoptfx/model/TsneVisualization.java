package com.witek.deoptfx.model;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.Arrays;

public class TsneVisualization {
    public static void main(String[] args){
        double[] vector = {1.2088798336224346E-4, 11798.19924030677, 73763.48633273457, 4.427616975892736E9, 132835.6172495265, -0.5789357559892325, 0.0, 0.435753485212662, 0.10148893016933444, 0.34529255462624253, 0.0, 1.1575240588656769E9, 0.08918501385974466};
        double[] vector2 = {1.2062739977980303E-4, 17074.806141879384, 95713.77908350594, 2.133267728575057E9, 125147.18468065685, 2.098254332629599, 0.0, 0.40069119054534985, 0.08491936552194687, 0.22327460549397476, 0.0, 4.543700066068689E8, 0.02290701811143772};
        double[] vector3 = {1.0380783491726448E-4, 17993.58113898249, 97276.02388988258, 2.495961969968992E9, 110240.23987102303, 2.321609363654683, 0.0, 0.2477787466233193, 0.07439838433230861, 0.16893592626700907, 0.0, 3.0295249058129984E8, 0.02793015358530479};

        double[][] macierz = {vector,vector2,vector3};

        //double[][] macierz = {vector};

        RealMatrix matrix = new Array2DRowRealMatrix(macierz);

        //Wykonanie PCA

        EigenDecomposition decomposition = new EigenDecomposition(matrix.transpose().multiply(matrix));

        RealMatrix eigenvectors = decomposition.getV();

        //Redukcja do 2D

        RealMatrix reductedData = matrix.multiply(eigenvectors.getSubMatrix(0, matrix.getColumnDimension() - 1, 0, 1));

        //Wyświetlanie wyników

        double[][] reductedDataArray = reductedData.getData();

        for(int i = 0; i < reductedDataArray.length ; i++){
            //for(int j = 0; j < reductedDataArray[i].length ; j++) {
            System.out.println("Data " + i + " " + Arrays.toString(reductedDataArray[i]));
            //}
        }



    }
}
