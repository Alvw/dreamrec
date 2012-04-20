package com.github.dreamrec;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

/**
 *
 */
public class DataSaveManagerTest {

    File file;
    Model model;

    @Before
    public void setup(){
        file = new File("tralivali");
        model = new Model();
    }

    @After
    public void tearDown(){
         file.delete();
    }

    @Test
    public void saveToFileTest() throws ApplicationException {
        long startTime = System.currentTimeMillis();
        model.setFrequency(13.3);
        model.setStartTime(startTime);
        for (int i = 0; i < 10000; i++) {
              model.addEyeData(i);
        }
        new DataSaveManager().saveToFile(file, model);
        Model restoredModel = new Model();
        new DataSaveManager().readFromFile(file,restoredModel);
        assertTrue(model.getFrequency() == restoredModel.getFrequency());
        assertTrue(model.getStartTime() == restoredModel.getStartTime());
        assertTrue(model.getEyeDataList().size() == restoredModel.getEyeDataList().size());
        for (int i = 0; i < model.getEyeDataList().size(); i++) {
              assertTrue(model.getEyeDataList().get(i).equals(restoredModel.getEyeDataList().get(i)));
        }
    }
}
