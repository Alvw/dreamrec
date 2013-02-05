package com.github.dreamrec;

import com.github.dreamrec.ads.*;
import com.github.dreamrec.edf.EdfFileChooser;
import com.github.dreamrec.edf.EdfModel;
import com.github.dreamrec.edf.EdfWriter;
import com.github.dreamrec.layout.gnu.TableLayout;
import com.github.dreamrec.layout.gnu.TableOption;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;


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
    private JCheckBox[] channelDrlEnabled;
    private JCheckBox[] channelLoffEnable;
    private JTextField[] channelElectrodeType;

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
    private boolean isAdvanced = false;
    private String start = "Start";
    private String stop = "Stop";
    private String saveAs = "SaveAs";
    private String browse = "Browse";
    private JButton startButton = new JButton(start);
    private JButton browsButton = new JButton(browse);

    private String advancedLabel = "Advanced";
    private JButton advancedButton = new JButton();

    private Color recordColor = Color.GREEN;
    private MarkerLabel markerLabel = new MarkerLabel();
    private JLabel reportLabel = new JLabel();
  
    Icon iconPlus =  new ImageIcon("img/plus.png");
    Icon iconMinus =  new ImageIcon("img/minus.png");
    Icon iconGreen = new ImageIcon("img/greenBall.png");
    Icon iconRed = new ImageIcon("img/redBall.png");
    private MarkerLabel[] channelLoffStatPositive;
    private MarkerLabel[] channelLoffStatNegative;

    private String title = "EDF Recorder";
    private String[] channelsHeaders = {"Number", "Enable", "Name", "Frequency (Hz)", "Hi Pass Filter (Hz)", "Electrode Type", "DRL", "Lead Off", "Lead Off Detection"};
    private JLabel[] channelsHeadersLabels;


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
        advancedButton.setIcon(iconPlus);

        spsField = new JComboBox(Sps.values());
        spsField.setSelectedItem(edfModel.getAdsModel().getSps());
        int textFieldLength = 5;
        comPortName = new JTextField(textFieldLength);
        
        textFieldLength = 50;
        patientIdentification = new JTextField(textFieldLength);
        recordingIdentification = new JTextField(textFieldLength);
        
        textFieldLength = 47;
        fileToSave = new JTextField(textFieldLength);
        
        channelFrequency = new JComboBox[adsChannelsNumber];
        channelHiPassFrequency = new JComboBox[adsChannelsNumber];
        channelEnable = new JCheckBox[adsChannelsNumber];
        channelName = new JTextField[adsChannelsNumber];
        channelElectrodeType = new JTextField[adsChannelsNumber];
        channelLoffStatPositive = new MarkerLabel[adsChannelsNumber];
        channelLoffStatNegative = new MarkerLabel[adsChannelsNumber];
        channelDrlEnabled = new JCheckBox[adsChannelsNumber];
        channelLoffEnable = new JCheckBox[adsChannelsNumber];
        textFieldLength = 16;
        for (int i = 0; i < adsChannelsNumber; i++) {
            channelFrequency[i] = new JComboBox();
            channelHiPassFrequency[i] = new JComboBox(HiPassFrequency.values());
            channelEnable[i] = new JCheckBox();
            channelName[i] = new JTextField(textFieldLength);
            channelElectrodeType[i] = new JTextField(textFieldLength);
            channelDrlEnabled[i] = new JCheckBox();
            channelLoffEnable[i] = new JCheckBox();
            channelLoffStatPositive[i] = new MarkerLabel();
            channelLoffStatNegative[i] = new MarkerLabel();
        }
        accelerometerEnable = new JCheckBox();
        accelerometerName = new JTextField(textFieldLength);
        accelerometerHiPassFrequency = new JComboBox(HiPassFrequency.values());
        accelerometerFrequency = new JComboBox();

        channelsHeadersLabels = new JLabel[channelsHeaders.length];
        for (int i = 0; i < channelsHeaders.length; i++) {
            channelsHeadersLabels[i] = new JLabel(channelsHeaders[i]);
        }
        setAdvanced();
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
                    enableAccelerometer(true);
                } else {
                    enableAccelerometer(false);
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
                    startButton.setText(start);
                    enableFields();
                    controller.stopRecording();
                } else {
                    if((getFileToSave() != null) & EdfFileChooser.isExistingFileReplace(getFileToSave(),mainFrame)) {
                        isRecording = true;
                        startButton.setText(stop);
                        comPortName.setEnabled(false);
                        disableFields();
                        saveDataToModel();
                        controller.startRecording();
                    }
                }

            }
        });
        
        advancedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                setAdvanced();
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

        patientIdentification.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent focusEvent) {
                patientIdentification.selectAll();
            }
        });


        recordingIdentification.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent focusEvent) {
                recordingIdentification.selectAll();
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

        int hgap = 0;
        int vgap = 0;        
        JPanel spsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, hgap, vgap));
        spsPanel.add(new JLabel(spsLabel));
        spsPanel.add(spsField);
        
        JPanel comPortPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, hgap, vgap));
        comPortPanel.add(new Label(comPortLabel));
        comPortPanel.add(comPortName);

        hgap = 20;
        vgap = 10;
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, hgap, vgap));
        topPanel.add(comPortPanel);
        topPanel.add(spsPanel);
        topPanel.add(buttonPanel);
        

        hgap = 20;
        vgap = 5;
        JPanel channelsPanel = new JPanel(new TableLayout(channelsHeaders.length, new TableOption(TableOption.CENTRE, TableOption.CENTRE), hgap, vgap));

        for (JLabel header : channelsHeadersLabels) {
              channelsPanel.add(header);            
        }

        for (int i = 0; i < edfModel.getAdsModel().getNumberOfAdsChannels(); i++) {
            channelsPanel.add(new JLabel(" " + i + " "));
            channelsPanel.add(channelEnable[i]);
            channelsPanel.add(channelName[i]);
            channelsPanel.add(channelFrequency[i]);
            channelsPanel.add(channelHiPassFrequency[i]);
            channelsPanel.add(channelElectrodeType[i]);
            channelsPanel.add(channelDrlEnabled[i]);
            channelsPanel.add(channelLoffEnable[i]);

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
        }
        int top = 10;
        int left = 0;
        int bottom = 0;
        int right = 0;
        channelsPanel.setBorder(BorderFactory.createEmptyBorder(top, left, bottom, right));
        
        hgap = 0;
        vgap = 0;
        JPanel advancedPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, hgap, vgap));
        advancedPanel.add(new JLabel(advancedLabel));
        advancedPanel.add(advancedButton);
        top = 0;
        left = 0;
        bottom = 0;
        right = 20;
        advancedPanel.setBorder(BorderFactory.createEmptyBorder(top, left, bottom, right));

        hgap = 0;
        vgap = 0;
        //JPanel channelsBorderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, hgap, vgap));
        JPanel channelsBorderPanel = new JPanel(new BorderLayout(hgap, vgap));
        channelsBorderPanel.setBorder(BorderFactory.createTitledBorder("Channels"));
        channelsBorderPanel.add(channelsPanel, BorderLayout.NORTH);
        channelsBorderPanel.add(advancedPanel, BorderLayout.CENTER);

        hgap = 5;
        vgap = 5;
        int cols = 2;
        JPanel identificationPanel = new JPanel(new TableLayout(cols, new TableOption(TableOption.LEFT, TableOption.CENTRE), hgap, vgap));
        identificationPanel.add(new JLabel(patientIdentificationLabel)); 
        identificationPanel.add(patientIdentification);
        identificationPanel.add(new JLabel(recordingIdentificationLabel));
        identificationPanel.add(recordingIdentification);

        hgap = 5;
        vgap = 5;
        JPanel identificationBorderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, hgap, vgap));
        identificationBorderPanel.setBorder(BorderFactory.createTitledBorder("Identification"));
        identificationBorderPanel.add(identificationPanel);

        hgap = 0;
        vgap = 0;
        JPanel saveAsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, hgap, vgap));
        saveAsPanel.add(fileToSave);
        saveAsPanel.add(browsButton);

        hgap = 10;
        vgap = 5;
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
            channelDrlEnabled[i].setEnabled(isEnable);
            channelLoffEnable[i].setEnabled(isEnable); 
            channelElectrodeType[i].setEnabled(isEnable);
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
                enableAdsChannel(i, false);
            }
        }
        if (!edfModel.getAdsModel().isAccelerometerEnabled()){
            enableAccelerometer(false);
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
        fileToSave.setText(new File(edfModel.getCurrentDirectory(), EdfWriter.FILENAME_PATTERN).toString());
        patientIdentification.setText(edfModel.getPatientIdentification());
        recordingIdentification.setText(edfModel.getRecordingIdentification());
        int numberOfAdsChannels = edfModel.getAdsModel().getNumberOfAdsChannels();
        for (int i = 0; i < numberOfAdsChannels; i++) {
            AdsChannelModel channel = edfModel.getAdsModel().getAdsChannel(i);
            channelName[i].setText(channel.getName());
            channelEnable[i].setSelected(channel.isEnabled());
            channelHiPassFrequency[i].setSelectedItem(channel.getHiPassFilterFrequency());
            channelDrlEnabled[i].setSelected(channel.isRldSenseEnabled());
            channelLoffEnable[i].setSelected(channel.isLoffEnable());
            channelElectrodeType[i].setText(channel.getElectrodeType());
            if (!channel.isEnabled()) {
                enableAdsChannel(i, false);
            }
        }

        if (edfModel.getAdsModel().getNumberOfAccelerometerChannels() > 0) {
            accelerometerName.setText(edfModel.getAdsModel().getAccelerometerName());
            accelerometerEnable.setSelected(edfModel.getAdsModel().isAccelerometerEnabled());
            accelerometerHiPassFrequency.setSelectedItem(edfModel.getAdsModel().getAccelerometerHiPassFrequency());
            if(!edfModel.getAdsModel().isAccelerometerEnabled()){
                enableAccelerometer(false);
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
            channel.setLoffEnable(isChannelLoffEnable(i));
            channel.setRldSenseEnabled(isChannelDrlEnabled(i));
            channel.setElectrodeType(getElectrodeType(i));
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

    private void enableAdsChannel(int channelNumber, boolean isEnable) {
        channelFrequency[channelNumber].setEnabled(isEnable);
        channelHiPassFrequency[channelNumber].setEnabled(isEnable);
        channelName[channelNumber].setEnabled(isEnable);
        channelDrlEnabled[channelNumber].setEnabled(isEnable);
        channelLoffEnable[channelNumber].setEnabled(isEnable);
        channelElectrodeType[channelNumber].setEnabled(isEnable);
    }
    


    private void enableAccelerometer(boolean isEnable) {
        accelerometerName.setEnabled(isEnable);
        accelerometerFrequency.setEnabled(isEnable);
        accelerometerHiPassFrequency.setEnabled(isEnable);

    }


    
    private void showAdvanced(boolean isVisible){
        channelsHeadersLabels[5].setVisible(isVisible);
        channelsHeadersLabels[6].setVisible(isVisible);
        channelsHeadersLabels[7].setVisible(isVisible);
        for (int i = 0; i < edfModel.getAdsModel().getNumberOfAdsChannels(); i++) {
            channelElectrodeType[i].setVisible(isVisible);
            channelDrlEnabled[i].setVisible(isVisible);
            channelLoffEnable[i].setVisible(isVisible);
        }
    }

    private void setAdvanced() {
        if(isAdvanced){
            advancedButton.setIcon(iconMinus);
            showAdvanced(true);
            isAdvanced = false;
        }
        else{
            advancedButton.setIcon(iconPlus);
            showAdvanced(false);
            isAdvanced = true;
        }
        pack();
        // place the window to the screen center
        setLocationRelativeTo(null);
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

    private boolean isChannelLoffEnable(int channelNumber){
        return channelLoffEnable[channelNumber].isSelected();
    }

    private boolean isChannelDrlEnabled(int channelNumber){
        return channelDrlEnabled[channelNumber].isSelected();
    }

    private String getChannelName(int channelNumber) {
        return channelName[channelNumber].getText();
    }
    
    private String getComPortName(){
        return comPortName.getText();
    }
    
    private String getElectrodeType(int channelNumber){
        return channelElectrodeType[channelNumber].getText();
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
                enableAdsChannel(channelNumber, true);
            } else {
                enableAdsChannel(channelNumber, false);
            }
        }
    }
}
