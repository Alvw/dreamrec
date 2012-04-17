package com.github.dreamrec;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;


/**
 *
 */
public class AveragingListViewTest {



    @Test
    public void testSize(){
        IListView<Integer> avgView = new AveragingListView(getTestData(9),10);
        assertTrue(avgView.size() == 0);


        avgView = new AveragingListView(getTestData(10),10);
        assertTrue(avgView.size()== 1);
        assertTrue(avgView.get(0) == 4);

        avgView = new AveragingListView(getTestData(25),10);
        assertTrue(avgView.size()== 2);
        assertTrue(avgView.get(1) == 14);
    }

    @Test (expected = IndexOutOfBoundsException.class)
    public void testOutOfBoundsIndex(){
        IListView<Integer> avgView = new AveragingListView(getTestData(35),10);
        avgView.get(4);
    }

    private IListView<Integer> getTestData(int size){
        ListView<Integer> data = new ListView<Integer>();
        for (int i = 0; i < size; i++) {
              data.add(i);
        }
        return data;
    }
}
