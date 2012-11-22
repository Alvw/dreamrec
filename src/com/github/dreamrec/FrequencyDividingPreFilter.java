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
public class FrequencyDividingPreFilter {
    private int bufferSize;
    private Queue<Integer> filteredData = new ConcurrentLinkedQueue<Integer>();
    private int divider;

    public FrequencyDividingPreFilter(int divider) {
        this.bufferSize = bufferSize;
        this.divider = divider;
    }

    public int size() {
        return filteredData.size() / divider;
    }

    public void add(int value) {
        filteredData.offer(value);
    }

    public int poll() {
        int sum = 0;
        for (int i = 0; i < divider; i++) {
            sum += filteredData.poll();
        }
        return  (sum / divider);
    }
}
