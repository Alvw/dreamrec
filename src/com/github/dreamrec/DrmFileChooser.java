package com.github.dreamrec;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 */
public class DrmFileChooser extends JFileChooser {

    private ApplicationProperties applicationProperties;
    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy_HH:mm");

    private static final Log log = LogFactory.getLog(DrmFileChooser.class);

    public DrmFileChooser(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
        addChoosableFileFilter(new FileNameExtensionFilter("EDF", "edf"));
    }

    @Override
    public int showOpenDialog(Component parent) throws HeadlessException {
        setLastVisitedDirectory();
        int fileChooserState = super.showOpenDialog(parent);
        if (fileChooserState == JFileChooser.APPROVE_OPTION) {
            applicationProperties.setLastVisitedDirectory(getCurrentDirectory().getPath());
        }
        return fileChooserState;
    }

    @Override
    public int showSaveDialog(Component parent) throws HeadlessException {
        String suggestedFileName = format.format(new Date(System.currentTimeMillis())) + ".edf";
        setSelectedFile(new File(suggestedFileName));
        setLastVisitedDirectory();
        int fileChooserState = super.showSaveDialog(parent);
        if (fileChooserState == JFileChooser.APPROVE_OPTION) {
            applicationProperties.setLastVisitedDirectory(getCurrentDirectory().getPath());
        }
        return fileChooserState;
    }

    private void setLastVisitedDirectory() {
        String dirName = applicationProperties.getLastVisitedDirectory();
        if (dirName != null) {
            setCurrentDirectory(new File(dirName));
        }
    }

}
