package com.github.dreamrec;

import com.github.dreamrec.gcomponent.GComponentFastModel;
import com.github.dreamrec.gcomponent.GComponentModel;
import com.github.dreamrec.gcomponent.GComponentSlowModel;

public class DreamRec {
    public static void main(String[] args) {
        Model model = new Model();
//        IDataProvider dataProvider = new DebugDataProvider();
        IDataProvider dataProvider = null;
        try {
            dataProvider = new EEGDataProvider();
        } catch (ApplicationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        Controller controller = new Controller(model,dataProvider);
        MainWindow mainWindow = new MainWindow(controller,model);
        controller.setMainWindow(mainWindow);
    }
}
