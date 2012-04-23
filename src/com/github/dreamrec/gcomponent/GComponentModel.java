package com.github.dreamrec.gcomponent;

import com.github.dreamrec.IListView;
import com.github.dreamrec.Model;

/**
 *
 */
public abstract class GComponentModel implements ITimePainterModel, IGraphPainterModel, IYAxisPainterModel {

    protected Model model;
    protected IListView<Integer> dataView;  // todo consider refactoring
    protected double maxValue = 200;
    protected int ySize = 200;
    protected double yZoom = 1;

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

    public void setYZoom(double yZoom) {
        this.yZoom = yZoom;
    }

    public IListView<Integer> getDataView() {
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
