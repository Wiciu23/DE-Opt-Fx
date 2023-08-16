package com.witek.deoptfx.model;
import smile.manifold.TSNE;
import smile.plot.swing.*;
import smile.projection.PCA;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

public class TsneVector {
    TSNE tsne;

    public TsneVector(ArrayList<VectorOperations[]> vectors) {
    }

    public static double[][] generateTsneCoordinates(VectorOperations[] vectors){
        double[][] coordinates = new double[vectors.length][];
        for(int i = 0; i < vectors.length; i ++){
            coordinates[i] = vectors[i].getCordinates();
        }
        TSNE tsne = new TSNE(coordinates,2,15,200,500);
        return tsne.coordinates;
        //return PCA.fit(coordinates).getProjection().toArray();
        //return PCA.cor(coordinates).getProjection().toArray();

    }





        //double[][] coordinates_tsne = tsne.coordinates;





       // ScatterPlot canvas = ScatterPlot.of(coordinates_tsne);



}
