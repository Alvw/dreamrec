package com.github.dreamrec.gcomponent;

/**
 *
 */
public class GComponentSlowPainter extends GComponentPainter{
    public GComponentSlowPainter(GComponentModel gModel) {
        super(gModel);
        timePainter = new FastTimePainter(gModel);
    }
}
