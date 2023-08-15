package com.witek.deoptfx.model;
import smile.manifold.TSNE;
import smile.plot.swing.*;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

public class TsneVector {
        private ArrayList<VectorOperations[]> vectors = new ArrayList<>();
        private double[][] coordinates;
        TSNE tsne;
    public TsneVector(ArrayList<VectorOperations[]> vectors) {
    }

    public double[][] generateTsneCoordinates(){
        this.tsne = new TSNE(coordinates,2,20,200,1000);

    }





        //double[][] coordinates_tsne = tsne.coordinates;





        ScatterPlot canvas = ScatterPlot.of(coordinates_tsne);



}
