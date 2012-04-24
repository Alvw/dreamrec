package com.github.dreamrec;

import com.github.dreamrec.gcomponent.GComponentModel;
import com.github.dreamrec.gcomponent.GComponentView;

import javax.swing.*;
import java.awt.*;

/**
 *
 */
public class MainWindow extends JFrame {


    public MainWindow(final GComponentModel... gComponentModels) {

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI(gComponentModels);
            }
        });
    }

    private void createAndShowGUI(GComponentModel[] gComponentModels) {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        JPanel mainPanel = new JPanel(new GridLayout(0, 1)) ;
        for (GComponentModel gComponentModel : gComponentModels) {
            mainPanel.add(new GComponentView(gComponentModel));
        }
        add(mainPanel);
        pack();
        // place the window to the screen center
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void showMessage(String s) {
        JOptionPane.showMessageDialog(null, s);
    }
}
