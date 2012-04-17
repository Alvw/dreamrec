package com.github.dreamrec.gcomponent;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 *
 */
public class GComponentView implements IModelChangeListener {

    JPanel panel;
    List<IPainter> painters;

    public void modelChanged(GComponentModel model) {
        panel.repaint();
    }

    private class GPanel extends JPanel{
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // ������������� �� ��������� � �������� ����� paint(Graphics g);
        }
    }

}
