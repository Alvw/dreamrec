package com.github.dreamrec.gcomponent;

import com.github.dreamrec.Filter;
import com.github.dreamrec.Model;

/**
 *
 */
public abstract class GComponentModel implements ITimePainterModel, IGraphPainterModel, IYAxisPainterModel {

    protected Model model;
    protected Filter<Integer> dataView;  // todo consider refactoring
    protected double maxValue = 200;
    protected int ySize = 200;
    protected double yZoom = 1;
    protected int leftIndent = 50;
    protected int rightIndent = 0;
    protected int topIndent = 0;
    protected int bottomIndent = 20;
    protected boolean isXCentered = false;

    public GComponentModel(Model model, Filter<Integer> dataView) {
        this.model = model;
        this.dataView = dataView;
    }

    /**
     *  place X axes to the screen center
     */
    public void centreX(){
        isXCentered = true;
        bottomIndent = bottomIndent/2;
        topIndent = bottomIndent;
    }

    public Model getModel() {
        return model;
    }

    public int getLeftIndent() {
        return leftIndent;
    }

    public int getRightIndent() {
        return rightIndent;
    }

    public int getTopIndent() {
        return topIndent;
    }

    public int getBottomIndent() {
        return bottomIndent;
    }

    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }

    public void setYSize(int ySize) {
        this.ySize = ySize;
    }

    public void setYZoom(double yZoom) {
        this.yZoom = yZoom;
    }

    public Filter<Integer> getDataView() {
        return dataView;
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
        return model.getXSize();
    }

     public long getStartGraphTime() {
        return model.getStartTime() + (long)(getStartIndex() * 1000 /getFrequency());
    }

    public abstract int getStartIndex();

    public abstract double getFrequency();

}
