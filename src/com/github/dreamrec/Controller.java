package com.github.dreamrec;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 *
 */
public class Controller {

    public static final int REPAINT_DELAY = 100;//milliseconds
    private Timer repaintTimer;
    private Model model;
    private IDataProvider dataProvider;
    private MainWindow mainWindow;
    private static final Log log = LogFactory.getLog(Controller.class);
    public static int CURSOR_SCROLL_STEP = 1; //in points
    private boolean isAutoScroll = false;

    public Controller(Model _model,  IDataProvider dataProvider) {
        this.dataProvider = dataProvider;
        this.model = _model;

    }

    public void setMainWindow(MainWindow mainWindow_){
        mainWindow = mainWindow_;
        //todo something with timer
        repaintTimer = new Timer(REPAINT_DELAY, new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                updateModel();
                mainWindow.repaint();
            }
        });
    }


    private void updateModel() {
        while (dataProvider.available() > 0) {
            model.addEyeData(dataProvider.poll());
            if(isAutoScroll){
               model.setFastGraphIndexMaximum();
            }
        }
    }

    public void saveToFile() {
        final JFileChooser fileChooser = new JFileChooser();
        int fileChooserState = fileChooser.showSaveDialog(mainWindow);
        if (fileChooserState == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                new DataSaveManager().saveToFile(file, model);
            } catch (ApplicationException e) {
                mainWindow.showMessage(e.getMessage());
            }
        }
    }

    public void readFromFile() {
        final JFileChooser fileChooser = new JFileChooser();
        int fileChooserState = fileChooser.showOpenDialog(mainWindow);
        if (fileChooserState == JFileChooser.APPROVE_OPTION) {
            try {
                File file = fileChooser.getSelectedFile();
                new DataSaveManager().readFromFile(file, model);
            } catch (ApplicationException e) {
                mainWindow.showMessage(e.getMessage());
            }
            mainWindow.repaint();
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
        isAutoScroll = true;
    }

    public void stopRecording() {
        dataProvider.stopRecording();
        repaintTimer.stop();
        isAutoScroll = false;
    }

    public void scrollCursorForward() {
        model.moveFastGraph(model.getFastGraphIndex()+CURSOR_SCROLL_STEP*Model.DIVIDER);
        if(model.isFastGraphIndexMaximum()){
            isAutoScroll = true;
        }
        mainWindow.repaint();
    }

    public void scrollCursorBackward() {
        model.moveFastGraph(model.getFastGraphIndex()- CURSOR_SCROLL_STEP*Model.DIVIDER);
        isAutoScroll = false;
        mainWindow.repaint();
    }

    public void moveCursor(int newPosition){
        model.moveCursor(newPosition);
    }

    public void scrollSlowGraph(int scrollPosition) {
        model.moveSlowGraph(scrollPosition);
        isAutoScroll = false;
    }
}
