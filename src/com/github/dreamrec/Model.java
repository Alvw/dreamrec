package com.github.dreamrec;

import java.util.List;

/**
 *
 */
public class Model {
    private ListView<Integer> eyeDataList = new ListView<Integer>();
    private double frequency;
    private long startTime;
    private int viewIndex;

    public List<Integer> getEyeDataList() {
        return eyeDataList;
    }

    public void addEyeData(int data) {
        eyeDataList.add(data);
    }

    public double getFrequency() {
        return frequency;
    }

    public long getStartTime() {
        return startTime;
    }

    public int getViewIndex() {
        return viewIndex;
    }

    public void setViewIndex(int viewIndex) {
        this.viewIndex = viewIndex;
    }

    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
    public void clear(){
        eyeDataList.clear();
        frequency = 0;
        startTime = 0;
    }
}



