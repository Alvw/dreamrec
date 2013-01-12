package com.github.dreamrec.ads;

/**
 *
 */
public enum HighPassFrequency {
    DISABLE (0, "Disabled"),
    HZ_01 (0.1 , "0.1 Hz"),
    HZ_1 (1, "1 Hz"),
    HZ_10 (10, "10 Hz");

        
    private double value;
    private String label;

    private HighPassFrequency(double value, String label) {
        this.value = value;
        this.label = label;
    }

    public double getValue(){
        return value;
    } 
    
    public String getLabel(){
        return label;
    }
}

