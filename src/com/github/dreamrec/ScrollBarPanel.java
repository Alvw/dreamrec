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
        boundedRangeModel.setRangeProperties(model.getSlowGraphIndex(),model.getXSize(),0,model.getSlowDataSize(),false);
    }
}
