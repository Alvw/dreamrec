package com.github.dreamrec;

import com.github.dreamrec.comport.ComPort;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Date;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 */
public class Ads1292DataProvider implements IDataProvider, FrameDecoderListener{

    private HiPassPreFilter chanel_1;
    private HiPassPreFilter chanel_2;
   /*  private FrequencyDividingPreFilter chanel_1;
    private FrequencyDividingPreFilter chanel_2;*/
     private FrequencyDividingPreFilter acc_1;
    private FrequencyDividingPreFilter acc_2 ;
    private FrequencyDividingPreFilter acc_3;
    private int frameCounter250;

    private int frequencyDivider;
    private ComPort comPort;
    private static final Log log = LogFactory.getLog(Ads1292DataProvider.class);
    private long startTime;
    private long stopTime;
    private double dataFrequency ;
    private FrameDecoder frameDecoder = new FrameDecoder();
    private ApplicationProperties applicationProperties;
    private int totalFrames;

    public Ads1292DataProvider(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
        frameDecoder.addListener(this);
        frequencyDivider = applicationProperties.getFrequencyDivider();
        dataFrequency = applicationProperties.getIncomingDataFrequency() / frequencyDivider;
        chanel_1 = new HiPassPreFilter(applicationProperties.getHiPassBufferSize(),frequencyDivider);
        chanel_2 = new HiPassPreFilter(applicationProperties.getHiPassBufferSize(),frequencyDivider);
//        chanel_1 = new FrequencyDividingPreFilter(frequencyDivider);
//        chanel_2 = new FrequencyDividingPreFilter(frequencyDivider);
        acc_1 = new FrequencyDividingPreFilter(frequencyDivider);
        acc_2 = new FrequencyDividingPreFilter(frequencyDivider);
        acc_3 = new FrequencyDividingPreFilter(frequencyDivider);
    }

    public void startRecording() throws ApplicationException {
           try {
               comPort = ComPort.getInstance();
               comPort.addDataProvider(this);
               comPort.connect(applicationProperties.getComPortName());
           } catch (Exception e) {
               log.error(e);
               throw new ApplicationException("EEG machine reading failure ", e);
           }
           startTime = System.currentTimeMillis();
           log.info("StartTime: " + new Date(startTime));
       }

       public void stopRecording() {
           stopTime = System.currentTimeMillis();
           comPort.writetoport("n".getBytes());
           comPort.writetoport("s".getBytes());
           comPort.writetoport("1".getBytes());
           comPort.disconnect();
           log.info("StopTime: " + new Date(stopTime));
           log.info("Predefined data frequency = " + dataFrequency);
           log.info("Real incoming data frequency = " + totalFrames * 1000.0 / (stopTime - startTime));
       }

    public double getIncomingDataFrequency() {
        return dataFrequency;
    }


    public long getStartTime() {
        return startTime;
    }

    public void receiveSample(int data) {
        frameDecoder.addByte(data);
    }

    public void setFrameCounter250(int value) {
        frameCounter250 = value;
        totalFrames++;
    }

    public void setCh1Value(int value) {
        chanel_1.add(value);
    }

    public void setCh2Value(int value) {
         chanel_2.add(value);
    }

    public void setAcc1Value(int value) {
        acc_1.add(value);
    }

    public void setAcc2Value(int value) {
        acc_2.add(value);
    }

    public void setAcc3Value(int value) {
        acc_3.add(value);
    }

    public int ch1Poll(){
        return chanel_1.poll();
    }
    public int ch1Size(){
        return chanel_1.size();
    }
    public int ch2Poll(){
        return chanel_2.poll();
    }
    public int ch2Size(){
         return chanel_2.size();
    }
    public int acc1Poll(){
        return acc_1.poll();
    }
    public int acc1Size(){
        return acc_1.size();
    }
    public int acc2Poll(){
        return acc_2.poll();
    }
    public int acc2Size(){
        return acc_2.size();
    }
    public int acc3Poll(){
        return acc_3.poll();
    }
    public int acc3Size(){
        return acc_3.size();
    }
}
