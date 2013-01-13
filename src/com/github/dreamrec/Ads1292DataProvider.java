package com.github.dreamrec;

import com.github.dreamrec.comport.ComPort;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Date;

/**
 *
 */
public class Ads1292DataProvider implements IDataProvider{

    private int frequencyDivider;
    private ComPort comPort;
    private static final Log log = LogFactory.getLog(Ads1292DataProvider.class);
    private long startTime;
    private long stopTime;
    private int dataFrequency;
    private ApplicationProperties applicationProperties;


    public Ads1292DataProvider(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
        frequencyDivider = applicationProperties.getFrequencyDivider();
        dataFrequency = applicationProperties.getIncomingDataFrequency() / frequencyDivider;
    }

    public void startRecording() throws ApplicationException {
        try {
           /* comPort = ComPort.getInstance();
            comPort.addDataProvider(this);
            comPort.connect(applicationProperties.getComPortName());*/
//            comPort.writeToPort("y".getBytes());
        } catch (Exception e) {
            log.error(e);
            throw new ApplicationException("EEG machine reading failure ", e);
        }
        startTime = System.currentTimeMillis();
        log.info("StartTime: " + new Date(startTime));
    }

    public void stopRecording() {
        stopTime = System.currentTimeMillis();
        comPort.disconnect();
        log.info("StopTime: " + new Date(stopTime));
        log.info("Predefined data frequency = " + dataFrequency);
//        log.info("Real incoming data frequency = " + totalFrames * 1000.0 / (stopTime - startTime));
    }

    public int getIncomingDataFrequency() {
        return dataFrequency;
    }


    public long getStartTime() {
        return startTime;
    }

    public int size() {
//        return frameDecoder.available();
        throw new UnsupportedOperationException();
    }

    public int poll() {
//        return frameDecoder.poll();
        throw new UnsupportedOperationException();
    }

    public void receiveSample(int data) {
//        frameDecoder.addByte(data);
        throw new UnsupportedOperationException();
    }

}
