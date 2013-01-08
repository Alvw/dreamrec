package com.github.dreamrec.ads;

/**
 *
 */
public class Channel_2Model extends ChannelModel{
    @Override
    public int rldSenseEnabledBits() {
        return isRldSenseEnabled ? 0x0C : 0;
    }

    @Override
    public int loffSenseEnabledBits() {
        return isLoffEnable ? 0x0C : 0;
    }
}
