package com.github.dreamrec;

import com.github.dreamrec.gcomponent.GComponentView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import static com.github.dreamrec.GUIActions.*;

/**
 *
 */
public class MainWindow extends JFrame {

    private JPanel mainPanel;
    private Model model;
    private Controller controller;
    private GraphScrollBar graphScrollBar;

    public MainWindow(Controller controller, Model model) {
        this.controller = controller;
        this.model = model;
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    private void createAndShowGUI() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        mainPanel = new JPanel(new GridLayout(0, 1));

        Filter<Integer> fastDreamView = new FirstDerivativeAbsFilter(model.getEyeDataList());
        mainPanel.add(Factory.getGComponentView(fastDreamView, model, controller));

        GComponentView eyeDataView = Factory.getGComponentView(model.getEyeDataList(), model, controller);
        mainPanel.add(eyeDataView);
        eyeDataView.getComponentModel().centreX();

        Filter<Integer> slowDreamView = new AveragingFilter(new FirstDerivativeAbsFilter(model.getEyeDataList()), Model.DIVIDER);
        mainPanel.add(Factory.getGComponentView(slowDreamView, model, controller));

        add(mainPanel, BorderLayout.CENTER);
        graphScrollBar = Factory.getSlowGraphScrollBar(model, controller);
        add(graphScrollBar, BorderLayout.SOUTH);
        registerKeyActions();
        pack();
        // place the window to the screen center
        setLocationRelativeTo(null);
        setVisible(true);
        setActionMap(new GUIActions(controller).getActionMap());
    }


    public void setActionMap(ActionMap actionMap) {
        mainPanel.setActionMap(actionMap);
    }

    public void showMessage(String s) {
        JOptionPane.showMessageDialog(null, s);
    }

    private void registerKeyActions() {
        mainPanel.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK), SAVE_ACTION);
        mainPanel.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK), OPEN_ACTION);
        mainPanel.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), SCROLL_CURSOR_BACKWARD_ACTION);
        mainPanel.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), SCROLL_CURSOR_FORWARD_ACTION);
    }

    @Override
    public void repaint() {
        super.repaint();
        graphScrollBar.updateModel();
    }
}
