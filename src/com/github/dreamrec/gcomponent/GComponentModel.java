package com.github.dreamrec.gcomponent;

import com.github.dreamrec.IListView;
import com.github.dreamrec.Model;

import java.util.List;

/**
 *
 */
public class GComponentModel implements TimePainterModel, GraphPainterModel, YAxisPainterModel, CursorPainterModel{

    private List<ModelChangeListener> listeners;
    private Model model;
    private IListView<Integer> dataList;
    private int startIndex;
    private double maxValue;
    private int ySize;
    private double yZoom;
    private int xSize;
    private int YAxisPosition;
    private int XAxisPosition;

    public IListView<Integer> getDataList() {
        return dataList;
    }

    public int getStartIndex() {
       return startIndex;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public int getYSize() {
        return ySize;
    }

    public double getYZoom() {
        return yZoom;
    }

    public int getXSize() {
        return xSize;
    }

    public double getTimeScale() {
        throw new UnsupportedOperationException("todo");
    }

    public long getStartTime() {
        //calculated value;
        throw new UnsupportedOperationException("todo");
    }

    public int getCursorPosition() {
        //setter for this value
        throw new UnsupportedOperationException("todo");
    }

    public int getCursorWidth() {
        //calculated value
        throw new UnsupportedOperationException("todo");
    }

    public void setStartIndex(int startIndex){
         throw new UnsupportedOperationException("todo");
    }
}
