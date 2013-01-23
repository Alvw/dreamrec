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
public class SettingsWindow  extends JFrame{
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
    private Color recordColor = Color.RED;
    private Color stopColor = Color.GREEN;
    private JLabel reportLabel = new JLabel();
    private JPanel reportPanel = new JPanel();
    
    private JButton startButton = new JButton("Start");
    private JButton stopButton = new JButton("Stop");
    private JButton saveAsButton = new JButton("Save As");

    private Color okColor = Color.GREEN;
    private Color problemColor = Color.RED;
    private ColoredLabel[] channelLoffStatPositive;
    private ColoredLabel[] channelLoffStatNegative;

    private String title = "Simple EDF Recorder";
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

        reportPanel.setVisible(false);
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
                    disableAccelerometerChannel();
                }
            }
        });


        spsField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JComboBox comboBox = (JComboBox) actionEvent.getSource();
                Sps sps = (Sps) comboBox.getSelectedItem();
                setAdsChannelAvailableFrequencies(sps);
                setAccelerometerAvailableFrequencies(sps);
            }
        });


        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                disableFields();
                startButton.setEnabled(false);
                stopButton.setEnabled(true);
                saveDataToModel();
                reportPanel.setVisible(true);
                controller.startRecording();
            }
        });


        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                enableFields();
                startButton.setEnabled(true);
                stopButton.setEnabled(false);
                controller.stopRecording();
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                controller.closeApplication();
                System.exit(0);
            }
        });
    }


    private void arrangeForm() {
        setTitle(title);

        int top = 10;
        int left = 5;
        int bottom = 10;
        int right = 5;
        JPanel adsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        adsPanel.add(new JLabel(spsLabel));
        adsPanel.add(spsField);
        adsPanel.setBorder(BorderFactory.createEmptyBorder(top, left, bottom, right));

        /*       JLabel colorLabel = new JLabel();
     colorLabel.setOpaque(true);
     colorLabel.setBackground(Color.RED);
     colorLabel.setPreferredSize(new Dimension(10, 10));
     adsPanel.add(colorLabel);   */

        int hgap = 20;
        int vgap = 5;
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
        // Add line of accelerometer
        channelsPanel.add(new JLabel(" " + adsModel.getNumberOfAdsChannels() + " "));
        channelsPanel.add(accelerometerEnable);
        channelsPanel.add(accelerometerName);
        channelsPanel.add(accelerometerFrequency);
        channelsPanel.add(accelerometerHiPassFrequency);
        channelsPanel.add(new JLabel(""));
        channelsPanel.add(new JLabel(""));
        
        channelsPanel.setBorder(BorderFactory.createEmptyBorder(top, left, bottom, right));
        JPanel channelsBorderPanel = new JPanel();
        channelsBorderPanel.setBorder(BorderFactory.createTitledBorder("Channels"));
        channelsBorderPanel.add(channelsPanel);

        reportPanel.add(markerLabel);
        reportPanel.add(reportLabel);
        reportPanel.setBorder(BorderFactory.createTitledBorder(""));
    

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(startButton);
        buttonPanel.add(stopButton);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(top, left, bottom, right));


        JPanel groupPanel = new JPanel(new BorderLayout());
        groupPanel.add(buttonPanel, BorderLayout.NORTH);
        groupPanel.add(reportPanel, BorderLayout.CENTER);



        // Root Panel of the SettingsWindow
        add(adsPanel, BorderLayout.NORTH);
        add(channelsBorderPanel, BorderLayout.CENTER);
        add(groupPanel, BorderLayout.SOUTH);
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

    private void disableFields(){
        disableEnableFields(false);
        
        
    }
    

    private void enableFields() {
         disableEnableFields(true);

        for (int i = 0; i < adsModel.getNumberOfAdsChannels(); i++) {
            if(!isChannelEnable(i)){
                disableAdsChannel(i);
            }
        }
    }

    public void setReport(boolean isRecording, String report){
        reportLabel.setText(report);
        if (isRecording) {
            markerLabel.setColor(recordColor);
        }
        else{
            markerLabel.setColor(stopColor);
        }
        pack();
    }

    private void loadDataFromModel() {
        spsField.setSelectedItem(adsModel.getSps());
        int numberOfAdsChannels = adsModel.getNumberOfAdsChannels();
        for (int i = 0; i < numberOfAdsChannels; i++) {
            AdsChannelModel channel = adsModel.getAdsChannel(i);
            channelName[i].setText(channel.getName());
            if (channel.getDivider() == 0) {
                channelEnable[i].setSelected(false);
                disableAdsChannel(i);
            } else {
                channelEnable[i].setSelected(true);
                setAdsChannelFrequency(i);
                setAdsChannelHiPassFrequency(i);
            }
        }

        if (adsModel.getNumberOfAccelerometerChannels() > 0) {
            ChannelModel channel = adsModel.getAccelerometerChannel(0);
            StringBuilder name = new StringBuilder(channel.getName());
            //delete last symbol
            name.deleteCharAt(name.length()-1);
            accelerometerName.setText(name.toString());
            if (channel.getDivider() == 0) {
                accelerometerEnable.setSelected(false);
                disableAccelerometerChannel();
            } else {
                accelerometerEnable.setSelected(true);
                setAccelerometerFrequency();
                setAccelerometerHiPassFrequency();
            }

        }
    }

    public void updateLoffStatus(int loffStatusRegisterValue){
        if((loffStatusRegisterValue & 8) == 0) {
            channelLoffStatPositive[0].setColor(okColor);
        }
        else{
            channelLoffStatPositive[0].setColor(problemColor);
        }
        if((loffStatusRegisterValue & 16) == 0) {
            channelLoffStatNegative[0].setColor(okColor);
        }
        else{
            channelLoffStatNegative[0].setColor(problemColor);
        }
        if((loffStatusRegisterValue & 32) == 0) {
            channelLoffStatPositive[1].setColor(okColor);
        } else{
            channelLoffStatPositive[1].setColor(problemColor);
        }
        if((loffStatusRegisterValue & 64) == 0) {
            channelLoffStatNegative[1].setColor(okColor);
        }
        else{
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
            channel.setHiPassPreFilterBufferSize(getAdsChannelHiPassBufferSize(i));
        }

        int numberOfAccelerometerChannels = adsModel.getNumberOfAccelerometerChannels();
        for (int i = 0; i < numberOfAccelerometerChannels; i++) {
            ChannelModel channel = adsModel.getAccelerometerChannel(i);
            if (i < accelerometerNamesEnds.length) {
                channel.setName(getAccelerometerName() + accelerometerNamesEnds[i]);
            } else {
                channel.setName(getAccelerometerName());
            }
            channel.setDivider(getAccelerometerDivider());
            channel.setHiPassPreFilterBufferSize(getAccelerometerHiPassBufferSize());
        }
    }

    private void setAdsChannelAvailableFrequencies(Sps sps) {
        int numberOfAdsChannels = adsModel.getNumberOfAdsChannels();
        for (int i = 0; i < numberOfAdsChannels; i++) {
            setAdsChannelAvailableFrequencies(sps, i);
        }
    }

    private void setAdsChannelAvailableFrequencies(Sps sps, int channelNumber) {
        Integer[] availableFrequencies = sps.getAdsChannelsAvailableFrequencies();
        channelFrequency[channelNumber].removeAllItems();
        for (Integer frequency : availableFrequencies) {
            channelFrequency[channelNumber].addItem(frequency);
        }
        if (!isChannelEnable(channelNumber)) {
            channelFrequency[channelNumber].addItem("");
            channelFrequency[channelNumber].setSelectedItem("");
        }
    }


    private void setAccelerometerAvailableFrequencies(Sps sps) {
        Integer[] availableFrequencies = sps.getAccelerometerChannelsAvailableFrequencies();
        accelerometerFrequency.removeAllItems();
        for (Integer frequency : availableFrequencies) {
            accelerometerFrequency.addItem(frequency);
        }
        if (!isAccelerometerEnable()) {
            accelerometerFrequency.addItem("");
            accelerometerFrequency.setSelectedItem("");
        }
    }

    private void disableAdsChannel(int channelNumber) {
        channelFrequency[channelNumber].addItem("");
        channelFrequency[channelNumber].setSelectedItem("");
        channelFrequency[channelNumber].setEnabled(false);
        channelHiPassFrequency[channelNumber].setSelectedItem(HiPassFrequency.DISABLED);
        channelHiPassFrequency[channelNumber].setEnabled(false);
        channelName[channelNumber].setEnabled(false);
    }

    private void enableAdsChannel(int channelNumber) {
        Sps sps = (Sps) spsField.getSelectedItem();
        setAdsChannelAvailableFrequencies(sps, channelNumber);
        channelFrequency[channelNumber].setEnabled(true);
        channelHiPassFrequency[channelNumber].setEnabled(true);
        channelName[channelNumber].setEnabled(true);
    }

    private void disableAccelerometerChannel() {
        accelerometerFrequency.addItem("");
        accelerometerFrequency.setSelectedItem("");
        //  accelerometerFrequency.setEnabled(false);
        accelerometerName.setEnabled(false);
        accelerometerHiPassFrequency.setSelectedItem(HiPassFrequency.DISABLED);
        accelerometerHiPassFrequency.setEnabled(false);

    }

    private void enableAccelerometer() {
        Sps sps = (Sps) spsField.getSelectedItem();
        setAccelerometerAvailableFrequencies(sps);
        // accelerometerFrequency[channelNumber].setEnabled(true);
        accelerometerName.setEnabled(true);
        accelerometerHiPassFrequency.setEnabled(true);
    }

    /*
     *   hiPassFrequency = channelFrequency / hiPassPreFilterBufferSize =  sps / (channelDivider *  hiPassPreFilterBufferSize)
     *   channelFrequency = sps / channelDivider
     */
    private void setAdsChannelHiPassFrequency(int channelNumber) {
        HiPassFrequency hiPassFrequency = HiPassFrequency.DISABLED;
        int hiPassPreFilterBufferSize = adsModel.getAdsChannel(channelNumber).getHiPassPreFilter().getBufferSize();
        int divider = adsModel.getAdsChannel(channelNumber).getDivider();
        if ((hiPassPreFilterBufferSize * divider) != 0) {
            double hiPassFrequencyValue = new Double(adsModel.getSps().getValue()) / (divider * hiPassPreFilterBufferSize);
            hiPassFrequency = HiPassFrequency.valueOf(hiPassFrequencyValue);
        }
        channelHiPassFrequency[channelNumber].setSelectedItem(hiPassFrequency);
    }

    private void setAccelerometerHiPassFrequency() {
        HiPassFrequency hiPassFrequency = HiPassFrequency.DISABLED;
        if (adsModel.getNumberOfAccelerometerChannels() > 0) {
            int hiPassPreFilterBufferSize = adsModel.getAccelerometerChannel(0).getHiPassPreFilter().getBufferSize();
            int divider = adsModel.getAccelerometerChannel(0).getDivider();
            if ((hiPassPreFilterBufferSize * divider) != 0) {
                double hiPassFrequencyValue = new Double(adsModel.getSps().getValue()) / (divider * hiPassPreFilterBufferSize);
                hiPassFrequency = HiPassFrequency.valueOf(hiPassFrequencyValue);
            }
        }
        accelerometerHiPassFrequency.setSelectedItem(hiPassFrequency);
    }

    private int getAdsChannelHiPassBufferSize(int channelNumber) {
        int hiPassPreFilterBufferSize = 0;
        if (getChannelDivider(channelNumber) * getChannelHiPassFrequency(channelNumber) != 0) {
            hiPassPreFilterBufferSize = (int) (getSps().getValue() / (getChannelDivider(channelNumber) * getChannelHiPassFrequency(channelNumber)));

        }
        return hiPassPreFilterBufferSize;
    }

    private int getAccelerometerHiPassBufferSize() {
        int hiPassPreFilterBufferSize = 0;
        if (getAccelerometerDivider() * getAccelerometerHiPassFrequency() != 0) {
            hiPassPreFilterBufferSize = (int) (getSps().getValue() / (getAccelerometerDivider() * getAccelerometerHiPassFrequency()));

        }
        return hiPassPreFilterBufferSize;
    }

    private void setAdsChannelFrequency(int channelNumber) {
        int divider = adsModel.getAdsChannel(channelNumber).getDivider();
        if (divider != 0) {
            setAdsChannelAvailableFrequencies(adsModel.getSps(), channelNumber);
            Integer frequency = adsModel.getSps().getValue() / divider;
            channelFrequency[channelNumber].setSelectedItem(frequency);
        }
    }

    private void setAccelerometerFrequency() {
        int divider = adsModel.getAccelerometerChannel(0).getDivider();
        if (divider != 0) {
            setAccelerometerAvailableFrequencies(adsModel.getSps());
            Integer frequency = adsModel.getSps().getValue() / divider;
            accelerometerFrequency.setSelectedItem(frequency);
        }
    }

    private int getChannelDivider(int channelNumber) {
        int divider = 0;
        if (isChannelEnable(channelNumber)) {
            divider = adsModel.getSps().getValue() / getChannelFrequency(channelNumber);
        }
        return divider;
    }

    private int getAccelerometerDivider() {
        int divider = 0;
        if (isAccelerometerEnable()) {
            divider = adsModel.getSps().getValue() / getAccelerometerFrequency();
        }
        return divider;
    }


    private int getChannelFrequency(int channelNumber) {
        return (Integer) channelFrequency[channelNumber].getSelectedItem();
    }

    private double getChannelHiPassFrequency(int channelNumber) {
        return ((HiPassFrequency) channelHiPassFrequency[channelNumber].getSelectedItem()).getValue();
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

    private double getAccelerometerHiPassFrequency() {
        return ((HiPassFrequency) accelerometerHiPassFrequency.getSelectedItem()).getValue();
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
