package com.github.dreamrec.ads;

/**
 *
 */
public enum Gain {
    G1(1),
    G2(2),
    G3(3),
    G4(4),
    G6(0),
    G8(5),
    G12(6);

    private int registerBits;

    private Gain(int registerBits) {
        this.registerBits = registerBits;
    }

    public int getRegisterBits(){
        return registerBits;
    }
}
