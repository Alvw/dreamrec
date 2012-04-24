package com.github.dreamrec;

import com.github.dreamrec.gcomponent.GComponentModel;
import com.github.dreamrec.gcomponent.GComponentView;

import javax.swing.*;
import java.awt.*;

/**
 *
 */
public class MainWindow extends JFrame {


    public MainWindow(GComponentModel... gComponentModels) {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(0, 1));
        for (GComponentModel gComponentModel : gComponentModels) {
            add(new GComponentView(gComponentModel));
        }
        pack();
        // place the window to the screen center
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void showMessage(String s) {
        JOptionPane.showMessageDialog(null, s);
    }
}
