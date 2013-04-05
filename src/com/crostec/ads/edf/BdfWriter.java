package com.crostec.ads.edf;

import com.crostec.ads.AdsDataListener;
import com.crostec.ads.model.AdsModel;
import com.crostec.ads.model.ChannelModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 */
public class BdfWriter implements AdsDataListener {

    public static final String FILE_EXTENSION = "bdf";
    public static final String FILE_EXTENSION_BIG = "BDF";
    public static final String FILENAME_PATTERN = "dd-mm-yyyy_hh-mm.bdf";
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy_HH-mm");
    private static final Log log = LogFactory.getLog(BdfWriter.class);
    private RandomAccessFile outStream = null;
    private BdfModel bdfModel;
    private int[] edfFrame;
    private int inputFramesCounter;
    private int inputFramesPerRecord;
    private double durationOfDataRecord = 1.0;  // duration of EDF data record (in seconds)
    private int numberOfDataRecords = -1;
    private File edfFile;
    private String report;   // can be Html
    private boolean isReportUpdated;
    private boolean isRecording;
    private long startRecordingTime;
    private long stopRecordingTime;
    private boolean isFirstFrame = true;


    public BdfWriter(BdfModel bdfModel) {
        this.bdfModel = bdfModel;
        openFile();
        inputFramesPerRecord = (int) Math.round(durationOfDataRecord) * bdfModel.getAdsModel().getSps().getValue() / AdsModel.MAX_DIV;
        edfFrame = new int[inputFramesPerRecord * bdfModel.getAdsModel().getFrameSize()];
        isRecording = true;
    }

    public File getBdfFile() {
        return edfFile;
    }


    public void stopRecording() {
        isRecording = false;
        durationOfDataRecord = (stopRecordingTime - startRecordingTime) * 0.001 / (numberOfDataRecords - 1);
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SS");
        log.info("Start recording time = " + startRecordingTime + " (" + dateFormat.format(new Date(startRecordingTime)));
        log.info("Stop recording time = " + stopRecordingTime + " (" + dateFormat.format(new Date(stopRecordingTime)));
        log.info("Duration of a data record = " + durationOfDataRecord);
        try {
            outStream.seek(0);
            outStream.write(createEdfHeader());
            outStream.close();
            createReport("Finished!    Duration: " + numberOfDataRecords + " sec.   " + "Saved to: " + edfFile.getName());
        } catch (IOException e) {
            log.error(e);
        }
    }

    private void createReport(String report) {
        this.report = report;
        isReportUpdated = true;
    }

    public String getReport() {
        isReportUpdated = false;
        return report;
    }

    public boolean isReportUpdated() {
        return isReportUpdated;
    }


    @Override
    public void onDataReceived(int[] dataFrame) {
        if (isRecording) {
            if (isFirstFrame) {
                try {
                    startRecordingTime = System.currentTimeMillis();
                    outStream.write(createEdfHeader());
                } catch (IOException e) {
                    log.error(e);
                }
                isFirstFrame = false;
            }
            ArrayList<ChannelModel> activeChannels = bdfModel.getAdsModel().getActiveChannels();
            int channelPosition = 0;
            for (ChannelModel channel : activeChannels) {
                int channelSampleNumber = AdsModel.MAX_DIV / channel.getDivider().getValue();

                for (int j = 0; j < channelSampleNumber; j++) {
                    edfFrame[channelPosition * inputFramesPerRecord + inputFramesCounter * channelSampleNumber + j] = dataFrame[channelPosition + j];
                }
                channelPosition += channelSampleNumber;
            }
            inputFramesCounter++;
            if (inputFramesCounter == inputFramesPerRecord) {  // when edfFrame is ready
                // change dateFormat to Little_endian and save to edfFile
                for (int i = 0; i < edfFrame.length; i++) {
                    try {
                        outStream.write(to24BitLittleEndian(edfFrame[i]));
                    } catch (IOException e) {
                        log.error(e);
                    }
                }
                inputFramesCounter = 0;
                if (numberOfDataRecords == -1) {
                    numberOfDataRecords = 1;
                } else {
                    numberOfDataRecords++;
                    stopRecordingTime = System.currentTimeMillis();
                }
                createReport("Recording...   Duration: " + numberOfDataRecords + " sec");
            }
        }
    }

