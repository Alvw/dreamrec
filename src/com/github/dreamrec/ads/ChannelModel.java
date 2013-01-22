package com.github.dreamrec.ads;

import com.github.dreamrec.HiPassPreFilter;

/**
 * 
 */
public class ChannelModel {
    private final String PHYSICAL_DIMENSION = "g";
    protected int divider;
    protected String name;
    private HiPassPreFilter hiPassPreFilter;

    public String getPhysicalDimension() {
        return PHYSICAL_DIMENSION;
    }

    public void setHiPassPreFilterBufferSize(int bufferSize){
          hiPassPreFilter =  new HiPassPreFilter(bufferSize);
    }

    public HiPassPreFilter getHiPassPreFilter() {
        return hiPassPreFilter;
    }

    public void setDivider(int divider) {
        this.divider = divider;
    }

    public int getDivider() {
        return divider;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }
}
