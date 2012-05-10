package com.github.dreamrec;

import com.github.dreamrec.gcomponent.GComponentModel;
import com.github.dreamrec.gcomponent.GComponentSlowModel;
import com.github.dreamrec.gcomponent.GComponentView;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import static com.github.dreamrec.GUIActions.*;

/**
 *
 */
public class MainWindow extends JFrame {

    private JPanel mainPanel;
    private JPanel scrollPanel;
    private Model model;
    private Controller controller;
    private java.util.List<GComponentView>  slowGComponentPanels = new ArrayList<GComponentView>();
    private SlowGraphScrollBar slowGraphScrollBar;

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
            GComponentView gComponentPanel = new GComponentView(gComponentModel);
            mainPanel.add(gComponentPanel);
            if(model == null){
                model = gComponentModel.getModel();
            }
            if(gComponentModel instanceof GComponentSlowModel){
               slowGComponentPanels.add(gComponentPanel);
            }
        }
        add(mainPanel, BorderLayout.NORTH);
        slowGraphScrollBar = new SlowGraphScrollBar(model);
        slowGraphScrollBar.setVisible(false);
        add(slowGraphScrollBar, BorderLayout.CENTER);
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
        slowGraphScrollBar.addScrollListener(new AdjustmentListener() {
            public void adjustmentValueChanged(AdjustmentEvent adjustmentEvent) {
                controller.scrollSlowGraph(adjustmentEvent.getValue());
            }
        });
        setActionMap(new GUIActions(controller).getActionMap());

        for (final GComponentView slowGComponentPanel : slowGComponentPanels) {
            slowGComponentPanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent mouseEvent) {
                    int newPosition = mouseEvent.getX() - slowGComponentPanel.getComponentModel().getLeftIndent();
                    controller.moveCursor(newPosition);
                }
            });
        }
    }

    @Override
    public void repaint() {
        super.repaint();
        if(model.getSlowDataSize() > model.getXSize()){
            if(!slowGraphScrollBar.isVisible()){
                slowGraphScrollBar.setVisible(true);
                pack();
            }
            slowGraphScrollBar.updateModel();
        }
    }
}