    private void openFile() {
        long openFileTime = System.currentTimeMillis();
        edfFile = bdfModel.getFileToSave();
        if (edfFile == null) {
            edfFile = new File(bdfModel.getCurrentDirectory(), dateFormat.format(new Date(openFileTime)) + "." + FILE_EXTENSION);
        } else {
            // change  FILENAME_PATTERN = dd-mm-yyyy_hh-mm.edf  to the current date-month-year_hour-minutes
            if (edfFile.toString().endsWith(FILENAME_PATTERN)) {
                String edfFileName = edfFile.toString();
                String newEdfFileName = edfFileName.substring(0, (edfFileName.length() - FILENAME_PATTERN.length())) + dateFormat.format(new Date(openFileTime)) + "." + FILE_EXTENSION;
                edfFile = new File(newEdfFileName);
            }
        }
        // delete old file with the same name if exist
        edfFile.delete();
        try {
            outStream = new RandomAccessFile(edfFile, "rw");
        } catch (Exception e) {
            log.error(e);
            //throw new ApplicationException("Error while creating edfFile " + fileName);
        }
    }

    /*
    HEADER RECORD  BDF
    8 ascii : Identification code: Byte 1: "255" (non ascii), Bytes 2-8 : "BIOSEMI" (ASCII)
    80 ascii : local patient identification
    80 ascii : local recording identification
    8 ascii : startdate of recording (dd.mm.yy)
    8 ascii : starttime of recording (hh.mm.ss)
    8 ascii : number of bytes in header record
    44 ascii : Version of data format:  "24BIT" (ASCII)
    8 ascii : number of data records (-1 if unknown)
    8 ascii : duration of a data record, in seconds
    4 ascii : number of signals (ns) in data record = number of active channels
    ns * 16 ascii : ns * label (e.g. EEG Fpz-Cz or Body temp)
    ns * 80 ascii : ns * transducer type (e.g. AgAgCl electrode)
    ns * 8 ascii : ns * physical dimension (e.g. uV or degreeC)
    ns * 8 ascii : ns * physical minimum (e.g. -500 or 34)
    ns * 8 ascii : ns * physical maximum (e.g. 500 or 40)
    ns * 8 ascii : ns * digital minimum (e.g. -2048)
    ns * 8 ascii : ns * digital maximum (e.g. 2047)
    ns * 80 ascii : ns * prefiltering (e.g. HP:0.1Hz LP:75Hz)
    ns * 8 ascii : ns * nr of samples in each data record
    ns * 32 ascii : ns * reserved

    ns - number of signals


    Note1:	Total header length (for BDF and EDF) is: {(N+1)*256} bytes, where N is number of channels (including the status channel).
 	Note2:	The "gain" of a specific channel can be calculated by: (Physical max - Physical min) / (Digital max - Digital min).
 	 	The result is the LSB value in the specified Physical dimension of channels. (31,25nV / 1uV in the BDF/EDF example Header from above)
 	Note3:	The last 10 fields are defined for each fields separately. Each channel can be different.
    */




