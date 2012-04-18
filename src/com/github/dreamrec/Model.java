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

    public int getCursorWidth() {
        return xSize/DIVIDER;
    }

     public int getCursorPosition() {
        return fastGraphIndex/DIVIDER - slowGraphIndex;
    }
    
    public void moveFastGraph(int newFastGraphIndex){
       newFastGraphIndex = checkFastGraphIndexBounds(newFastGraphIndex);
       fastGraphIndex = newFastGraphIndex;
    }
    //correct fastGraphIndex if it points to invalid data index: < 0 and >data list size
    private int checkFastGraphIndexBounds(int newFastGraphIndex){
        //index should be positive
        if(newFastGraphIndex < 0){
            newFastGraphIndex = 0;
        }
        // graphic can not be moved (index always = 0) if  data list size <= screen size
        if(eyeDataList.size() <= xSize){
            newFastGraphIndex = 0;
        }
        // maximum value of fastGraphIndex (the most left position of the graphic) < data list size - screen size)
        if(newFastGraphIndex > eyeDataList.size()-xSize){
            newFastGraphIndex = eyeDataList.size()-xSize;
        }
        return newFastGraphIndex;
    }

    public void moveCursor(int newCursorPosition){
        newCursorPosition = checkCursorBounds(newCursorPosition);
        // move cursor to new position, even if this new position is out of the screen
        fastGraphIndex = (slowGraphIndex + newCursorPosition) * DIVIDER;
        //adjust slowGraphIndex to place cursor at the beginning of the screen
        if(getCursorPosition() < 0){
            slowGraphIndex+=getCursorPosition();
        }
         //adjust slowGraphIndex to place cursor at the end of the screen
        if(getCursorPosition() > xSize-getCursorWidth()){
            slowGraphIndex-=getCursorPosition()-xSize+getCursorWidth();
        }
    }
    //correct cursor positions if it points to invalid data index: < 0 and >data list size
    private int checkCursorBounds(int newCursorPosition) {
        if(slowGraphIndex+newCursorPosition < 0){
            newCursorPosition = -slowGraphIndex;
        }
        if(slowGraphIndex+newCursorPosition > eyeDataList.size()/DIVIDER - getCursorWidth()){
            newCursorPosition = eyeDataList.size()/DIVIDER - getCursorWidth()- slowGraphIndex;
        }
        return  newCursorPosition;
    }


}



