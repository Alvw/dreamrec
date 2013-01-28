package com.github.dreamrec;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

/**
 *
 */
public class EdfFileChooser extends JFileChooser {

    private File fileToSaveOrDir;
    private static final Log log = LogFactory.getLog(EdfFileChooser.class);

    public EdfFileChooser() {
        FileFilter fileFilter = new FileNameExtensionFilter("EDF", "edf");
        addChoosableFileFilter(fileFilter);
        setFileFilter(fileFilter);
        setFileSelectionMode(JFileChooser.FILES_ONLY);
    }

    public EdfFileChooser(File fileToSaveOrDir) {
        this();
        this.fileToSaveOrDir = fileToSaveOrDir;
    }
    
    public File chooseFileToSave() {
        int fileChooserState = showSaveDialog(null);
        if (fileChooserState == JFileChooser.APPROVE_OPTION) {
            return getSelectedFile();
        }
        else{
            return null;
        }        
    }

    public File chooseFileToSave(File fileToSave) {
        fileToSaveOrDir = fileToSave;
        return chooseFileToSave();
    }
    
    public File chooseFileToOpen() {
        int fileChooserState = showOpenDialog(null);
        if (fileChooserState == JFileChooser.APPROVE_OPTION) {
            return getSelectedFile();
        }
        else{
            return null;
        }
    }

    public File chooseFileToOpen(File dir) {
        fileToSaveOrDir = dir;
        return chooseFileToOpen();
    }

    @Override
    public void approveSelection() {
        File selectedFile = getSelectedFile();
        if(selectedFile.exists() & (getDialogType() != JFileChooser.OPEN_DIALOG)) {
            String msg = getSelectedFile().getName() + " - already exists. \nDo you want to replace it?";
            String title = "";
            int selectedOption = JOptionPane.showConfirmDialog(this, msg, title, JOptionPane.YES_NO_OPTION);
            if (selectedOption == JOptionPane.YES_OPTION) {
                super.approveSelection();
            }
        }
        else{
            super.approveSelection();
        }
    }

    @Override
    public int showOpenDialog(Component component) throws HeadlessException {
        if(fileToSaveOrDir != null){
            setCurrentDirectory(fileToSaveOrDir);
        }
        return super.showOpenDialog(component);
    }

    @Override
    public int showSaveDialog(Component parent) throws HeadlessException {
        if(fileToSaveOrDir != null){
            setSelectedFile(fileToSaveOrDir.getAbsoluteFile());
        }
        // Standard showSaveDialog() dont work correctly under MAC OS
        // And in such manner we bypass that Java bug
        return super.showDialog(parent, "Save As");

    }
}
