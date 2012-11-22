package com.github.dreamrec;

/**
 *
 */
public interface IDataProvider {

    void startRecording() throws ApplicationException;
    void stopRecording();
    double getIncomingDataFrequency();
    long getStartTime();
    int ch1Poll();
    int ch1Size();
    int ch2Poll();
    int ch2Size();
    int acc1Poll();
    int acc1Size();
    int acc2Poll();
    int acc2Size();
    int acc3Poll();
    int acc3Size();
}
