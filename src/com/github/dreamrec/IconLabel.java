package com.github.dreamrec;

import javax.swing.*;
import java.awt.*;

/**
 *
 */
public class IconLabel extends JLabel {

    private Color  backgroundColor;
    private Dimension defaultDimension = new Dimension(10,10);

    public IconLabel() {
        setPreferredSize(defaultDimension);
        setOpaque(true);
        backgroundColor = getBackground();
    }

    public IconLabel(Dimension dimension) {
        this();
        setPreferredSize(dimension);
    }

    public IconLabel(Icon icon) {
        this();
        setIcon(icon);

    }

    public void setIcon(Icon icon) {
        if(icon != null){
            setPreferredSize(new Dimension(icon.getIconWidth(), icon.getIconHeight()));
        }
        super.setIcon(icon);
    }

    public void setColor(Color color) {
        setBackground(color);
        setIcon(null);
    }

    public void setBackgroundColor(){
        setColor(backgroundColor);
    }
}
