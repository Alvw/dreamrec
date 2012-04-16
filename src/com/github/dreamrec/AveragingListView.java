package com.github.dreamrec;

import java.util.List;

/**
 *
 */
public class AveragingListView implements ListView<Integer>{

    int divider;
    List<Integer> incomingData;

    public AveragingListView(List<Integer> incomingData, int divider) {
        this.incomingData = incomingData;
        this.divider = divider;
    }

    public int size() {
        return incomingData.size()/divider;
    }

    public Integer get(int index) {
        if(index > size()){
            return 0;
        }
        int sum = 0;
        for (int i = index; i < index+divider; i++) {
              sum += incomingData.get(i);

        }
        return sum/divider;
    }
}
