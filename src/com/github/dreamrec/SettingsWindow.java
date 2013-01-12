package com.github.dreamrec;

import com.github.dreamrec.ads.AdsModel;
import com.github.dreamrec.ads.HighPassFrequency;
import com.github.dreamrec.ads.Sps;
import com.github.dreamrec.layout.gnu.TableLayout;
import com.github.dreamrec.layout.gnu.TableOption;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 */
public class SettingsWindow extends JFrame {
    private AdsModel adsModel;
    
    private JComboBox  sps;
    private String spsLabel = "SPS";

    private JComboBox[]  frequency;
    private JComboBox[]  highPassFrequency;
    private JCheckBox[]  isEnable;
    private JCheckBox[]  isLoffEnable;
    private JCheckBox[]  drl;
    private JTextField[] name;
    private JButton startButton = new JButton( "Start" );

    private String title = "Simple EDF Server";
    private String[] header = {"Number", "Enable", "Name", "Frequency", "<html><center>High Pass <br> Filter Frequency</center></html>", "DRL", "Loff Status"};
    private int textFieldLength = 10;
    int hgap = 20;
    int vgap = 5;




    public  SettingsWindow (AdsModel adsModel) {
        this.adsModel = adsModel;
        setDefaultCloseOperation( EXIT_ON_CLOSE );
        
        int nChannels = adsModel.getNumberOfChannels();
        
        sps = new JComboBox(Sps.values());

        frequency = new JComboBox[nChannels];
        highPassFrequency = new JComboBox[nChannels];
        isEnable = new JCheckBox[nChannels];
        isLoffEnable = new JCheckBox[nChannels];
        drl = new JCheckBox[nChannels];
        name = new JTextField[nChannels];
        
        for (int i = 0; i < nChannels; i++) {
            frequency[i] = new JComboBox();
            highPassFrequency[i] = new JComboBox(HighPassFrequency.values());
            isEnable[i] = new JCheckBox();
            isLoffEnable[i] = new JCheckBox();
            drl[i] = new JCheckBox();
            name[i] = new JTextField(textFieldLength);
        }

        startButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

            }
        });

        arrangeForm();
 
    }


    
    private void arrangeForm(){
        setTitle( title );
        
        JPanel adsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        adsPanel.setBorder(BorderFactory.createEtchedBorder());
        adsPanel.add(new JLabel(spsLabel));
        adsPanel.add(sps);
        

        JPanel channelPanel = new JPanel(new TableLayout(header.length, new TableOption(TableOption.CENTRE, TableOption.CENTRE) , hgap, vgap));

        for (int i = 0; i < header.length; i++) {
            channelPanel.add(new JLabel(header[i]));

        }

        for (int i = 0; i < adsModel.getNumberOfChannels(); i++) {
            channelPanel.add(new JLabel(" "+i+" "));
            channelPanel.add(isEnable[i]);
            channelPanel.add(name[i]);
            channelPanel.add(frequency[i]);
            channelPanel.add(highPassFrequency[i]);
            channelPanel.add(drl[i]);
            channelPanel.add(isLoffEnable[i]);
        }

        JPanel buttonPanel = new JPanel();
        buttonPanel.add( startButton );
        buttonPanel.setBorder(BorderFactory.createEtchedBorder());

        // Root Panel of the SettingsWindow
        add( adsPanel , BorderLayout.NORTH);
        add( channelPanel , BorderLayout.CENTER);
        add( buttonPanel, BorderLayout.SOUTH );
        pack();
        // place the window to the screen center
        setLocationRelativeTo(null);
        setVisible(true);
    }

}
