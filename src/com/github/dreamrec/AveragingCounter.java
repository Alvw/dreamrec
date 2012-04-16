package com.github.dreamrec;

/**
 *
 */
public class AveragingCounter {
    private int invocationCounter;
    protected int averagingPeriod;
    protected int sumValue;

    public AveragingCounter(int averagingPeriod) {
        this.averagingPeriod = averagingPeriod;
    }

    public Integer getAverageValue(Integer value){
        invocationCounter++;
        sumValue += value;
        if(invocationCounter % averagingPeriod == 0){
            int result = sumValue / averagingPeriod;
            sumValue = 0;
            return  result;
        }
        return null;
    }
}
