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
    private ArrayList<AccelerometerChannelModel>  accelerometerChannels = new ArrayList<AccelerometerChannelModel>();

    
    public int[]  getAllDividers(){
        int[] dividers = new int[getNumberOfChannels() + getNumberOfAccelerometerChannels()];
        for (int i = 0; i < channels.size(); i++) {
            dividers[i] = channels.get(i).getDivider();           
        }
        for (int i = 0; i < accelerometerChannels.size(); i++) {
            dividers[i] = accelerometerChannels.get(i).getDivider();
        }
        return dividers;
    }
    
    public int getNumberOfChannels()  {
        return channels.size();
    }


    public void addChannel(ChannelModel channel) {
         channels.add(channel);
    }

    public ChannelModel getChannel (int chanelNumber) {
        if ( chanelNumber < channels.size() ) {
            return channels.get(chanelNumber);
        }
        else {
            return null;
        }
    }

    public int getNumberOfAccelerometerChannels()  {
        return accelerometerChannels.size();
    }

    public void addAccelerometerChannel(AccelerometerChannelModel accelerometerChannel) {
        accelerometerChannels.add(accelerometerChannel);
    }

    public ChannelModel getAccelerometerChannel (int accelerometerChanelNumber) {
        if ( accelerometerChanelNumber < channels.size() ) {
            return channels.get(accelerometerChanelNumber);
        }
        else {
            return null;
        }
    }

    /*
     *
     */
    public int getFrameSize(){
        int frameSize = 0;
        for (int i = 0; i < accelerometerChannels.size(); i++) {
            if(accelerometerChannels.get(i).getDivider() != 0){
                frameSize += MAX_DIV/accelerometerChannels.get(i).getDivider();
            }
        }
        for (int i = 0; i < channels.size(); i++) {
            if(channels.get(i).getDivider() != 0){
                frameSize += MAX_DIV/channels.get(i).getDivider();
            }

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
        boolean isCh1Test = channels.get(0).getCommutatorState().equals(CommutatorState.TEST_SIGNAL);
        boolean isCh2Test = channels.get(1).getCommutatorState().equals(CommutatorState.TEST_SIGNAL);
        return (isCh1Test | isCh2Test) ? 0x03 : 0;
    }
    
    public int rldEnabledBit(){
        return 0x20;
    }

    public int rldLoffSenseBit() {
        return 0x10;
    }
}
