package com.github.dreamrec;


import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 */
public class AveragingBuffer  {
    private int incomingCounter;
    protected int divider;
    private Queue<Integer> averagedDataQueue = new ConcurrentLinkedQueue<Integer>();
    protected int sumValue;

    public AveragingBuffer(int divider) {
        this.divider = divider;
    }

    public void add(int incomingValue){
        incomingCounter++;
        sumValue += incomingValue;
        if(incomingCounter % divider == 0){
            averagedDataQueue.offer(sumValue / divider);
            sumValue = 0;
        }
       // averagedDataQueue.offer(incomingValue);

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
