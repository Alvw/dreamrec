package com.github.dreamrec;

import com.github.dreamrec.ads.*;
import com.github.dreamrec.comport.ComPort;
import com.github.dreamrec.gcomponent.GComponentFastModel;
import com.github.dreamrec.gcomponent.GComponentModel;
import com.github.dreamrec.gcomponent.GComponentSlowModel;
import com.github.dreamrec.gcomponent.GComponentView;

import java.awt.event.*;

/**
 *
 */
public class Factory {

    public static GComponentView getGComponentView(Model model, final Controller controller, Filter... filter) {
        GComponentView gComponentView;
        if (filter[0].divider() == Model.DIVIDER) {
            gComponentView = createSlowGComponent(model, controller, filter);
        } else if (filter[0].divider() == 1) {
            gComponentView = createFastGComponent(model, filter);
        } else {
            throw new UnsupportedOperationException("divider = " + filter[0].divider() + " .Shoud be 1 or " + Model.DIVIDER);
        }
        addGComponentListeners(gComponentView, controller);
        return gComponentView;
    }

    private static GComponentView createFastGComponent(Model model, Filter... filter) {
        GComponentView gComponentView;
        GComponentModel gModel = new GComponentFastModel(model, filter);
        gComponentView = new GComponentView(gModel);
        return gComponentView;
    }

    private static GComponentView createSlowGComponent(Model model, final Controller controller,Filter... filter) {
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

   /* public static IDataProvider getDataProvider(ApplicationProperties applicationProperties) throws ApplicationException {
        return new Ads1292DataProvider(applicationProperties);
    }*/

    public static ComPort getComPort(ApplicationProperties appProperties){
        return new ComPort();
    }
    

    
    public static AdsModel getAdsModel(ApplicationProperties applicationProperties){
        // array_size = Number of Channels
        int[] rldSenseEnabledBits = {0x03, 0x0C};
        int[] loffSenseEnabledBits = {0x03, 0x0C};

        AdsModel adsModel = new AdsModel();

        for (int chNum = 0; chNum < 2; chNum++) {
            AdsChannelModel adsChannelModel = new AdsChannelModel();
            adsChannelModel.setDivider(applicationProperties.getChannelDivider(chNum));
            adsChannelModel.setHiPassBufferSize(applicationProperties.getChannelHiPassBufferSize(chNum));
            adsChannelModel.setName(applicationProperties.getChannelName(chNum));
            adsChannelModel.setGain(applicationProperties.getChannelGain(chNum));
            adsChannelModel.setCommutatorState(applicationProperties.getChannelCommutatorState(chNum));
            adsChannelModel.setLoffEnable(applicationProperties.isChannelLoffEnable(chNum));
            adsChannelModel.setRldSenseEnabled(applicationProperties.isChannelRldSenseEnable(chNum));
            adsChannelModel.setRldSenseEnabledBits(rldSenseEnabledBits[chNum]);
            adsChannelModel.setLoffSenseEnabledBits(loffSenseEnabledBits[chNum]);
            
            adsModel.addAdsChannel(adsChannelModel);
        }

        for (int chNum = 0; chNum < 3; chNum++) {
            ChannelModel accelerometerChannelModel = new AdsChannelModel();
            accelerometerChannelModel.setDivider(applicationProperties.getAccelerometerDivider());
            accelerometerChannelModel.setHiPassBufferSize(applicationProperties.getAccelerometerHiPassBufferSize());
            accelerometerChannelModel.setName(applicationProperties.getAccelerometerName(chNum));

            adsModel.addAccelerometerChannel(accelerometerChannelModel);
        }

        adsModel.setSps(applicationProperties.getSps());
        adsModel.setAccelerometerEnabled(applicationProperties.isAccelerometerEnable());
        return adsModel;
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

