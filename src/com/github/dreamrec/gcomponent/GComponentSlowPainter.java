package com.github.dreamrec.gcomponent;

/**
 *
 */
public class GComponentSlowPainter extends GComponentPainter {

    public GComponentSlowPainter() {
        cursorPainter = new CursorPainter();
        timePainter = new SlowTimePainter();
    }
}
