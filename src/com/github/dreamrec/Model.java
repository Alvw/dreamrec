package com.github.dreamrec;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class Model {
    private int xSize; //data points per screen.
    public static final int DIVIDER = 120; //frequency divider for slow graphics
    private DataList<Short> eyeDataList = new DataList<Short>();   //list with prefiltered incoming data of eye movements
    private DataList<Short> chanel2DataList = new DataList<Short>();   //list with prefiltered incoming chanel2 data
    private DataList<Short> acc1DataList = new DataList<Short>();   //list with accelerometer 1 chanel data
    private DataList<Short> acc2DataList = new DataList<Short>();   //list with accelerometer 1 chanel data
    private DataList<Short> acc3DataList = new DataList<Short>();   //list with accelerometer 1 chanel data

    private double frequency; //frequency Hz of the incoming data (for fast graphics)
    private long startTime; //time when data recording was started
    private int fastGraphIndex; //index for the first point on a screen for fast graphics
    private int slowGraphIndex; //index for the first point on a screen for slow graphics

    public DataList<Short> getEyeDataList() {
        return eyeDataList;
    }

    public DataList<Short> getAcc1DataList() {
        return acc1DataList;
    }

    public DataList<Short> getAcc2DataList() {
        return acc2DataList;
    }

    public DataList<Short> getAcc3DataList() {
        return acc3DataList;
    }

     public DataList<Short> getCh2DataList() {
        return chanel2DataList;
    }

    public void addEyeData(short data) {
        eyeDataList.add(data);
    }

    public void addCh2Data(short data) {
        chanel2DataList.add(data);
    }

    public void addAcc1Data(short data) {
        acc1DataList.add(data);
    }

    public void addAcc2Data(short data) {
        acc2DataList.add(data);
    }

    public void addAcc3Data(short data) {
        acc3DataList.add(data);
    }

    public double getFrequency() {
        return frequency;
    }

    public long getStartTime() {
        return startTime;
    }

    public int getFastGraphIndex() {
        return fastGraphIndex;
    }

    public int getSlowGraphIndex() {
        return slowGraphIndex;
    }

    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public int getXSize() {
        return xSize;
    }

    public void setXSize(int xSize) {
        this.xSize = xSize;
        checkCursorScreenBounds();
    }

    public int getDataSize() {
        return eyeDataList.size();
    }

    public void clear() {
        eyeDataList.clear();
        frequency = 0;
        startTime = 0;
    }

    public int getCursorWidth() {
        return xSize / DIVIDER;
    }

    public int getCursorPosition() {
        return fastGraphIndex / DIVIDER - slowGraphIndex;
    }

    public void moveFastGraph(int newFastGraphIndex) {
        newFastGraphIndex = checkGraphIndexBounds(newFastGraphIndex, getDataSize());
        fastGraphIndex = newFastGraphIndex;
        checkCursorScreenBounds();
    }

    public void moveSlowGraph(int newSlowGraphIndex) {
        newSlowGraphIndex = checkGraphIndexBounds(newSlowGraphIndex, getSlowDataSize());
        slowGraphIndex = newSlowGraphIndex;
        if(getCursorPosition()<0){
            moveCursor(0);
        }
        int cursorMaxPosition = xSize - getCursorWidth() -1;
        if(getCursorPosition() > cursorMaxPosition){
            moveCursor(cursorMaxPosition);
        }
    }



    //correct graph index if it points to invalid data. Should be > 0 and < (graphSize - xSize)
    private int checkGraphIndexBounds(int newIndex, int dataSize) {
        int maxValue = getIndexMax(dataSize);
        newIndex = newIndex < 0 ? 0 : newIndex;
        newIndex = newIndex > maxValue ? maxValue : newIndex;
        return newIndex;
    }

    public int getIndexMax(int dataSize) {
        int maxValue = dataSize - xSize - 1;
        maxValue = maxValue < 0 ? 0 : maxValue;
        return maxValue;
    }

    public int getSlowDataSize(){
        return getDataSize()/DIVIDER;
    }

    public boolean isFastGraphIndexMaximum() {
        return fastGraphIndex == getIndexMax(getDataSize());
    }

    public void setFastGraphIndexMaximum() {
         moveFastGraph(getIndexMax(getDataSize()));
    }

    public void moveCursor(int newCursorPosition) {
        newCursorPosition = checkCursorIndexBounds(newCursorPosition, getSlowDataSize());
        // move cursor to new position, even if this new position is out of the screen
        fastGraphIndex = (slowGraphIndex + newCursorPosition) * DIVIDER;
        checkCursorScreenBounds();
    }

    private void checkCursorScreenBounds() {
        //adjust slowGraphIndex to place cursor at the beginning of the screen
        if (getCursorPosition() < 0) {
            slowGraphIndex += getCursorPosition();
        } else
            //adjust slowGraphIndex to place cursor at the end of the screen
            if (getCursorPosition() > xSize - getCursorWidth() - 1) {
                slowGraphIndex += getCursorPosition() - xSize + getCursorWidth();
            }
    }

    //correct cursor positions if it points to invalid data index: < 0 and > graphSize
    private int checkCursorIndexBounds(int newCursorPosition, int dataSize) {
        int minValue = -slowGraphIndex;
        int maxValue = dataSize - slowGraphIndex - getCursorWidth() - 1;
        maxValue = maxValue < minValue ? minValue : maxValue;
        newCursorPosition = newCursorPosition < minValue ? minValue : newCursorPosition;
        newCursorPosition = newCursorPosition > maxValue ? maxValue : newCursorPosition;
        return newCursorPosition;
    }
}



