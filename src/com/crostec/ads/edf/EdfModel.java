package com.crostec.ads.edf;

import com.crostec.ads.model.AdsModel;

import java.io.File;

/**
 * 
 */
public class EdfModel {
    private AdsModel adsModel;
    private String patientIdentification;
    private String recordingIdentification;
    private File fileToSave;
    private File currentDirectory;

    public EdfModel(AdsModel adsModel) {
        this.adsModel = adsModel;
    }

    public AdsModel getAdsModel() {
        return adsModel;
    }

    public void setAdsModel(AdsModel adsModel) {
        this.adsModel = adsModel;
    }

    public String getPatientIdentification() {
        return patientIdentification;
    }

    public void setPatientIdentification(String patientIdentification) {
        this.patientIdentification = patientIdentification;
    }

    public String getRecordingIdentification() {
        return recordingIdentification;
    }

    public void setRecordingIdentification(String recordingIdentification) {
        this.recordingIdentification = recordingIdentification;
    }

    public File getFileToSave() {
        return fileToSave;
    }

    public void setCurrentDirectory(File directory) {
        currentDirectory = directory;
    }

    public void setFileToSave(File fileToSave) {
        this.fileToSave = fileToSave;
        if (fileToSave != null){
            if (fileToSave.getParentFile() != null){
                if (fileToSave.getParentFile().isDirectory()){
                    currentDirectory = fileToSave.getParentFile();
                }
                else {
                    fileToSave.getParentFile().mkdirs();
                    currentDirectory = fileToSave.getParentFile();
                }
            }
        }
    }

    public File getCurrentDirectory() {
        if(currentDirectory == null){
            currentDirectory = new File(System.getProperty("user.dir"));
        }
        return currentDirectory;
    }
}
