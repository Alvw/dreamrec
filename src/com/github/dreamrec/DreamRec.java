package com.github.dreamrec;

import com.github.dreamrec.gcomponent.GComponentFastModel;
import com.github.dreamrec.gcomponent.GComponentModel;
import com.github.dreamrec.gcomponent.GComponentSlowModel;

public class DreamRec {
    public static void main(String[] args) {



        IListView<Integer>  slowDreamView = new ChainListView(new FistDerivativeAbsView(),new AveragingListView(Model.DIVIDER));
        IListView<Integer> fastDreamView = new ChainListView(new FistDerivativeAbsView());

        Model model = new Model(fastDreamView, new DoNothingListView(),slowDreamView) ;



        IDataProvider dataProvider = new DebugDataProvider();
        Controller controller = new Controller(model,dataProvider);
        MainWindow mainWindow = new MainWindow(model,controller);
        controller.setMainWindow(mainWindow);
        controller.startRecording();
    }
}
