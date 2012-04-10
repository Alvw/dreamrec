package com.github.dreamrec.gcomponent;

import com.github.dreamrec.ListView;

/**
 *
 */
public interface GraphPainterModel {

    public ListView<Integer> getDataList();

    public int getStartIndex();

    public double getYZoom();

    public int getXSize();

}
