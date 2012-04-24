package com.github.dreamrec.gcomponent;

/**
 *
 */
public class GComponentSlowPainter extends GComponentPainter {

    public GComponentSlowPainter() {
        super();
        cursorPainter = new CursorPainter();
        timePainter = new SlowTimePainter();
    }
}
