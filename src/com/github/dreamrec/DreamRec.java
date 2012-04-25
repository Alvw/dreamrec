package com.github.dreamrec;

import com.github.dreamrec.gcomponent.GComponentFastModel;
import com.github.dreamrec.gcomponent.GComponentModel;
import com.github.dreamrec.gcomponent.GComponentSlowModel;

public class DreamRec {
    public static void main(String[] args) {

        Model model = new Model();

        IListView<Integer>  slowDreamView = new AveragingListView(new FistDerivativeAbsView(model.getEyeDataList()), Model.DIVIDER);
        GComponentModel slowDreamGModel = new GComponentSlowModel(model,slowDreamView);

        ListView<Integer> eyeDataList = model.getEyeDataList();
        GComponentModel eyeDataGModel = new GComponentFastModel(model,eyeDataList);

        IListView<Integer> fastDerivativeView = new FistDerivativeAbsView(model.getEyeDataList());
        GComponentModel fastDerivativeViewGModel = new GComponentFastModel(model,fastDerivativeView);

        eyeDataGModel.centreX();
        MainWindow mainWindow = new MainWindow(fastDerivativeViewGModel,eyeDataGModel,slowDreamGModel);
        IDataProvider dataProvider = new DebugDataProvider();

        Controller controller = new Controller(model,mainWindow,dataProvider);
        controller.startRecording();

        mainWindow.setActionMap(new GUIActions(controller).getActionMap());
    }
}
