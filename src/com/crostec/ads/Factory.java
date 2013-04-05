package com.crostec.ads;

import com.crostec.ads.edf.BdfModel;
import com.crostec.ads.model.AdsChannelModel;
import com.crostec.ads.model.AdsModel;
import com.crostec.ads.model.ChannelModel;

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
            accelerometerChannelModel.setEnabled(applicationProperties.isAccelerometerEnabled());
            adsModel.addAccelerometerChannel(accelerometerChannelModel);
        }
        return adsModel;
    }

    public static BdfModel getEdfModel(ApplicationProperties applicationProperties) {
        BdfModel bdfModel = new BdfModel(createAdsModel(applicationProperties));
        bdfModel.setCurrentDirectory(applicationProperties.getLastVisitedDirectory());
        bdfModel.setPatientIdentification(applicationProperties.getPatientIdentification());
        bdfModel.setRecordingIdentification(applicationProperties.getRecordingIdentification());
        return bdfModel;
    }

}

