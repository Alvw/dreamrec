package com.github.dreamrec.gcomponent;

import java.awt.*;

/**
 *
 */
public class CursorPainter implements IPainter {

    private ICursorPainterModel cursorPainterModel;

    public CursorPainter(ICursorPainterModel cursorPainterModel) {
        this.cursorPainterModel = cursorPainterModel;
    }

    public void paint(Graphics2D g) {
        throw new UnsupportedOperationException("todo");
    }
}
