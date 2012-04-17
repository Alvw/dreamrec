package com.github.dreamrec;


public class FistDerivativeAbsView implements IListView<Integer>{

    IListView<Integer> incomingData;

    public FistDerivativeAbsView(IListView<Integer> incomingData) {
        this.incomingData = incomingData;
    }

    public int size() {
        return incomingData.size();
    }

    public Integer get(int index) {
        if(index == 0){
            return 0;
        }
        return (Math.abs(incomingData.get(index) - incomingData.get(index-1)));
    }

    public int getTotalDivider() {
        return incomingData.getTotalDivider();
    }
}
