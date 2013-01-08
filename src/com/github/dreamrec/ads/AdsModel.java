package com.github.dreamrec.ads;

import java.util.List;

/**
 *
 */
public class AdsModel {

    private Sps sps;     // samples per second (sample rate)
    private boolean isAccelerometerEnabled;

    ChannelModel channel_1;
    ChannelModel channel_2;

    public ChannelModel getChannel_1() {
        return channel_1;
    }

    public void setChannel_1(ChannelModel channel_1) {
        this.channel_1 = channel_1;
    }

    public ChannelModel getChannel_2() {
        return channel_2;
    }

    public void setChannel_2(ChannelModel channel_2) {
        this.channel_2 = channel_2;
    }

    public int getAccumulationBufferSize(){
        throw new UnsupportedOperationException("todo");
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
        throw new UnsupportedOperationException();
    }
    
    public int intTestEnabledBits(){
        throw new UnsupportedOperationException();
    }
    
    public int rldEnabledBit(){
        throw new UnsupportedOperationException();
    }

    public int rldLoffSenseBit() {
        throw new UnsupportedOperationException();
    }
}
