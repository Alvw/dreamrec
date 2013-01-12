package com.github.dreamrec;

import com.github.dreamrec.ads.*;

public class DreamRec {
    public static void main(String[] args) {
        ApplicationProperties applicationProperties = new ApplicationProperties();
        Model model = Factory.getModel(applicationProperties);
        Controller controller = new Controller(model,applicationProperties);
        MainWindow mainWindow = new MainWindow(controller, model, applicationProperties);
        controller.setMainWindow(mainWindow);

        ChannelModel channel1 = new Channel_1Model();
        ChannelModel channel2 = new Channel_2Model();

        AdsModel adsModel = new AdsModel();
        adsModel.setSps(Sps.S250);
        adsModel.setChannel_1(channel1);
        adsModel.setChannel_2(channel2);
        SettingsWindow settingsWindow = new SettingsWindow(adsModel);
    }
}
