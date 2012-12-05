package com.github.dreamrec;

import java.util.LinkedList;
import java.util.Queue;

/**
 *
 */
public class FrequencyDividingPreFilter {
    private int bufferSize;
    private Queue<Short> filteredData = new LinkedList<Short>();
    private int divider;

    public FrequencyDividingPreFilter(int divider) {
        this.divider = divider;
    }

    public int size() {
        return filteredData.size() / divider;
    }

    public void add(short value) {
        filteredData.offer(value);
    }

    public short poll() {
        int sum = 0;
        for (int i = 0; i < divider; i++) {
            sum += filteredData.poll();
        }
        return  (short)(sum / divider);
    }
}
