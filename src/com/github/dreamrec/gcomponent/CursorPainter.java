package com.github.dreamrec.gcomponent;

import java.awt.*;

/**
 *
 */
public class CursorPainter implements IPainter<ICursorPainterModel> {
    public void paint(Graphics2D g, ICursorPainterModel paintModel) {
        int x = paintModel.getCursorPosition();
        int width = paintModel.getCursorWidth();
        int height = paintModel.getYSize();
        g.drawRect(x,0,width,height);
    }
}
