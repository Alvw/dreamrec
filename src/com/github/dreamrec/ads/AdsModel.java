package com.github.dreamrec.ads;

import com.github.dreamrec.comport.ChannelModel;

import java.util.List;

/**
 *
 */
public class AdsModel {
    public static final int SPS_10    = 10;
    public static final int SPS_50    = 50;
    public static final int SPS_100   = 100;
    public static final int SPS_250   = 250;
    public static final int SPS_500   = 500;
    public static final int SPS_1000  = 1000;
    public static final int SPS_2000  = 2000;
    public static final int SPS_4000  = 4000;
    public static final int SPS_8000  = 8000;
    
    private int sps;     // samples per second (sample rate)
    List<ChannelModel>  activeChannels;

}
