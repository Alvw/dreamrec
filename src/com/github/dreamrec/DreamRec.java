package com.github.dreamrec;

import com.github.dreamrec.ads.*;
import com.github.dreamrec.edf.EdfWriter;

public class DreamRec {
    public static void main(String[] args) {
        ApplicationProperties applicationProperties = new ApplicationProperties();
        Model model = Factory.getModel(applicationProperties);
        AdsModel adsModel = Factory.getAdsModel(applicationProperties);
        Controller controller = new Controller(model,adsModel,Factory.getComPort(applicationProperties), applicationProperties);
        //MainWindow mainWindow = new MainWindow(controller, model, applicationProperties);
        //controller.setMainWindow(mainWindow);
        SettingsWindow settingsWindow = new SettingsWindow(controller);

    }
}
