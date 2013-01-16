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
    
/**
 *  Active channels - channels with divider != 0;
 *  All Channels  means including channels with divider == 0
 *  
 *  ChannelType = { ADS, ACCELEROMETER }
 */

    public int getNumberOfAllChannels(){
        return ( adsChannels.size() + accelerometerChannels.size() );
    }
    
    public int getNumberOfAllChannels(ChannelType channelType){
        int channelsNumber = 0;
        if (channelType == ChannelType.ADS) {
            channelsNumber = adsChannels.size();
        }
        else if (channelType == ChannelType.ACCELEROMETER) {
            channelsNumber = accelerometerChannels.size();
        }
        return channelsNumber;
    }


    public int getNumberOfActiveChannels(){
        int number = 0;
        for (AdsChannelModel channel : adsChannels) {
            if(channel.getDivider() != 0) {
                number++;
            }
        }
        for (ChannelModel channel : accelerometerChannels) {
            if(channel.getDivider() != 0) {
                number++;
            }
        }
        return number;
    }

    public int getNumberOfActiveChannels(ChannelType channelType){
        int number = 0;
        if (channelType == ChannelType.ADS){
            for (ChannelModel channel : adsChannels) {
                if(channel.getDivider() != 0) {
                    number++;
                }
            }
        }
        else if (channelType == ChannelType.ACCELEROMETER) {
            for (ChannelModel channel : accelerometerChannels) {
                if(channel.getDivider() != 0) {
                    number++;
                }
            }
        }
        return number;
    }
    
    public int[]  getAllDividers(){
        int[] dividers = new int[getNumberOfAllChannels()];
        int i = 0;
        for (ChannelModel channel : adsChannels) {
            dividers[i] = channel.getDivider();
            i++;
        }
        for (ChannelModel channel : accelerometerChannels) {
            dividers[i] = channel.getDivider();
            i++;
        }
        return dividers;
    }

    public int[]  getAllDividers(ChannelType channelType){
        int[] dividers = new int[getNumberOfAllChannels(channelType)];
        if (channelType == ChannelType.ADS) {
            for (int i = 0; i < adsChannels.size(); i++) {
                dividers[i] = adsChannels.get(i).getDivider();
            }
        }
        else if (channelType == ChannelType.ACCELEROMETER) {
            for (int i = 0; i < accelerometerChannels.size(); i++) {
                dividers[i] = accelerometerChannels.get(i).getDivider();
            }
        }                       
        return dividers;
    }


    public int[]  getActiveChannelsDividers(){
        int[] dividers = new int[getNumberOfActiveChannels()];
        int i = 0;
        for (AdsChannelModel channel : adsChannels) {
            if(channel.getDivider() != 0){
                dividers[i] = channel.getDivider();
                i++;
            }
        }
        for (ChannelModel channel : accelerometerChannels) {
            if(channel.getDivider() != 0){
                dividers[i] = channel.getDivider();
                i++;
            }
        }
        return dividers;
    }

    public int[]  getActiveChannelsDividers(ChannelType channelType){
        int[] dividers = new int[getNumberOfActiveChannels(channelType)];
        int i = 0;
        if (channelType == ChannelType.ADS){
            for (AdsChannelModel channel : adsChannels) {
                if(channel.getDivider() != 0){
                    dividers[i] = channel.getDivider();
                    i++;
                }
            }
        }
        else if (channelType == ChannelType.ACCELEROMETER) {
            for (ChannelModel channel : accelerometerChannels) {
                if(channel.getDivider() != 0){
                    dividers[i] = channel.getDivider();
                    i++;
                }
            }
        }
        return dividers;
    }


    public AdsChannelModel getAdsChannel (int chanelNumber) {
        AdsChannelModel channel = null;
        if ( chanelNumber < adsChannels.size() ){
            channel = adsChannels.get(chanelNumber);
            
        }
         return channel;
    }

    public ChannelModel getAccelerometerChannel (int chanelNumber) {
        ChannelModel channel = null;
        if ( chanelNumber < accelerometerChannels.size() ){
            channel = accelerometerChannels.get(chanelNumber);

        }
        return channel;
    }
    

    public void addChannel(ChannelModel channel, ChannelType channelType) {
        if (channelType == ChannelType.ADS){
            adsChannels.add( (AdsChannelModel)channel );
        }
        if (channelType == ChannelType.ACCELEROMETER){
            accelerometerChannels.add( channel );
        }
    }

    public ArrayList<ChannelModel> getActiveChannels() {
        ArrayList<ChannelModel>  activeChannels = new ArrayList<ChannelModel>();
        for (ChannelModel channel : adsChannels) {
            if (channel.getDivider() != 0){
                activeChannels.add(channel);
            }
        }
        for (ChannelModel channel : accelerometerChannels) {
            if (channel.getDivider() != 0){
                activeChannels.add(channel);
            }
        }
        return  activeChannels;
    }

    public ArrayList<ChannelModel> getActiveChannels(ChannelType channelType) {
        ArrayList<ChannelModel>  activeChannels = new ArrayList<ChannelModel>();
        if (channelType == ChannelType.ADS) {
            for (ChannelModel channel : adsChannels) {
                if (channel.getDivider() != 0){
                    activeChannels.add(channel);
                }
            }
        }
        else if (channelType == ChannelType.ACCELEROMETER) {
            for (ChannelModel channel : accelerometerChannels) {
                if (channel.getDivider() != 0){
                    activeChannels.add(channel);
                }
            }
        }
        return  activeChannels;
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
        return 0x40;
//        return 0x00;
    }
    
    public int intTestEnabledBits(){
        boolean isCh1Test = adsChannels.get(0).getCommutatorState().equals(CommutatorState.TEST_SIGNAL);
        boolean isCh2Test = adsChannels.get(1).getCommutatorState().equals(CommutatorState.TEST_SIGNAL);
        return (isCh1Test | isCh2Test) ? 0x03 : 0;
    }
    
    public int rldLoffSenseBit() {
        return 0x10;
    }
}
