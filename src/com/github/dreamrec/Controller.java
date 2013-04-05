package com.github.dreamrec;

import com.github.dreamrec.ads.AdsChannelModel;
import com.github.dreamrec.ads.AdsManager;
import com.github.dreamrec.ads.ChannelModel;
import com.github.dreamrec.comport.ComPort;
import com.github.dreamrec.edf.EdfModel;
import com.github.dreamrec.edf.EdfWriter;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 *
 */
public class Controller {

     private Timer repaintTimer;
    private SettingsWindow settingsWindow;

    public void setSettingsWindow(SettingsWindow settingsWindow) {
        this.settingsWindow = settingsWindow;
    }

    private ApplicationProperties applicationProperties;
    private static final Log log = LogFactory.getLog(Controller.class);
    private FrameDecoder frameDecoder;
    private EdfModel edfModel;
    private EdfWriter edfWriter;
    private ComPort comport;
    private boolean isRecording = false;
    private ArrayList<AdsDataListener> adsDataListeners = new ArrayList<AdsDataListener>();


    public Controller(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
        edfModel = Factory.getEdfModel(applicationProperties);
        comport = new ComPort();
        repaintTimer = new Timer(applicationProperties.getRepaintDelay(), new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                updateModel();
            }
        });
    }

    public EdfModel getEdfModel() {
        return edfModel;
    }

    public boolean isRecording() {
        return isRecording;
    }


    public void addAdsDataListener(AdsDataListener adsDataListener) {
        adsDataListeners.add(adsDataListener);
    }

    protected void updateModel() {
        boolean isLoffUpdated = false;
        while (frameDecoder.available()) {
            int[] frame = frameDecoder.poll();
            notifyListeners(frame);
            if (!isLoffUpdated) {
                settingsWindow.updateLoffStatus(frame[frame.length - 1]);
                isLoffUpdated = true;
            }
        }
        if(isRecording()) {
            if (edfWriter.isReportUpdated()) {
                settingsWindow.setProcessReport(edfWriter.getReport());
            }
        }
    }

    private void notifyListeners(int[] frame) {
        for (AdsDataListener adsDataListener : adsDataListeners) {
            adsDataListener.onDataReceived(frame);
        }
    }

    public void startRecording() {
        isRecording = true;
        edfWriter = new EdfWriter(edfModel);
        this.addAdsDataListener(edfWriter);
        settingsWindow.setFileToSave(edfWriter.getEdfFile());
        String failConnectMessage = "Connection failed. Check com port settings.\nReset power on the target amplifier. Restart the application.";
        try {
            comport.connect(edfModel.getAdsModel().getComPortName());
            frameDecoder = new FrameDecoder(edfModel.getAdsModel().getFrameSize());
            comport.setComPortListener(frameDecoder);
            AdsManager adsManager = new AdsManager();
            comport.writeToPort(adsManager.writeModelState(edfModel.getAdsModel()));
        } catch (NoSuchPortException e) {
            String msg = "No port with the name " + applicationProperties.getComPortName() + "\n" + failConnectMessage;
            log.error(msg, e);
            JOptionPane.showMessageDialog(null, msg);
            System.exit(0);
        }catch (PortInUseException e) {
            log.error(failConnectMessage, e);
            JOptionPane.showMessageDialog(null, failConnectMessage);
            System.exit(0);
        } catch (Throwable e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, failConnectMessage);
           // System.exit(0);
        }
        repaintTimer.start();
    }

    public void stopRecording() {
        isRecording = false;
         repaintTimer.stop();
        comport.writeToPort(new AdsManager().startPinLo());
        edfWriter.stopRecording();
        settingsWindow.setReport(edfWriter.getReport());
    }

    public void closeApplication() {
        saveAdsModelToProperties();
        applicationProperties.save();
        try {
            comport.writeToPort(new AdsManager().startPinLo());
            Thread.sleep(200);
        } catch (Exception e) {
            e.printStackTrace();
        }
        comport.disconnect();
    }

    private void saveAdsModelToProperties() {
        applicationProperties.setSps(edfModel.getAdsModel().getSps());
        applicationProperties.setComPortName(edfModel.getAdsModel().getComPortName());
        applicationProperties.setLastVisitedDirectory(edfModel.getCurrentDirectory());
        applicationProperties.setPatientIdentification(edfModel.getPatientIdentification());
        applicationProperties.setRecordingIdentification(edfModel.getRecordingIdentification());
        for (int i = 0; i < edfModel.getAdsModel().getNumberOfAdsChannels(); i++) {
            AdsChannelModel channel = edfModel.getAdsModel().getAdsChannel(i);
            applicationProperties.setChannelDivider(i, channel.getDivider());
            applicationProperties.setChannelName(i, channel.getName());
            applicationProperties.setChannelHiPassFrequency(i, channel.getHiPassFilterFrequency());
            applicationProperties.setChannelEnabled(i, channel.isEnabled());
            applicationProperties.setChannelLoffEnabled(i, channel.isLoffEnable());
            applicationProperties.setChannelRldSenseEnabled(i, channel.isRldSenseEnabled());
            applicationProperties.setChannelElectrodeType(i, channel.getElectrodeType());
        }
        for (int i = 0; i < edfModel.getAdsModel().getNumberOfAccelerometerChannels(); i++) {
            ChannelModel channel = edfModel.getAdsModel().getAccelerometerChannel(i);
            applicationProperties.setAccelerometerDivider(channel.getDivider());
            applicationProperties.setAccelerometerName(i, channel.getName());
            applicationProperties.setAccelerometerHiPassFrequency(channel.getHiPassFilterFrequency());
            applicationProperties.setAccelerometerEnabled(channel.isEnabled());
        }
    }

}
