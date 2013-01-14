package com.github.dreamrec.edf;

import com.github.dreamrec.AdsDataListener;
import com.github.dreamrec.ApplicationException;
import com.github.dreamrec.Model;
import com.github.dreamrec.ads.AdsModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 */
public class EdfWriter implements AdsDataListener{

    private static final Log log = LogFactory.getLog(EdfWriter.class);
    private DataOutputStream outStream = null;
    private AdsModel adsModel;
    private int[] edfFrame;

    private int recordingDuration = 1;

    private String localPatientIdentification = "Rabbit";
    private String recordingIdentification = "Tralivali";
    private String durationOfADataRecord = "1";

    private String prefiltering = "HP:0.05Hz LP:250Hz N:50Hz";
    private String channelsTransducerType = "AgAgCl cup electrodes";
    private String channelsDigitalMaximum = "32767";
    private String channelsDigitalMinimum = "-32767";

    private String accelerometerTransducerType = "none";
    private String accelerometerDigitalMaximum = "1024";
    private String accelerometerDigitalMinimum = "-1024";
    private String physicalMaximum = "g";
    private String physicalMinimum = "1";


    
    public EdfWriter(AdsModel adsModel) throws ApplicationException {
        this.adsModel = adsModel;
        openFile();

    } 

    private void openFile() throws ApplicationException {  
        Date date = new Date(System.currentTimeMillis());
        String fileName = new SimpleDateFormat("ss_mm_HH_dd_MM_yyyy").format(date) + ".edf";
        try {
            outStream = new DataOutputStream(new FileOutputStream(fileName));
        } catch (Exception e) {
            log.error(e);
            throw new ApplicationException("Error while creating file " + fileName);
        }
    }

    @Override
    public void onDataReceived(int[] dataFrame) {
        
    }

    /*
        HEADER RECORD
    8 ascii : version of this data format (0)
    80 ascii : local patient identification
    80 ascii : local recording identification
    8 ascii : startdate of recording (dd.mm.yy)
    8 ascii : starttime of recording (hh.mm.ss)
    8 ascii : number of bytes in header record
    44 ascii : reserved
    8 ascii : number of data records (-1 if unknown, obey item 10 of the additional EDF+ specs)
    8 ascii : duration of a data record, in seconds
    4 ascii : number of signals (ns) in data record
    ns * 16 ascii : ns * label (e.g. EEG Fpz-Cz or Body temp) (mind item 9 of the additional EDF+ specs)
    ns * 80 ascii : ns * transducer type (e.g. AgAgCl electrode)
    ns * 8 ascii : ns * physical dimension (e.g. uV or degreeC)
    ns * 8 ascii : ns * physical minimum (e.g. -500 or 34)
    ns * 8 ascii : ns * physical maximum (e.g. 500 or 40)
    ns * 8 ascii : ns * digital minimum (e.g. -2048)
    ns * 8 ascii : ns * digital maximum (e.g. 2047)
    ns * 80 ascii : ns * prefiltering (e.g. HP:0.1Hz LP:75Hz)
    ns * 8 ascii : ns * nr of samples in each data record
    ns * 32 ascii : ns * reserved
    */


    /**
     * if the String.length() is more then the given length we cut the String
     * if the String.length() is less then the given length we append spaces to the end of the String
     */

    private String adjustLength (String text, int length) {
        StringBuilder sB = new StringBuilder(text);
        if( sB.length() > length ) {
            sB.delete((length + 1), sB.length());
            
        } else {
            for (int i = sB.length(); i < length; i++) {
                sB.append(" ");
            }            
        }
        return sB.toString();
    }

    /**
     * change Big_endian format of numbers (java)  to Little_endian format (microcontroller)
     */
    private Short toLittleEndian(Short value) {
        int capacity = 2;
        return ByteBuffer.allocate(capacity)
                .order(ByteOrder.BIG_ENDIAN).putShort(value)
                .order(ByteOrder.LITTLE_ENDIAN).getShort(0);
    }
}
