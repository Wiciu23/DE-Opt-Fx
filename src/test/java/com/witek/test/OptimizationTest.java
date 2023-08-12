package com.witek.test;
import com.witek.deoptfx.model.Optimization;
import com.witek.deoptfx.model.Vector;
import org.junit.jupiter.api.Test;

public class OptimizationTest {
    @Test
    public void testDrawUniqueIndividuals(){
        //Przygotowanie danych testowych
        Vector[] population = {
                new Vector(new double[] {1.0, 1.0, 1.0}),
                new Vector(new double[] {2.0, 4.0, 12.0}),
                new Vector(new double[] {7.0, 1.0, 3.0}),
                new Vector(new double[] {3.4, 3.0, 34.0})
        };
        Vector uniqueIndividual = new Vector(new double[] {1.0, 1.0, 1.0});

        Optimization optimization = new Optimization();



    }
}
