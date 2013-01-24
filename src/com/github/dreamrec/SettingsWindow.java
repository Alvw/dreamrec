package com.github.dreamrec;

import com.github.dreamrec.ads.*;
import com.github.dreamrec.layout.gnu.TableLayout;
import com.github.dreamrec.layout.gnu.TableOption;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


/**
 *
 */
public class SettingsWindow extends JFrame {
    private AdsModel adsModel;
    private Controller controller;


    private JComboBox spsField;
    private String spsLabel = "Sampling Frequency (Hz)";
    private String[] accelerometerNamesEnds = {"X", "Y", "Z"};

    private JComboBox[] channelFrequency;
    private JComboBox[] channelHiPassFrequency;
    private JCheckBox[] channelEnable;
    private JTextField[] channelName;

    private JComboBox accelerometerFrequency;
    private JTextField accelerometerName;
    private JCheckBox accelerometerEnable;
    private JComboBox accelerometerHiPassFrequency;

    private ColoredLabel markerLabel = new ColoredLabel();
    private Color recordColor = Color.GREEN;
    private JLabel reportLabel = new JLabel();
    private JPanel reportPanel = new JPanel();

    private boolean isRecording = false;
    private String start = "Start";
    private String stop = "Stop";
    private JButton startButton = new JButton(start);
    private JButton saveAsButton = new JButton("Save As");

    private Color okColor = Color.GREEN;
    private Color problemColor = Color.RED;
    private ColoredLabel[] channelLoffStatPositive;
    private ColoredLabel[] channelLoffStatNegative;

    private String title = "EDF Recorder";
    private String[] channelsHeaders = {"Number", "Enable", "Name", "Frequency (Hz)", "Hi Pass Filter (Hz)",  "Lead Off Detection"};


    public SettingsWindow(Controller controller) {
        this.controller = controller;
        adsModel = controller.getAdsModel();
        init();
        arrangeForm();
        setActions();
        loadDataFromModel();
        setVisible(true);
    }

    private void init() {
        int adsChannelsNumber = adsModel.getNumberOfAdsChannels();

        spsField = new JComboBox(Sps.values());
        spsField.setSelectedItem(adsModel.getSps());

        channelFrequency = new JComboBox[adsChannelsNumber];
        channelHiPassFrequency = new JComboBox[adsChannelsNumber];
        channelEnable = new JCheckBox[adsChannelsNumber];
        channelName = new JTextField[adsChannelsNumber];
        channelLoffStatPositive = new ColoredLabel[adsChannelsNumber];
        channelLoffStatNegative = new ColoredLabel[adsChannelsNumber];

        int textFieldLength = 10;
        for (int i = 0; i < adsChannelsNumber; i++) {
            channelFrequency[i] = new JComboBox();
            channelHiPassFrequency[i] = new JComboBox(HiPassFrequency.values());
            channelEnable[i] = new JCheckBox();
            channelName[i] = new JTextField(textFieldLength);
            channelLoffStatPositive[i] = new ColoredLabel();
            channelLoffStatNegative[i] = new ColoredLabel();
        }


        accelerometerEnable = new JCheckBox();
        accelerometerName = new JTextField(textFieldLength);
        accelerometerHiPassFrequency = new JComboBox(HiPassFrequency.values());
        accelerometerFrequency = new JComboBox();
        accelerometerFrequency.setEnabled(false);
    }

    private void setActions() {

        for (int i = 0; i < adsModel.getNumberOfAdsChannels(); i++) {
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
                    isRecording = true;
                    disableFields();
                    startButton.setText(stop);
                    saveDataToModel();
                    controller.startRecording();
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

        int hgap = 5;
        int vgap = 10;

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(startButton);

        JPanel adsPanel = new JPanel();
        adsPanel.add(new JLabel(spsLabel));
        adsPanel.add(spsField);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, hgap, vgap));
        topPanel.add(adsPanel);
        topPanel.add(buttonPanel);


        hgap = 20;
        vgap = 5;
        JPanel channelsPanel = new JPanel(new TableLayout(channelsHeaders.length, new TableOption(TableOption.CENTRE, TableOption.CENTRE), hgap, vgap));

        for (int i = 0; i < channelsHeaders.length; i++) {
            channelsPanel.add(new JLabel(channelsHeaders[i]));

        }

