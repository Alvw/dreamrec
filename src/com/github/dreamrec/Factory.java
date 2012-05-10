package com.github.dreamrec;

import com.github.dreamrec.gcomponent.GComponentFastModel;
import com.github.dreamrec.gcomponent.GComponentModel;
import com.github.dreamrec.gcomponent.GComponentSlowModel;
import com.github.dreamrec.gcomponent.GComponentView;

import java.awt.event.*;

/**
 *
 */
public class Factory {

    public static GComponentView getGComponentView(Filter filter, Model model, final Controller controller) {
        GComponentView gComponentView;
        if (filter.divider() == Model.DIVIDER) {
            gComponentView = createSlowGComponent(filter, model, controller);
        } else if (filter.divider() == 1) {
            gComponentView = createFastGComponent(filter, model);
        } else {
            throw new UnsupportedOperationException("divider = " + filter.divider() + " .Shoud be 1 or " + Model.DIVIDER);
        }
        addGComponentListeners(gComponentView, model);
        return gComponentView;
    }

    private static GComponentView createFastGComponent(Filter filter, Model model) {
        GComponentView gComponentView;GComponentModel gModel = new GComponentFastModel(model, filter);
        gComponentView = new GComponentView(gModel);
        return gComponentView;
    }

    private static GComponentView createSlowGComponent(Filter filter, Model model, final Controller controller) {
        GComponentView gComponentView;
        final GComponentModel gModel = new GComponentSlowModel(model, filter);
        gComponentView = new GComponentView(gModel);
        gComponentView.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                int newPosition = mouseEvent.getX() - gModel.getLeftIndent();
                controller.moveCursor(newPosition);
            }
        });
        return gComponentView;
    }

    private static void addGComponentListeners(final GComponentView gComponentView, final Model model) {
        gComponentView.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent componentEvent) {
                GComponentModel gModel = gComponentView.getComponentModel();
                gModel.setYSize(componentEvent.getComponent().getHeight() - gModel.getTopIndent() - gModel.getBottomIndent());
                model.setXSize(componentEvent.getComponent().getWidth() - gModel.getLeftIndent() - gModel.getRightIndent());
            }
        });
        gComponentView.addMouseWheelListener(new MouseWheelListener() {
            public void mouseWheelMoved(MouseWheelEvent e) {
                int rotation = e.getWheelRotation();
                GComponentModel gModel = gComponentView.getComponentModel();
                gModel.setYZoom(gModel.getYZoom() * (1+rotation/10.0));
            }
        });

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

