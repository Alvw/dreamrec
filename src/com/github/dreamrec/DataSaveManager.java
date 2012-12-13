package com.github.dreamrec;

import com.github.dreamrec.edf.EdfHeaderData;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Saves measurement data to file and reads from it
 */
public class DataSaveManager {


    private static final Log log = LogFactory.getLog(DataSaveManager.class);

    public void saveToFile(File file, Model model) throws ApplicationException {
        DataOutputStream outStream = null;
        try {
            outStream = new DataOutputStream(new FileOutputStream(file));
            saveStateToStream(outStream, model);
        } catch (Exception e) {
            log.error(e);
            throw new ApplicationException("Error while saving file " + file.getName());
        } finally {
            try {
                outStream.close();
            } catch (IOException e) {
                log.error(e);
            }
        }
    }

    private void saveStateToStream(DataOutputStream outStream, Model model) throws IOException {
        outStream.writeLong(model.getStartTime());
        outStream.writeDouble(model.getFrequency());
        for (int i = 0; i < model.getEyeDataList().size(); i++) {
            outStream.writeShort(model.getEyeDataList().get(i).intValue());
            outStream.writeShort(model.getCh2DataList().get(i).intValue());
            outStream.writeShort(model.getAcc1DataList().get(i).intValue());
            outStream.writeShort(model.getAcc2DataList().get(i).intValue());
            outStream.writeShort(model.getAcc3DataList().get(i).intValue());
        }
    }


    public void readFromFile(File file, Model model) throws ApplicationException {
        DataInputStream dataInputStream = null;
        try {
            dataInputStream = new DataInputStream(new FileInputStream(file));
            loadStateFromInStream(dataInputStream, model);
        } catch (Exception e) {
            log.error(e);
            throw new ApplicationException("Error while reading from file " + file.getName());
        } finally {
            try {
                dataInputStream.close();
            } catch (IOException e) {
                log.error(e);
            }
        }
    }

    private void loadStateFromInStream(DataInputStream inputStream, Model model) throws IOException {
        try {
            long startTime = inputStream.readLong();
            double frequency = inputStream.readDouble();
            model.clear();
            model.setFrequency(frequency);
            model.setStartTime(startTime);
            while (true) {
                model.addEyeData(inputStream.readShort());
                model.addCh2Data(inputStream.readShort());
                model.addAcc1Data(inputStream.readShort());
                model.addAcc2Data(inputStream.readShort());
                model.addAcc3Data(inputStream.readShort());
            }
        } catch (EOFException e) {
            log.info("End of file");
        }
    }

    public void saveAsEdf(File file, Model model) throws ApplicationException {
        DataOutputStream outStream = null;
        try {
            outStream = new DataOutputStream(new FileOutputStream(file));
            EdfHeaderData headerData1 = new EdfHeaderData();
            EdfHeaderData headerData2 = new EdfHeaderData();
            headerData2.setLabel("EEG 2 chanel");

            EdfHeaderData headerData3 = new EdfHeaderData();
            headerData3.setNrOfSamplesInEachDataRecord(String.valueOf((int)model.getFrequency()));
            headerData3.setLabel("Accel X ");

            EdfHeaderData headerData4 = new EdfHeaderData();
            headerData4.setNrOfSamplesInEachDataRecord(String.valueOf((int)model.getFrequency()));
            headerData4.setLabel("Accel Y");

            EdfHeaderData headerData5 = new EdfHeaderData();
            headerData5.setNrOfSamplesInEachDataRecord(String.valueOf((int)model.getFrequency()));
            headerData5.setLabel("Accel Z");

            writeEdfHeader(model, outStream, headerData1, headerData2, headerData3, headerData4, headerData5);
            for (int j = 0; j < model.getEyeDataList().size() / 250; j++) {
                for (int i = 0; i < 250; i++) {
                    outStream.writeShort(toLittleEndian(model.getEyeDataList().get(i+j*250)));
                }
                for (int i = 0; i < 250; i++) {
                    outStream.writeShort(toLittleEndian(model.getCh2DataList().get(i+j*250)));
                }
                 for (int i = 0; i < 250; i++) {
                    try {
                        outStream.writeShort(toLittleEndian(model.getAcc1DataList().get(i+j*250)));
                    } catch (IOException e) {
                        e.printStackTrace();  //todo refactor
                    }
                }
                for (int i = 0; i < 250; i++) {
                    try {
                        outStream.writeShort(toLittleEndian(model.getAcc2DataList().get(i+j*250)));
                    } catch (IOException e) {
                        e.printStackTrace();  //todo refactor
                    }
                }
               for (int i = 0; i < 250; i++) {
                    try {
                        outStream.writeShort(toLittleEndian(model.getAcc3DataList().get(i+j*250)));
                    } catch (IOException e) {
                        e.printStackTrace();  //todo refactor
                    }
                }
            }
        } catch (Exception e) {
            log.error(e);
            throw new ApplicationException("Error while saving file " + file.getName());
        } finally {
            try {
                outStream.close();
            } catch (IOException e) {
                log.error(e);
            }
        }
    }

