package com.github.dreamrec;

import com.github.dreamrec.gcomponent.GComponentModel;
import com.github.dreamrec.gcomponent.GComponentView;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import static com.github.dreamrec.GUIActions.*;

/**
 *
 */
public class MainWindow extends JFrame {

    private JPanel mainPanel;
    private Model model;
    private SlowGraphScrollBar slowGraphScrollBar;

    public MainWindow(Model model, Controller controller) {
        this.model = model;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        mainPanel = new JPanel(new GridLayout(0, 1)) ;
        for (GComponentModel gModel : model.getGModels()) {
            mainPanel.add(new GComponentView(gModel, controller));
        }
        add(mainPanel, BorderLayout.NORTH);
        slowGraphScrollBar = new SlowGraphScrollBar(model,controller);
        slowGraphScrollBar.setVisible(false);
        add(slowGraphScrollBar, BorderLayout.CENTER);
        pack();
        // place the window to the screen center
        setLocationRelativeTo(null);
        setVisible(true);
        registerKeyActions();
        setActionMap(new GUIActions(controller).getActionMap());
    }


    public void setActionMap(ActionMap actionMap){
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
        if(model.getSlowDataSize() > model.getXSize()){
            if(!slowGraphScrollBar.isVisible()){
                slowGraphScrollBar.setVisible(true);
                pack();
            }
            slowGraphScrollBar.updateModel();
        }
    }
}
