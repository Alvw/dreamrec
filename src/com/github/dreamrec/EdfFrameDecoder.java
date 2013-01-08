package com.github.dreamrec;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class EdfFrameDecoder {

    private int frameIndex;
    private long counter;
    long startTime;
    long stopTime;

    public static final int FRAME_SIZE = 154;

    private int[] rawFrame = new int[FRAME_SIZE];
    private Queue<Integer> ch1Queue = new ConcurrentLinkedQueue<Integer>();
    private Queue<Integer> ch2Queue = new ConcurrentLinkedQueue<Integer>();
    private static final Log log = LogFactory.getLog(FrameDecoder.class);

    public void addByte(int inByte) {
        if (frameIndex == 0 && inByte == 254) {
            rawFrame[frameIndex] = inByte;
            frameIndex++;
        } else if (frameIndex > 0 && frameIndex < (FRAME_SIZE - 1)) {
            rawFrame[frameIndex] = inByte;
            frameIndex++;
        } else if (frameIndex == (FRAME_SIZE - 1)) {
            rawFrame[frameIndex] = inByte;
            frameIndex = 0;
            onFrameReceived();
        } else {
            log.warn("Lost rawFrame. Frame index = " + frameIndex + " inByte = " + inByte);
            frameIndex = 0;
        }
    }

    private void onFrameReceived() {
        ch1Queue.offer(((rawFrame[153] << 24) + ((rawFrame[152]) << 16) + (rawFrame[151] << 8)) >> 8);
       /* for (int i = 0; i < 50; i++) {
            ch1Queue.offer(((rawFrame[(i*3+2)+1] << 24) + ((rawFrame[(i*3+1)+1]) << 16) + (rawFrame[i*3+1] << 8)) >> 8);
        }*/
//        ch2Queue.offer(((rawFrame[78] << 24) + ((rawFrame[77]) << 16) + (rawFrame[76] << 8)) / 256);
    }

    public int size(){
        return ch1Queue.size();
    }

    public int poll(){
        return ch1Queue.poll();
    }

}
