package com.github.dreamrec.gcomponent;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: galafit
 * Date: 23/04/12
 * Time: 16:14
 * To change this template use File | Settings | File Templates.
 */
public class SlowTimePainter {
    private ITimePainterModel timePainterModel;

    public SlowTimePainter(ITimePainterModel timePainterModel) {
        this.timePainterModel = timePainterModel;
    }

    public void paint(Graphics2D g) {
        throw new UnsupportedOperationException("todo");
    }
}
