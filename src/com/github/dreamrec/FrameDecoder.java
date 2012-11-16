package com.github.dreamrec;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FrameDecoder {

    private int frameIndex;
    private int[] frame = new int[16];
    public static final int FRAME_SIZE = 11;   //frame size for chanel 1 only
    private FrameDecoderListener frameListener;
    private static final Log log = LogFactory.getLog(FrameDecoder.class);

    public void addByte(int inByte) {
        if (frameIndex == 0 && inByte == 192) {
            frame[frameIndex] = inByte;
            frameIndex++;
        } else if (frameIndex > 0 && frameIndex < (FRAME_SIZE - 1)) {
            frame[frameIndex] = inByte;
            frameIndex++;
        } else if (frameIndex == (FRAME_SIZE - 1)) {
            frame[frameIndex] = inByte;
            frameIndex = 0;
            notifyListeners();
        } else {
            log.warn("Lost frame. Frame index = " + frameIndex + " inByte = " + inByte);
            frameIndex = 0;
        }
    }

    private void notifyListeners() {
        frameListener.setFrameCounter250(frame[1]);
        int val = ((frame[2] << 24) + ((frame[3]) << 16) + (frame[4] << 8)) / 256;
        frameListener.setCh1Value(val);
        frameListener.setAcc1Value((frame[6] * 256 + frame[5])-512);
        frameListener.setAcc2Value((frame[8] * 256 + frame[7])-512);
        frameListener.setAcc3Value((frame[10] * 256 + frame[9])-512);
    }

    public void addListener(FrameDecoderListener listener) {
        frameListener = listener;
    }
}
