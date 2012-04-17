package com.github.dreamrec;

/**
 *
 */
public class DebugDataProvider implements IDataProvider {

    public void startRecording() {
    }

    public void stopRecording() {
    }

    public double getIncomingDataFrequency() {
        throw new UnsupportedOperationException("todo");
    }
    public long getStartTime() {
        throw new UnsupportedOperationException("todo");
    }
    public int poll() {
        throw new UnsupportedOperationException("todo");
    }

    public int available() {
        throw new UnsupportedOperationException("todo");
    }
}
