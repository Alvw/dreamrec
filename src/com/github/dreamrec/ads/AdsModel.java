package com.github.dreamrec.ads;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class AdsModel {

    private Sps sps;     // samples per second (sample rate)
    private boolean isAccelerometerEnabled;
    public static final int MAX_DIV = 50;

    private ArrayList<ChannelModel> channels = new ArrayList<ChannelModel>();

    ChannelModel channel_1;
    ChannelModel channel_2;
    
    public ChannelModel getChannel (int chanelNumber) {
        if ( chanelNumber < channels.size() ) {
            return channels.get(chanelNumber);
        }
        else {
            return null;
        }
    }
    
    public int getNumberOfChannels()  {
        return channels.size();
    }


    public void addChannel(ChannelModel channel) {
         channels.add(channel);
    }


    public ChannelModel getChannel_1() {
        return channel_1;
    }

    public void setChannel_1(ChannelModel channel_1) {
        this.channel_1 = channel_1;
        channels.add(0,channel_1);
    }

    public ChannelModel getChannel_2() {
        return channel_2;
    }

    public void setChannel_2(ChannelModel channel_2) {
        this.channel_2 = channel_2;
        channels.add(1,channel_2);
    }
    
    /*
     *
     */
    public int getFrameSize(){
        int frameSize = isAccelerometerEnabled ? 3 : 0;
        if(channel_1.getDivider() != 0){
            frameSize += MAX_DIV/channel_1.getDivider();
        }
        if(channel_2.getDivider() != 0){
            frameSize += MAX_DIV/channel_2.getDivider();
        }
        return frameSize;
    }

    public Sps getSps() {
        return sps;
    }

    public void setSps(Sps sps) {
        this.sps = sps;
    }

    public boolean isAccelerometerEnabled() {
        return isAccelerometerEnabled;
    }

    public void setAccelerometerEnabled(boolean accelerometerEnabled) {
        isAccelerometerEnabled = accelerometerEnabled;
    }

    public int loffComparatorEnabledBit(){
//        return 0x40;
        return 0x00;
    }
    
    public int intTestEnabledBits(){
        boolean isCh1Test = channel_1.getCommutatorState().equals(CommutatorState.TEST_SIGNAL);
        boolean isCh2Test = channel_2.getCommutatorState().equals(CommutatorState.TEST_SIGNAL);
        return (isCh1Test | isCh2Test) ? 0x03 : 0;
    }
    
    public int rldEnabledBit(){
        return 0x20;
    }

    public int rldLoffSenseBit() {
        return 0x10;
    }
}
