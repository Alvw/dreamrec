package com.github.dreamrec;

/**
 *
 */
public class DoNothingListView implements  IListView<Integer>{
    IListView<Integer> incomingData =  new ListView<Integer>();
    int divider = 1;

    @Override
    public int size() {
            return incomingData.size()/divider;
    }

    @Override
    public Integer get(int index) {
        return incomingData.get(index);
    }

    @Override
    public int getDivider() {
        return divider;
    }

    @Override
    public boolean isPositive() {
        return true;
    }

    @Override
    public void setInputDataList(IListView<Integer> inputDataList) {
        this.incomingData = inputDataList;
    }
}
