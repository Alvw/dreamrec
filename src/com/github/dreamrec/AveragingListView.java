package com.github.dreamrec;

/**
 *
 */
public class AveragingListView implements IListView<Integer> {

    int divider;
    IListView<Integer> incomingData;

    public AveragingListView(IListView<Integer> incomingData, int divider) {
        this.incomingData = incomingData;
        this.divider = divider;
    }

    public int size() {
        return incomingData.size()/divider;
    }

    public Integer get(int index) {
        int sum = 0;
        int incomingDataIndex = index*divider;
        for (int i = 0; i < divider; i++) {
              sum += incomingData.get(incomingDataIndex+i);

        }
        return sum/divider;
    }

    public int getTotalDivider() {
        return divider*incomingData.getTotalDivider();
    }
}
