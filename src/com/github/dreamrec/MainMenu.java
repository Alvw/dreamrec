package com.github.dreamrec;

import javax.swing.*;

public class MainMenu extends JMenuBar {

    private ActionMap actionMap;
    private  ApplicationProperties applicationProperties;

	public MainMenu(ActionMap actionMap, ApplicationProperties applicationProperties) {
        this.actionMap = actionMap;
        this.applicationProperties = applicationProperties;
		createMainMenu();
	}

	private void createMainMenu() {
		JMenu fileMenu = new JMenu("File");
		add(fileMenu);
		fileMenu.add(actionMap.get(GUIActions.OPEN_ACTION));
		fileMenu.add(actionMap.get(GUIActions.SAVE_ACTION));
		fileMenu.add(actionMap.get(GUIActions.SAVE_AS_EDF_ACTION));

		JMenu recordMenu = new JMenu("Record");
		add(recordMenu);
        recordMenu.add(actionMap.get(GUIActions.START_RECORDING_ACTION));
        recordMenu.add(actionMap.get(GUIActions.STOP_RECORDING_ACTION));


        JMenu optionsMenu = new JMenu("Options");
        JMenu providerMenu = new JMenu("Data Provider");
        optionsMenu.add(providerMenu);
        add(optionsMenu);

        ButtonGroup itemGroup = new ButtonGroup();
        for (Provider provider : Provider.values()) {
            Action action = actionMap.get(provider.name());
            JRadioButtonMenuItem providerItem = new JRadioButtonMenuItem(action);
            if(provider == applicationProperties.getDataProvider()){
                providerItem.setSelected(true);
            }
            itemGroup.add(providerItem);
            providerItem.setText(provider.getLabel());
            providerMenu.add(providerItem);
        }
	}
}
