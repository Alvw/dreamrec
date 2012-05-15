package com.github.dreamrec;

import javax.swing.*;

public class MainMenu extends JMenuBar {

    private ActionMap actionMap;

	public MainMenu(ActionMap actionMap) {
        this.actionMap = actionMap;
		createMainMenu();
	}

	private void createMainMenu() {
		JMenu fileMenu = new JMenu("File");
		add(fileMenu);
		fileMenu.add(actionMap.get(GUIActions.OPEN_ACTION));
		fileMenu.add(actionMap.get(GUIActions.SAVE_ACTION));
		
		JMenu recordMenu = new JMenu("Record");
		add(recordMenu);
        recordMenu.add(actionMap.get(GUIActions.START_RECORDING_ACTION));
        recordMenu.add(actionMap.get(GUIActions.STOP_RECORDING_ACTION));


        JMenu optionsMenu = new JMenu("Options");
        JMenu providerMenu = new JMenu("Data Provider");
        optionsMenu.add(providerMenu);
        add(optionsMenu);

        Action action = actionMap.get(GUIActions.SELECT_DATA_PROVIDER_ACTION);
        for (Provider provider : Provider.values()) {
            JMenuItem providerItem = new JMenuItem(action);
            providerItem.setText(provider.getName());
            providerMenu.add(providerItem);
        }
	}
}
