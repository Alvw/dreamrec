package com.github.dreamrec;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 *
 */
public class DirectoryChooser extends JFileChooser {
    private File currentDir;
    private static final Log log = LogFactory.getLog(EdfFileChooser.class);

    public DirectoryChooser() {
        setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    }

    public DirectoryChooser(File currentDir) {
        this();
        this.currentDir = currentDir;
    }

    public File chooseDirectory() {
        int fileChooserState = showOpenDialog(null);
        if (fileChooserState == JFileChooser.APPROVE_OPTION) {
            return getSelectedFile();
        }
        else{
            return null;
        }
    }

    public File chooseDirectory(File currentDir) {
        this.currentDir = currentDir;
        return chooseDirectory();
    }


    @Override
    public int showOpenDialog(Component component) throws HeadlessException {
        if(currentDir != null){
            setCurrentDirectory(currentDir);
        }
        return super.showOpenDialog(component);
    }

}
