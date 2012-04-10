package com.github.dreamrec;

/**
 *
 */
public interface IncomingDataProvider<T> {

    void StartRecording();

    void StopRecording();

    double getIncomingDataFrequency();

    long getStartTime();

    void addDataListener();
}
