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

    public Model(double frequency, long startTime) {
        this.frequency = frequency;
        this.startTime = startTime;
    }

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
}



