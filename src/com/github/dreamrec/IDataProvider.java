package com.github.dreamrec;

/**
 *
 */
public interface IDataProvider {

    void startRecording() throws ApplicationException;

    void stopRecording();

    double getIncomingDataFrequency();

    long getStartTime();

    int poll();

    int available();
}
