package com.github.dreamrec.gcomponent;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 *
 */
public class GComponentView implements ModelChangeListener {

    JPanel panel;
    List<Painter> painters;

    public void modelChanged(GComponentModel model) {
        panel.repaint();
    }

    private class GPanel extends JPanel{
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // итерироваться по пэйнтерам и вызывать метод paint(Graphics g);
        }
    }

}
