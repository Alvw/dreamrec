package com.github.dreamrec.edf;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 *
 */
public class EdfFileChooser extends JFileChooser {

    private File fileToSelect;
    private static final Log log = LogFactory.getLog(EdfFileChooser.class);
    private boolean isFileExistCheckEnabled = false;

    public EdfFileChooser() {
        setFileFilter();
    }

    public EdfFileChooser(File fileOrDir) {
        super(fileOrDir);
        setFileFilter();
    }

    public File chooseFileToSave() {
        int fileChooserState = showSaveDialog(null);
        if (fileChooserState == JFileChooser.APPROVE_OPTION) {
            return getSelectedFile();
        } else {
            return null;
        }
    }

    public File chooseFileToOpen() {
        int fileChooserState = showOpenDialog(null);
        if (fileChooserState == JFileChooser.APPROVE_OPTION) {
            return getSelectedFile();
        } else {
            return null;
        }
    }

    // if true, in SaveAs dialog will be done  checkup whether the file with the same name already exist before saving
    public void setFileExistCheckEnabled(boolean fileExistCheckEnabled) {
        isFileExistCheckEnabled = fileExistCheckEnabled;
    }

    @Override
    public void setCurrentDirectory(File fileOrDir) {
        super.setCurrentDirectory(fileOrDir);
        if (!fileOrDir.isDirectory()) {

            fileToSelect = fileOrDir;
        }
    }

    @Override
    public void approveSelection() {
        if (getDialogType() != JFileChooser.OPEN_DIALOG) {
            //set selected file to canonical form if possible ot null
            File selectedFile = getSelectedFile();
            setSelectedFile(getCanonicalFile(selectedFile, this));
            if (getSelectedFile() != null) {
                // for files with no extension add "edf" extension
                String file = getSelectedFile().toString();
                if (FilenameUtils.getExtension(file).equals("")) {
                    file += ("." + EdfWriter.FILE_EXTENSION);
                    setSelectedFile(new File(file));
                }
                if (isFileExistCheckEnabled) {
                    if (isExistingFileReplace(getSelectedFile(), this)) {
                        super.approveSelection();
                    }
                } else {
                    super.approveSelection();
                }
            }
        }
    }


    @Override
    public int showOpenDialog(Component component) throws HeadlessException {
        return super.showOpenDialog(component);
    }

    @Override
    public int showSaveDialog(Component parent) throws HeadlessException {
        setSelectedFile(fileToSelect);

        // Standard showSaveDialog() dont work correctly under MAC OS
        // And in such manner we bypass that Java bug
        return super.showDialog(parent, "Save As");

    }


    private void setFileFilter() {
        FileFilter fileFilter = new FileNameExtensionFilter(EdfWriter.FILE_EXTENSION_BIG, EdfWriter.FILE_EXTENSION);
        addChoosableFileFilter(fileFilter);
        setFileFilter(fileFilter);
        setFileSelectionMode(JFileChooser.FILES_ONLY);
    }


    public static boolean isExistingFileReplace(File file, Component parentComponentForMsgPanel) {
        if(file != null){
            if (file.exists()) {
                String msg = "<html><center>" + file + "<br> already exists. Do you want to replace it?</center></html>";
                String title = "";
                int selectedOption = JOptionPane.showConfirmDialog(parentComponentForMsgPanel, msg, title, JOptionPane.YES_NO_OPTION);
                if (selectedOption == JOptionPane.NO_OPTION) {
                    return false;
                }
            }
        }
        return true;
    }

    public static File getCanonicalFile(File file, Component parentComponentForMsgPanel) {
        try {
            if (file != null) {
                return file.getCanonicalFile();
            }
        } catch (IOException e) {
            String title = "";
            JOptionPane.showConfirmDialog(parentComponentForMsgPanel, "Error: " + e.getMessage(), title, JOptionPane.OK_OPTION);
        }
        return null;
    }
}
