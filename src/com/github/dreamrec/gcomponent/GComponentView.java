package com.github.dreamrec.gcomponent;

import javax.swing.*;
import java.awt.*;

/**
 *
 */
public class GComponentView extends JPanel {

    private GComponentPainter componentPainter = new GComponentPainter();
    private GComponentModel gModel;

    public GComponentView(GComponentModel gModel) {
        this.gModel = gModel;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        componentPainter.paint(g2d, gModel);
    }

}
