package com.github.dreamrec.ads;

/**
 * 
 */
public abstract class ChannelModel {
    private int number;
    private int divider;
    private Gain gain = Gain.G6;
    private CommutatorState commutatorState = CommutatorState.INPUT_SHORT;
    protected boolean isLoffEnable;
    private int hiPassBufferSize;
    private String label;
    protected boolean isRldSenseEnabled;

    public boolean isRldSenseEnabled() {
        return isRldSenseEnabled;
    }

    public void setRldSenseEnabled(boolean rldSenseEnabled) {
        isRldSenseEnabled = rldSenseEnabled;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getDivider() {
        return divider;
    }

    public void setDivider(int divider) {
        this.divider = divider;
    }

    public int getHiPassBufferSize() {
        return hiPassBufferSize;
    }

    public void setHiPassBufferSize(int hiPassBufferSize) {
        this.hiPassBufferSize = hiPassBufferSize;
    }

    public Gain getGain() {
        return gain;
    }

    public void setGain(Gain gain) {
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
    
    public int enabledBit(){
        return divider == 0 ? 0x80 : 0;
    }
    
    public abstract int rldSenseEnabledBits();
    
    public abstract int loffSenseEnabledBits();
}
