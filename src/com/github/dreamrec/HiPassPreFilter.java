package com.github.dreamrec;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 */
public class HiPassPreFilter {

    private List<Integer> rawData = new ArrayList<Integer>();
    private int bufferSize;
    private Queue<Short> filteredData = new ConcurrentLinkedQueue<Short>();
    private int divider;
    private static final Log log = LogFactory.getLog(HiPassPreFilter.class);

    public HiPassPreFilter(int bufferSize, int divider) {
        this.bufferSize = bufferSize;
        this.divider = divider;
    }

    public int size() {
        return filteredData.size() / divider;
    }

    public void add(int value) {
        rawData.add(value);
        if (rawData.size() == bufferSize+1) {
            rawData.remove(0);
        } else if (rawData.size()>bufferSize+1){
            throw new IllegalStateException("bufferSize exceeds maximum value: "+ rawData.size());
        }
        long rawDataBufferSum = 0;
        for (Integer val : rawData) {
            rawDataBufferSum+=val;
        }
        int filteredValue = value - (int) (rawDataBufferSum /rawData.size());
        if (filteredValue > Short.MAX_VALUE) {
            log.warn("Incoming value exceeds Short.MAX_VALUE: " + filteredValue);
            filteredValue = Short.MAX_VALUE;
        } if (filteredValue < Short.MIN_VALUE) {
            log.warn("Incoming value less than Short.MIN_VALUE: " + filteredValue);
            filteredValue = Short.MIN_VALUE;
        }
        filteredData.offer((short)filteredValue);
//        filteredData.offer(value);
    }

    public short poll() {
        int sum = 0;
        for (int i = 0; i < divider; i++) {
            sum += filteredData.poll();
        }
        return (short)(sum / divider);
    }
}
