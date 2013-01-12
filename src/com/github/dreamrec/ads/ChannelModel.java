package com.github.dreamrec.ads;

/**
 * 
 */
public abstract class ChannelModel {
    private int divider;
    private Gain gain = Gain.G6;
    private CommutatorState commutatorState = CommutatorState.INPUT_SHORT;
    protected boolean isLoffEnable;
    private int hiPassBufferSize;
    private String name;
    protected boolean isRldSenseEnabled;   // DRL

    public boolean isRldSenseEnabled() {
        return isRldSenseEnabled;
    }

    public void setRldSenseEnabled(boolean rldSenseEnabled) {
        isRldSenseEnabled = rldSenseEnabled;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract int getNumber();

    public int getDivider() {
        return divider;
    }

    public void setDivider(int divider) {
        this.divider = divider;
        if(divider == 0){
            commutatorState = CommutatorState.INPUT_SHORT;
            isLoffEnable = false;
            isRldSenseEnabled = false;
        }
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
