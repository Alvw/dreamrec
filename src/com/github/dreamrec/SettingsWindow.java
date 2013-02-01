package com.github.dreamrec;

import com.github.dreamrec.ads.*;
import com.github.dreamrec.edf.EdfFileChooser;
import com.github.dreamrec.edf.EdfModel;
import com.github.dreamrec.edf.EdfWriter;
import com.github.dreamrec.layout.gnu.TableLayout;
import com.github.dreamrec.layout.gnu.TableOption;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;


/**
 *
 */
public class SettingsWindow extends JFrame {
    private EdfModel edfModel;
    private Controller controller;


    private JComboBox spsField;
    private String spsLabel = "Sampling Frequency (Hz)";
    private JTextField comPortName;
    private String comPortLabel = "Com Port";
    private String[] accelerometerNamesEnds = {"X", "Y", "Z"};

    private JComboBox[] channelFrequency;
    private JComboBox[] channelHiPassFrequency;
    private JCheckBox[] channelEnable;
    private JTextField[] channelName;

    private JComboBox accelerometerFrequency;
    private JTextField accelerometerName;
    private JCheckBox accelerometerEnable;
    private JComboBox accelerometerHiPassFrequency;
    private JFrame mainFrame = this;

    private String patientIdentificationLabel = "Patient";
    private String recordingIdentificationLabel = "Record";
    private JTextField patientIdentification;
    private JTextField recordingIdentification;

    private String fileToSaveLabel = "<html><center>[dd-mm-yyyy_hh-mm].edf<br> by default</center></html>";
    private String defaultFileToSave = "dd-mm-yyyy_hh-mm.edf";
    private JTextField fileToSave;
    
    private boolean isRecording = false;
    private String start = "Start";
    private String stop = "Stop";
    private String saveAs = "SaveAs";
    private String browse = "Browse";
    private JButton startButton = new JButton(start);
    private JButton saveAsButton = new JButton(saveAs);
    private JButton browsButton = new JButton(browse);

    private Color recordColor = Color.GREEN;
    private MarkerLabel markerLabel = new MarkerLabel();
    private JLabel reportLabel = new JLabel();
  
   
    Icon iconGreen = new ImageIcon("img/greenBall.png");
    Icon iconRed = new ImageIcon("img/redBall.png");
    private MarkerLabel[] channelLoffStatPositive;
    private MarkerLabel[] channelLoffStatNegative;

    private String title = "EDF Recorder";
    private String[] channelsHeaders = {"Number", "Enable", "Name", "Frequency (Hz)", "Hi Pass Filter (Hz)",  "Lead Off Detection"};


    public SettingsWindow(Controller controller) {
        this.controller = controller;
        edfModel = controller.getEdfModel();
        init();
        arrangeForm();
        setActions();
        loadDataFromModel();
        setVisible(true);
    }

    private void init() {
        int adsChannelsNumber = edfModel.getAdsModel().getNumberOfAdsChannels();

        spsField = new JComboBox(Sps.values());
        spsField.setSelectedItem(edfModel.getAdsModel().getSps());
        int textFieldLength = 5;
        comPortName = new JTextField(textFieldLength);
        
        textFieldLength = 50;
        patientIdentification = new JTextField(textFieldLength);
        recordingIdentification = new JTextField(textFieldLength);
        
        textFieldLength = 45;
        fileToSave = new JTextField(textFieldLength);
        
        channelFrequency = new JComboBox[adsChannelsNumber];
        channelHiPassFrequency = new JComboBox[adsChannelsNumber];
        channelEnable = new JCheckBox[adsChannelsNumber];
        channelName = new JTextField[adsChannelsNumber];
        channelLoffStatPositive = new MarkerLabel[adsChannelsNumber];
        channelLoffStatNegative = new MarkerLabel[adsChannelsNumber];

        textFieldLength = 10;
        for (int i = 0; i < adsChannelsNumber; i++) {
            channelFrequency[i] = new JComboBox();
            channelHiPassFrequency[i] = new JComboBox(HiPassFrequency.values());
            channelEnable[i] = new JCheckBox();
            channelName[i] = new JTextField(textFieldLength);
            channelLoffStatPositive[i] = new MarkerLabel();
            channelLoffStatNegative[i] = new MarkerLabel();
        }


        accelerometerEnable = new JCheckBox();
        accelerometerName = new JTextField(textFieldLength);
        accelerometerHiPassFrequency = new JComboBox(HiPassFrequency.values());
        accelerometerFrequency = new JComboBox();
    }

