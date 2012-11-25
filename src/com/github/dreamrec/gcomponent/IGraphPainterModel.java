package com.github.dreamrec.gcomponent;

import com.github.dreamrec.Filter;

/**
 *
 */
public interface IGraphPainterModel {

    public Filter<Short>[] getDataView();

    public int getStartIndex();

    public double getYZoom();

    public int getXSize();

}
