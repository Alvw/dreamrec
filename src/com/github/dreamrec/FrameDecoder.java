package com.github.dreamrec;

import com.github.dreamrec.comport.ComPortListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class FrameDecoder implements ComPortListener {

    private int frameIndex;
    private long counter;
    private int rawFrameSize;
    private int decodedFrameSize;
    private int[] rawFrame;
    private Queue<int[]> decodedFrameQueue;
    private static final Log log = LogFactory.getLog(FrameDecoder.class);

    public FrameDecoder(int decodedFrameSize) {
        this.decodedFrameSize = decodedFrameSize;
        rawFrameSize = (decodedFrameSize * 3) + 3; //3 bytes for each value + marker + counter + loff
        rawFrame = new int[rawFrameSize];
        decodedFrameQueue = new ConcurrentLinkedQueue<int[]>();
    }

    public void onByteReceived(int inByte) {
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
        for (int i = 0; i < 50; i++) {
            decodedFrame[i] = (((rawFrame[i * 3 + 3] << 24) + ((rawFrame[i * 3 + 2]) << 16) + (rawFrame[i * 3 + 1] << 8)) /256);
        }
        decodedFrameQueue.offer(decodedFrame);
        //todo check lost frames and loff status;
    }

    public boolean available() {
        return decodedFrameQueue.size()>0;
    }

    public int[] poll() {
        return decodedFrameQueue.poll();
    }
}
