package com.github.dreamrec.ads;

/**
 *
 */
public enum Gain {
    G1(1, 1),
    G2(2, 2),
    G3(3, 3),
    G4(4, 4),
    G6(0, 6),
    G8(5, 8),
    G12(6, 12);

    private int registerBits;
    private int value;

    private Gain(int registerBits, int value) {
        this.registerBits = registerBits;
        this.value = value;
    }

    public static Gain valueOf(int value) throws IllegalArgumentException {
        for (Gain gain : Gain.values()) {
            if (gain.getValue() == value) {
                return gain;
            }
        }
        String msg = "Invalid Gain value";
        throw new IllegalArgumentException(msg);
    }

    public int getRegisterBits() {
        return registerBits;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString(){
        return new Integer(value).toString();
    }
}
