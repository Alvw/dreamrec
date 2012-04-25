package com.github.dreamrec;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class GUIActions {

    private Controller controller;

    public static final String SAVE_ACTION = "save";
    public static final String OPEN_ACTION = "open";
    public static final String SCROLL_CURSOR_FORWARD_ACTION = "scroll_forward";
    public static final String SCROLL_CURSOR_BACKWARD_ACTION = "scroll_backward";

    private ActionMap actionMap = new ActionMap();

    public GUIActions(Controller controller) {
        this.controller = controller;
        getActionMap().put(SAVE_ACTION, new SaveAction());
        getActionMap().put(OPEN_ACTION, new OpenAction());
        getActionMap().put(SCROLL_CURSOR_BACKWARD_ACTION, new ScrollFastGraphBackwardAction());
        getActionMap().put(SCROLL_CURSOR_FORWARD_ACTION, new ScrollFastGraphForwardAction());
    }

    public ActionMap getActionMap() {
        return actionMap;
    }

    public Action getActionByName(String name) {
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


