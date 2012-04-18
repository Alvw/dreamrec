package com.github.dreamrec;

import java.util.List;

/**
 *
 */
public class Model {
    private int xSize = 1200;
    public static final int DIVIDER = 120; //frequency divider for slow graphics
    private ListView<Integer> eyeDataList = new ListView<Integer>();   //list with raw incoming data of eye movements
    private double frequency; //frequency Hz of the incoming data (for fast graphics)
    private long startTime; //time when data recording was started
    private int fastGraphIndex; //index for the first point on a screen for fast graphics
    private int slowGraphIndex; //index for the first point on a screen for slow graphics

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

    public int getFastGraphIndex() {
        return fastGraphIndex;
    }

    public void setFastGraphIndex(int fastGraphIndex) {
        this.fastGraphIndex = fastGraphIndex;
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

     public int getCursorPosition() {
        return fastGraphIndex/DIVIDER - slowGraphIndex;
    }

    public void moveCursor(int newCursorPosition){
        if (checkOutOfBounds(newCursorPosition)){
            return;
        }
        // move cursor to new position, even if this new position is out of the screen
        fastGraphIndex = (slowGraphIndex + newCursorPosition) * DIVIDER;
        //adjust slowGraphIndex to place cursor at the beginning of the screen
        if(getCursorPosition()<0){
            slowGraphIndex+=getCursorPosition();
        }
         //adjust slowGraphIndex to place cursor at the end of the screen
        if(getCursorPosition()>xSize){
            slowGraphIndex-=getCursorPosition()-xSize;
        }
    }
    //check cursor points to valid data index: >=0 and < data list size
    private boolean checkOutOfBounds(int newCursorPosition) {
        if(slowGraphIndex+newCursorPosition < 0){
            return true;
        }
        if(slowGraphIndex+newCursorPosition>eyeDataList.size()/DIVIDER){
            return true;
        }
        return false;
    }


}



