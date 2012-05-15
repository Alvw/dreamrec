package com.github.dreamrec;

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
     private static final String DATA_PROVIDER = "dataProvider";
    public static final String DIRECTORY_NAME = "data_save_directory";
    public static final String CHANEL = "chanel";
    public static final String DATA_FREQUENCY = "frequency";
    private PropertiesConfiguration config;

    public ApplicationProperties() {
        try {
            config = new PropertiesConfiguration(APPLICATION_PROPERTIES);
        } catch (ConfigurationException e) {
            log.error(e);
            JOptionPane.showMessageDialog(null, "Error reading from properties file: " + APPLICATION_PROPERTIES);
        }        
    }

    public int getChanel(){
        return config.getInt(CHANEL);
    }

    public double getIncomingDataFrequency(){
        return config.getDouble(DATA_FREQUENCY);
    }

    public String getDataProvider() {
        return config.getString(DATA_PROVIDER);
    }

    public void setDataProvider(String dataProvider) {
        config.setProperty(DATA_PROVIDER,dataProvider);
    }

    public int getXSize() {
        return config.getInt(X_SIZE);
    }

    public void setXSize(int xSize) {
        config.setProperty(X_SIZE, xSize);
    }

    public void setLastVisitedDirectory(String directory){
        config.setProperty(DIRECTORY_NAME,directory);
    }

    public String getLastVisitedDirectory(){
        return config.getString(DIRECTORY_NAME);
    }
    
    public void save(){
        try {
            config.save(APPLICATION_PROPERTIES);
        } catch (ConfigurationException e) {
            log.error(e);
            JOptionPane.showMessageDialog(null, "Error saving to properties file: " + APPLICATION_PROPERTIES);
        }
    }
}
