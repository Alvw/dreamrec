package com.github.dreamrec;

/**
 *
 */
public interface IDataProvider {

    void startRecording() throws ApplicationException;
    void stopRecording();
    double getIncomingDataFrequency();
    long getStartTime();
    short ch1Poll();
    int ch1Size();
    short ch2Poll();
    int ch2Size();
    short acc1Poll();
    int acc1Size();
    short acc2Poll();
    int acc2Size();
    short acc3Poll();
    int acc3Size();
}
