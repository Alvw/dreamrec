package com.github.dreamrec;

import com.github.dreamrec.comport.ComPort;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Date;

/**
 *
 */
public class Ads1292DataProvider implements IDataProvider, FrameDecoderListener{

    private int frequencyDivider =1;
    ComPort comPort;
    private static final Log log = LogFactory.getLog(Ads1292DataProvider.class);
    private long startTime;
    private long stopTime;
     private AveragingBuffer averagingBuffer;
    private double dataFrequency ;
    private FrameDecoder frameDecoder = new FrameDecoder();
    ApplicationProperties applicationProperties;

    public Ads1292DataProvider(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
        frameDecoder.addListener(this);
        frequencyDivider = applicationProperties.getFrequencyDivider();
        dataFrequency = 250.0 / frequencyDivider;
        averagingBuffer = new AveragingBuffer(frequencyDivider);
    }

    public void startRecording() throws ApplicationException {
           try {
               comPort = ComPort.getInstance();
               comPort.addDataProvider(this);
               comPort.connect(applicationProperties.getComPortName());
           } catch (Exception e) {
               log.error(e);
               throw new ApplicationException("EEG machine reading failure ", e);
           }
           startTime = System.currentTimeMillis();
           log.info("StartTime: " + new Date(startTime));
       }

       public void stopRecording() {
           stopTime = System.currentTimeMillis();
           comPort.writetoport("n".getBytes());
           comPort.disconnect();
           int numberOfIncomingPackets = averagingBuffer.getIncomingCounter();
           log.info("StopTime: " + new Date(stopTime));
           log.info("Predefined data frequency = " + dataFrequency);
           log.info("Real incoming data frequency = " + numberOfIncomingPackets * 1000.0 / (stopTime - startTime));
       }

    public double getIncomingDataFrequency() {
        return dataFrequency;
    }


    public long getStartTime() {
        return startTime;
    }

    public int poll() {
        return averagingBuffer.poll();
    }

    public int available() {
        return averagingBuffer.available();
    }

    public void receiveSample(int data) {
        frameDecoder.addByte(data);
    }

    public void onFrameReceived(int value) {
        averagingBuffer.add(value);
    }
}
