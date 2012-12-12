package com.github.dreamrec.comport;

import com.github.dreamrec.Ads1292DataProvider;
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class ComPort {

//    private Timer checkConnectionTimer;
    private static Log log = LogFactory.getLog(ComPort.class);
    private InputStream inputStream;
    private OutputStream outputStream;
    private boolean isConnected;
    Ads1292DataProvider dataProvider;
    private static ComPort instance = new ComPort();
    CommPort commPort;
    SerialReader serialReader;
    Thread serialReaderThread;

    private ComPort() {

    }

    public static ComPort getInstance() {
        return instance;
    }

    public void connect(String comPortName) throws Exception {
        if (isConnected) {
            return;
        }
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(comPortName);
        if (portIdentifier.isCurrentlyOwned()) {
            System.out.println("Error: Port is currently in use");
        } else {
            commPort = portIdentifier.open(this.getClass().getName(), 2000);
            if (commPort instanceof SerialPort) {
                SerialPort serialPort = (SerialPort) commPort;
                serialPort.setSerialPortParams(230400, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
                inputStream = serialPort.getInputStream();
                outputStream = serialPort.getOutputStream();
                isConnected = true;
                serialReader = new SerialReader(inputStream, dataProvider);
                serialReaderThread =new Thread(serialReader);
                serialReaderThread.start();
            } else {
                System.out.println("Error: Only serial ports are handled by this example.");
            }
        }
    }


    public void disconnect() {
        if (!isConnected)
            return;
        try {
            isConnected = false;
            serialReader.stopReading();
            try {
                serialReaderThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            inputStream.close();
            outputStream.close();
            commPort.close();
        } catch (IOException e) {
            log.error(e);
        }

    }

    public void writeToPort(byte[] bytes) {
        (new Thread(new SerialWriter(outputStream, bytes))).start();
    }

    public void addDataProvider(Ads1292DataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    public static class SerialReader implements Runnable {
        private boolean isRecording = true;
        private InputStream in;
        Ads1292DataProvider dataProvider;

        public SerialReader(InputStream in, Ads1292DataProvider dataProvider) {
            this.in = in;
            this.dataProvider = dataProvider;
        }

        public void stopReading() {
            isRecording = false;
        }

        public void run() {
            int len = -1;
            byte[] buf = new byte[1024];
            try {
                while (isRecording) {
                    len = this.in.read(buf);
                    if (len != -1) {
                        for (int i = 0; i < len; i++) {
                            dataProvider.receiveSample((buf[i] & 0xFF));
                        }
                    } else {
                        Thread.sleep(100);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static class SerialWriter implements Runnable {
        private OutputStream out;
        byte[] data;

        public SerialWriter(OutputStream out, byte[] data) {
            this.out = out;
            this.data = data;
        }

        public void run() {
            try {
                out.write(data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
