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
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 */
public class EdfWriter implements AdsDataListener{

    private static final Log log = LogFactory.getLog(EdfWriter.class);
    DataOutputStream outStream = null;
    AdsModel adsModel;
    int[] edfFrame;
    
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
}
