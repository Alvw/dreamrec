package com.github.dreamrec;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class FrameDecoder {

    private int frameIndex;

    public static final int FRAME_SIZE = 8;   //Frame size for 2 chanels
    private int[] rawFrame = new int[FRAME_SIZE];
    private Queue<int[]> decodedFramesQueue = new ConcurrentLinkedQueue<int[]>();
    private static final Log log = LogFactory.getLog(FrameDecoder.class);

    public void addByte(int inByte) {
        if (frameIndex == 0 && inByte == 192) {
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
        int[] decodedFrame = new int[3];
        decodedFrame[0] = rawFrame[1];
        decodedFrame[1] = ((rawFrame[2] << 24) + ((rawFrame[3]) << 16) + (rawFrame[4] << 8)) / 256;
        decodedFrame[2] = ((rawFrame[5] << 24) + ((rawFrame[6]) << 16) + (rawFrame[7] << 8)) / 256;
        decodedFramesQueue.offer(decodedFrame);
        /*decodedFrame[3] = (rawFrame[6] * 256 + rawFrame[5])-512;
        decodedFrame[4] = (rawFrame[8] * 256 + rawFrame[7])-512;
        decodedFrame[5] = (rawFrame[10] * 256 + rawFrame[9])-512;*/
    }

    public int size(){
        return decodedFramesQueue.size();
    }

    public int[] poll(){
        return decodedFramesQueue.poll();
    }

}
