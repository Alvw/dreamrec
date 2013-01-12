package com.github.dreamrec;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class FrameDecoder {

    private int frameIndex;
    private long counter;
    private int rawFrameSize;
    private int decodedFrameSize;
    private int[] rawFrame;
    private Queue<int[]> decodedFrameQueue;
    private static final Log log = LogFactory.getLog(FrameDecoder.class);

    public void init(int decodedFrameSize) {
        this.decodedFrameSize = decodedFrameSize;
        rawFrameSize = (decodedFrameSize * 3) + 3; //3 bytes for each value + marker + counter + loff
        rawFrame = new int[rawFrameSize];
        decodedFrameQueue = new ConcurrentLinkedQueue<int[]>();
    }

    public void addByte(int inByte) {
        if (frameIndex == 0 && inByte == 254) {
            rawFrame[frameIndex] = inByte;
            frameIndex++;
        } else if (frameIndex > 0 && frameIndex < (rawFrameSize - 1)) {
            rawFrame[frameIndex] = inByte;
            frameIndex++;
        } else if (frameIndex == (rawFrameSize - 1)) {
            rawFrame[frameIndex] = inByte;
            frameIndex = 0;
            onFrameReceived();
        } else {
            log.warn("Lost rawFrame. Frame index = " + frameIndex + " inByte = " + inByte);
            frameIndex = 0;
        }
    }

    private void onFrameReceived() {
        int[] decodedFrame = new int[decodedFrameSize];
        for (int i = 0; i < decodedFrameSize; i++) {
            decodedFrame[i] = (((rawFrame[i * 3 + 3] << 24) + ((rawFrame[i * 3 + 2]) << 16) + (rawFrame[i * 3 + 1] << 8)) >> 8);
        }
        //todo check lost frames and loff status;
    }

    public int size() {
        return decodedFrameQueue.size();
    }

    public int[] poll() {
        return decodedFrameQueue.poll();
    }
}
