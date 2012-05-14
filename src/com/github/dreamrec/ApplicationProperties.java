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
     public static final String APPLICATION_PROPERTIES = "application.properties";
     private static final String X_SIZE = "xSize";
     private static final String DATA_PROVIDER = "dataProvider";
    
    private PropertiesConfiguration config;

    public ApplicationProperties() {
        try {
            config = new PropertiesConfiguration(APPLICATION_PROPERTIES);
        } catch (ConfigurationException e) {
            log.error(e);
            JOptionPane.showMessageDialog(null, "Error reading from properties file: " + APPLICATION_PROPERTIES);
        }        
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
    
    public void save(){
        try {
            config.save(APPLICATION_PROPERTIES);
        } catch (ConfigurationException e) {
            log.error(e);
            JOptionPane.showMessageDialog(null, "Error saving to properties file: " + APPLICATION_PROPERTIES);
        }

    }
}
