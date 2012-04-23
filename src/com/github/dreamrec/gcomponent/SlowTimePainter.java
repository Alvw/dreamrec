package com.github.dreamrec.gcomponent;

import java.awt.*;

/**
 */
public class SlowTimePainter implements IPainter{

    private ITimePainterModel timePainterModel;

    public SlowTimePainter(ITimePainterModel timePainterModel) {
        this.timePainterModel = timePainterModel;
    }

    public void paint(Graphics2D g) {
        throw new UnsupportedOperationException("todo");
    }
}
