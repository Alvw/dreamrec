package com.github.dreamrec;

import com.webkitchen.eeg.acquisition.EEGAcquisitionController;
import com.webkitchen.eeg.acquisition.IRawSampleListener;
import com.webkitchen.eeg.acquisition.RawSample;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.util.Date;

import static com.github.dreamrec.ApplicationSettings.APPLICATION_PROPERTIES;

/**
 *
 */
public class EEGDataProvider implements IncomingDataProvider, IRawSampleListener{

    private static final Log log = LogFactory.getLog(EEGDataProvider.class);
    private double dataFrequency;
    private static final int FREQUENCY_DIVIDER = 25;
    private int packetNumber = -1;
    private final int chanel;
    private AveragingBuffer averagingBuffer = new AveragingBuffer(FREQUENCY_DIVIDER);
    private long startTime;
    private long stopTime;

    public EEGDataProvider() throws ApplicationException {
        try {
            PropertiesConfiguration config = new PropertiesConfiguration(APPLICATION_PROPERTIES);
            chanel = config.getInt("chanel");
            dataFrequency = config.getDouble("frequency");
        } catch (ConfigurationException e) {
            String msg = "Error while loading the properties file: " + APPLICATION_PROPERTIES;
            log.error(msg);
            throw new ApplicationException(msg);
        }
         EEGAcquisitionController.getInstance().getChannelSampleGenerator().addSampleListener(this,new int[]{chanel+1});
    }

    public void StartRecording() throws ApplicationException {
        try {
            EEGAcquisitionController.getInstance().startReading(false);
        } catch (IOException e) {
            log.error(e);
            throw new ApplicationException("EEG machine reading failure ", e);
        }
        startTime = System.currentTimeMillis();
        log.info("StartTime: "+ new Date(startTime));
    }

    public void StopRecording() {
        stopTime = System.currentTimeMillis();
        EEGAcquisitionController.getInstance().stopReading();
        int numberOfIncomingPackets = averagingBuffer.getIncomingCounter();
        log.info("StopTime: "+new Date(stopTime));
        log.info("Predefined data frequency = " +  dataFrequency);
        log.info("Real incoming data frequency = " + numberOfIncomingPackets * 1000/(stopTime - startTime));
    }

    public double getIncomingDataFrequency() {
        return dataFrequency;
    }

    public long getStartTime() {
        return startTime;
    }

    public int read() {
       return averagingBuffer.read();
    }

    public int available() {
        return averagingBuffer.available();
    }

    public void receiveSample(RawSample rawSample) {
        checkLostPackets(rawSample.getPacketNumber());
        int incomingValue = rawSample.getSamples()[chanel];
        averagingBuffer.add(incomingValue);
    }

    private void checkLostPackets(int newPacketNumber) {
        if (packetNumber == -1) {
            packetNumber = newPacketNumber;
        }
        if (newPacketNumber - packetNumber != 1) {
            log.warn("Lost packet!!! Packet number = " + packetNumber + "; " + (newPacketNumber - packetNumber) + "packets were lost");
        }
    }
}
