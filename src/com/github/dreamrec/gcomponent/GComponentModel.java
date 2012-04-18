package com.github.dreamrec.gcomponent;

import com.github.dreamrec.IListView;
import com.github.dreamrec.Model;

/**
 *
 */
public class GComponentModel implements ITimePainterModel, IGraphPainterModel, IYAxisPainterModel, ICursorPainterModel {

    private Model model;

    private IListView<Integer> dataView;  // todo consider refactoring
    private int startIndex;
    private double maxValue = 200;
    private int ySize = 200;
    private int xSize = 1200;
    private double yZoom = 1;
    private int YAxisPosition = 20;
    private int XAxisPosition = 20;

    public GComponentModel(Model model, IListView<Integer> dataView) {
        this.model = model;
        this.dataView = dataView;
    }

    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }

    public void setYSize(int ySize) {
        this.ySize = ySize;
    }

    public void setXSize(int xSize) {
        this.xSize = xSize;
    }

    public void setYZoom(double yZoom) {
        this.yZoom = yZoom;
    }

    public void setYAxisPosition(int YAxisPosition) {
        this.YAxisPosition = YAxisPosition;
    }

    public void setXAxisPosition(int XAxisPosition) {
        this.XAxisPosition = XAxisPosition;
    }

    public IListView<Integer> getDataView() {
        return dataView;
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

    public double getFrequency() {
           return model.getFrequency()/dataView.getTotalDivider();
    }

    public long getStartGraphTime() {
            return model.getStartTime()+(long)(startIndex*1000/getFrequency());

    }

    public int getCursorPosition() {
        return model.getViewIndex()/dataView.getTotalDivider() - startIndex;
    }

    public void setCursorPosition(int newPosition){
        model.setViewIndex((startIndex+newPosition)*dataView.getTotalDivider());
    }

    public int getCursorWidth() {
        return xSize/dataView.getTotalDivider();
    }

    public void setStartIndex(int newStartIndex){
        int oldCursorPosition = getCursorPosition();
        startIndex = newStartIndex;
        setCursorPosition(oldCursorPosition);
    }
}
