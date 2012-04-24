package com.github.dreamrec;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

/**
 *
 */
public class Controller {

    public static final int REPAINT_DELAY = 1000;//milliseconds
    private Timer repaintTimer;
    private Model model;
    private IDataProvider dataProvider;
    private MainWindow mainWindow;
    private static final Log log = LogFactory.getLog(Controller.class);

    public Controller(Model _model, MainWindow _mainWindow, IDataProvider dataProvider) {
        this.dataProvider = dataProvider;
        this.mainWindow = _mainWindow;
        this.model = _model;
        repaintTimer = new Timer(REPAINT_DELAY,new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                updateModel();
                mainWindow.repaint();
            }
        });
    }

    private void updateModel() {
        while (dataProvider.available()>0) {
              model.addEyeData(dataProvider.poll());
        }
    }

    public void saveToFile(File file) {
        try {   //todo remove try catch duplicating code
            new DataSaveManager().saveToFile(file,model);
        } catch (ApplicationException e) {
            mainWindow.showMessage(e.getMessage());
        }
    }

    public void readFromFile(File file) {
        try {
            new DataSaveManager().readFromFile(file,model);
        } catch (ApplicationException e) {
             mainWindow.showMessage(e.getMessage());
        }
    }

    public void startRecording() {
        try {
            dataProvider.startRecording();
        } catch (ApplicationException e) {
            mainWindow.showMessage(e.getMessage());
        }
        model.clear();
        model.setFrequency(dataProvider.getIncomingDataFrequency());
        model.setStartTime(dataProvider.getStartTime());
        repaintTimer.start();
    }

    public void stopRecording() {
        dataProvider.stopRecording();
        repaintTimer.stop();
    }

}
