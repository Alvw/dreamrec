package com.github.dreamrec.ads;

/**
 *
 */
public class AdsChannelModel extends ChannelModel {

    private final String PHYSICAL_DIMENSION = "uV";
    private Gain gain;
    private CommutatorState commutatorState;
    protected boolean isLoffEnable = true;
    private boolean isRldSenseEnabled = true;   // DRL
    private int rldSenseEnabledBits;
    private int loffSenseEnabledBits;
    private String  electrodeType;


    public void setElectrodeType(String electrodeType) {
        this.electrodeType = electrodeType;
    }

    public String getElectrodeType() {
        return electrodeType;
    }

    public String getPhysicalDimension() {
        return PHYSICAL_DIMENSION;
    }

    @Override
    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
        if (!enabled){
            commutatorState = CommutatorState.INPUT_SHORT;
        }
    }

    public void setLoffEnable(boolean loffEnable) {
        isLoffEnable = loffEnable;
    }

    public void setRldSenseEnabled(boolean rldSenseEnabled) {
        isRldSenseEnabled = rldSenseEnabled;
    }

    public boolean isLoffEnable() {
        return isLoffEnable;
    }

    public boolean isRldSenseEnabled() {
        return isRldSenseEnabled;
    }

    public Gain getGain() {
        return gain;
    }

    public void setGain(Gain gain) {
        this.gain = gain;
    }

    public CommutatorState getCommutatorState() {
        return (!isEnabled) ? CommutatorState.INPUT_SHORT : commutatorState;
    }

    public void setCommutatorState(CommutatorState commutatorState) {
        this.commutatorState = commutatorState;
    }


    public int enabledBit() {
        return (!isEnabled) ? 0x80 : 0;
    }

    public int getRldSenseEnabledBits() {
        return (isRldSenseEnabled & isEnabled) ? rldSenseEnabledBits : 0;
    }

    public void setRldSenseEnabledBits(int rldSenseEnabledBits) {
        this.rldSenseEnabledBits = rldSenseEnabledBits;
    }

    public int getLoffSenseEnabledBits() {
        if (!isEnabled) {
            return 0;
        } else{
            return isLoffEnable ? loffSenseEnabledBits : 0;
        }
    }

    public void setLoffSenseEnabledBits(int loffSenseEnabledBits) {
        this.loffSenseEnabledBits = loffSenseEnabledBits;
    }
}
