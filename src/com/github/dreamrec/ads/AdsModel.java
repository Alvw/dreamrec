package com.github.dreamrec.ads;

import java.util.ArrayList;

/**
 *
 */
public class AdsModel {

    private Sps sps;     // samples per second (sample rate)
    private boolean isAccelerometerEnabled;
    public static final int MAX_DIV = 50;

    private ArrayList<AdsChannelModel> adsChannels = new ArrayList<AdsChannelModel>();
    private ArrayList<ChannelModel>  accelerometerChannels = new ArrayList<ChannelModel>();

    
    public int[]  getAllDividers(){
        int[] dividers = new int[getNumberOfAdsChannels() + getNumberOfAccelerometerChannels()];
        for (int i = 0; i < adsChannels.size(); i++) {
            dividers[i] = adsChannels.get(i).getDivider();
        }
        for (int i = 0; i < accelerometerChannels.size(); i++) {
            dividers[i + adsChannels.size()] = accelerometerChannels.get(i).getDivider();
        }
        return dividers;
    }

    // all active channels including accelerometer channels
    public int getNumberOfActiveChannels(){
        int number = 0;
        int[] dividers = getAllDividers();
        for (int i = 0; i < dividers.length; i++) {
            if(dividers[i] != 0) {
                number++;
            }
        }
        return number;
    }
    
    // all dividers that != 0, including dividers for accelerometer channels
    public int[]  getActiveChannelDividers(){
        int[] allDividers = getAllDividers();
        int[] activeDividers = new int[getNumberOfActiveChannels()];
        int counter = 0;
        for (int i = 0; i < allDividers.length; i++) {
            if(allDividers[i] != 0) {
                activeDividers[counter] = allDividers[i];
                counter++;
            }
        }
        return  activeDividers;
    }

    /*
     *  this method threat  adsChannels and  accelerometerChannels as single array
     */
    public ChannelModel getChannel (int chanelNumber) {
        if ( chanelNumber < adsChannels.size() ) {
            return adsChannels.get(chanelNumber);
        }
        else if ( (chanelNumber - adsChannels.size()) < accelerometerChannels.size() ) {
            return accelerometerChannels.get(chanelNumber - adsChannels.size());
        }
        else {
            return null;
        }
    }
    
    public int getNumberOfAdsChannels()  {
        return adsChannels.size();
    }


    public void addAdsChannel(AdsChannelModel channel) {
         adsChannels.add(channel);
    }

    public AdsChannelModel getAdsChannel(int chanelNumber) {
        if ( chanelNumber < adsChannels.size() ) {
            return adsChannels.get(chanelNumber);
        }
        else {
            return null;
        }
    }

    public int getNumberOfAccelerometerChannels()  {
        return accelerometerChannels.size();
    }

    public void addAccelerometerChannel(ChannelModel channel) {
        accelerometerChannels.add(channel);
    }

    public ChannelModel getAccelerometerChannel (int chanelNumber) {
        if ( chanelNumber < accelerometerChannels.size() ) {
            return accelerometerChannels.get(chanelNumber);
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
        for (int i = 0; i < adsChannels.size(); i++) {
            if(adsChannels.get(i).getDivider() != 0){
                frameSize += MAX_DIV/ adsChannels.get(i).getDivider();
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
        boolean isCh1Test = adsChannels.get(0).getCommutatorState().equals(CommutatorState.TEST_SIGNAL);
        boolean isCh2Test = adsChannels.get(1).getCommutatorState().equals(CommutatorState.TEST_SIGNAL);
        return (isCh1Test | isCh2Test) ? 0x03 : 0;
    }
    
    public int rldEnabledBit(){
        return 0x20;
    }

    public int rldLoffSenseBit() {
        return 0x10;
    }
}
