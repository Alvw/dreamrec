package com.github.dreamrec;


public class FistDerivativeAbsView extends  DoNothingListView{




    public Integer get(int index) {
        if(index == 0){
            return 0;
        }
        return (Math.abs(incomingData.get(index) - incomingData.get(index-1)));
    }
}
