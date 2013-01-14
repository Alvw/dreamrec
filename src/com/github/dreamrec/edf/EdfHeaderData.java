package com.github.dreamrec.edf;

/**
 *
 */
public class EdfHeaderData {

    private String localPatientIdentification = "Rabbit";
    private String recordingIdentification = "Tralivali";
    private String durationOfADataRecord = "1";

    private String label = "EEG 1 chanel";
    private String transducerType = "AgAgCl cup electrodes";
    private String physicalDimension = "uV";
    private String physicalMinimum = "-6304";
    private String physicalMaximum = "6304";
    private String digitalMinimum = "-32767";
    private String digitalMaximum = "32767";
    private String prefiltering = "HP:0.05Hz LP:250Hz N:50Hz";
    private String nrOfSamplesInEachDataRecord = "250";

    public String getLocalPatientIdentification() {
        return localPatientIdentification;
    }

    public void setLocalPatientIdentification(String localPatientIdentification) {
        this.localPatientIdentification = localPatientIdentification;
    }

    public String getRecordingIdentification() {
        return recordingIdentification;
    }

    public void setRecordingIdentification(String recordingIdentification) {
        this.recordingIdentification = recordingIdentification;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDurationOfADataRecord() {
        return durationOfADataRecord;
    }

    public void setDurationOfADataRecord(String durationOfADataRecord) {
        this.durationOfADataRecord = durationOfADataRecord;
    }

    public String getTransducerType() {
        return transducerType;
    }

    public void setTransducerType(String transducerType) {
        this.transducerType = transducerType;
    }

    public String getPhysicalDimension() {
        return physicalDimension;
    }

    public void setPhysicalDimension(String physicalDimension) {
        this.physicalDimension = physicalDimension;
    }

    public String getPhysicalMinimum() {
        return physicalMinimum;
    }

    public void setPhysicalMinimum(String physicalMinimum) {
        this.physicalMinimum = physicalMinimum;
    }

    public String getPhysicalMaximum() {
        return physicalMaximum;
    }

    public void setPhysicalMaximum(String physicalMaximum) {
        this.physicalMaximum = physicalMaximum;
    }

    public String getDigitalMinimum() {
        return digitalMinimum;
    }

    public void setDigitalMinimum(String digitalMinimum) {
        this.digitalMinimum = digitalMinimum;
    }

    public String getDigitalMaximum() {
        return digitalMaximum;
    }

    public void setDigitalMaximum(String digitalMaximum) {
        this.digitalMaximum = digitalMaximum;
    }

    public String getPrefiltering() {
        return prefiltering;
    }

    public void setPrefiltering(String prefiltering) {
        this.prefiltering = prefiltering;
    }

    public String getNrOfSamplesInEachDataRecord() {
        return nrOfSamplesInEachDataRecord;
    }

    public void setNrOfSamplesInEachDataRecord(String nrOfSamplesInEachDataRecord) {
        this.nrOfSamplesInEachDataRecord = nrOfSamplesInEachDataRecord;
    }
}
