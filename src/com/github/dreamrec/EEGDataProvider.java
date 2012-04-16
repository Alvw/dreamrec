package com.github.dreamrec;

/**
 *
 */
public class EEGDataProvider implements IncomingDataProvider{

    public void StartRecording() {
    }

    public void StopRecording() {
    }

    public double getIncomingDataFrequency() {
        throw new UnsupportedOperationException("todo");
    }

    public long getStartTime() {
        throw new UnsupportedOperationException("todo");
    }

    public int read() {
        throw new UnsupportedOperationException("todo");
    }

    public int available() {
        throw new UnsupportedOperationException("todo");
    }
}
