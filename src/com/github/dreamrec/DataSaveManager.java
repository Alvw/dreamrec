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
            /*outStream.writeInt(model.getAcc1DataList().get(i).intValue());
            outStream.writeInt(model.getAcc2DataList().get(i).intValue());
            outStream.writeInt(model.getAcc3DataList().get(i).intValue());*/
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
               /* model.addAcc2Data(inputStream.readInt());
                model.addAcc3Data(inputStream.readInt());*/
            }
        } catch (EOFException e) {
            log.info("End of file");
        }
    }

    public void saveAsEdf(File file, Model model) throws ApplicationException {
        DataOutputStream outStream = null;
        try {
            outStream = new DataOutputStream(new FileOutputStream(file));
            EdfHeaderData headerData = new EdfHeaderData();
            writeEdfHeader(headerData, model, outStream);
            for (int i = 0; i < model.getEyeDataList().size(); i++) {
                Short bigEndianValue = (short)model.getEyeDataList().get(i).intValue();
                Short littleEndianValue =  ByteBuffer.allocate(2)
                .order(ByteOrder.BIG_ENDIAN).putShort(bigEndianValue)
                .order(ByteOrder.LITTLE_ENDIAN).getShort(0);
                outStream.writeShort(littleEndianValue);
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

    private void writeEdfHeader(EdfHeaderData headerData, Model model, DataOutputStream outStream) throws IOException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH.mm.ss");
        Charset characterSet = Charset.forName("US-ASCII");

        String version = appendSpaces("0", 8);
        outStream.write(version.getBytes(characterSet));

        String localPatientIdentification = appendSpaces(headerData.getLocalPatientIdentification(), 80);
        outStream.write(localPatientIdentification.getBytes(characterSet));

        String localRecordingIdentification = appendSpaces(headerData.getRecordingIdentification(), 80);
        outStream.write(localRecordingIdentification.getBytes(characterSet));

        String startDateOfRecording = dateFormat.format(new Date(model.getStartTime()));
        outStream.write(startDateOfRecording.getBytes(characterSet));

        String startTimeOfRecording = timeFormat.format(new Date(model.getStartTime()));
        outStream.write(startTimeOfRecording.getBytes(characterSet));

        String numberOfBytesInHeaderRecord = appendSpaces("512", 8);
        outStream.write(numberOfBytesInHeaderRecord.getBytes(characterSet));

        String reserved = appendSpaces("", 44);
        outStream.write(reserved.getBytes(characterSet));

        String numberOfDataRecords = appendSpaces("-1", 8);
        outStream.write(numberOfDataRecords.getBytes(characterSet));

        String durationOfADataRecord = appendSpaces(headerData.getDurationOfADataRecord(), 8);
        outStream.write(durationOfADataRecord.getBytes(characterSet));

        String numberOfSignals = appendSpaces("1", 4);
        outStream.write(numberOfSignals.getBytes(characterSet));

        String chanelLabel = appendSpaces(headerData.getLabel(), 16);
        outStream.write(chanelLabel.getBytes(characterSet));

        String transducerType = appendSpaces(headerData.getTransducerType(), 80);
        outStream.write(transducerType.getBytes(characterSet));

        String physicalDimension = appendSpaces(headerData.getPhysicalDimension(), 8);
        outStream.write(physicalDimension.getBytes(characterSet));

        String physicalMinimum = appendSpaces(headerData.getPhysicalMinimum(), 8);
        outStream.write(physicalMinimum.getBytes(characterSet));

        String physicalMaximum = appendSpaces(headerData.getPhysicalMaximum(), 8);
        outStream.write(physicalMaximum.getBytes(characterSet));

        String digitalMinimum = appendSpaces(headerData.getDigitalMinimum(), 8);
        outStream.write(digitalMinimum.getBytes(characterSet));

        String digitalMaximum = appendSpaces(headerData.getDigitalMaximum(), 8);
        outStream.write(digitalMaximum.getBytes(characterSet));

        String prefiltering = appendSpaces(headerData.getPrefiltering(), 80);
        outStream.write(prefiltering.getBytes(characterSet));

        String nrOfSamplesInEachDataRecord = appendSpaces(headerData.getNrOfSamplesInEachDataRecord(), 8);
        outStream.write(nrOfSamplesInEachDataRecord.getBytes(characterSet));

        String reserved1 = appendSpaces("", 32);
        outStream.write(reserved1.getBytes(characterSet));
    }

    private String appendSpaces(String text, int fieldLength) {
        StringBuilder sB = new StringBuilder(text);
        for (int i = text.length(); i < fieldLength; i++) {
            sB.append(" ");
        }
        return sB.toString();
    }
}

