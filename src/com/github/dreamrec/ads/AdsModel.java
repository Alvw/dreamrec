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
    
    public int getMaxDivider(){
        return Math.max(channel_1.getDivider(),channel_2.getDivider());
    }

    /*
     * frame size == 1 marker byte + 1 counter byte + 1 loff byte + accelerometer 9 bytes +
     *  + maxDiv/div for each chanel;
     */
    public int getFrameSize(){
        int accelerometerSize = isAccelerometerEnabled ? 9 : 0;
        return 3 + getMaxDivider()/channel_1.getDivider() + getMaxDivider()/channel_2.getDivider() + accelerometerSize;
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
        return 0x40;
    }
    
    public int intTestEnabledBits(){
        return 0x03;
    }
    
    public int rldEnabledBit(){
        return 0x20;
    }

    public int rldLoffSenseBit() {
        return 0x10;
    }
}
