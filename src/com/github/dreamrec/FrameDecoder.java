package com.github.dreamrec;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FrameDecoder {

    private int frameCounter;
    private int[] frame = new int[4];
    private FrameDecoderListener frameListener;
    private static final Log log = LogFactory.getLog(FrameDecoder.class);

    public void addByte(int inByte) {
        if (frameCounter == 0 && inByte == 192) {
            frameCounter = 1;
        } else if (frameCounter == 1) {
            frame[0] = inByte;
            frameCounter = 2;
        } else if (frameCounter == 2) {
            frame[1] = inByte;
            frameCounter = 3;
        } else if (frameCounter == 3) {
            frame[2] = inByte;
            frameCounter = 0;
            notifyListeners(convertFrameToInt(frame));
            /* }  else if(frameCounter == 3){
            frame[2] = inByte;
            frameCounter = 0;
            notifyListeners(convertFrameToInt(frame));*/
        } else {
            log.warn("Lost frame. Frame counter = " + frameCounter + " inByte = " + inByte);
            frameCounter=0;
        }
    }

    private int convertFrameToInt(int[] frame) {
         int result = (frame[0]<<24)+((frame[1])<<16)+(frame[2]<<8);
         return result / 256;
        //int result = (frame[2] << 8) + ((frame[3]));
        // log.info("result = "+result);
        //return result;
    }

    private void notifyListeners(int value) {
        frameListener.onFrameReceived(value);
    }

    public void addListener(FrameDecoderListener listener) {
        frameListener = listener;
    }
}
