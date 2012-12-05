package com.github.dreamrec;

import com.github.dreamrec.edf.EdfHeaderData;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 */
public class Controller {

    private Timer repaintTimer;
    private Model model;
    private IDataProvider dataProvider;
    private MainWindow mainWindow;
    private ApplicationProperties applicationProperties;
    private static final Log log = LogFactory.getLog(Controller.class);
    public static int CURSOR_SCROLL_STEP = 1; //in points
    private boolean isAutoScroll = false;
     private HiPassPreFilter ch1PreFilter;
    private HiPassPreFilter ch2PreFilter;

    public Controller(Model model, ApplicationProperties applicationProperties) {
        this.model = model;
        this.applicationProperties = applicationProperties;
        int frequencyDivider = applicationProperties.getFrequencyDivider();
        int hiPassBufferSize = applicationProperties.getHiPassBufferSize();
        ch1PreFilter = new HiPassPreFilter(hiPassBufferSize,frequencyDivider);
        ch2PreFilter = new HiPassPreFilter(hiPassBufferSize,frequencyDivider);
    }

    public void setMainWindow(MainWindow _mainWindow) {
        this.mainWindow = _mainWindow;
        repaintTimer = new Timer(applicationProperties.getRepaintDelay(), new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                updateModel();
                mainWindow.repaint();
            }
        });
    }

    protected void updateModel() {
        while (dataProvider.size() >0){
            int[] decodedFrame = dataProvider.poll();
            ch1PreFilter.add(decodedFrame[1]);
            ch2PreFilter.add(decodedFrame[2]);
            model.addEyeData(ch1PreFilter.poll());
            model.addCh2Data(ch2PreFilter.poll());
        }
        if (isAutoScroll) {
            model.setFastGraphIndexMaximum();
        }
    }

    public void saveToFile() {
        try {
            JFileChooser fileChooser = new DrmFileChooser(applicationProperties);
            int fileChooserState = fileChooser.showSaveDialog(mainWindow);
            if (fileChooserState == JFileChooser.APPROVE_OPTION) {
                new DataSaveManager().saveToFile(fileChooser.getSelectedFile(), model);
            }
        } catch (ApplicationException e) {
            mainWindow.showMessage(e.getMessage());
        }
    }

     public void saveAsEdf() {
        try {
            JFileChooser fileChooser = new DrmFileChooser(applicationProperties);
            int fileChooserState = fileChooser.showSaveDialog(mainWindow);
            if (fileChooserState == JFileChooser.APPROVE_OPTION) {
                new DataSaveManager().saveAsEdf(fileChooser.getSelectedFile(), model);
            }
        } catch (ApplicationException e) {
            mainWindow.showMessage(e.getMessage());
        }
    }

    public void readFromFile() {
        try {
            JFileChooser fileChooser = new DrmFileChooser(applicationProperties);
            int fileChooserState = fileChooser.showOpenDialog(mainWindow);
            if (fileChooserState == JFileChooser.APPROVE_OPTION) {
                new DataSaveManager().readFromFile(fileChooser.getSelectedFile(), model);
            }
        } catch (ApplicationException e) {
            mainWindow.showMessage(e.getMessage());
        }
        mainWindow.repaint();
    }

    public void startRecording() {
        try {
            dataProvider = Factory.getDataProvider(applicationProperties.getDataProvider(),applicationProperties);
            dataProvider.startRecording();
            model.clear();
            model.setFrequency(dataProvider.getIncomingDataFrequency());
            model.setStartTime(dataProvider.getStartTime());
            repaintTimer.start();
            isAutoScroll = true;
        } catch (ApplicationException e) {
            mainWindow.showMessage(e.getMessage());
        }
    }

    public void stopRecording() {
        dataProvider.stopRecording();
        repaintTimer.stop();
        isAutoScroll = false;
    }

    public void changeXSize(int xSize) {
        model.setXSize(xSize);
        applicationProperties.setXSize(xSize);
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

    public void closeApplication() {
        applicationProperties.save();
    }

    public void setDataProvider(Provider provider) {
        applicationProperties.setDataProvider(provider);
    }
}
