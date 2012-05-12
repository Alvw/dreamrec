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
}
