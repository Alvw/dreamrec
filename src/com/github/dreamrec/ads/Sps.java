package com.github.dreamrec.ads;

/**
 *
 */
public enum Sps {
 //   S125(0, 125, "125 Hz"),
    S250(1, 250, "250"),
    S500(2, 500, "500"),
    S1000(3, 1000, "1000"),
    S2000(4, 2000, "2000"),
    S4000(5, 4000, "4000"),
    S8000(6, 8000, "8000");

    private int registerBits;
    private int value;
    String label;
    private int[] adsChannelsAvailableDividers;
    private int[] accelerometerChannelsAvailableDividers;


    private Sps(int registerBits, int value, String label) {
        this.registerBits = registerBits;
        this.value = value;
        this.label = label;
        adsChannelsAvailableDividers = new int[]{1, 2, 5, 10, 25, 50};
        if (value ==  250) {
            accelerometerChannelsAvailableDividers = new int[] {25};
        }
        else{
            accelerometerChannelsAvailableDividers = new int[] {50};
        }
    }

    public Integer[] getAdsChannelsAvailableFrequencies(){
        Integer[] frequencies = new Integer[adsChannelsAvailableDividers.length];
        for (int i = 0; i < adsChannelsAvailableDividers.length; i++) {
            frequencies[i] = value / adsChannelsAvailableDividers[i];
        }
        return frequencies;
    }

    public Integer[] getAccelerometerChannelsAvailableFrequencies(){
        Integer[] frequencies = new Integer[accelerometerChannelsAvailableDividers.length];
        for (int i = 0; i < accelerometerChannelsAvailableDividers.length; i++) {
            frequencies[i] = value / accelerometerChannelsAvailableDividers[i];
        }
        return frequencies;
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

    @Override
    public String toString(){
        return label;
    }
}
