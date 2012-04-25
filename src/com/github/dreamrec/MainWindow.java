package com.github.dreamrec;

import com.github.dreamrec.gcomponent.GComponentModel;
import com.github.dreamrec.gcomponent.GComponentView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import static com.github.dreamrec.GUIActions.*;

/**
 *
 */
public class MainWindow extends JFrame {

    private JPanel mainPanel;

    public MainWindow(final GComponentModel... gComponentModels) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI(gComponentModels);
            }
        });
    }

    private void createAndShowGUI(GComponentModel[] gComponentModels) {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        mainPanel = new JPanel(new GridLayout(0, 1)) ;
        for (GComponentModel gComponentModel : gComponentModels) {
            mainPanel.add(new GComponentView(gComponentModel));
        }
        add(mainPanel);
        registerKeyActions();
        pack();
        // place the window to the screen center
        setLocationRelativeTo(null);
        setVisible(true);
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
}
