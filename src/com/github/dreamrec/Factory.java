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
        addGComponentListeners(gComponentView, controller);
        return gComponentView;
    }

    private static GComponentView createFastGComponent(Filter filter, Model model) {
        GComponentView gComponentView;
        GComponentModel gModel = new GComponentFastModel(model, filter);
        gComponentView = new GComponentView(gModel);
        return gComponentView;
    }

    private static GComponentView createSlowGComponent(Filter filter, Model model, final Controller controller) {
        GComponentView gComponentView;
        final GComponentSlowModel gModel = new GComponentSlowModel(model, filter);
        gComponentView = new GComponentView(gModel);
        gComponentView.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                int newPosition = mouseEvent.getX() - gModel.getLeftIndent() - gModel.getCursorWidth()/2;
                controller.moveCursor(newPosition);
            }
        });
        return gComponentView;
    }

    private static void addGComponentListeners(final GComponentView gComponentView, final Controller controller) {
        gComponentView.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent componentEvent) {
                GComponentModel gModel = gComponentView.getComponentModel();
                gModel.setYSize(componentEvent.getComponent().getHeight() - gModel.getTopIndent() - gModel.getBottomIndent());
                controller.changeXSize(componentEvent.getComponent().getWidth() - gModel.getLeftIndent() - gModel.getRightIndent());
            }
        });
        gComponentView.addMouseWheelListener(new MouseWheelListener() {
            public void mouseWheelMoved(MouseWheelEvent e) {
                int rotation = e.getWheelRotation();
                GComponentModel gModel = gComponentView.getComponentModel();
                gModel.setYZoom(gModel.getYZoom() * (1+rotation/10.0));
                gComponentView.repaint();
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
    
    public static Model getModel(ApplicationProperties applicationProperties){
        Model model = new Model();
        model.setXSize(applicationProperties.getXSize());
        return model;
    }

    public static IDataProvider getDataProvider(Provider provider,ApplicationProperties applicationProperties) throws ApplicationException {
        /*if(provider == Provider.DEBUG){
            return new DebugDataProvider();
        } else if(provider == Provider.EEG){
            return new EEGDataProvider(applicationProperties.getChanel(), applicationProperties.getIncomingDataFrequency());
        } else {
            throw new ApplicationException("Wrong data provider name");
        }*/
        return new Ads1292DataProvider(applicationProperties);
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

