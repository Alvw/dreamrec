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
        return 0;
    }

    public long getStartTime() {
        throw new UnsupportedOperationException("todo");
    }

    public void addDataListener() {
        throw new UnsupportedOperationException("todo");
    }
}