    public byte[] createEdfHeader() {
        Charset characterSet = Charset.forName("US-ASCII");
        StringBuilder asciiHeader = new StringBuilder();

        String identificationCode = "BIOSEMI";

        String localPatientIdentification = "Patient: " + bdfModel.getPatientIdentification();
        String localRecordingIdentification = "Record: " + bdfModel.getRecordingIdentification();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH.mm.ss");

        String startDateOfRecording = dateFormat.format(new Date(startRecordingTime));
        String startTimeOfRecording = timeFormat.format(new Date(startRecordingTime));

        int numberOfSignals = bdfModel.getAdsModel().getNumberOfActiveChannels();  // number of signals in data record = number of active channels
        int numberOfBytesInHeaderRecord = 256 * (1 + numberOfSignals);
        String versionOfDataFormat = "24BIT";

        String channelsDigitalMaximum = "8388607";
        String channelsDigitalMinimum = "-8388608";
        String channelsPhysicalMaximum = "1209600";  // todo function(channel.gain)
        String channelsPhysicalMinimum = "-1209600"; // todo function(channel.gain)

        String accelerometerDigitalMaximum = "1024";
        String accelerometerDigitalMinimum = "-1024";
        String accelerometerPhysicalMaximum = "1";
        String accelerometerPhysicalMinimum = "0";

        asciiHeader.append(adjustLength(identificationCode, 7));  // !!! 7 not 8 because first non ascii byte we will add later !!!
        asciiHeader.append(adjustLength(localPatientIdentification, 80));
        asciiHeader.append(adjustLength(localRecordingIdentification, 80));
        asciiHeader.append(startDateOfRecording);
        asciiHeader.append(startTimeOfRecording);
        asciiHeader.append(adjustLength(Integer.toString(numberOfBytesInHeaderRecord), 8));
        asciiHeader.append(adjustLength(versionOfDataFormat, 44));
        asciiHeader.append(adjustLength(Integer.toString(numberOfDataRecords), 8));
        asciiHeader.append(adjustLength(String.format("%.6f", durationOfDataRecord).replace(",", "."), 8));
        asciiHeader.append(adjustLength(Integer.toString(numberOfSignals), 4));

        StringBuilder labels = new StringBuilder();
        StringBuilder transducerTypes = new StringBuilder();
        StringBuilder physicalDimensions = new StringBuilder();
        StringBuilder physicalMinimums = new StringBuilder();
        StringBuilder physicalMaximums = new StringBuilder();
        StringBuilder digitalMinimums = new StringBuilder();
        StringBuilder digitalMaximums = new StringBuilder();
        StringBuilder preFilterings = new StringBuilder();
        StringBuilder samplesNumbers = new StringBuilder();
        StringBuilder reservedForChannels = new StringBuilder();


        for (ChannelModel channel : bdfModel.getAdsModel().getAdsActiveChannels()) {
            labels.append(adjustLength(channel.getName(), 16));

            transducerTypes.append(adjustLength(channel.getElectrodeType(), 80));
            physicalDimensions.append(adjustLength(channel.getPhysicalDimension(), 8));
            physicalMinimums.append(adjustLength(channelsPhysicalMinimum, 8));
            physicalMaximums.append(adjustLength(channelsPhysicalMaximum, 8));
            digitalMinimums.append(adjustLength(channelsDigitalMinimum, 8));
            digitalMaximums.append(adjustLength(channelsDigitalMaximum, 8));
            preFilterings.append(adjustLength("None", 80));

            int nrOfSamplesInEachDataRecord = (int) Math.round(durationOfDataRecord) * bdfModel.getAdsModel().getSps().getValue() / channel.getDivider().getValue();

            samplesNumbers.append(adjustLength(Integer.toString(nrOfSamplesInEachDataRecord), 8));
            reservedForChannels.append(adjustLength("", 32));
        }
        for (ChannelModel channel : bdfModel.getAdsModel().getAccelerometerActiveChannels()) {
            labels.append(adjustLength(channel.getName(), 16));
            transducerTypes.append(adjustLength(channel.getElectrodeType(), 80));
            physicalDimensions.append(adjustLength(channel.getPhysicalDimension(), 8));
            physicalMinimums.append(adjustLength(accelerometerPhysicalMinimum, 8));
            physicalMaximums.append(adjustLength(accelerometerPhysicalMaximum, 8));
            digitalMinimums.append(adjustLength(accelerometerDigitalMinimum, 8));
            digitalMaximums.append(adjustLength(accelerometerDigitalMaximum, 8));

            preFilterings.append(adjustLength("None", 80));

            int nrOfSamplesInEachDataRecord = (int) Math.round(durationOfDataRecord) * bdfModel.getAdsModel().getSps().getValue() / channel.getDivider().getValue();

            samplesNumbers.append(adjustLength(Integer.toString(nrOfSamplesInEachDataRecord), 8));
            reservedForChannels.append(adjustLength("", 32));
        }

        asciiHeader.append(labels);
        asciiHeader.append(transducerTypes);
        asciiHeader.append(physicalDimensions);
        asciiHeader.append(physicalMinimums);
        asciiHeader.append(physicalMaximums);
        asciiHeader.append(digitalMinimums);
        asciiHeader.append(digitalMaximums);
        asciiHeader.append(preFilterings);
        asciiHeader.append(samplesNumbers);
        asciiHeader.append(reservedForChannels);

        // add first non ascii byte  "255"
        ByteBuffer byteBuffer = ByteBuffer.allocate(asciiHeader.length() + 1);
        byteBuffer.put((byte)255);
        byteBuffer.put(asciiHeader.toString().getBytes(characterSet));
        return byteBuffer.array();

    }


    /**
     * if the String.length() is more then the given length we cut the String
     * if the String.length() is less then the given length we append spaces to the end of the String
     */

    private String adjustLength(String text, int length) {
        StringBuilder sB = new StringBuilder(text);
        if (text.length() > length) {
            sB.delete((length + 1), text.length());

        } else {
            for (int i = text.length(); i < length; i++) {
                sB.append(" ");
            }
        }
        return sB.toString();
    }

    /**
     * convert int data format to 24 bit (3 bytes) data format valid for Bdf and
     * change Big_endian dateFormat of numbers (java)  to Little_endian dateFormat (for bdf and microcontroller)
     *
     */
    private byte[] to24BitLittleEndian(int value) {
        int sizeOfInt = 4;
        ByteBuffer byteBuffer = ByteBuffer.allocate(sizeOfInt).putInt(value);
        byte[] result = new byte[3];
        result[0] = byteBuffer.get(sizeOfInt-1);
        result[1] = byteBuffer.get(sizeOfInt-2);
        result[2] = byteBuffer.get(sizeOfInt-3);
        return result;
    }
}
