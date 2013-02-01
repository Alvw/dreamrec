package com.github.dreamrec.ads;

/**
 *
 */
public enum HiPassFrequency {
    DISABLED(0, "Disabled"),
    H001(0.01 , "0.01"),
    H01(0.1 , "0.1"),
    H1(1, "1");

    private double value;
    private String label;

    private HiPassFrequency(double value, String label) {
        this.value = value;
        this.label = label;
    }

    public static HiPassFrequency valueOf(double value) throws IllegalArgumentException{
        for (HiPassFrequency hiPassFrequency : HiPassFrequency.values()) {
            if (hiPassFrequency.getValue() == value) {
                return hiPassFrequency;
            }
        }
        String msg = "Invalid HiPassFilterFrequency value";
        throw new IllegalArgumentException(msg);
    }

    public double getValue(){
        return value;
    } 
    
    public String getLabel(){
        return label;
    }

    @Override
    public String toString(){
        return label;
    }
}

