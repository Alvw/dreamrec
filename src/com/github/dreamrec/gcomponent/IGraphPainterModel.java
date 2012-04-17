package com.github.dreamrec.gcomponent;

import com.github.dreamrec.IListView;

/**
 *
 */
public interface IGraphPainterModel {

    public IListView<Integer> getDataList();

    public int getStartIndex();

    public double getYZoom();

    public int getXSize();

}
