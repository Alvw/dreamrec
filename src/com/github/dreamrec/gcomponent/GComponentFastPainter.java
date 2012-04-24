package com.github.dreamrec.gcomponent;

/**
 *
 */
public class GComponentFastPainter extends GComponentPainter {

    public GComponentFastPainter() {
        cursorPainter = new EmptyPainter();
        timePainter = new FastTimePainter();
    }
}
