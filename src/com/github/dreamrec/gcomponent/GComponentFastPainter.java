package com.github.dreamrec.gcomponent;

/**
 *
 */
public class GComponentFastPainter extends GComponentPainter{
    public GComponentFastPainter(GComponentModel gModel) {
        super(gModel);
        timePainter = new FastTimePainter(gModel);
    }
}
