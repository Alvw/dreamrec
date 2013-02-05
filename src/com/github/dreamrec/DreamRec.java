package com.github.dreamrec;

import com.github.dreamrec.ads.*;
import com.github.dreamrec.edf.EdfWriter;

public class DreamRec {
    public static void main(String[] args) {
        ApplicationProperties applicationProperties = new ApplicationProperties();
        Controller controller = new Controller(applicationProperties);
//        MainWindow mainWindow = new MainWindow(controller, model, applicationProperties);
//        controller.setMainWindow(mainWindow);
        SettingsWindow settingsWindow = new SettingsWindow(controller);
        controller.setSettingsWindow(settingsWindow);

    }
}
