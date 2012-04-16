package com.github.dreamrec;

import java.util.List;

/**
 *
 */
public class AveragingListView implements ListView<Integer>{

    int divider;
    ListView<Integer> incomingData;

    public AveragingListView(ListView<Integer> incomingData, int divider) {
        this.incomingData = incomingData;
        this.divider = divider;
    }

    public int size() {
        return incomingData.size()/divider;
    }

    public Integer get(int index) {
        if(index >= size()){
            return 0;
        }
        int sum = 0;
        int incomingDataIndex = index*divider;
        for (int i = 0; i < divider; i++) {
              sum += incomingData.get(incomingDataIndex+i);

        }
        return sum/divider;
    }
}
