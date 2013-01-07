package com.github.dreamrec;

import com.github.dreamrec.comport.AdsManager;
import com.github.dreamrec.comport.ComPort;
import com.github.dreamrec.gcomponent.GComponentView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

import static com.github.dreamrec.GUIActions.*;

/**
 *
 */
public class MainWindow extends JFrame implements KeyListener{

    private JPanel mainPanel;
    private Model model;
    private Controller controller;
    private GraphScrollBar graphScrollBar;
    private ActionMap actionMap;
    private ApplicationProperties applicationProperties;
    private AdsManager adsManager = new AdsManager();

    public MainWindow(Controller controller, Model model, ApplicationProperties applicationProperties) {
        this.controller = controller;
        this.model = model;
        this.applicationProperties = applicationProperties;
        actionMap = new GUIActions(controller).getActionMap();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    private void createAndShowGUI() {
        setTitle("DreamRec");
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                controller.closeApplication();
                System.exit(0);
            }
        });

        mainPanel = new JPanel(new GridLayout(0, 1));
        mainPanel.addKeyListener(this);

       // Filter<Short> fastDreamView = new FirstDerivativeAbsFilter(model.getEyeDataList());
       // mainPanel.add(Factory.getGComponentView(fastDreamView, model, controller));
        GComponentView acc1DataView = Factory.getGComponentView(model, controller, new LoPassFilter(applicationProperties.getLoPassBufferSize(),model.getAcc1DataList()),
                new LoPassFilter(applicationProperties.getLoPassBufferSize(),model.getAcc2DataList()),
                new LoPassFilter(applicationProperties.getLoPassBufferSize(),model.getAcc3DataList()));
        mainPanel.add(acc1DataView);
        acc1DataView.getComponentModel().centreX();

//        GComponentView eyeDataView = Factory.getGComponentView(model, controller, new FirstDerivativeAbsFilter(model.getEyeDataList()));
        GComponentView eyeDataView = Factory.getGComponentView(model, controller, model.getEyeDataList());
//        GComponentView eyeDataView = Factory.getGComponentView(model, controller, model.getEyeDataList());
        mainPanel.add(eyeDataView);
        eyeDataView.getComponentModel().centreX();

        GComponentView ch2DataView = Factory.getGComponentView(model, controller, model.getCh2DataList());
        mainPanel.add(ch2DataView);
        ch2DataView.getComponentModel().centreX();

        /*Filter<Short> slowDreamView = new AveragingFilter(new FirstDerivativeAbsFilter(model.getEyeDataList()), Model.DIVIDER);
        mainPanel.add(Factory.getGComponentView(model, controller,slowDreamView));*/

        add(mainPanel, BorderLayout.CENTER);
        graphScrollBar = Factory.getSlowGraphScrollBar(model, controller);
        add(graphScrollBar, BorderLayout.SOUTH);
        setActionMap(actionMap);
        registerKeyActions();
        setJMenuBar(new MainMenu(actionMap, applicationProperties));
        pack();
        // place the window to the screen center
        setLocationRelativeTo(null);
        setVisible(true);


    }


    public void setActionMap(ActionMap actionMap) {
        mainPanel.setActionMap(actionMap);
    }

    public void showMessage(String s) {
        JOptionPane.showMessageDialog(this, s);
    }

    private void registerKeyActions() {
        mainPanel.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK), SAVE_ACTION);
        mainPanel.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK), OPEN_ACTION);
        mainPanel.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), SCROLL_CURSOR_BACKWARD_ACTION);
        mainPanel.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), SCROLL_CURSOR_FORWARD_ACTION);
        mainPanel.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_M, InputEvent.CTRL_MASK), START_RECORDING_ACTION);
        mainPanel.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK), STOP_RECORDING_ACTION);
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
       List<Byte> byteList = new ArrayList<Byte>();
       switch (e.getKeyCode()) {
            case KeyEvent.VK_T :
                byteList.addAll(adsManager.writeCommand(0x11));  //stop continious
                byteList.addAll(adsManager.writeRegister(0x42, 0xA3));  //test signal
                byteList.addAll(adsManager.writeRegister(0x44, 0x05));  //ch1 for test signal
                byteList.addAll(adsManager.writeCommand(0x10));   //start continious
                ComPort.getInstance().writeToPort(byteList);
                break;
            case KeyEvent.VK_I:
                byteList.addAll(adsManager.writeCommand(0x11));  //stop continious
                byteList.addAll(adsManager.writeRegister(0x42, 0xA0));  //test signal
                byteList.addAll(adsManager.writeRegister(0x44, 0x00));  //ch1 for test signal
                byteList.addAll(adsManager.writeCommand(0x10));   //start continious
                ComPort.getInstance().writeToPort(byteList);
                break;
            case KeyEvent.VK_S   :
                byteList.addAll(adsManager.writeCommand(0x11));  //stop continious
                byteList.addAll(adsManager.writeRegister(0x42, 0xA0));  //test signal
                byteList.addAll(adsManager.writeRegister(0x44, 0x01));  //ch1 for test signal
                byteList.addAll(adsManager.writeCommand(0x10));   //start continious
                ComPort.getInstance().writeToPort(byteList);
                break;
            case KeyEvent.VK_Y   : ComPort.getInstance().writeToPort(adsManager.startPinHi());   break;
            case KeyEvent.VK_N   : ComPort.getInstance().writeToPort(adsManager.startPinLo());    break;
            case KeyEvent.VK_4   :
                byteList.addAll(adsManager.writeCommand(0x11));  //stop continious
                byteList.addAll(adsManager.writeRegister(0x41, 0x01));  //sps 250
                byteList.addAll(adsManager.writeCommand(0x10));   //start continious
                ComPort.getInstance().writeToPort(byteList);
                break;
            case KeyEvent.VK_5   :
                byteList.addAll(adsManager.writeCommand(0x11));  //stop continious
                byteList.addAll(adsManager.writeRegister(0x41, 0x02));  //sps 500
                byteList.addAll(adsManager.writeCommand(0x10));   //start continious
                ComPort.getInstance().writeToPort(byteList);
                break;
            case KeyEvent.VK_6   :
                byteList.addAll(adsManager.writeCommand(0x11));  //stop continious
                byteList.addAll(adsManager.writeRegister(0x41, 0x03));  //sps 1000
                byteList.addAll(adsManager.writeCommand(0x10));   //start continious
                ComPort.getInstance().writeToPort(byteList);
                break;
//            case KeyEvent.VK_7   : ComPort.getInstance().writeToPort("7".getBytes());   break;
//            case KeyEvent.VK_2   : ComPort.getInstance().writeToPort("2".getBytes());   break;
//            case KeyEvent.VK_1   : ComPort.getInstance().writeToPort("1".getBytes());   break;
//            case KeyEvent.VK_R   : ComPort.getInstance().writeToPort("r".getBytes());   break;
    }
    }




    public void keyReleased(KeyEvent e) {
    }
}
