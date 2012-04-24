package com.github.dreamrec.gcomponent;

import java.awt.*;

/**
 *
 */
public class YAxisPainter implements IPainter<IYAxisPainterModel> {

    private long minValueStep = 10;  //min value between two labels
    private int minPointStep = 20; //min distance between two labels in pixels
    private int minValue = 0;



    protected long getValueStep(double multiplier) {
        return (long)(minPointStep/(multiplier*minValueStep)+1)*minValueStep;
    }

    protected String getLabelText(long value){
        return value+"";
    }

    public void paint(Graphics2D g, IYAxisPainterModel paintModel) {
        long valueStep = getValueStep(paintModel.getYZoom());
        int numberOfColumns = (int)(paintModel.getYSize()/(paintModel.getYZoom()*valueStep));
        for (int i = 1; i < numberOfColumns+1; i++) {
            long gridValue = (minValue/valueStep)*valueStep + i*valueStep;
            int position = (int)Math.round(paintModel.getYZoom()*(gridValue - minValue));
            g.drawLine(-3, -position, +3, -position);
            g.drawString(getLabelText(gridValue), -25, -position+5);
        }
    }
}
