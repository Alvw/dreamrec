package com.github.dreamrec.ads;

/**
 *
 */
public enum Sps {
    S125(0, 125, "125 Hz"),
    S250(1, 250, "250 Hz"),
    S500(2, 500, "500 Hz"),
    S1000(3, 1000, "1000 Hz"),
    S2000(4, 2000, "2000 Hz"),
    S4000(5, 4000, "4000 Hz"),
    S8000(6, 8000, "8000 Hz");

    private int registerBits;
    private int value;
    String label;

    private Sps(int registerBits, int value, String label) {
        this.registerBits = registerBits;
        this.value = value;
        this.label = label;
    }

    public int getRegisterBits(){
        return registerBits;
    }
    
    public int getValue(){
        return value;
    }
    
    public String getLabel(){
        return label;
    }
}
