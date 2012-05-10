package com.github.dreamrec;

import javax.swing.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class SlowGraphScrollBar extends JScrollBar implements AdjustmentListener {

    private Model model;
    private boolean notifyListeners;
    private List<AdjustmentListener> listenerList = new ArrayList<AdjustmentListener>();

    public SlowGraphScrollBar(Model model) {
        super(JScrollBar.HORIZONTAL);
        this.model = model;
        addAdjustmentListener(this);
    }

    public void updateModel() {
        notifyListeners = false;
        if (model.getSlowDataSize() < model.getXSize()) {
            return;
        }
        BoundedRangeModel boundedRangeModel = getModel();
        boundedRangeModel.setRangeProperties(model.getSlowGraphIndex(), model.getXSize(), 0, model.getSlowDataSize(), false);
    }

    /**
     * Add listeners than do not respond to updateModel() method invocation;
     */
    public void addScrollListener(AdjustmentListener adjustmentListener) {
        listenerList.add(adjustmentListener);
    }

    public void adjustmentValueChanged(AdjustmentEvent e) {
        if (notifyListeners) {
            for (AdjustmentListener adjustmentListener : listenerList) {
                adjustmentListener.adjustmentValueChanged(e);
            }
        } else {
            notifyListeners = true;
        }

    }
}
