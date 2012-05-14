package com.github.dreamrec;

import com.github.dreamrec.gcomponent.GComponentFastModel;
import com.github.dreamrec.gcomponent.GComponentModel;
import com.github.dreamrec.gcomponent.GComponentSlowModel;

public class DreamRec {
    public static void main(String[] args) {
        Model model = new Model();
        Controller controller = new Controller(model);
        MainWindow mainWindow = new MainWindow(controller,model);
        controller.setMainWindow(mainWindow);
    }
}
