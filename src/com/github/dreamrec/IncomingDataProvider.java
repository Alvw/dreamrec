package com.github.dreamrec;

/**
 *
 */
public interface IncomingDataProvider {

    void StartRecording();

    void StopRecording();

    double getIncomingDataFrequency();

    long getStartTime();

    int read();

    int available();
}
