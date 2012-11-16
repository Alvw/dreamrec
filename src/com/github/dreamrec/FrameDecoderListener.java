package com.github.dreamrec;

/**
 *
 */
public interface FrameDecoderListener {
    void setFrameCounter250(int value);
    void setCh1Value(int value);
    void setCh2Value(int value);
    void setAcc1Value(int value);
    void setAcc2Value(int value);
    void setAcc3Value(int value);
}
