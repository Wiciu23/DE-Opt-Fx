package com.witek.deoptfx.model;
import smile.manifold.TSNE;
import smile.manifold.UMAP;
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

    public static double[][] generateTsneCoordinates(VectorOperations[] vectors,int perplexity,  int eta, int iterations){
        double[][] coordinates = new double[vectors.length][];
        for(int i = 0; i < vectors.length; i ++){
            coordinates[i] = vectors[i].getCordinates();
        }
        //TSNE tsne = new TSNE(coordinates,2, perplexity, eta, iterations);
        //return tsne.coordinates;
        return UMAP.of(coordinates,3,2,100,1.0,0.0000000001,2.0,1,1.4).coordinates;
        //return PCA.fit(coordinates).getProjection().toArray();
        //return PCA.cor(coordinates).getProjection().toArray();

    }





        //double[][] coordinates_tsne = tsne.coordinates;





       // ScatterPlot canvas = ScatterPlot.of(coordinates_tsne);



}
