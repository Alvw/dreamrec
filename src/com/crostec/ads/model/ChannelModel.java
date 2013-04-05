package com.crostec.ads.model;


/**
 * 
 */
public class ChannelModel {
    protected Divider divider;
    private final String PHYSICAL_DIMENSION = "g";
    protected String name;
    protected boolean isEnabled;
    private final String  electrodeType = "none";


    public String getElectrodeType() {
        return electrodeType;
    }

    public boolean isPositiveOk() {
        return isPositiveOk;
    }

    public void setPositiveOk(boolean positiveOk) {
        isPositiveOk = positiveOk;
    }

    public boolean isNegativeOk() {
        return isNegativeOk;
    }

    public void setNegativeOk(boolean negativeOk) {
        isNegativeOk = negativeOk;
    }

    protected boolean  isPositiveOk;   // is positive electrode good connected
    protected boolean isNegativeOk;   // is negative electrode good connected


    public int getIntDivider(){
        int intDivider = 0;
        if (isEnabled){
            intDivider = divider.getValue();
        }
        return intDivider;

    }
    
    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public String getPhysicalDimension() {
        return PHYSICAL_DIMENSION;
    }

    public void setDivider(Divider divider) {
        this.divider = divider;
    }

    public Divider getDivider() {
        return divider;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }
}
