package com.github.dreamrec;

import com.github.dreamrec.ads.*;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.omg.CORBA.PUBLIC_MEMBER;

import javax.swing.*;
import java.io.File;

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
    public static final String LO_PASS_BUFFER_SIZE = "loPassBufferSize";

    public static final String PATIENT_IDENTIFICATION = "patientIdentification";
    public static final String RECORDING_IDENTIFICATION = "recordingIdentification";

    public static final String SPS = "sps";

    public static final String CHANNEL_HI_PASS_FREQUENCY = "hiPassFrequencyChannel";
    public static final String CHANNEL_DIVIDER = "dividerChannel";
    public static final String CHANNEL_GAIN = "gainChannel";
    public static final String CHANNEL_COMMUTATOR_STATE = "commutatorStateChannel";
    public static final String CHANNEL_NAME = "nameChannel";
    public static final String CHANNEL_IS_ENABLED = "isEnabledChannel";
    public static final String CHANNEL_ELECTRODE_TYPE = "electrodeTypeChannel";
    public static final String CHANNEL_LOFF_ENABLED = "loffEnabledChannel";
    public static final String CHANNEL_RLD_SENSE_ENABLED = "rldSenseEnabledChannel";

    public static final String ACCELEROMETER_HI_PASS_FREQUENCY = "hiPassFrequencyAccelerometer";
    public static final String ACCELEROMETER_DIVIDER = "dividerAccelerometer";
    public static final String ACCELEROMETER_NAME = "nameAccelerometer";
    public static final String ACCELEROMETER_IS_ENABLED = "isEnabledAccelerometer";

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


    public int getNumberOfChannels() {
        return config.getInt(NUMBER_OF_CHANNELS);
    }

    public String getComPortName() {
        return config.getString(COM_PORT_NAME);
    }

    public void setComPortName(String comPortName) {
        config.setProperty(COM_PORT_NAME, comPortName);
    }

    public int getFrequencyDivider() {
        return config.getInt(FREQUENCY_DIVIDER);
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

    public void setLastVisitedDirectory(File directory) {
        if (directory != null) {
            config.setProperty(DIRECTORY_NAME, directory);
        }
    }

    public File getLastVisitedDirectory() {
        if (config.getString(DIRECTORY_NAME) != null) {
            return new File(config.getString(DIRECTORY_NAME));
        } else {
            return null;
        }
    }

    public Sps getSps() {
        try {
            return Sps.valueOf(config.getInt(SPS));
        } catch (IllegalArgumentException e) {
            String msg = "application.properties file " + e.getMessage();
            log.error(msg);
            JOptionPane.showMessageDialog(null, msg);
            throw new IllegalArgumentException(msg);
        }
    }

    public Divider getAccelerometerDivider() {
        try {
            return Divider.valueOf(config.getInt(ACCELEROMETER_DIVIDER));
        } catch (IllegalArgumentException e) {
            String msg = "application.properties file: " + e.getMessage();
            log.error(msg);
            JOptionPane.showMessageDialog(null, msg);
            throw new IllegalArgumentException(msg);
        }
    }

    public HiPassFrequency getAccelerometerHiPassFrequency() {
        try {
            HiPassFrequency hiPassFrequency;
            String hiPassFrequencyLabel = config.getString(ACCELEROMETER_HI_PASS_FREQUENCY);
            if (hiPassFrequencyLabel.equals(HiPassFrequency.DISABLED.getLabel())) {
                hiPassFrequency = HiPassFrequency.DISABLED;
            } else {
                Double hiPassFrequencyValue = new Double(hiPassFrequencyLabel);
                hiPassFrequency = HiPassFrequency.valueOf(hiPassFrequencyValue);
            }
            return hiPassFrequency;
        } catch (IllegalArgumentException e) {
            String msg = "application.properties file: " + e.getMessage();
            log.error(msg);
            JOptionPane.showMessageDialog(null, msg);
            throw new IllegalArgumentException(msg);
        }
    }


    public String getAccelerometerName(int channelNumber) {
        return config.getString(ACCELEROMETER_NAME + channelNumber);
    }

    public String getChannelName(int channelNumber) {
        return config.getString(CHANNEL_NAME + channelNumber);
    }

    public String getChannelElectrodeType(int channelNumber) {
        return config.getString(CHANNEL_ELECTRODE_TYPE + channelNumber);
    }

    public boolean isChannelEnabled(int channelNumber) {
        return config.getBoolean(CHANNEL_IS_ENABLED + channelNumber);
    }

    public void setChannelEnabled(int channelNumber, boolean isEnabled) {
        config.setProperty(CHANNEL_IS_ENABLED + channelNumber, isEnabled);
    }


    public boolean isAccelerometerEnabled() {
        return config.getBoolean(ACCELEROMETER_IS_ENABLED);
    }

    public void setAccelerometerEnabled(boolean isEnabled) {
        config.setProperty(ACCELEROMETER_IS_ENABLED, isEnabled);
    }


    public HiPassFrequency getChannelHiPassFrequency(int channelNumber) {
        try {
            HiPassFrequency hiPassFrequency;
            String hiPassFrequencyLabel = config.getString(CHANNEL_HI_PASS_FREQUENCY + channelNumber);
            if (hiPassFrequencyLabel.equals(HiPassFrequency.DISABLED.getLabel())) {
                hiPassFrequency = HiPassFrequency.DISABLED;

            } else {
                Double hiPassFrequencyValue = new Double(hiPassFrequencyLabel);
                hiPassFrequency = HiPassFrequency.valueOf(hiPassFrequencyValue);
            }
            return hiPassFrequency;
        } catch (IllegalArgumentException e) {
            String msg = "application.properties file: " + channelNumber + "channel " + e.getMessage();
            log.error(msg);
            JOptionPane.showMessageDialog(null, msg);
            throw new IllegalArgumentException(msg);
        }
    }

    public Divider getChannelDivider(int channelNumber) {
        try {
            return Divider.valueOf(config.getInt(CHANNEL_DIVIDER + channelNumber));
        } catch (IllegalArgumentException e) {
            String msg = "application.properties file: " + channelNumber + "channel " + e.getMessage();
            log.error(msg);
            JOptionPane.showMessageDialog(null, msg);
            throw new IllegalArgumentException(msg);
        }
    }

    public Gain getChannelGain(int channelNumber) {
        try {
            return Gain.valueOf(config.getInt(CHANNEL_GAIN + channelNumber));
        } catch (IllegalArgumentException e) {
            String msg = "application.properties file: " + channelNumber + "channel " + e.getMessage();
            log.error(msg);
            JOptionPane.showMessageDialog(null, msg);
            throw new IllegalArgumentException(msg);
        }
    }

    public CommutatorState getChannelCommutatorState(int channelNumber) {
        return CommutatorState.valueOf(config.getString(CHANNEL_COMMUTATOR_STATE + channelNumber));
    }


    public void setSps(Sps sps) {
        config.setProperty(SPS, sps);
    }

    public void setChannelDivider(int channelNumber, Divider divider) {
        config.setProperty(CHANNEL_DIVIDER + channelNumber, divider);
    }

    public void setAccelerometerDivider(Divider divider) {
        config.setProperty(ACCELEROMETER_DIVIDER, divider);
    }

    public void setChannelName(int channelNumber, String name) {
        config.setProperty(CHANNEL_NAME + channelNumber, name);
    }

    public void setAccelerometerName(int channelNumber, String name) {
        config.setProperty(ACCELEROMETER_NAME + channelNumber, name);
    }

    public void setChannelHiPassFrequency(int channelNumber, HiPassFrequency frequency) {
        config.setProperty(CHANNEL_HI_PASS_FREQUENCY + channelNumber, frequency);
    }

    public void setAccelerometerHiPassFrequency(HiPassFrequency frequency) {
        config.setProperty(ACCELEROMETER_HI_PASS_FREQUENCY, frequency);
    }

    public boolean isChannelLoffEnable(int channelNumber) {
        return config.getBoolean(CHANNEL_LOFF_ENABLED + channelNumber);
    }

    public String getPatientIdentification() {
        return config.getString(PATIENT_IDENTIFICATION);
    }

    public String getRecordingIdentification() {
        return config.getString(RECORDING_IDENTIFICATION);
    }

    public void setPatientIdentification(String identification) {
        config.setProperty(PATIENT_IDENTIFICATION, identification);
    }

    public void setRecordingIdentification(String identification) {
        config.setProperty(RECORDING_IDENTIFICATION, identification);
    }

    public boolean isChannelRldSenseEnable(int channelNumber) {
        return config.getBoolean(CHANNEL_RLD_SENSE_ENABLED + channelNumber);
    }

    public void setChannelRldSenseEnabled(int channelNumber, boolean isRldEnabled) {
        config.setProperty(CHANNEL_RLD_SENSE_ENABLED + channelNumber, isRldEnabled);
    }

    public void setChannelLoffEnabled(int channelNumber, boolean isLoffEnabled) {
        config.setProperty(CHANNEL_LOFF_ENABLED + channelNumber, isLoffEnabled);
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
