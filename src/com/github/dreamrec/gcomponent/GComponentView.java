package com.github.dreamrec.gcomponent;

import javax.swing.*;
import java.awt.*;

/**
 *
 */
public class GComponentView extends JPanel {

    private GComponentPainter componentPainter;
    private GComponentModel gModel;

    public GComponentView(GComponentModel gModel) {
        this.gModel = gModel;
        if (gModel instanceof GComponentFastModel) {
            componentPainter = new GComponentFastPainter();
        } else if (gModel instanceof GComponentSlowModel) {
            componentPainter = new GComponentSlowPainter();
        }
        setPreferredSize(new Dimension(gModel.getXSize() + gModel.YAxisPosition, gModel.getYSize() + gModel.XAxisPosition));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        componentPainter.paint(g2d, gModel);
    }

}
