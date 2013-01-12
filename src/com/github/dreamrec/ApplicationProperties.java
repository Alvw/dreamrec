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
    public static final String CH_1_HI_PASS_BUFFER_SIZE = "ch1HiPassBufferSize";
    public static final String CH_1_DIVIDER = "ch1Divider";
    public static final String CH_1_GAIN = "ch1Gain";
    public static final String CH_1_COMMUTATOR_STATE = "ch1CommutatorState";
    public static final String CH_1_LOFF_ENABLED = "ch1LoffEnabled";
    public static final String CH_1_LABEL = "ch1Label";
    public static final String CH_1_RLD_SENSE_ENABLED = "ch1RldSenseEnabled";
    public static final String CH_2_HI_PASS_BUFFER_SIZE = "ch2HiPassBufferSize";
    public static final String CH_2_DIVIDER = "ch2Divider";
    public static final String CH_2_GAIN = "ch2Gain";
    public static final String CH_2_COMMUTATOR_STATE = "ch2CommutatorState";
    public static final String CH_2_LOFF_ENABLED = "ch2LoffEnabled";
    public static final String CH_2_LABEL = "ch2Label";
    public static final String CH_2_RLD_SENSE_ENABLED = "ch2RldSenseEnabled";
    
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

    public boolean isAccelerometerEnabled() {
        return config.getBoolean(ACCELEROMETER_ENABLED);
    }

    public int ch1HiPassBufferSize() {
        return config.getInt(CH_1_HI_PASS_BUFFER_SIZE);
    }

    public int ch1Divider() {

        return config.getInt(CH_1_DIVIDER);
    }

    public Gain ch1Gain() {
        for (Gain gain : Gain.values()) {
            if (gain.getValue() == config.getInt(CH_1_GAIN)) {
                return gain;
            }
        }
        String msg = "Invalid ch1Gain value in application.properties file";
        log.error(msg);
        JOptionPane.showMessageDialog(null, msg);
        throw new IllegalArgumentException(msg);
    }

    public CommutatorState ch1CommutatorState() {
        return CommutatorState.valueOf(config.getString(CH_1_COMMUTATOR_STATE));
    }

    public boolean ch1LoffEnabled() {
        return config.getBoolean(CH_1_LOFF_ENABLED);
    }

    public String ch1Label() {
        return config.getString(CH_1_LABEL);
    }

    public boolean ch1RldSenseEnabled() {
        return config.getBoolean(CH_1_RLD_SENSE_ENABLED);
    }

    //    ---------------
    public int ch2HiPassBufferSize() {
        return config.getInt(CH_2_HI_PASS_BUFFER_SIZE);
    }

    public int ch2Divider() {

        return config.getInt(CH_2_DIVIDER);
    }

    public Gain ch2Gain() {
        for (Gain gain : Gain.values()) {
            if (gain.getValue() == config.getInt(CH_2_GAIN)) {
                return gain;
            }
        }
        String msg = "Invalid ch2Gain value in application.properties file";
        log.error(msg);
        JOptionPane.showMessageDialog(null, msg);
        throw new IllegalArgumentException(msg);
    }

    public CommutatorState ch2CommutatorState() {
        return CommutatorState.valueOf(config.getString(CH_2_COMMUTATOR_STATE));
    }

    public boolean ch2LoffEnabled() {
        return config.getBoolean(CH_2_LOFF_ENABLED);
    }

    public String ch2Label() {
        return config.getString(CH_2_LABEL);
    }

    public boolean ch2RldSenseEnabled() {
        return config.getBoolean(CH_2_RLD_SENSE_ENABLED);
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
