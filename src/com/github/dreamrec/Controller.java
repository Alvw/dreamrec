package com.github.dreamrec;

import com.github.dreamrec.comport.ComPort;
import com.github.dreamrec.edf.EdfHeaderData;
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

    public Controller(Model model, ApplicationProperties applicationProperties) {
        this.model = model;
        this.applicationProperties = applicationProperties;
        int frequencyDivider = applicationProperties.getFrequencyDivider();
        int hiPassBufferSize = applicationProperties.getHiPassBufferSize();
        ch1PreFilter = new HiPassPreFilter(hiPassBufferSize,frequencyDivider);
        ch2PreFilter = new HiPassPreFilter(hiPassBufferSize,frequencyDivider);
        acc1PreFilter = new HiPassPreFilter(hiPassBufferSize,frequencyDivider);
        acc2PreFilter = new HiPassPreFilter(hiPassBufferSize,frequencyDivider);
        acc3PreFilter = new HiPassPreFilter(hiPassBufferSize,frequencyDivider);
        try {
            outputStream = new DataOutputStream(new FileOutputStream("tralivali.edf"));    //todo refactor
            int frequency = applicationProperties.getIncomingDataFrequency();
            EdfHeaderData headerData1 = new EdfHeaderData();
            headerData1.setNrOfSamplesInEachDataRecord(String.valueOf(frequency));

            EdfHeaderData headerData2 = new EdfHeaderData();
            headerData2.setNrOfSamplesInEachDataRecord(String.valueOf(frequency));
            headerData2.setLabel("EEG 2 chanel");

            EdfHeaderData headerData3 = new EdfHeaderData();
            headerData3.setNrOfSamplesInEachDataRecord(String.valueOf(frequency));
            headerData3.setLabel("Accel X ");

            EdfHeaderData headerData4 = new EdfHeaderData();
            headerData4.setNrOfSamplesInEachDataRecord(String.valueOf(frequency));
            headerData4.setLabel("Accel Y");

            EdfHeaderData headerData5 = new EdfHeaderData();
            headerData5.setNrOfSamplesInEachDataRecord(String.valueOf(frequency));
            headerData5.setLabel("Accel Z");


            dataSaveManager.writeEdfHeader(model, outputStream, headerData1, headerData2, headerData3, headerData4, headerData5);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            acc1PreFilter.add(decodedFrame[3]);
            acc2PreFilter.add(decodedFrame[4]);
            acc3PreFilter.add(decodedFrame[5]);
            model.addEyeData(ch1PreFilter.poll());
            model.addCh2Data(ch2PreFilter.poll());
            model.addAcc1Data(acc1PreFilter.poll());
            model.addAcc2Data(acc2PreFilter.poll());
            model.addAcc3Data(acc3PreFilter.poll());
           /* model.addAcc1Data((short)decodedFrame[3]);
            model.addAcc2Data((short)decodedFrame[4]);
            model.addAcc3Data((short)decodedFrame[5]);*/
            int size = model.getEyeDataList().size();
            int frequency = applicationProperties.getIncomingDataFrequency();
            if(size%frequency == 0){
                for (int i = size - frequency; i < size; i++) {
                    try {
                        outputStream.writeShort(dataSaveManager.toLittleEndian(model.getEyeDataList().get(i)));
                    } catch (IOException e) {
                        e.printStackTrace();  //todo refactor
                    }
                }
                for (int i = size - frequency; i < size; i++) {
                    try {
                        outputStream.writeShort(dataSaveManager.toLittleEndian(model.getCh2DataList().get(i)));
                    } catch (IOException e) {
                        e.printStackTrace();  //todo refactor
                    }
                }
                for (int i = size - frequency; i < size; i++) {
                    try {
                        outputStream.writeShort(dataSaveManager.toLittleEndian(model.getAcc1DataList().get(i)));
                    } catch (IOException e) {
                        e.printStackTrace();  //todo refactor
                    }
                }
                for (int i = size - frequency; i < size; i++) {
                    try {
                        outputStream.writeShort(dataSaveManager.toLittleEndian(model.getAcc2DataList().get(i)));
                    } catch (IOException e) {
                        e.printStackTrace();  //todo refactor
                    }
                }
                for (int i = size - frequency; i < size; i++) {
                    try {
                        outputStream.writeShort(dataSaveManager.toLittleEndian(model.getAcc3DataList().get(i)));
                    } catch (IOException e) {
                        e.printStackTrace();  //todo refactor
                    }
                }
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
            dataProvider.stopRecording();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();  //todo refactor
        }
    }

    public void setDataProvider(Provider provider) {
        applicationProperties.setDataProvider(provider);
    }
}
