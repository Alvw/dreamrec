package com.github.dreamrec.gcomponent;

import com.github.dreamrec.IListView;

/**
 *
 */
public interface IGraphPainterModel {

    public IListView<Integer> getDataView();

    public int getStartIndex();

    public double getYZoom();

    public int getXSize();

}