    private void setActions() {

        for (int i = 0; i < edfModel.getAdsModel().getNumberOfAdsChannels(); i++) {
            channelEnable[i].addActionListener(new AdsChannelEnableListener(i));
        }

        accelerometerEnable.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JCheckBox checkBox = (JCheckBox) actionEvent.getSource();
                if (checkBox.isSelected()) {
                    enableAccelerometer();
                } else {
                    disableAccelerometer();
                }
            }
        });


        spsField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JComboBox comboBox = (JComboBox) actionEvent.getSource();
                Sps sps = (Sps) comboBox.getSelectedItem();
                setChannelsFrequencies(sps);
            }
        });


        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (isRecording) {
                    isRecording = false;
                    enableFields();
                    startButton.setText(start);
                    controller.stopRecording();
                } else {
                    if((getFileToSave() != null) & EdfFileChooser.isExistingFileReplace(getFileToSave(),mainFrame)) {
                        isRecording = true;
                        comPortName.setEnabled(false);
                        disableFields();
                        startButton.setText(stop);
                        saveDataToModel();
                        controller.startRecording();
                    }
                }
            }
        });
        
        browsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
               EdfFileChooser fileChooser = new EdfFileChooser(edfModel.getCurrentDirectory());
               File selectedFile = fileChooser.chooseFileToSave();
               if(selectedFile !=  null) {
                   fileToSave.setText(selectedFile.toString());
               }
            }
        });


        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                saveDataToModel();
                controller.closeApplication();
                System.exit(0);
            }
        });
    }


    private void arrangeForm() {
        setTitle(title);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(startButton);
        
        JPanel spsPanel = new JPanel();
        spsPanel.add(new JLabel(spsLabel));
        spsPanel.add(spsField);
        
        JPanel comPortPanel = new JPanel();
        comPortPanel.add(new Label(comPortLabel));
        comPortPanel.add(comPortName);

        int hgap = 20;
        int vgap = 10;
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, hgap, vgap));
        topPanel.add(comPortPanel);
        topPanel.add(spsPanel);
        topPanel.add(buttonPanel);


        hgap = 20;
        vgap = 5;
        JPanel channelsPanel = new JPanel(new TableLayout(channelsHeaders.length, new TableOption(TableOption.CENTRE, TableOption.CENTRE), hgap, vgap));

        for (int i = 0; i < channelsHeaders.length; i++) {
            channelsPanel.add(new JLabel(channelsHeaders[i]));

        }

        for (int i = 0; i < edfModel.getAdsModel().getNumberOfAdsChannels(); i++) {
            channelsPanel.add(new JLabel(" " + i + " "));
            channelsPanel.add(channelEnable[i]);
            channelsPanel.add(channelName[i]);
            channelsPanel.add(channelFrequency[i]);
            channelsPanel.add(channelHiPassFrequency[i]);

            JPanel loffPanel = new JPanel();
            loffPanel.add(channelLoffStatPositive[i]);
            loffPanel.add(channelLoffStatNegative[i]);
            channelsPanel.add(loffPanel);
        }
        
        if(edfModel.getAdsModel().getNumberOfAccelerometerChannels() > 0) {
            // Add line of accelerometer
            channelsPanel.add(new JLabel(" " + edfModel.getAdsModel().getNumberOfAdsChannels() + " "));
            channelsPanel.add(accelerometerEnable);
            channelsPanel.add(accelerometerName);
            channelsPanel.add(accelerometerFrequency);
            channelsPanel.add(accelerometerHiPassFrequency);
            channelsPanel.add(new JLabel(""));
            channelsPanel.add(new JLabel(""));            
        }


        int top = 10;
        int left = 5;
        int bottom = 10;
        int right = 5;
        channelsPanel.setBorder(BorderFactory.createEmptyBorder(top, left, bottom, right));
        hgap = 0;
        vgap = 0;
        JPanel channelsBorderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, hgap, vgap));
        channelsBorderPanel.setBorder(BorderFactory.createTitledBorder("Channels"));
        channelsBorderPanel.add(channelsPanel);

        hgap = 5;
        vgap = 5;
        int cols = 2;
        JPanel identificationPanel = new JPanel(new TableLayout(cols, new TableOption(TableOption.LEFT, TableOption.LEFT), hgap, vgap));
        identificationPanel.add(new JLabel(patientIdentificationLabel)); 
        identificationPanel.add(patientIdentification);
        identificationPanel.add(new JLabel(recordingIdentificationLabel));
        identificationPanel.add(recordingIdentification);

        hgap = 0;
        vgap = 0;
        identificationPanel.setBorder(BorderFactory.createEmptyBorder(top, left, bottom, right));
        JPanel identificationBorderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, hgap, vgap));
        identificationBorderPanel.setBorder(BorderFactory.createTitledBorder("Identification"));
        identificationBorderPanel.add(identificationPanel);

        
        JPanel saveAsPanel = new JPanel();
        saveAsPanel.add(browsButton);
        saveAsPanel.add(fileToSave);
        saveAsPanel.setBorder(BorderFactory.createEmptyBorder(top, left, bottom, right));
        JPanel saveAsBorderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, hgap, vgap));
        saveAsBorderPanel.setBorder(BorderFactory.createTitledBorder("Save As"));
        saveAsBorderPanel.add(saveAsPanel);

        
        JPanel reportPanel = new JPanel();
        reportPanel.add(new JLabel(" "));
        reportPanel.add(markerLabel);
        reportPanel.add(reportLabel);

        hgap = 0;
        vgap = 5;
        JPanel adsPanel = new JPanel(new BorderLayout(hgap,vgap));
        adsPanel.add(channelsBorderPanel, BorderLayout.NORTH);
        adsPanel.add(identificationBorderPanel, BorderLayout.CENTER);
        adsPanel.add(saveAsBorderPanel, BorderLayout.SOUTH);

        // Root Panel of the SettingsWindow
        add(topPanel, BorderLayout.NORTH);
        add(adsPanel, BorderLayout.CENTER);
        add(reportPanel, BorderLayout.SOUTH);

        pack();
        // place the window to the screen center
        setLocationRelativeTo(null);
    }

    private void disableEnableFields(boolean isEnable) {
        spsField.setEnabled(isEnable);
        patientIdentification.setEnabled(isEnable);
        recordingIdentification.setEnabled(isEnable);
        browsButton.setEnabled(isEnable);
        fileToSave.setEnabled(isEnable);

        accelerometerName.setEnabled(isEnable);
        accelerometerEnable.setEnabled(isEnable);
        accelerometerHiPassFrequency.setEnabled(isEnable);
        accelerometerFrequency.setEnabled(isEnable);

        for (int i = 0; i < edfModel.getAdsModel().getNumberOfAdsChannels(); i++) {
            channelEnable[i].setEnabled(isEnable);
            channelName[i].setEnabled(isEnable);
            channelFrequency[i].setEnabled(isEnable);
            channelHiPassFrequency[i].setEnabled(isEnable);
        }
    }

    
    private void disableFields() {
        boolean isEnable = false;
        disableEnableFields(isEnable);


    }


    private void enableFields() {
        boolean isEnable = true;
        disableEnableFields(isEnable);
        for (int i = 0; i < edfModel.getAdsModel().getNumberOfAdsChannels(); i++) {
            if (!isChannelEnable(i)) {
                disableAdsChannel(i);
            }
        }
        if (!edfModel.getAdsModel().isAccelerometerEnabled()){
            disableAccelerometer();
        }
    }

    public void setReport(String report) {
        reportLabel.setText(report);
        if (isRecording) {
            markerLabel.setColor(recordColor);
        } else {
            markerLabel.setBackgroundColor();
        }
        pack();
    }

    private void loadDataFromModel() {
        spsField.setSelectedItem(edfModel.getAdsModel().getSps());
        comPortName.setText(edfModel.getAdsModel().getComPortName());
        fileToSave.setText(FileUtils.getFile(edfModel.getCurrentDirectory(), EdfWriter.FILENAME_PATTERN).toString());
        int numberOfAdsChannels = edfModel.getAdsModel().getNumberOfAdsChannels();
        for (int i = 0; i < numberOfAdsChannels; i++) {
            AdsChannelModel channel = edfModel.getAdsModel().getAdsChannel(i);
            channelName[i].setText(channel.getName());
            channelEnable[i].setSelected(channel.isEnabled());
            channelHiPassFrequency[i].setSelectedItem(channel.getHiPassFilterFrequency());
            if (!channel.isEnabled()) {
                disableAdsChannel(i);
            }
        }

        if (edfModel.getAdsModel().getNumberOfAccelerometerChannels() > 0) {
            accelerometerName.setText(edfModel.getAdsModel().getAccelerometerName());
            accelerometerEnable.setSelected(edfModel.getAdsModel().isAccelerometerEnabled());
            accelerometerHiPassFrequency.setSelectedItem(edfModel.getAdsModel().getAccelerometerHiPassFrequency());
            if(!edfModel.getAdsModel().isAccelerometerEnabled()){
                disableAccelerometer();
            }
        }
        setChannelsFrequencies(edfModel.getAdsModel().getSps());
    }

    public void updateLoffStatus(int loffStatusRegisterValue) {
        if ((loffStatusRegisterValue & 8) == 0) {
            channelLoffStatPositive[0].setIcon(iconGreen);
        } else {
            channelLoffStatPositive[0].setIcon(iconRed);
        }
        if ((loffStatusRegisterValue & 16) == 0) {
            channelLoffStatNegative[0].setIcon(iconGreen);
        } else {
            channelLoffStatNegative[0].setIcon(iconRed);
        }
        if ((loffStatusRegisterValue & 32) == 0) {
            channelLoffStatPositive[1].setIcon(iconGreen);
        } else {
            channelLoffStatPositive[1].setIcon(iconRed);
        }
        if ((loffStatusRegisterValue & 64) == 0) {
            channelLoffStatNegative[1].setIcon(iconGreen);
        } else {
            channelLoffStatNegative[1].setIcon(iconRed);
        }
    }

    private void saveDataToModel() {
        edfModel.getAdsModel().setSps(getSps());
        int numberOfAdsChannels = edfModel.getAdsModel().getNumberOfAdsChannels();
        edfModel.getAdsModel().setComPortName(getComPortName());
        edfModel.setPatientIdentification(getPatientIdentification());
        edfModel.setRecordingIdentification(getRecordingIdentification());
        edfModel.setFileToSave(getFileToSave());
        for (int i = 0; i < numberOfAdsChannels; i++) {
            AdsChannelModel channel = edfModel.getAdsModel().getAdsChannel(i);
            channel.setName(getChannelName(i));
            channel.setDivider(getChannelDivider(i));
            channel.setHiPassFilterFrequency(getChannelFrequency(i), getChannelHiPassFrequency(i));
            channel.setEnabled(isChannelEnable(i));
        }

        int numberOfAccelerometerChannels = edfModel.getAdsModel().getNumberOfAccelerometerChannels();
        for (int i = 0; i < numberOfAccelerometerChannels; i++) {
            ChannelModel channel = edfModel.getAdsModel().getAccelerometerChannel(i);
            if (i < accelerometerNamesEnds.length) {
                channel.setName(getAccelerometerName() + accelerometerNamesEnds[i]);
            } else {
                channel.setName(getAccelerometerName());
            }
            channel.setEnabled(isAccelerometerEnable());
            channel.setDivider(getAccelerometerDivider());
            channel.setHiPassFilterFrequency(getAccelerometerFrequency(), getAccelerometerHiPassFrequency());
        }
    }

    private void setChannelsFrequencies(Sps sps) {
        int numberOfAdsChannels = edfModel.getAdsModel().getNumberOfAdsChannels();
        Integer[] channelsAvailableFrequencies = sps.getChannelsAvailableFrequencies();
        // set available frequencies
        for (int i = 0; i < numberOfAdsChannels; i++) {
            channelFrequency[i].removeAllItems();
            for (Integer frequency : channelsAvailableFrequencies) {
                channelFrequency[i].addItem(frequency);
            }
            // select channel frequency
            ChannelModel channel = edfModel.getAdsModel().getAdsChannel(i);
            Integer frequency = sps.getValue() / channel.getDivider().getValue();
            channelFrequency[i].setSelectedItem(frequency);
        }
        if(edfModel.getAdsModel().getNumberOfAccelerometerChannels() > 0){
            Integer[] accelerometerAvailableFrequencies = sps.getAccelerometerAvailableFrequencies();
            accelerometerFrequency.removeAllItems();
            for (Integer frequency : accelerometerAvailableFrequencies) {
                accelerometerFrequency.addItem(frequency);
            }
            // select channel frequency
            Integer frequency = sps.getValue() / edfModel.getAdsModel().getAccelerometerDivider().getValue();
            accelerometerFrequency.setSelectedItem(frequency);
             if(numberOfAdsChannels > 0){
                 // put the size if field   accelerometerFrequency equal to the size of fields  channelFrequency
                 accelerometerFrequency.setPreferredSize(channelFrequency[0].getPreferredSize());

             }
         }
    }


    private void disableAdsChannel(int channelNumber) {
        channelFrequency[channelNumber].setEnabled(false);
        channelHiPassFrequency[channelNumber].setEnabled(false);
        channelName[channelNumber].setEnabled(false);
    }

    private void enableAdsChannel(int channelNumber) {
        channelFrequency[channelNumber].setEnabled(true);
        channelHiPassFrequency[channelNumber].setEnabled(true);
        channelName[channelNumber].setEnabled(true);
    }

    private void disableAccelerometer() {
        accelerometerName.setEnabled(false);
        accelerometerFrequency.setEnabled(false);
        accelerometerHiPassFrequency.setEnabled(false);

    }

    private void enableAccelerometer() {
        accelerometerName.setEnabled(true);
        accelerometerFrequency.setEnabled(true);
        accelerometerHiPassFrequency.setEnabled(true);
    }



    private Divider getChannelDivider(int channelNumber) {
        int divider = edfModel.getAdsModel().getSps().getValue() / getChannelFrequency(channelNumber);
        return Divider.valueOf(divider);
    }

    private Divider getAccelerometerDivider() {
        int divider = edfModel.getAdsModel().getSps().getValue() / getAccelerometerFrequency();
        return Divider.valueOf(divider);
    }


    private int getChannelFrequency(int channelNumber) {
        return (Integer) channelFrequency[channelNumber].getSelectedItem();
    }

    private HiPassFrequency getChannelHiPassFrequency(int channelNumber) {
        return (HiPassFrequency) channelHiPassFrequency[channelNumber].getSelectedItem();
    }

    private boolean isChannelEnable(int channelNumber) {
        return channelEnable[channelNumber].isSelected();
    }

    private String getChannelName(int channelNumber) {
        return channelName[channelNumber].getText();
    }
    
    private String getComPortName(){
        return comPortName.getText();
    }
    
    private String getPatientIdentification(){
       return patientIdentification.getText();
    }
    
    private String getRecordingIdentification(){
        return recordingIdentification.getText();
    }

    private File getFileToSave(){      
          return EdfFileChooser.getCanonicalFile(new File(fileToSave.getText()), mainFrame); 
    }

    private boolean isAccelerometerEnable() {
        return accelerometerEnable.isSelected();
    }

    private String getAccelerometerName() {
        return accelerometerName.getText();
    }

    private HiPassFrequency  getAccelerometerHiPassFrequency() {
        return (HiPassFrequency) accelerometerHiPassFrequency.getSelectedItem();
    }

    private int getAccelerometerFrequency() {
        return (Integer) accelerometerFrequency.getSelectedItem();
    }

    private Sps getSps() {
        return (Sps) spsField.getSelectedItem();
    }



    private class AdsChannelEnableListener implements ActionListener {
        private int channelNumber;

        private AdsChannelEnableListener(int channelNumber) {
            this.channelNumber = channelNumber;
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            JCheckBox checkBox = (JCheckBox) actionEvent.getSource();
            if (checkBox.isSelected()) {
                enableAdsChannel(channelNumber);
            } else {
                disableAdsChannel(channelNumber);
            }
        }
    }
}
