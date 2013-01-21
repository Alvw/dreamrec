package com.github.dreamrec.ads;

/**
 *
 */
public enum HiPassFrequency {
    DISABLED(0, "Disabled"),
    H001(0.01 , "0.01"),
    H01(0.1 , "0.1"),
    H1(1, "1"),
    H10(10, "10");

        
    private double value;
    private String label;

    private HiPassFrequency(double value, String label) {
        this.value = value;
        this.label = label;
    }

    public static HiPassFrequency valueOf( double value){
        if (value == H001.getValue()) {
            return H001;
        }
        else if (value == H01.getValue()) {
            return H01;
        }
        else if (value == H1.getValue()) {
            return H1;
        }
        else if (value == H10.getValue()) {
            return H10;
        }
        else {
            return DISABLED;
        }
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

