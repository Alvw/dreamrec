package com.github.dreamrec.ads;

/**
 *
 */
public class Channel_1Model extends ChannelModel{

    @Override
    public int getNumber() {
        return 0;
    }

    @Override
    public int rldSenseEnabledBits() {
        return isRldSenseEnabled ? 0x03 : 0;
    }

    @Override
    public int loffSenseEnabledBits() {
        return isLoffEnable ? 0x03 : 0;
    }
}
