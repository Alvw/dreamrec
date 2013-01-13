package com.github.dreamrec.ads;

/**
 * 
 */
public class AccelerometerChannelModel {
    private int divider;
    private String name;

    public AccelerometerChannelModel(int divider, String name) {
        this.divider = divider;
        this.name = name;
    }

    public int getDivider() {
        return divider;
    }

    public String getName() {
        return name;
    }
}
