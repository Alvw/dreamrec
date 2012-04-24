package com.github.dreamrec;

import com.github.dreamrec.gcomponent.GComponentFastModel;
import com.github.dreamrec.gcomponent.GComponentModel;

public class DreamRec {
    public static void main(String[] args) {
        Model model = new Model();
//       IListView<Integer>  slowDreamView = new AveragingListView(new FistDerivativeAbsView(model.getEyeDataList()), 120);
        ListView<Integer> eyeDataList = model.getEyeDataList();
        GComponentModel eyeDataGModel = new GComponentFastModel(model,eyeDataList);
        eyeDataGModel.centreX();
        MainWindow mainWindow = new MainWindow(eyeDataGModel);
        IDataProvider dataProvider = new DebugDataProvider();
        Controller controller = new Controller(model,mainWindow,dataProvider);
        controller.startRecording();
    }
}
