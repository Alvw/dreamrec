package com.github.dreamrec.gcomponent;

import com.github.dreamrec.Filter;
import com.github.dreamrec.IFilter;

/**
 *
 */
public interface IGraphPainterModel {

    public IFilter<Integer> getDataView();

    public int getStartIndex();

    public double getYZoom();

    public int getXSize();

}
