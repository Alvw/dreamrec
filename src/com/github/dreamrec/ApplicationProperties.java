package com.github.dreamrec;

import com.github.dreamrec.ads.CommutatorState;
import com.github.dreamrec.ads.Gain;
import com.github.dreamrec.ads.Sps;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;

/**
 *
 */
public class ApplicationProperties {
    private static final Log log = LogFactory.getLog(ApplicationProperties.class);
    private static final String APPLICATION_PROPERTIES = "application.properties";
    private static final String X_SIZE = "xSize";
    public static final String DIRECTORY_NAME = "data_save_directory";
    public static final String DATA_FREQUENCY = "frequency";
    public static final String REPAINT_DELAY = "repaintDelay";
    public static final String COM_PORT_NAME = "comPort";
    public static final String FREQUENCY_DIVIDER = "frequencyDivider";
    public static final String HI_PASS_BUFFER_SIZE = "hiPassBufferSize";
    public static final String LO_PASS_BUFFER_SIZE = "loPassBufferSize";

    public static final String SPS = "sps";
    public static final String ACCELEROMETER_ENABLED = "accelerometerEnabled";

    public static final String CHANNEL_HI_PASS_BUFFER_SIZE = "hiPassBufferSize_channel";
    public static final String CHANNEL_DIVIDER = "divider_channel";
    public static final String CHANNEL_GAIN = "gain_channel";
    public static final String CHANNEL_COMMUTATOR_STATE = "commutatorState_channel";
    public static final String CHANNEL_LOFF_ENABLED = "loffEnabled_channel";
    public static final String CHANNEL_NAME = "name_channel";
    public static final String CHANNEL_RLD_SENSE_ENABLED = "rldSenseEnabled_channel";

    public static final String ACCELEROMETER_HI_PASS_BUFFER_SIZE = "hiPassBufferSize_accelerometer";
    public static final String ACCELEROMETER_DIVIDER = "divider_accelerometer";
    public static final String ACCELEROMETER_NAME = "name_accelerometer";
    
    public static final String NUMBER_OF_CHANNELS = "numberOfChannels";

    private PropertiesConfiguration config;

    public ApplicationProperties() {
        try {
            config = new PropertiesConfiguration(APPLICATION_PROPERTIES);
        } catch (ConfigurationException e) {
            log.error(e);
            JOptionPane.showMessageDialog(null, "Error reading from properties file: " + APPLICATION_PROPERTIES);
        }
    }

    public int getNumberOfChannels () {
        return config.getInt(NUMBER_OF_CHANNELS);
    }

    public String getComPortName() {
        return config.getString(COM_PORT_NAME);
    }

    public int getFrequencyDivider() {
        return config.getInt(FREQUENCY_DIVIDER);
    }

    public int getHiPassBufferSize() {
        return config.getInt(HI_PASS_BUFFER_SIZE);
    }

    public int getLoPassBufferSize() {
        return config.getInt(LO_PASS_BUFFER_SIZE);
    }

    public int getRepaintDelay() {
        return config.getInt(REPAINT_DELAY);
    }

    public int getIncomingDataFrequency() {
        return config.getInt(DATA_FREQUENCY);
    }

    public int getXSize() {
        return config.getInt(X_SIZE);
    }

    public void setXSize(int xSize) {
        config.setProperty(X_SIZE, xSize);
    }

    public void setLastVisitedDirectory(String directory) {
        config.setProperty(DIRECTORY_NAME, directory);
    }

    public void setSps(int spsValue) {
        config.setProperty(SPS, spsValue);
    }

    public void setChannelDivider(int channelNumber, int divider){
        config.setProperty(CHANNEL_DIVIDER+channelNumber, divider);
    }

    public void setAccelerometerDivider(int channelNumber, int divider){
        config.setProperty(ACCELEROMETER_DIVIDER+channelNumber, divider);
    }

    public String getLastVisitedDirectory() {
        return config.getString(DIRECTORY_NAME);
    }

    public Sps getSps() {
        for (Sps sps : Sps.values()) {
            if (sps.getValue() == config.getInt(SPS)) {
                return sps;
            }
        }
        String msg = "Invalid sps value in application.properties file";
        log.error(msg);
        JOptionPane.showMessageDialog(null, msg);
        throw new IllegalArgumentException(msg);
    }

    public boolean isAccelerometerEnable() {
        return config.getBoolean(ACCELEROMETER_ENABLED);
    }


    public int getAccelerometerDivider() {

        return config.getInt(ACCELEROMETER_DIVIDER);
    }

    public int getAccelerometerHiPassBufferSize() {
        return config.getInt(ACCELEROMETER_HI_PASS_BUFFER_SIZE);
    }

    public String getAccelerometerName(int accelerometerChannelNumber) {
        return config.getString(ACCELEROMETER_NAME +accelerometerChannelNumber);
    }

    public String getChannelName(int channelNumber) {
        return config.getString(CHANNEL_NAME+channelNumber);
    }


    public int getChannelHiPassBufferSize(int channelNumber) {
        return config.getInt(CHANNEL_HI_PASS_BUFFER_SIZE+channelNumber);
    }

    public int getChannelDivider(int channelNumber) {

        return config.getInt(CHANNEL_DIVIDER+channelNumber);
    }

    public Gain getChannelGain(int channelNumber) {
        for (Gain gain : Gain.values()) {
            if (gain.getValue() == config.getInt(CHANNEL_GAIN+channelNumber)) {
                return gain;
            }
        }
        String msg = "Invalid ch1Gain value in application.properties file";
        log.error(msg);
        JOptionPane.showMessageDialog(null, msg);
        throw new IllegalArgumentException(msg);
    }

    public CommutatorState getChannelCommutatorState(int channelNumber) {
        return CommutatorState.valueOf(config.getString(CHANNEL_COMMUTATOR_STATE+channelNumber));
    }

    public boolean isChannelLoffEnable(int channelNumber) {
        return config.getBoolean(CHANNEL_LOFF_ENABLED+channelNumber);
    }


    public boolean isChannelRldSenseEnable(int channelNumber) {
        return config.getBoolean(CHANNEL_RLD_SENSE_ENABLED+channelNumber);
    }


    public void save() {
        try {
            config.save(APPLICATION_PROPERTIES);

        } catch (ConfigurationException e) {
            log.error(e);
            JOptionPane.showMessageDialog(null, "Error saving to properties file: " + APPLICATION_PROPERTIES);
        }
    }
}
