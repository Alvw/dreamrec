package com.github.dreamrec;

import javax.swing.*;

/**
 *
 */
public class ScrollBarPanel extends JScrollBar{

    Model model;

    public ScrollBarPanel(Model model) {
        super(JScrollBar.HORIZONTAL);
        this.model = model;
    }

    public void updateModel(){
        if(model.getSlowDataSize()<model.getXSize()){
            return;
        }
        BoundedRangeModel boundedRangeModel = getModel();
        boundedRangeModel.setMaximum(model.getSlowDataSize());
        boundedRangeModel.setExtent(model.getXSize());
        boundedRangeModel.setValue(model.getSlowGraphIndex());
    }
}
