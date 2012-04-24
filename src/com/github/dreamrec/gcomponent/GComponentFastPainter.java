package com.github.dreamrec.gcomponent;

/**
 *
 */
public class GComponentFastPainter extends GComponentPainter {

    public GComponentFastPainter() {
        super();
        cursorPainter = new EmptyPainter();
        timePainter = new FastTimePainter();
    }
}