    public Short toLittleEndian(Short value) {   //todo change to private
        return ByteBuffer.allocate(2)
                .order(ByteOrder.BIG_ENDIAN).putShort(value)
                .order(ByteOrder.LITTLE_ENDIAN).getShort(0);
    }

    public void writeEdfHeader(Model model, DataOutputStream outStream, EdfHeaderData... headerData) throws IOException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH.mm.ss");
        Charset characterSet = Charset.forName("US-ASCII");

        String version = appendSpaces("0", 8);
        outStream.write(version.getBytes(characterSet));

        String localPatientIdentification = appendSpaces(headerData[0].getLocalPatientIdentification(), 80);
        outStream.write(localPatientIdentification.getBytes(characterSet));

        String localRecordingIdentification = appendSpaces(headerData[0].getRecordingIdentification(), 80);
        outStream.write(localRecordingIdentification.getBytes(characterSet));

        String startDateOfRecording = dateFormat.format(new Date(model.getStartTime()));
        outStream.write(startDateOfRecording.getBytes(characterSet));

        String startTimeOfRecording = timeFormat.format(new Date(model.getStartTime()));
        outStream.write(startTimeOfRecording.getBytes(characterSet));

        String numberOfBytesInHeaderRecord = appendSpaces("1536", 8);
        outStream.write(numberOfBytesInHeaderRecord.getBytes(characterSet));

        String reserved = appendSpaces("", 44);
        outStream.write(reserved.getBytes(characterSet));

        String numberOfDataRecords = appendSpaces(String.valueOf(model.getEyeDataList().size()/250), 8);
        outStream.write(numberOfDataRecords.getBytes(characterSet));

        String durationOfADataRecord = appendSpaces(headerData[0].getDurationOfADataRecord(), 8);
        outStream.write(durationOfADataRecord.getBytes(characterSet));

        String numberOfSignals = appendSpaces(String.valueOf(headerData.length), 4);
        outStream.write(numberOfSignals.getBytes(characterSet));
        //---------
        for (EdfHeaderData edfHeaderData : headerData) {
            String chanelLabel = appendSpaces(edfHeaderData.getLabel(), 16);
            outStream.write(chanelLabel.getBytes(characterSet));
        }

        for (EdfHeaderData edfHeaderData : headerData) {
            String transducerType = appendSpaces(edfHeaderData.getTransducerType(), 80);
            outStream.write(transducerType.getBytes(characterSet));
        }

        for (EdfHeaderData edfHeaderData : headerData) {
            String physicalDimension = appendSpaces(edfHeaderData.getPhysicalDimension(), 8);
            outStream.write(physicalDimension.getBytes(characterSet));
        }

        for (EdfHeaderData edfHeaderData : headerData) {
            String physicalMinimum = appendSpaces(edfHeaderData.getPhysicalMinimum(), 8);
            outStream.write(physicalMinimum.getBytes(characterSet));
        }

        for (EdfHeaderData edfHeaderData : headerData) {
            String physicalMaximum = appendSpaces(edfHeaderData.getPhysicalMaximum(), 8);
            outStream.write(physicalMaximum.getBytes(characterSet));
        }

        for (EdfHeaderData edfHeaderData : headerData) {
            String digitalMinimum = appendSpaces(edfHeaderData.getDigitalMinimum(), 8);
            outStream.write(digitalMinimum.getBytes(characterSet));
        }

        for (EdfHeaderData edfHeaderData : headerData) {
            String digitalMaximum = appendSpaces(edfHeaderData.getDigitalMaximum(), 8);
            outStream.write(digitalMaximum.getBytes(characterSet));
        }

        for (EdfHeaderData edfHeaderData : headerData) {
            String prefiltering = appendSpaces(edfHeaderData.getPrefiltering(), 80);
            outStream.write(prefiltering.getBytes(characterSet));
        }

        for (EdfHeaderData edfHeaderData : headerData) {
            String nrOfSamplesInEachDataRecord = appendSpaces(edfHeaderData.getNrOfSamplesInEachDataRecord(), 8);
            outStream.write(nrOfSamplesInEachDataRecord.getBytes(characterSet));
        }

        for (EdfHeaderData edfHeaderData : headerData) {
            String reserved1 = appendSpaces("", 32);
            outStream.write(reserved1.getBytes(characterSet));
        }
    }

    private String appendSpaces(String text, int fieldLength) {
        StringBuilder sB = new StringBuilder(text);
        for (int i = text.length(); i < fieldLength; i++) {
            sB.append(" ");
        }
        return sB.toString();
    }
}

