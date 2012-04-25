package com.github.dreamrec;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class GUIActions {
    
    private Controller controller;
	
	public static final String SAVE_ACTION = "save";
	public static final String OPEN_ACTION = "open";
	public static final String MONITORING_MODE_ACTION = "monitoring_mode";
	public static final String REVIEWING_MODE_ACTION = "reviewing_mode";
	public static final String SCROLL_CURSOR_FORWARD_ACTION = "scroll_forward";
	public static final String SCROLL_CURSOR_BACKWARD_ACTION = "scroll_backward";
	public static final String SCROLL_SLOW_GRAPH_FORWARD_ACTION = "scroll_slow_graph_forward";
	public static final String SCROLL_SLOW_GRAPH_BACKWARD_ACTION = "scroll_slow_graph_backward";
	
	private static ActionMap actionMap = new ActionMap();

    public GUIActions(Controller controller) {
        this.controller = controller;
        getActionMap().put(SAVE_ACTION, new SaveAction());
		getActionMap().put(OPEN_ACTION, new OpenAction());
		getActionMap().put(REVIEWING_MODE_ACTION, new SetReviewingModeAction());
		getActionMap().put(MONITORING_MODE_ACTION, new SetMonitoringModeAction());
		getActionMap().put(SCROLL_CURSOR_BACKWARD_ACTION, new ScrollFastGraphBackwardAction());
		getActionMap().put(SCROLL_CURSOR_FORWARD_ACTION, new ScrollFastGraphForwardAction());
		getActionMap().put(SCROLL_SLOW_GRAPH_BACKWARD_ACTION, new ScrollCursorBackwardAction());
		getActionMap().put(SCROLL_SLOW_GRAPH_FORWARD_ACTION, new ScrollCursorForwardAction());
    }

	public static ActionMap getActionMap(){
		return actionMap;
	}
	
	public static Action getActionByName(String name){
		return actionMap.get(name);
	}
    class ScrollFastGraphForwardAction extends AbstractAction {
	public void actionPerformed(ActionEvent e) {
		controller.scrollCursorForward();
	}
}

class ScrollFastGraphBackwardAction extends AbstractAction {
	public void actionPerformed(ActionEvent e) {
		controller.scrollCursorBackward();
	}
}

class ScrollCursorForwardAction extends AbstractAction {
	public void actionPerformed(ActionEvent e) {
//		controller.scrollSlowGraphForward();
	}
}

class ScrollCursorBackwardAction extends AbstractAction {
	public void actionPerformed(ActionEvent e) {
//		controller.scrollSlowGraphBackward();
	}
}

class SetReviewingModeAction extends AbstractAction {
	public void actionPerformed(ActionEvent e) {
//		controller.setState(new ControllerReviewingState());
	}
}

class SetMonitoringModeAction extends AbstractAction {
	public void actionPerformed(ActionEvent e) {
//		controller.setState(new ControllerMonitoringState());
	}
}

class SaveAction extends AbstractAction {

	public SaveAction() {
		super("Save");
	}
	public void actionPerformed(ActionEvent e) {
		controller.saveToFile();
	}
}

class OpenAction extends AbstractAction {
	public OpenAction() {
		super("Open");
	}

	public void actionPerformed(ActionEvent e) {
		controller.readFromFile();
	}
}
}


