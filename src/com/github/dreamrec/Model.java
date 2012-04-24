package com.github.dreamrec;

import java.util.List;

/**
 *
 */
public class Model {
    private int xSize = 1200; //data points per screen.
    public static final int DIVIDER = 120; //frequency divider for slow graphics
    private ListView<Integer> eyeDataList = new ListView<Integer>();   //list with raw incoming data of eye movements
    private double frequency; //frequency Hz of the incoming data (for fast graphics)
    private long startTime; //time when data recording was started
    private int fastGraphIndex; //index for the first point on a screen for fast graphics
    private int slowGraphIndex; //index for the first point on a screen for slow graphics

    public ListView<Integer> getEyeDataList() {
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
    }

    public int dataSize(){
        return eyeDataList.size();
    }

    public void clear(){
        eyeDataList.clear();
        frequency = 0;
        startTime = 0;
    }

    public int getCursorWidth() {
        return xSize /DIVIDER;
    }

     public int getCursorPosition() {
        return fastGraphIndex/DIVIDER - slowGraphIndex;
    }

    public void moveFastGraph(int newFastGraphIndex){
       newFastGraphIndex = checkGraphIndexBounds(newFastGraphIndex, dataSize());
       fastGraphIndex = newFastGraphIndex;
    }

     public void moveSlowGraph(int newSlowGraphIndex) {
        newSlowGraphIndex = checkGraphIndexBounds(newSlowGraphIndex, dataSize() / DIVIDER);
        slowGraphIndex = newSlowGraphIndex;
    }

    //correct graph index if it points to invalid data. Should be > 0 and < (dataSize - xSize)
    private int checkGraphIndexBounds(int newIndex, int dataSize){
        int maxValue = dataSize - xSize;
        maxValue = maxValue < 0 ? 0 : maxValue;
        newIndex = newIndex < 0 ? 0 : newIndex;
        newIndex = newIndex > maxValue ? maxValue : newIndex;
        return newIndex;
    }

    public void moveCursor(int newCursorPosition){
        newCursorPosition = checkCursorBounds(newCursorPosition, dataSize()/DIVIDER);
        // move cursor to new position, even if this new position is out of the screen
        fastGraphIndex = (slowGraphIndex + newCursorPosition) * DIVIDER;
        //adjust slowGraphIndex to place cursor at the beginning of the screen
        if(getCursorPosition() < 0){
            slowGraphIndex+=getCursorPosition();
        } else
         //adjust slowGraphIndex to place cursor at the end of the screen
        if(getCursorPosition() > xSize -getCursorWidth()){
            slowGraphIndex-=getCursorPosition()- xSize +getCursorWidth();
        }
    }

    //correct cursor positions if it points to invalid data index: < 0 and > dataSize
    private int checkCursorBounds(int newCursorPosition, int dataSize) {
        int minValue = - slowGraphIndex;
        int maxValue = dataSize - slowGraphIndex - getCursorWidth();
        maxValue = maxValue < minValue ? minValue : maxValue;
        newCursorPosition = newCursorPosition < minValue ? minValue : newCursorPosition;
        newCursorPosition = newCursorPosition > maxValue ? maxValue : newCursorPosition;
        return  newCursorPosition;
    }
}



