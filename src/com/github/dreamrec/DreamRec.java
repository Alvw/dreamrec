package com.github.dreamrec;

public class DreamRec {
    public static void main(String[] args) {
        ApplicationProperties applicationProperties = new ApplicationProperties();
        Model model = Factory.getModel(applicationProperties);
        Controller controller = new Controller(model,applicationProperties);
        MainWindow mainWindow = new MainWindow(controller,model);
        controller.setMainWindow(mainWindow);
    }
}
