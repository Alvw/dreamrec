package com.github.dreamrec;

import javax.swing.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class GraphScrollBar extends JScrollBar implements AdjustmentListener {

    private GraphScrollBarModel model;
    private boolean notifyListeners;
    private List<AdjustmentListener> listenerList = new ArrayList<AdjustmentListener>();

    public GraphScrollBar(GraphScrollBarModel model) {
        super(JScrollBar.HORIZONTAL);
        this.model = model;
        addAdjustmentListener(this);
    }

    public void updateModel() {
        notifyListeners = false;
        if (model.graphSize() < model.screenSize()) {
            return;
        }
        BoundedRangeModel boundedRangeModel = getModel();
        boundedRangeModel.setRangeProperties(model.graphIndex(), model.screenSize(), 0, model.graphSize(), false);
    }

    /**
     * Add listeners that do not respond to updateModel() method invocation;
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
