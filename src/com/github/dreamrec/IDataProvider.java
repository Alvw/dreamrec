package com.github.dreamrec;

/**
 *
 */
public interface IDataProvider {

    void StartRecording() throws ApplicationException;

    void StopRecording();

    double getIncomingDataFrequency();

    long getStartTime();

    int poll();

    int available();
}
