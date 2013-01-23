package com.github.dreamrec;

import javax.swing.*;
import java.awt.*;

/**
 *
 */
public class ColoredLabel extends JLabel {

    private Dimension defaultDimension = new Dimension(10,10);
    private Color  defaultColor = Color.GRAY;

    public ColoredLabel() {
        setOpaque(true);
        setPreferredSize(defaultDimension);
        setBackground(defaultColor);
    }

    public ColoredLabel(Dimension dimension) {
        this();
        setPreferredSize(dimension);
    }

    public void setColor(Color color) {
        setBackground(color);
    }


}
