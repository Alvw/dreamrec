package com.github.dreamrec;

import com.github.dreamrec.ads.AdsManager;
import com.github.dreamrec.ads.AdsModel;
import com.github.dreamrec.comport.ComPort;
import com.github.dreamrec.edf.EdfHeaderData;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

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
    private HiPassPreFilter acc1PreFilter;
    private HiPassPreFilter acc2PreFilter;
    private HiPassPreFilter acc3PreFilter;
    private DataSaveManager dataSaveManager = new DataSaveManager();//todo delete
    private DataOutputStream outputStream;
    private FrameDecoder frameDecoder;
    private AdsModel adsModel;
    private ComPort comport;

    public Controller(Model model, AdsModel adsModel, ComPort comport, ApplicationProperties applicationProperties) {
        this.model = model;
        this.adsModel = adsModel;
        this.comport = comport;
        this.applicationProperties = applicationProperties;
        int frequencyDivider = applicationProperties.getFrequencyDivider();
        int hiPassBufferSize = applicationProperties.getHiPassBufferSize();
        ch1PreFilter = new HiPassPreFilter(hiPassBufferSize, frequencyDivider);
        /*ch2PreFilter = new HiPassPreFilter(hiPassBufferSize, frequencyDivider);
        acc1PreFilter = new HiPassPreFilter(hiPassBufferSize, frequencyDivider);
        acc2PreFilter = new HiPassPreFilter(hiPassBufferSize, frequencyDivider);
        acc3PreFilter = new HiPassPreFilter(hiPassBufferSize, frequencyDivider);*/

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
        while (frameDecoder.available()) {
            int[] frame = frameDecoder.poll();
            for (int i = 0; i < 50; i++) {
                ch1PreFilter.add(frame[i]);
                model.addEyeData(ch1PreFilter.poll());
            }
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
        model.clear();
        model.setFrequency(250);
        model.setStartTime(System.currentTimeMillis());
        frameDecoder = new FrameDecoder(adsModel.getFrameSize());
        comport.setComPortListener(frameDecoder);
        AdsManager adsManager = new AdsManager();
        try {
            comport.writeToPort(adsManager.writeModelState(adsModel));
        } catch (Exception e) {
            e.printStackTrace();
        }
        repaintTimer.start();
        isAutoScroll = true;

    }

    public void stopRecording() {
        dataProvider.stopRecording();
        repaintTimer.stop();
        isAutoScroll = false;
        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace(); //todo refactor
        }
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
        try {
            comport.writeToPort(new AdsManager().startPinLo());
            Thread.sleep(200);
        } catch (Exception e) {
            e.printStackTrace();
        }

        comport.disconnect();
    }
}
