package com.github.dreamrec;

import java.util.LinkedList;

/**
 *
 */
public class AveragingBuffer  {
    private int incomingCounter;
    protected int divider;
    private LinkedList<Integer> averagedDataQueue = new LinkedList<Integer>();
    protected int sumValue;

    public AveragingBuffer(int divider) {
        this.divider = divider;
    }

    public void add(int incomingValue){
        incomingCounter++;
        sumValue += incomingValue;
        if(incomingCounter % divider == 0){
            averagedDataQueue.add(sumValue / divider);
            sumValue = 0;
        }
    }

    public int poll() {
        return averagedDataQueue.poll();
    }

    public int getIncomingCounter() {
        return incomingCounter;
    }

    public int available() {
        return averagedDataQueue.size();
    }
}
