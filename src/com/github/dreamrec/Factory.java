package com.github.dreamrec;

import com.github.dreamrec.gcomponent.GComponentFastModel;
import com.github.dreamrec.gcomponent.GComponentModel;
import com.github.dreamrec.gcomponent.GComponentSlowModel;
import com.github.dreamrec.gcomponent.GComponentView;

import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 *
 */
public class Factory {

    public static GComponentView getGComponentView(Filter filter, Model model,final Controller controller){
        if(filter.divider() == Model.DIVIDER){    //todo consider refactoring if else to polymorphism.
            final GComponentModel gModel = new GComponentSlowModel(model,filter);
            final GComponentView gComponentView = new GComponentView(gModel);
            gComponentView.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent mouseEvent) {
                    int newPosition = mouseEvent.getX() - gModel.getLeftIndent();
                    controller.moveCursor(newPosition);
                }
            });
            return gComponentView;
        }else if (filter.divider() == 1){
            GComponentModel gModel = new GComponentFastModel(model,filter);
            final GComponentView gComponentView = new GComponentView(gModel);
            return gComponentView;
        } else {
            throw new UnsupportedOperationException("divider = " + filter.divider() + " .Shoud be 1 or " + Model.DIVIDER);
        }
    }

    public static GraphScrollBar getSlowGraphScrollBar(Model model, final Controller controller) {
        GraphScrollBar scrollBar = new GraphScrollBar(new ScrollBarModelAdapter(model));
        scrollBar.addScrollListener(new AdjustmentListener() {
            public void adjustmentValueChanged(AdjustmentEvent adjustmentEvent) {
                controller.scrollSlowGraph(adjustmentEvent.getValue());
            }
        });
        return scrollBar;
    }

    static class ScrollBarModelAdapter implements GraphScrollBarModel {
        private Model model;
        public ScrollBarModelAdapter(Model model) {
            this.model = model;
        }
        public int graphSize() {
            return model.getSlowDataSize();
        }
        public int graphIndex() {
            return model.getSlowGraphIndex();
        }
        public int screenSize() {
            return model.getXSize();
        }
    }
}

