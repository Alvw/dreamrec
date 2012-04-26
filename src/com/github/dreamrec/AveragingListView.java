package com.github.dreamrec;

/**
 *
 */
public class AveragingListView extends DoNothingListView{


    public AveragingListView(int divider) {
        this.divider = divider;
    }


    public Integer get(int index) {
        int sum = 0;
        int incomingDataIndex = index*divider;
        for (int i = 0; i < divider; i++) {
              sum += incomingData.get(incomingDataIndex+i);

        }
        return sum/divider;
    }

}
