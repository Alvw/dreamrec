package com.github.dreamrec.gcomponent;

import java.awt.*;

/**
 *
 */
public class CursorPainter implements Painter {

    private CursorPainterModel cursorPainterModel;

    public CursorPainter(CursorPainterModel cursorPainterModel) {
        this.cursorPainterModel = cursorPainterModel;
    }

    public void paint(Graphics2D g) {
        throw new UnsupportedOperationException("todo");
    }
}
