package com.github.dreamrec.gcomponent;

/**
 *
 */
public interface ITimePainterModel {
    /**
     * number of data points per SECOND
     */
    public int getFrequency();
    /**
     * in milliseconds
     */
    public long getStartGraphTime();

    public int getXSize();
}
