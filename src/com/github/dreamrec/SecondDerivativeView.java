package com.github.dreamrec;

/**
 *
 */
public class SecondDerivativeView implements IListView<Integer>{

    IListView<Integer> incomingData;

    public SecondDerivativeView(IListView<Integer> incomingData) {
        this.incomingData = incomingData;
    }

    public int size() {
       return incomingData.size();
    }

    public Integer get(int index) {
        if(index < 8){
            return 0;
        }
        int sum = incomingData.get(index - 8) +
                incomingData.get(index - 7) * 2 +
                incomingData.get(index - 6) * 4 +
                incomingData.get(index - 5) * 8 +
                incomingData.get(index - 3) * 8 +
                incomingData.get(index - 2) * 4 +
                incomingData.get(index - 1) * 2 +
                incomingData.get(index);
        return incomingData.get(index - 4) - sum / 30;
    }
}