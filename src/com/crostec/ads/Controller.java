package com.crostec.ads;

import com.crostec.ads.comport.ComPort;
import com.crostec.ads.edf.BdfModel;
import com.crostec.ads.edf.BdfWriter;
import com.crostec.ads.gui.SettingsWindow;
import com.crostec.ads.model.AdsChannelModel;
import com.crostec.ads.model.AdsManager;
import com.crostec.ads.model.ChannelModel;
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
    private BdfModel bdfModel;
    private BdfWriter edfWriter;
    private ComPort comport;
    private boolean isRecording = false;
    private ArrayList<AdsDataListener> adsDataListeners = new ArrayList<AdsDataListener>();


    public Controller(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
        bdfModel = Factory.getEdfModel(applicationProperties);
        comport = new ComPort();
        repaintTimer = new Timer(applicationProperties.getRepaintDelay(), new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                updateModel();
            }
        });
    }

    public BdfModel getBdfModel() {
        return bdfModel;
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
        edfWriter = new BdfWriter(bdfModel);
        this.addAdsDataListener(edfWriter);
        settingsWindow.setFileToSave(edfWriter.getBdfFile());
        String failConnectMessage = "Connection failed. Check com port settings.\nReset power on the target amplifier. Restart the application.";
        try {
            comport.connect(bdfModel.getAdsModel().getComPortName());
            frameDecoder = new FrameDecoder(bdfModel.getAdsModel().getFrameSize());
            comport.setComPortListener(frameDecoder);
            AdsManager adsManager = new AdsManager();
            comport.writeToPort(adsManager.writeModelState(bdfModel.getAdsModel()));
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
        applicationProperties.setSps(bdfModel.getAdsModel().getSps());
        applicationProperties.setComPortName(bdfModel.getAdsModel().getComPortName());
        applicationProperties.setLastVisitedDirectory(bdfModel.getCurrentDirectory());
        applicationProperties.setPatientIdentification(bdfModel.getPatientIdentification());
        applicationProperties.setRecordingIdentification(bdfModel.getRecordingIdentification());
        for (int i = 0; i < bdfModel.getAdsModel().getNumberOfAdsChannels(); i++) {
            AdsChannelModel channel = bdfModel.getAdsModel().getAdsChannel(i);
            applicationProperties.setChannelDivider(i, channel.getDivider());
            applicationProperties.setChannelName(i, channel.getName());
            applicationProperties.setChannelEnabled(i, channel.isEnabled());
            applicationProperties.setChannelLoffEnabled(i, channel.isLoffEnable());
            applicationProperties.setChannelRldSenseEnabled(i, channel.isRldSenseEnabled());
            applicationProperties.setChannelElectrodeType(i, channel.getElectrodeType());
        }
        for (int i = 0; i < bdfModel.getAdsModel().getNumberOfAccelerometerChannels(); i++) {
            ChannelModel channel = bdfModel.getAdsModel().getAccelerometerChannel(i);
            applicationProperties.setAccelerometerDivider(channel.getDivider());
            applicationProperties.setAccelerometerName(i, channel.getName());
            applicationProperties.setAccelerometerEnabled(channel.isEnabled());
        }
    }

}
