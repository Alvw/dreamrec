package com.github.dreamrec.gcomponent;

import com.github.dreamrec.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 *
 */
public class GComponentView extends JPanel  {

    private GComponentPainter componentPainter;
    private GComponentModel gModel;

    public GComponentView(GComponentModel gModel_, final Controller controller) {
        this.gModel = gModel_;
        if (gModel instanceof GComponentFastModel) {
            componentPainter = new GComponentFastPainter();
        } else if (gModel instanceof GComponentSlowModel) {
            componentPainter = new GComponentSlowPainter();
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent mouseEvent) {
                    int newPosition = mouseEvent.getX() - gModel.getLeftIndent();
                    controller.moveCursor(newPosition);
                }
            });
        }
        setPreferredSize(new Dimension(gModel.getXSize() + gModel.getLeftIndent() + gModel.getRightIndent(),
                gModel.getYSize() + gModel.getTopIndent() + gModel.getBottomIndent()));
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }
    
    public GComponentModel getComponentModel(){
        return gModel;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        componentPainter.paint(g2d, gModel);
    }

}
