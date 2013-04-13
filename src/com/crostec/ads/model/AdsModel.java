package com.crostec.ads.model;

import java.util.ArrayList;

/**
 *
 */
public class AdsModel {
    
    public static final int MAX_DIV = 50;
    public static final int NUMBER_OF_ACCELEROMETER_CHANNELS = 3;

    private Sps sps;     // samples per second (sample rate)
    private String comPortName;

    private ArrayList<AdsChannelModel> adsChannels = new ArrayList<AdsChannelModel>();
    private ArrayList<ChannelModel> accelerometerChannels = new ArrayList<ChannelModel>();

    /**
     * Active channels = enabled channels;
     *
     * Methods where channel type (Ads or Accelerometer) is not specified explicitly at the name threats
     * adsChannels and  accelerometerChannels as common/united array of Channels
     */


    public int getNumberOfChannels() {
        return (adsChannels.size() + accelerometerChannels.size());
    }

    public int getNumberOfAdsChannels() {
        return adsChannels.size();
    }

    public int getNumberOfAccelerometerChannels() {
        return accelerometerChannels.size();
    }


    public int getNumberOfActiveChannels() {
        int number = 0;
        for (AdsChannelModel channel : adsChannels) {
            if (channel.isEnabled()) {
                number++;
            }
        }
        for (ChannelModel channel : accelerometerChannels) {
            if (channel.isEnabled()) {
                number++;
            }
        }
        return number;
    }


    public Divider getAccelerometerDivider() {
        Divider divider = null;
        if(accelerometerChannels.size() > 0){
            divider = accelerometerChannels.get(0).getDivider();
        }
        return divider;
    }

    public int getAccelerometerIntDivider() {
        int intDivider = 0;
        if(accelerometerChannels.size() > 0){
            intDivider = accelerometerChannels.get(0).getIntDivider();
        }
        return intDivider;
    }

    public boolean isAccelerometerEnabled() {
        boolean isAccelerometerEnabled = false;
        if (accelerometerChannels.size() > 0){
            isAccelerometerEnabled = accelerometerChannels.get(0).isEnabled();
        }
        return isAccelerometerEnabled;
    }


    // return general part of Accelerometer Channels names (without last char, that specifies accelerometer channel X,Y, Z)
    public String getAccelerometerName() {
        String name = null;
        if (accelerometerChannels.size() > 0){
            StringBuilder channelName = new StringBuilder(accelerometerChannels.get(0).getName());
            //delete last symbol
            channelName.deleteCharAt(channelName.length() - 1);
            name = channelName.toString();
        }
        return name;
    }

    // dividers of all active (enabled) Channels
    public Divider[] getActiveDividers() {
        Divider[] dividers = new Divider[getNumberOfActiveChannels()];
        int i = 0;
        for (ChannelModel channel : adsChannels) {
            if (channel.isEnabled()) {
                dividers[i] = channel.getDivider();
                i++;
            }
        }
        for (ChannelModel channel : accelerometerChannels) {
            if (channel.isEnabled()) {
                dividers[i] = channel.getDivider();
                i++;
            }
        }
        return dividers;
    }


    public AdsChannelModel getAdsChannel(int chanelNumber) {
        AdsChannelModel channel = null;
        if (chanelNumber < adsChannels.size()) {
            channel = adsChannels.get(chanelNumber);

        }
        return channel;
    }

    public ChannelModel getAccelerometerChannel(int chanelNumber) {
        ChannelModel channel = null;
        if (chanelNumber < accelerometerChannels.size()) {
            channel = accelerometerChannels.get(chanelNumber);

        }
        return channel;
    }


    public void addAdsChannel(AdsChannelModel adsChannel) {
        adsChannels.add(adsChannel);
    }

    public void addAccelerometerChannel(ChannelModel accelerometerChannel) {
        accelerometerChannels.add(accelerometerChannel);
    }

    public ArrayList<ChannelModel> getActiveChannels() {
        ArrayList<ChannelModel> activeChannels = new ArrayList<ChannelModel>();
        for (ChannelModel channel : adsChannels) {
            if (channel.isEnabled()) {
                activeChannels.add(channel);
            }
        }
        for (ChannelModel channel : accelerometerChannels) {
            if (channel.isEnabled()) {
                activeChannels.add(channel);
            }
        }
        return activeChannels;
    }

    public ArrayList<AdsChannelModel> getAdsActiveChannels() {
        ArrayList<AdsChannelModel> activeChannels = new ArrayList<AdsChannelModel>();
        for (AdsChannelModel channel : adsChannels) {
            if (channel.isEnabled()) {
                activeChannels.add(channel);
            }
        }
        return activeChannels;
    }

    public ArrayList<ChannelModel> getAccelerometerActiveChannels() {
        ArrayList<ChannelModel> activeChannels = new ArrayList<ChannelModel>();
        for (ChannelModel channel : accelerometerChannels) {
            if (channel.isEnabled()) {
                activeChannels.add(channel);
            }
        }
        return activeChannels;
    }

    /*
     *
     */
    public int getFrameSize() {
        int frameSize = 0;
        for (int i = 0; i < accelerometerChannels.size(); i++) {
            if (accelerometerChannels.get(i).isEnabled()) {
                frameSize += MAX_DIV / accelerometerChannels.get(i).getDivider().getValue();
            }
        }
        for (int i = 0; i < adsChannels.size(); i++) {
            if (adsChannels.get(i).isEnabled()) {
                frameSize += MAX_DIV / adsChannels.get(i).getDivider().getValue();
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

    public String getComPortName() {
        return comPortName;
    }

    public void setComPortName(String comPortName) {
        this.comPortName = comPortName;
    }


    public int loffComparatorEnabledBit() {
        return 0x40;
//        return 0x00;
    }

    public int intTestEnabledBits() {
        boolean isCh1Test = adsChannels.get(0).getCommutatorState().equals(CommutatorState.TEST_SIGNAL);
        boolean isCh2Test = adsChannels.get(1).getCommutatorState().equals(CommutatorState.TEST_SIGNAL);
        return (isCh1Test | isCh2Test) ? 0x03 : 0;
    }

    public int rldLoffSenseBit() {
        return 0x10;
    }
}
