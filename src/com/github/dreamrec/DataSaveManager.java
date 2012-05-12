package com.github.dreamrec;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.github.dreamrec.ApplicationSettings.APPLICATION_PROPERTIES;

/**
 * Saves measurement data to file and reads from it
 */
public class DataSaveManager {


    public static final String DIRECTORY_NAME = "data_save_directory";
    private static final Log log = LogFactory.getLog(DataSaveManager.class);

    public void saveToFile(MainWindow mainWindow, Model model) {
        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Dream record file", "drm"));
        SimpleDateFormat format = new SimpleDateFormat("dd_MM_yyyy_HH_mm");
        String suggestedFileName = format.format(new Date(System.currentTimeMillis())) + ".drm";
        fileChooser.setSelectedFile(new File(suggestedFileName));
        setLastVisitedDirectory(fileChooser);
        int fileChooserState = fileChooser.showSaveDialog(mainWindow);
        if (fileChooserState == JFileChooser.APPROVE_OPTION) {
            saveDirectoryLocation(fileChooser);
            File file = fileChooser.getSelectedFile();
            DataOutputStream outStream = null;
            try {
                outStream = new DataOutputStream(new FileOutputStream(file));
                saveStateToStream(outStream, model);
            } catch (Exception e) {
                log.error(e);
                mainWindow.showMessage("Error while saving file " + file.getName());
            } finally {
                try {
                    outStream.close();
                } catch (IOException e) {
                    log.error(e);
                }
            }
        }
    }

    private void saveDirectoryLocation(JFileChooser fileChooser) {
        try {
            PropertiesConfiguration config = new PropertiesConfiguration(APPLICATION_PROPERTIES);
            config.setProperty(DIRECTORY_NAME, fileChooser.getCurrentDirectory().getPath());
            config.save(APPLICATION_PROPERTIES);
        } catch (ConfigurationException e) {
            log.error(e);
        }
    }

    private void setLastVisitedDirectory(JFileChooser fileChooser) {
        try {
            PropertiesConfiguration config = new PropertiesConfiguration(APPLICATION_PROPERTIES);
            String dirName =  config.getString(DIRECTORY_NAME);
            if(dirName != null){
                fileChooser.setCurrentDirectory(new File(dirName));
            }
        } catch (ConfigurationException e) {
            log.error(e);
        }
    }

    private void saveStateToStream(DataOutputStream outStream, Model model) throws IOException {
        outStream.writeLong(model.getStartTime());
        outStream.writeDouble(model.getFrequency());
        for (int i = 0; i < model.getEyeDataList().size(); i++) {
            outStream.writeInt(model.getEyeDataList().get(i).intValue());
        }
    }


    public void readFromFile(MainWindow mainWindow, Model model) {

        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Dream record", "drm"));
        setLastVisitedDirectory(fileChooser);
        int fileChooserState = fileChooser.showOpenDialog(mainWindow);
        if (fileChooserState == JFileChooser.APPROVE_OPTION) {
            saveDirectoryLocation(fileChooser);
            DataInputStream dataInputStream = null;
            try {
                File file = fileChooser.getSelectedFile();
                dataInputStream = new DataInputStream(new FileInputStream(file));
                loadStateFromInStream(dataInputStream, model);
            } catch (Exception e) {
                mainWindow.showMessage(e.getMessage());
            } finally {
                try {
                    dataInputStream.close();
                } catch (IOException e) {
                    log.error(e);
                    mainWindow.showMessage("Error reading file");
                }
            }
            mainWindow.repaint();
        }


    }

    private void loadStateFromInStream(DataInputStream inputStream, Model model) throws IOException {
        try {
            long startTime = inputStream.readLong();
            double frequency = inputStream.readDouble();
            model.clear();
            model.setFrequency(frequency);
            model.setStartTime(startTime);
            while (true) {
                model.addEyeData(inputStream.readInt());
            }
        } catch (EOFException e) {
            log.info("End of file");
        }
    }
}

