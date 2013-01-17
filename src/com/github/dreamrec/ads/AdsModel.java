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
    private ArrayList<ChannelModel> accelerometerChannels = new ArrayList<ChannelModel>();

    /**
     * Active channels - channels with divider != 0;
     *
     * Methods where channel type (Ads or Accelerometer) is not specified explicitly at the name threat
     * adsChannels and  accelerometerChannels as common (united) array of Channels
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
            if (channel.getDivider() != 0) {
                number++;
            }
        }
        for (ChannelModel channel : accelerometerChannels) {
            if (channel.getDivider() != 0) {
                number++;
            }
        }
        return number;
    }

    public int getNumberOfAdsActiveChannels() {
        int channelsNumber = 0;
        for (ChannelModel channel : adsChannels) {
            if (channel.getDivider() != 0) {
                channelsNumber++;
            }
        }
        return channelsNumber;
    }

    public int getNumberOfAccelerometerActiveChannels() {
        int channelsNumber = 0;
        for (ChannelModel channel : accelerometerChannels) {
            if (channel.getDivider() != 0) {
                channelsNumber++;
            }
        }
        return channelsNumber;
    }

    // dividers of all Channels (AdsChannels and AccelerometerChannels)
    public int[] getDividers() {
        int[] dividers = new int[getNumberOfChannels()];
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

    // dividers of all AdsChannels
    public int[] getAdsDividers() {
        int[] dividers = new int[getNumberOfAdsChannels()];
        for (int i = 0; i < adsChannels.size(); i++) {
            dividers[i] = adsChannels.get(i).getDivider();
        }
        return dividers;
    }

    // dividers of all AccelerometerChannels
    public int[] getAccelerometerDividers() {
        int[] dividers = new int[getNumberOfAccelerometerChannels()];
        for (int i = 0; i < accelerometerChannels.size(); i++) {
            dividers[i] = accelerometerChannels.get(i).getDivider();
        }
        return dividers;
    }

    // non-zero dividers of all Channels
    public int[] getActiveDividers() {
        int[] dividers = new int[getNumberOfActiveChannels()];
        int i = 0;
        for (ChannelModel channel : adsChannels) {
            if (channel.getDivider() != 0) {
                dividers[i] = channel.getDivider();
                i++;
            }
        }
        for (ChannelModel channel : accelerometerChannels) {
            if (channel.getDivider() != 0) {
                dividers[i] = channel.getDivider();
                i++;
            }
        }
        return dividers;
    }

    // non-zero dividers of all AdsChannels
    public int[] getActiveAdsChannelsDividers() {
        int[] dividers = new int[getNumberOfAdsActiveChannels()];
        int i = 0;
        for (ChannelModel channel : adsChannels) {
            if (channel.getDivider() != 0) {
                dividers[i] = channel.getDivider();
                i++;
            }
        }
        return dividers;
    }

    // non-zero dividers of all AccelerometerChannels
    public int[] getActiveAccelerometerDividers() {
        int[] dividers = new int[getNumberOfAdsActiveChannels()];
        int i = 0;
        for (ChannelModel channel : accelerometerChannels) {
            if (channel.getDivider() != 0) {
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
            if (channel.getDivider() != 0) {
                activeChannels.add(channel);
            }
        }
        for (ChannelModel channel : accelerometerChannels) {
            if (channel.getDivider() != 0) {
                activeChannels.add(channel);
            }
        }
        return activeChannels;
    }

    public ArrayList<AdsChannelModel> getAdsActiveChannels() {
        ArrayList<AdsChannelModel> activeChannels = new ArrayList<AdsChannelModel>();
        for (AdsChannelModel channel : adsChannels) {
            if (channel.getDivider() != 0) {
                activeChannels.add(channel);
            }
        }
        return activeChannels;
    }

    public ArrayList<ChannelModel> getAccelerometerActiveChannels() {
        ArrayList<ChannelModel> activeChannels = new ArrayList<ChannelModel>();
        for (ChannelModel channel : accelerometerChannels) {
            if (channel.getDivider() != 0) {
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
            if (accelerometerChannels.get(i).getDivider() != 0) {
                frameSize += MAX_DIV / accelerometerChannels.get(i).getDivider();
            }
        }
        for (int i = 0; i < adsChannels.size(); i++) {
            if (adsChannels.get(i).getDivider() != 0) {
                frameSize += MAX_DIV / adsChannels.get(i).getDivider();
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
