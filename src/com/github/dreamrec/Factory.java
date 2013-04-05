package com.github.dreamrec;

import com.github.dreamrec.ads.*;
import com.github.dreamrec.edf.EdfModel;
import java.awt.event.*;

/**
 *
 */
public class Factory {

    
    private static AdsModel createAdsModel(ApplicationProperties applicationProperties){
        // array_size = Number of Channels
        int[] rldSenseEnabledBits = {0x03, 0x0C};
        int[] loffSenseEnabledBits = {0x03, 0x0C};

        AdsModel adsModel = new AdsModel();
        adsModel.setSps(applicationProperties.getSps());
        adsModel.setComPortName(applicationProperties.getComPortName());
        for (int chNum = 0; chNum < applicationProperties.getNumberOfChannels(); chNum++) {
            AdsChannelModel adsChannelModel = new AdsChannelModel();
            adsChannelModel.setDivider(applicationProperties.getChannelDivider(chNum));
            int chFrequency = applicationProperties.getSps().getValue() /  applicationProperties.getChannelDivider(chNum).getValue();
            adsChannelModel.setHiPassFilterFrequency(chFrequency, applicationProperties.getChannelHiPassFrequency(chNum));
            adsChannelModel.setName(applicationProperties.getChannelName(chNum));
            adsChannelModel.setGain(applicationProperties.getChannelGain(chNum));
            adsChannelModel.setCommutatorState(applicationProperties.getChannelCommutatorState(chNum));
            adsChannelModel.setLoffEnable(applicationProperties.isChannelLoffEnable(chNum));
            adsChannelModel.setRldSenseEnabled(applicationProperties.isChannelRldSenseEnable(chNum));
            adsChannelModel.setRldSenseEnabledBits(rldSenseEnabledBits[chNum]);
            adsChannelModel.setLoffSenseEnabledBits(loffSenseEnabledBits[chNum]);
            adsChannelModel.setEnabled(applicationProperties.isChannelEnabled(chNum));
            adsChannelModel.setElectrodeType(applicationProperties.getChannelElectrodeType(chNum));
            adsModel.addAdsChannel(adsChannelModel);
        }

        for (int chNum = 0; chNum < AdsModel.NUMBER_OF_ACCELEROMETER_CHANNELS; chNum++) {
            ChannelModel accelerometerChannelModel = new ChannelModel();
            accelerometerChannelModel.setDivider(applicationProperties.getAccelerometerDivider());
            accelerometerChannelModel.setName(applicationProperties.getAccelerometerName(chNum));
            int chFrequency = applicationProperties.getSps().getValue() /  applicationProperties.getAccelerometerDivider().getValue();
            accelerometerChannelModel.setHiPassFilterFrequency(chFrequency, applicationProperties.getAccelerometerHiPassFrequency());
            accelerometerChannelModel.setEnabled(applicationProperties.isAccelerometerEnabled());
            adsModel.addAccelerometerChannel(accelerometerChannelModel);
        }
        return adsModel;
    }

    public static EdfModel getEdfModel(ApplicationProperties applicationProperties) {
        EdfModel edfModel = new EdfModel(createAdsModel(applicationProperties));
        edfModel.setCurrentDirectory(applicationProperties.getLastVisitedDirectory());
        edfModel.setPatientIdentification(applicationProperties.getPatientIdentification());
        edfModel.setRecordingIdentification(applicationProperties.getRecordingIdentification());
        return edfModel;
    }

}

