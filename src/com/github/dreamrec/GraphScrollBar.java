package com.github.dreamrec;

import javax.swing.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class GraphScrollBar extends JScrollBar implements AdjustmentListener, ModelUpdateListener {

    private GraphScrollBarModel model;
    private boolean notifyListeners;
    private List<AdjustmentListener> listenerList = new ArrayList<AdjustmentListener>();

    public GraphScrollBar(GraphScrollBarModel model_) {
        super(JScrollBar.HORIZONTAL);
        model = model_;
        addAdjustmentListener(this);
       model.addModelUpdateListener(this);
    }

    public void modelUpdated() {
        BoundedRangeModel boundedRangeModel = getModel();
        notifyListeners = false;
        if (model.graphSize() < model.screenSize()) {
            boundedRangeModel.setRangeProperties(0, model.screenSize(), 0, model.graphSize(), false);
        }else{
            boundedRangeModel.setRangeProperties(model.graphIndex(), model.screenSize(), 0, model.graphSize(), false);
        }
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
