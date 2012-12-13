package com.github.dreamrec.gcomponent;

/**
 *
 */
public interface ITimePainterModel {
    /**
     * number of data points per SECOND
     */
    public double getFrequency();
    /**
     * in milliseconds
     */
    public long getStartGraphTime();

    public int getXSize();
}
