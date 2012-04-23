package com.github.dreamrec.gcomponent;

import java.awt.*;

/**
 *
 */
public class FastTimePainter implements IPainter {

    private ITimePainterModel timePainterModel;

    public FastTimePainter(ITimePainterModel timePainterModel) {
        this.timePainterModel = timePainterModel;
    }

    public void paint(Graphics2D g) {
        throw new UnsupportedOperationException("todo");
    }
}