        for (int i = 0; i < adsModel.getNumberOfAdsChannels(); i++) {
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
        
        if(adsModel.getNumberOfAccelerometerChannels() > 0) {
            // Add line of accelerometer
            channelsPanel.add(new JLabel(" " + adsModel.getNumberOfAdsChannels() + " "));
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
        JPanel channelsBorderPanel = new JPanel();
        channelsBorderPanel.setBorder(BorderFactory.createTitledBorder("Channels"));
        channelsBorderPanel.add(channelsPanel);

        reportPanel.add(new JLabel(" "));
        reportPanel.add(markerLabel);
        reportPanel.add(reportLabel);


        // Root Panel of the SettingsWindow
        add(topPanel, BorderLayout.NORTH);
        add(channelsBorderPanel, BorderLayout.CENTER);
        add(reportPanel, BorderLayout.SOUTH);
        pack();
        // place the window to the screen center
        setLocationRelativeTo(null);
    }

    private void disableEnableFields(boolean isEnable) {
        spsField.setEnabled(isEnable);

        accelerometerName.setEnabled(isEnable);
        accelerometerEnable.setEnabled(isEnable);
        accelerometerHiPassFrequency.setEnabled(isEnable);

        for (int i = 0; i < adsModel.getNumberOfAdsChannels(); i++) {
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
        for (int i = 0; i < adsModel.getNumberOfAdsChannels(); i++) {
            if (!isChannelEnable(i)) {
                disableAdsChannel(i);
            }
        }
        if (!adsModel.isAccelerometerEnabled()){
            disableAccelerometer();
        }
    }

    public void setReport(boolean isRecording, String report) {
        reportLabel.setText(report);
        if (isRecording) {
            markerLabel.setColor(recordColor);
        } else {
            markerLabel.setBackgroundColor();
        }
        pack();
    }

    private void loadDataFromModel() {
        spsField.setSelectedItem(adsModel.getSps());
        int numberOfAdsChannels = adsModel.getNumberOfAdsChannels();
        for (int i = 0; i < numberOfAdsChannels; i++) {
            AdsChannelModel channel = adsModel.getAdsChannel(i);
            channelName[i].setText(channel.getName());
            channelEnable[i].setSelected(channel.isEnabled());
            channelHiPassFrequency[i].setSelectedItem(channel.getHiPassFilterFrequency());
            if (!channel.isEnabled()) {
                disableAdsChannel(i);
            }

        }

        if (adsModel.getNumberOfAccelerometerChannels() > 0) {
            accelerometerName.setText(adsModel.getAccelerometerName());
            accelerometerEnable.setSelected(adsModel.isAccelerometerEnabled());
            accelerometerHiPassFrequency.setSelectedItem(adsModel.getAccelerometerHiPassFrequency());
            if(!adsModel.isAccelerometerEnabled()){
                disableAccelerometer();
            }
        }
        setChannelsFrequencies(adsModel.getSps());
    }

    public void updateLoffStatus(int loffStatusRegisterValue) {
        if ((loffStatusRegisterValue & 8) == 0) {
            channelLoffStatPositive[0].setColor(okColor);
        } else {
            channelLoffStatPositive[0].setColor(problemColor);
        }
        if ((loffStatusRegisterValue & 16) == 0) {
            channelLoffStatNegative[0].setColor(okColor);
        } else {
            channelLoffStatNegative[0].setColor(problemColor);
        }
        if ((loffStatusRegisterValue & 32) == 0) {
            channelLoffStatPositive[1].setColor(okColor);
        } else {
            channelLoffStatPositive[1].setColor(problemColor);
        }
        if ((loffStatusRegisterValue & 64) == 0) {
            channelLoffStatNegative[1].setColor(okColor);
        } else {
            channelLoffStatNegative[1].setColor(problemColor);
        }

    }

    private void saveDataToModel() {
        adsModel.setSps(getSps());
        int numberOfAdsChannels = adsModel.getNumberOfAdsChannels();
        for (int i = 0; i < numberOfAdsChannels; i++) {
            AdsChannelModel channel = adsModel.getAdsChannel(i);
            channel.setName(getChannelName(i));
            channel.setDivider(getChannelDivider(i));
            channel.setHiPassFilterFrequency(getChannelFrequency(i), getChannelHiPassFrequency(i));
            channel.setEnabled(isChannelEnable(i));
        }

        int numberOfAccelerometerChannels = adsModel.getNumberOfAccelerometerChannels();
        for (int i = 0; i < numberOfAccelerometerChannels; i++) {
            ChannelModel channel = adsModel.getAccelerometerChannel(i);
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
        int numberOfAdsChannels = adsModel.getNumberOfAdsChannels();
        Integer[] availableFrequencies = sps.getChannelsAvailableFrequencies();
        // set available frequencies
        for (int i = 0; i < numberOfAdsChannels; i++) {
            channelFrequency[i].removeAllItems();
            for (Integer frequency : availableFrequencies) {
                channelFrequency[i].addItem(frequency);
            }
            // select channel frequency
            ChannelModel channel = adsModel.getAdsChannel(i);
            Integer frequency = sps.getValue() / channel.getDivider().getValue();
            channelFrequency[i].setSelectedItem(frequency);
            
        }
        if(adsModel.getNumberOfAccelerometerChannels() > 0){
            accelerometerFrequency.removeAllItems();
            for (Integer frequency : availableFrequencies) {
                accelerometerFrequency.addItem(frequency);
            }
            accelerometerFrequency.setSelectedItem(sps.getAccelerometerFrequency());
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
        accelerometerHiPassFrequency.setEnabled(false);

    }

    private void enableAccelerometer() {
        accelerometerName.setEnabled(true);
        accelerometerHiPassFrequency.setEnabled(true);
    }



    private Divider getChannelDivider(int channelNumber) {
        int divider = adsModel.getSps().getValue() / getChannelFrequency(channelNumber);
        return Divider.valueOf(divider);
    }

    private Divider getAccelerometerDivider() {
        int divider = adsModel.getSps().getValue() / getAccelerometerFrequency();
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
