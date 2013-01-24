package com.github.dreamrec;


import com.github.dreamrec.ads.HiPassFrequency;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 *
 */
public class HiPassPreFilter {

    private List<Integer> rawData = new ArrayList<Integer>();
    private int bufferSize;
    private static final Log log = LogFactory.getLog(HiPassPreFilter.class);
    private HiPassFrequency cutOffFrequency;

    /*
    *   bufferSize = inputFrequency/cutOffFrequency
    */

    public HiPassPreFilter(int inputFrequency, HiPassFrequency cutOffFrequency) {
        this.cutOffFrequency = cutOffFrequency;
        if(cutOffFrequency == HiPassFrequency.DISABLED){
           bufferSize = 0;

        }
        else{
            bufferSize = (int) (inputFrequency / cutOffFrequency.getValue());

        }
    }

    public HiPassFrequency getCutOffFrequency() {
        return cutOffFrequency;
    }

    public short getFilteredValue(int value) {
        int filteredValue = value;


        if (bufferSize != 0){
            rawData.add(value);
            if (rawData.size() == bufferSize + 1) {
                rawData.remove(0);
            } else if (rawData.size() > bufferSize + 1) {
                throw new IllegalStateException("bufferSize exceeds maximum value: " + rawData.size());
            }
            long rawDataBufferSum = 0;

            for (int i = 0; i < rawData.size(); i++) {
                rawDataBufferSum += rawData.get(i) * i;
            }

            int rawDataSize = rawData.size();
            if (rawDataSize > 1) {
                filteredValue = value - (int) (rawDataBufferSum * 2 / (rawDataSize * (rawDataSize - 1)));
            }
        }

        if (filteredValue > Short.MAX_VALUE) {
            log.info("Incoming value exceeds Short.MAX_VALUE: " + filteredValue);
            filteredValue = Short.MAX_VALUE;
        }
        if (filteredValue < Short.MIN_VALUE) {
            log.info("Incoming value less than Short.MIN_VALUE: " + filteredValue);
            filteredValue = Short.MIN_VALUE;
        }

        return (short) filteredValue;
    }
}
