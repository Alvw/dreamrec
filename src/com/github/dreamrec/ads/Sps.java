package com.github.dreamrec.ads;

/**
 *
 */
public enum Sps {
    S125(0, 125),
    S250(1, 250),
    S500(2, 500),
    S1000(3, 1000),
    S2000(4, 2000),
    S4000(5, 4000),
    S8000(6, 8000);

    private int registerBits;
    private int value;

    private Sps(int registerBits, int value) {
        this.registerBits = registerBits;
        this.value = value;
    }

    public int getRegisterBits(){
        return registerBits;
    }
    
    public int getValue(){
        return value;
    }
}
