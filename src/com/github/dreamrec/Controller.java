package com.github.dreamrec;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

/**
 *
 */
public class Controller {

    public static final int REPAINT_DELAY = 1000;//milliseconds
    private Timer repaintTimer;
    private Model model;
    private IDataProvider dataProvider;
    private MainWindow mainWindow;
    private static final Log log = LogFactory.getLog(Controller.class);

    public Controller(Model _model) {
        this.model = _model;
        repaintTimer = new Timer(REPAINT_DELAY,new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                updateModel();
                mainWindow.repaint();
            }
        });
    }

    private void updateModel() {
        while (dataProvider.available()>0) {
              model.addEyeData(dataProvider.poll());
        }
    }

    public void saveToFile(File file) {
        DataOutputStream outStream = null;
		try {
			outStream = new DataOutputStream(new FileOutputStream(file));
			saveStateToStream(outStream);
		} catch (Exception e) {
			log.error(e);
            mainWindow.showMessage("Error while saving file " + file.getName());   // TODO create message bundle;
		} finally {
			try {
				outStream.close();
			} catch (IOException e) {
				log.error(e);
			}
		}
    }

    private void saveStateToStream(DataOutputStream outStream) throws IOException {
        outStream.writeLong(model.getStartTime());
        outStream.writeDouble(model.getFrequency());
        for (int i = 0; i < model.getEyeDataList().size(); i++) {
			outStream.writeShort((short)model.getEyeDataList().get(i).intValue());
		}
    }


    public void readFromFile(File file) {
        DataInputStream dataInputStream = null;
		try {
			dataInputStream = new DataInputStream(new FileInputStream(file));
			loadStateFromInStream(dataInputStream);
		} catch (Exception e) {
			log.error(e);
            mainWindow.showMessage("Error reading file " + file.getName());
		} finally {
			try {
				dataInputStream.close();
			} catch (IOException e) {
				log.error(e);
			}
		}
    }

    private void loadStateFromInStream(DataInputStream inputStream) throws IOException {
		try {
            long startTime = inputStream.readLong();
            double frequency = inputStream.readDouble();
            model = new Model(frequency, startTime);
			while (true) {
                model.addEyeData(inputStream.readShort());
			}
		} catch (EOFException e) {
			log.info("End of file");
		}
	}

    void StartRecording() {
        try {
            dataProvider.startRecording();
        } catch (ApplicationException e) {
            mainWindow.showMessage(e.getMessage());
        }
        model = new Model(dataProvider.getIncomingDataFrequency(),dataProvider.getStartTime());
    }

    void StopRecording() {
        dataProvider.stopRecording();
    }
    //todo delete
    public Model getModel(){
        return model;
    }
}
