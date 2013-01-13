package com.github.dreamrec;

import org.junit.Test;
import static org.junit.Assert.*;


/**
 *
 */
public class AveragingListViewTest {



    @Test
    public void testSize(){
        /*AbstractFilter<Integer> avgView = new AveragingFilter(getTestData(9),10);
        assertTrue(avgView.available() == 0);


        avgView = new AveragingFilter(getTestData(10),10);
        assertTrue(avgView.available()== 1);
        assertTrue(avgView.get(0) == 4);

        avgView = new AveragingFilter(getTestData(25),10);
        assertTrue(avgView.available()== 2);
        assertTrue(avgView.get(1) == 14);*/
    }

    @Test (expected = IndexOutOfBoundsException.class)
    public void testOutOfBoundsIndex(){
        /*AbstractFilter<Integer> avgView = new AveragingFilter(getTestData(35),10);
        avgView.get(4);*/
    }

    private Filter<Integer> getTestData(int size){
        DataList<Integer> data = new DataList<Integer>();
        for (int i = 0; i < size; i++) {
              data.add(i);
        }
        return data;
    }
}
