package com.github.dreamrec;


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
    private Queue<Short> filteredData = new LinkedList<Short>();
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
        if (rawData.size() == bufferSize + 1) {
            rawData.remove(0);
        } else if (rawData.size() > bufferSize + 1) {
            throw new IllegalStateException("bufferSize exceeds maximum value: " + rawData.size());
        }
        long rawDataBufferSum = 0;

        for (int i = 0; i < rawData.size(); i++) {
            rawDataBufferSum += rawData.get(i) * i;
        }
        int filteredValue = value;
        int rawDataSize = rawData.size();
        if (rawDataSize > 1) {
            filteredValue = value - (int) (rawDataBufferSum * 2 / (rawData.size() * (rawData.size() - 1)));
        }
        if (filteredValue > Short.MAX_VALUE) {
            log.info("Incoming value exceeds Short.MAX_VALUE: " + filteredValue);
            filteredValue = Short.MAX_VALUE;
        }
        if (filteredValue < Short.MIN_VALUE) {
            log.info("Incoming value less than Short.MIN_VALUE: " + filteredValue);
            filteredValue = Short.MIN_VALUE;
        }
        filteredData.offer((short) filteredValue);
//        filteredData.offer(value);
    }

    public short poll() {
        int sum = 0;
        for (int i = 0; i < divider; i++) {
            sum += filteredData.poll();
        }
        return (short) (sum / divider);
    }
}
