package com.github.dreamrec;

/**
 *
 */
public interface IDataProvider {

    void startRecording() throws ApplicationException;
    void stopRecording();
    int getIncomingDataFrequency();
    long getStartTime();
    int size();
    int[] poll();
}
