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
import java.util.ArrayList;


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
    private JCheckBox[] channelLoffEnable;
    private JCheckBox[] channelRldSenseEnable;
    private JTextField[] channelName;

    private JComboBox accelerometerFrequency;
    private JTextField accelerometerName;
    private JCheckBox accelerometerEnable;
    private JComboBox accelerometerHiPassFrequency;


    private JButton startButton = new JButton("Start");
    private JButton stopButton = new JButton("Stop");

    private Color okColor = Color.GREEN;
    private Color problemColor = Color.RED;

    private String title = "Simple EDF Recorder";
    private String[] channelsHeaders = {"Number", "Enable", "Name", "Frequency (Hz)", "Hi Pass Filter (Hz)", "DRL", "Lead Off Detection"};
    private String[] accelerometerHeaders = {"Enable", "Name", "Frequency (Hz)", "Hi Pass Filter (Hz)"};


    public SettingsWindow(Controller controller) {
        this.controller = controller;
        adsModel = controller.getAdsModel();
        init();
        arrangeForm();
        setActions();
        loadDataFromModel();
    }

    private void init() {
        int adsChannelsNumber = adsModel.getNumberOfAdsChannels();
        int accelerometerChannelsNumber = adsModel.getNumberOfAccelerometerChannels();

        spsField = new JComboBox(Sps.values());
        spsField.setSelectedItem(adsModel.getSps());

        channelFrequency = new JComboBox[adsChannelsNumber];
        channelHiPassFrequency = new JComboBox[adsChannelsNumber];
        channelEnable = new JCheckBox[adsChannelsNumber];
        channelLoffEnable = new JCheckBox[adsChannelsNumber];
        channelRldSenseEnable = new JCheckBox[adsChannelsNumber];
        channelName = new JTextField[adsChannelsNumber];

        int textFieldLength = 10;
        for (int i = 0; i < adsChannelsNumber; i++) {
            channelFrequency[i] = new JComboBox();
            channelHiPassFrequency[i] = new JComboBox(HiPassFrequency.values());
            channelEnable[i] = new JCheckBox();
            channelLoffEnable[i] = new JCheckBox();
            channelRldSenseEnable[i] = new JCheckBox();
            channelName[i] = new JTextField(textFieldLength);
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
                startButton.setEnabled(false);
                stopButton.setEnabled(true);
                saveDataToModel();
                controller.startRecording();

            }
        });


        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
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
        JPanel adsChannelsPanel = new JPanel(new TableLayout(channelsHeaders.length, new TableOption(TableOption.CENTRE, TableOption.CENTRE), hgap, vgap));

        for (int i = 0; i < channelsHeaders.length; i++) {
            adsChannelsPanel.add(new JLabel(channelsHeaders[i]));

        }

        for (int i = 0; i < adsModel.getNumberOfAdsChannels(); i++) {
            adsChannelsPanel.add(new JLabel(" " + i + " "));
            adsChannelsPanel.add(channelEnable[i]);
            adsChannelsPanel.add(channelName[i]);
            adsChannelsPanel.add(channelFrequency[i]);
            adsChannelsPanel.add(channelHiPassFrequency[i]);
            adsChannelsPanel.add(channelRldSenseEnable[i]);
            adsChannelsPanel.add(channelLoffEnable[i]);
        }
        adsChannelsPanel.setBorder(BorderFactory.createEmptyBorder(top, left, bottom, right));
        JPanel adsChannelsBorderPanel = new JPanel();
        adsChannelsBorderPanel.setBorder(BorderFactory.createTitledBorder("Ads Channels"));
        adsChannelsBorderPanel.add(adsChannelsPanel);


        JPanel accelerometerPanel = new JPanel(new TableLayout(accelerometerHeaders.length, new TableOption(TableOption.CENTRE, TableOption.CENTRE), hgap, vgap));
        for (int i = 0; i < accelerometerHeaders.length; i++) {
            accelerometerPanel.add(new JLabel(accelerometerHeaders[i]));
        }
        accelerometerPanel.add(accelerometerEnable);
        accelerometerPanel.add(accelerometerName);
        accelerometerPanel.add(accelerometerFrequency);
        accelerometerPanel.add(accelerometerHiPassFrequency);
        accelerometerPanel.setBorder(BorderFactory.createEmptyBorder(top, left, bottom, right));

        JPanel accelerometerBorderPanel = new JPanel();
        accelerometerBorderPanel.setBorder(BorderFactory.createTitledBorder("Accelerometer"));
        accelerometerBorderPanel.add(accelerometerPanel);


        hgap = 0;
        vgap = 20;
        JPanel channelsPanel = new JPanel(new BorderLayout(hgap, vgap));
        channelsPanel.add(adsChannelsBorderPanel, BorderLayout.NORTH);
        channelsPanel.add(accelerometerBorderPanel, BorderLayout.CENTER);


        JPanel buttonPanel = new JPanel();
        buttonPanel.add(startButton);
        buttonPanel.add(stopButton);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(top, left, bottom, right));


        // Root Panel of the SettingsWindow
        add(adsPanel, BorderLayout.NORTH);
        add(channelsPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        pack();
        // place the window to the screen center
        setLocationRelativeTo(null);
        setVisible(true);
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
                channelLoffEnable[i].setSelected(channel.isLoffEnable());
                channelRldSenseEnable[i].setSelected(channel.isRldSenseEnabled());
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

    private void saveDataToModel() {
        adsModel.setSps(getSps());
        int numberOfAdsChannels = adsModel.getNumberOfAdsChannels();
        for (int i = 0; i < numberOfAdsChannels; i++) {
            AdsChannelModel channel = adsModel.getAdsChannel(i);
            channel.setName(getChannelName(i));
            channel.setLoffEnable(isChannelLoffEnable(i));
            channel.setRldSenseEnabled(isChannelRldSenseEnable(i));
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
        channelLoffEnable[channelNumber].setSelected(false);
        channelLoffEnable[channelNumber].setEnabled(false);
        channelRldSenseEnable[channelNumber].setSelected(false);
        channelRldSenseEnable[channelNumber].setEnabled(false);
        channelName[channelNumber].setEnabled(false);
    }

    private void enableAdsChannel(int channelNumber) {
        Sps sps = (Sps) spsField.getSelectedItem();
        setAdsChannelAvailableFrequencies(sps, channelNumber);
        channelFrequency[channelNumber].setEnabled(true);
        channelHiPassFrequency[channelNumber].setEnabled(true);
        channelLoffEnable[channelNumber].setEnabled(true);
        channelRldSenseEnable[channelNumber].setEnabled(true);
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

    private boolean isChannelLoffEnable(int channelNumber) {
        return channelLoffEnable[channelNumber].isSelected();
    }

    private boolean isChannelRldSenseEnable(int channelNumber) {
        return channelRldSenseEnable[channelNumber].isSelected();
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
