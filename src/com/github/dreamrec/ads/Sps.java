package com.github.dreamrec.ads;

import java.util.ArrayList;

/**
 *
 */
public enum Sps {

    S250(1, 250),
    S500(2, 500),
    S1000(3, 1000),
    S2000(4, 2000);
//    S4000(5, 4000)
//    S8000(6, 8000);

    private int registerBits;
    private int value;
    private int accelerometerDivider;


    private Sps(int registerBits, int value) {
        this.registerBits = registerBits;
        this.value = value;
        accelerometerDivider = 10;
    }

    public static Sps valueOf(int value) throws IllegalArgumentException {
        for (Sps sps : Sps.values()) {
            if (sps.getValue() == value) {
                return sps;
            }
        }
        String msg = "Invalid Sps value";
        throw new IllegalArgumentException(msg);
    }

    public Integer[] getChannelsAvailableFrequencies(){
        Integer[] frequencies = new Integer[Divider.values().length];
        int i = 0;
        for (Divider divider : Divider.values()) {
            frequencies[i] = (Integer) value / divider.getValue();
            i++;
        }
        return frequencies;
    }

    public Integer getAccelerometerFrequency(){
        return value / accelerometerDivider;
    }

    public int getRegisterBits(){
        return registerBits;
    }
    
    public int getValue(){
        return value;
    }


    @Override
    public String toString(){
        return new Integer(value).toString();
    }
}
