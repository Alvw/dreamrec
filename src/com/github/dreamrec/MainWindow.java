package com.github.dreamrec;

import com.github.dreamrec.gcomponent.GComponentModel;
import com.github.dreamrec.gcomponent.GComponentView;


import javax.swing.*;
import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import static com.github.dreamrec.GUIActions.*;

/**
 *
 */
public class MainWindow extends JFrame {

    private JPanel mainPanel;
    private JPanel scrollPanel;
    private Model model;
    private JScrollBar scrollBar;
    private Controller controller;
    private java.util.List<JPanel>  SlowGComponentPanels;

    public MainWindow(final GComponentModel... gComponentModels) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI(gComponentModels);
            }
        });
    }

    private void createAndShowGUI(GComponentModel[] gComponentModels) {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        mainPanel = new JPanel(new GridLayout(0, 1)) ;
        for (GComponentModel gComponentModel : gComponentModels) {
            mainPanel.add(new GComponentView(gComponentModel));
            if(model == null){
                model = gComponentModel.getModel();
            }
        }
        add(mainPanel, BorderLayout.NORTH);
        
        int scrollExtent = 10; //model.getXSize()*model.getXSize()/model.getDataSize();
        scrollBar = new JScrollBar(Adjustable.HORIZONTAL,0,scrollExtent,0,model.getXSize());
        JPanel scrollBarPanel = new JPanel();
        scrollBarPanel.add(scrollBar);
        add(scrollBarPanel, BorderLayout.CENTER);
        registerKeyActions();
        pack();
        // place the window to the screen center
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void setActionMap(ActionMap actionMap){
        mainPanel.setActionMap(actionMap);
    }

    public void showMessage(String s) {
        JOptionPane.showMessageDialog(null, s);
    }

    private void registerKeyActions() {
	    mainPanel.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK), SAVE_ACTION);
	    mainPanel.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK), OPEN_ACTION);
	    mainPanel.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), SCROLL_CURSOR_BACKWARD_ACTION);
	    mainPanel.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), SCROLL_CURSOR_FORWARD_ACTION);
	}
    
    public void setController(Controller _controller){
        controller = _controller;
        scrollBar.addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent adjustmentEvent) {
                   controller.scrollSlowGraph(10);
            }
        });
        setActionMap(new GUIActions(controller).getActionMap());
    }
}
