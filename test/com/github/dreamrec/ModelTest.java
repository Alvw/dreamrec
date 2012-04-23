package com.github.dreamrec;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 */
public class ModelTest {

    @Test
    public void testFastIndex(){
        Model model_100 = getModel(100);
        model_100.moveFastGraph(-1);
        assertTrue(model_100.getFastGraphIndex() == 0);
        model_100.moveFastGraph(1000);
        assertTrue(model_100.getFastGraphIndex() == 0);
        Model model_1000 = getModel(1500);
        model_1000.moveFastGraph(1600);
        assertTrue(model_1000.getFastGraphIndex() == 300);
        model_1000.moveFastGraph(1400);
        assertTrue(model_1000.getFastGraphIndex() == 300);
        model_1000.moveFastGraph(200);
        assertTrue(model_1000.getFastGraphIndex() == 200);
    }

    @Test
    public void testSlowIndex(){
        Model model_100 = getModel(100*Model.DIVIDER);
        model_100.moveSlowGraph(-1);
        assertTrue(model_100.getSlowGraphIndex() == 0);
        model_100.moveSlowGraph(1000);
        assertTrue(model_100.getSlowGraphIndex() == 0);
        Model model_1000 = getModel(1500*Model.DIVIDER);
        model_1000.moveSlowGraph(1600);
        assertTrue(model_1000.getSlowGraphIndex() == 300);
        model_1000.moveSlowGraph(1400);
        assertTrue(model_1000.getSlowGraphIndex() == 300);
        model_1000.moveSlowGraph(200);
        assertTrue(model_1000.getSlowGraphIndex() == 200);
    }

    private Model getModel(int dataSize){
        Model model = new Model();
        for (int i = 0; i < dataSize; i++) {
            model.addEyeData(i);
        }
        return model;
    }
}
