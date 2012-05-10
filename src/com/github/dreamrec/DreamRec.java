package com.github.dreamrec;

import com.github.dreamrec.gcomponent.GComponentFastModel;
import com.github.dreamrec.gcomponent.GComponentModel;
import com.github.dreamrec.gcomponent.GComponentSlowModel;

public class DreamRec {
    public static void main(String[] args) {

        Model model = new Model();

        Filter<Integer> slowDreamView = new AveragingFilter(new FirstDerivativeAbsFilter(model.getEyeDataList()), Model.DIVIDER);
        GComponentModel slowDreamGModel = new GComponentSlowModel(model,slowDreamView);

        DataList<Integer> eyeDataList = model.getEyeDataList();
        GComponentModel eyeDataGModel = new GComponentFastModel(model,eyeDataList);

        Filter<Integer> fastDerivativeView = new FirstDerivativeAbsFilter(model.getEyeDataList());
        GComponentModel fastDerivativeViewGModel = new GComponentFastModel(model,fastDerivativeView);

        eyeDataGModel.centreX();
        MainWindow mainWindow = new MainWindow(fastDerivativeViewGModel,eyeDataGModel,slowDreamGModel);
        IDataProvider dataProvider = new DebugDataProvider();

        Controller controller = new Controller(model,mainWindow,dataProvider);
        controller.startRecording();

        mainWindow.setController(controller);
    }
}
