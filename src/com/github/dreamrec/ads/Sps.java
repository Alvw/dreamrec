package com.github.dreamrec.ads;

/**
 *
 */
public enum Sps {
    S125(0),
    S250(1),
    S500(2),
    S1000(3),
    S2000(4),
    S4000(5),
    S8000(6);

    private int registerBits;

    private Sps(int registerBits) {
        this.registerBits = registerBits;
    }

    public int getRegisterBits(){
        return registerBits;
    }
}
