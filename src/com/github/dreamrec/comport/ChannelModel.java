package com.github.dreamrec.comport;

/**
 * 
 */
public class ChannelModel {
    private int number;
    private int divider;
    private int gain;
    private CommutatorState commutatorState;
    boolean isLoffEnable;

    public ChannelModel(int number, int divider) {
        this.number = number;
        this.divider = divider;
    }

    public int getGain() {
        return gain;
    }

    public void setGain(int gain) {
        this.gain = gain;
    }

    public CommutatorState getCommutatorState() {
        return commutatorState;
    }

    public void setCommutatorState(CommutatorState commutatorState) {
        this.commutatorState = commutatorState;
    }

    public boolean isLoffEnable() {
        return isLoffEnable;
    }

    public void setLoffEnable(boolean loffEnable) {
        isLoffEnable = loffEnable;
    }
}
