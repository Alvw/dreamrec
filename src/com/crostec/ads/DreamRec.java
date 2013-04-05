package com.crostec.ads;

import com.crostec.ads.gui.SettingsWindow;

public class DreamRec {
    public static void main(String[] args) {
         ApplicationProperties applicationProperties = new ApplicationProperties();
        Controller controller = new Controller(applicationProperties);
        SettingsWindow settingsWindow = new SettingsWindow(controller);
        controller.setSettingsWindow(settingsWindow);

    }
}
