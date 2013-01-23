package com.github.dreamrec;

import javax.swing.*;
import java.awt.*;

/**
 *
 */
public class ColoredLabel extends JLabel {

    private Dimension defaultDimension = new Dimension(10,10);

    public ColoredLabel() {
        setOpaque(true);
        setPreferredSize(defaultDimension);
    }

    public ColoredLabel(Dimension dimension) {
        setOpaque(true);
        setPreferredSize(dimension);
    }

    public void setColor(Color color) {
        setBackground(color);
    }


}
