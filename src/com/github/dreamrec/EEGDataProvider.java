package com.github.dreamrec;

import com.webkitchen.eeg.acquisition.IRawSampleListener;
import com.webkitchen.eeg.acquisition.RawSample;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.LinkedList;

import static com.github.dreamrec.ApplicationSettings.APPLICATION_PROPERTIES;

/**
 *
 */
public class EEGDataProvider implements IncomingDataProvider, IRawSampleListener {

    private static final Log log = LogFactory.getLog(EEGDataProvider.class);
    private static final int FREQUENCY_DIVIDER = 25;
    private int packetNumber = -1;
    private final int chanel;
    private LinkedList<Integer>     outputDataQueue = new LinkedList<Integer>();
        private int incomingDataSum;// to calculate  average for 25 incoming values and reduce data frequency;
        private int incomingDataCounter;// used to trigger averaging sum calculation
        private AverageInvocationDivider averageInvocationDivider;

    public EEGDataProvider() throws ApplicationException {
        try {
            PropertiesConfiguration config = new PropertiesConfiguration(APPLICATION_PROPERTIES);
            chanel = config.getInt("chanel");
        } catch (ConfigurationException e) {
            String msg = "Error while loading the properties file: " + APPLICATION_PROPERTIES;
            log.error(msg);
            throw new ApplicationException(msg);
        }
       /* averageInvocationDivider = new AverageInvocationDivider(FREQUENCY_DIVIDER) {
            @Override
            protected void invoke(Integer averageValue) {
                outputDataQueue.add(averageValue);
            }
        };*/
    }

    public void StartRecording() {
    }

    public void StopRecording() {
    }

    public double getIncomingDataFrequency() {
        throw new UnsupportedOperationException("todo");
    }

    public long getStartTime() {
        throw new UnsupportedOperationException("todo");
    }

    public int read() {
        throw new UnsupportedOperationException("todo");
    }

    public int available() {
        throw new UnsupportedOperationException("todo");
    }

    public void receiveSample(RawSample rawSample) {
        checkLostPackets(rawSample.getPacketNumber());
        int dataValue = rawSample.getSamples()[chanel];
//        averageInvocationDivider.count(dataValue);
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
