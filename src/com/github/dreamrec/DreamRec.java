package com.github.dreamrec;

import com.github.dreamrec.gcomponent.GComponentFastModel;
import com.github.dreamrec.gcomponent.GComponentModel;
import com.github.dreamrec.gcomponent.GComponentSlowModel;

public class DreamRec {
    public static void main(String[] args) {
        Model model = new Model();
        IDataProvider dataProvider = new DebugDataProvider();
        Controller controller = new Controller(model,dataProvider);
        MainWindow mainWindow = new MainWindow(controller,model);
        controller.setMainWindow(mainWindow);
        controller.startRecording();
    }
}
