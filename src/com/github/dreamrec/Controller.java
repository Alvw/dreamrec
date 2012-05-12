package com.github.dreamrec;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
    public static int CURSOR_SCROLL_STEP = 1; //in points
    private boolean isAutoScroll = false;

    public Controller(Model _model, IDataProvider dataProvider) {
        this.dataProvider = dataProvider;
        this.model = _model;
    }

    public void setMainWindow(MainWindow _mainWindow) {
        this.mainWindow = _mainWindow;
        repaintTimer = new Timer(REPAINT_DELAY, new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                updateModel();
                mainWindow.repaint();
            }
        });
    }

    protected void updateModel() {
        while (dataProvider.available() > 0) {
            model.addEyeData(dataProvider.poll());
        }
        if (isAutoScroll) {
            model.setFastGraphIndexMaximum();
        }
    }

    public void saveToFile() {
        new DataSaveManager().saveToFile(mainWindow, model);
    }

    public void readFromFile() {
        new DataSaveManager().readFromFile(mainWindow, model);
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

    public void changeXSize(int xSize) {
        model.setXSize(xSize);
        mainWindow.repaint();
    }

    public void scrollCursorForward() {
        model.moveFastGraph(model.getFastGraphIndex() + CURSOR_SCROLL_STEP * Model.DIVIDER);
        if (model.isFastGraphIndexMaximum()) {
            isAutoScroll = true;
        }
        mainWindow.repaint();
    }

    public void scrollCursorBackward() {
        model.moveFastGraph(model.getFastGraphIndex() - CURSOR_SCROLL_STEP * Model.DIVIDER);
        isAutoScroll = false;
        mainWindow.repaint();
    }

    public void moveCursor(int newPosition) {
        model.moveCursor(newPosition);
        mainWindow.repaint();
    }

    public void scrollSlowGraph(int scrollPosition) {
        model.moveSlowGraph(scrollPosition);
        isAutoScroll = false;
        mainWindow.repaint();
    }
}
