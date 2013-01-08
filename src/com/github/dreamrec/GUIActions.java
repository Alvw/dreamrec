package com.github.dreamrec;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class GUIActions {

    private Controller controller;

    public static final String SAVE_ACTION = "save";
    public static final String SAVE_AS_EDF_ACTION = "save_as_edf";
    public static final String OPEN_ACTION = "open";
    public static final String SCROLL_CURSOR_FORWARD_ACTION = "scroll_forward";
    public static final String SCROLL_CURSOR_BACKWARD_ACTION = "scroll_backward";
    public static final String START_RECORDING_ACTION = "start";
    public static final String STOP_RECORDING_ACTION = "stop";

    private ActionMap actionMap = new ActionMap();

    public GUIActions(Controller controller) {
        this.controller = controller;
        getActionMap().put(SAVE_ACTION, new SaveAction());
        getActionMap().put(SAVE_AS_EDF_ACTION, new SaveAsEdfAction());
        getActionMap().put(OPEN_ACTION, new OpenAction());
        getActionMap().put(SCROLL_CURSOR_BACKWARD_ACTION, new ScrollFastGraphBackwardAction());
        getActionMap().put(SCROLL_CURSOR_FORWARD_ACTION, new ScrollFastGraphForwardAction());
        getActionMap().put(START_RECORDING_ACTION, new StartRecordingAction());
        getActionMap().put(STOP_RECORDING_ACTION, new StopRecordingAction());
    }

    public ActionMap getActionMap() {
        return actionMap;
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

    class SaveAsEdfAction extends AbstractAction {
        public SaveAsEdfAction() {
            super("SaveAsEdf");
        }

        public void actionPerformed(ActionEvent e) {
            controller.saveAsEdf();
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

    class StartRecordingAction extends AbstractAction {
        StartRecordingAction() {
            super("Start");
        }

        public void actionPerformed(ActionEvent e) {
            controller.startRecording();
        }
    }

    class StopRecordingAction extends AbstractAction {
        StopRecordingAction() {
            super("Stop");
        }

        public void actionPerformed(ActionEvent e) {
            controller.stopRecording();
        }
    }
}


