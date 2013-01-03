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
    SerialWriter serialWriter;
    Thread serialWriterThread;

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
                serialReaderThread = new Thread(serialReader);
                serialReaderThread.start();
                serialWriter = new SerialWriter(outputStream);
                serialWriterThread = new Thread(serialWriter);
                serialWriterThread.start();
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
            serialReader.disconnect();
            serialWriter.disconnect();
            try {
                serialReaderThread.join();
                serialWriterThread.join();
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
        List<Byte> dataList = new ArrayList<Byte>();
        for (int i = 0; i < bytes.length; i++) {
            dataList.add(bytes[i]);
        }
        serialWriter.write(dataList);
    }

    public void addDataProvider(Ads1292DataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    public static class SerialReader implements Runnable {
        private boolean isConnected = true;
        private InputStream in;
        Ads1292DataProvider dataProvider;

        public SerialReader(InputStream in, Ads1292DataProvider dataProvider) {
            this.in = in;
            this.dataProvider = dataProvider;
        }

        public void disconnect() {
            isConnected = false;
        }

        public void run() {
            int len = -1;
            byte[] buf = new byte[8];
            try {
                while (isConnected) {
                    len = this.in.read(buf);
                    while (isConnected && (len = this.in.read(buf)) > -1) {
//                        System.out.print(new String(buf,0,len));
                        for (int i = 0; i < len; i++) {
                            dataProvider.receiveSample((buf[i] & 0xFF));
                        }
                    }
                    Thread.sleep(100);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static class SerialWriter implements Runnable {
        private OutputStream out;
        List<Byte> data = new ArrayList<Byte>();
        boolean isConnected = true;
        boolean isDataReady = false;

        public SerialWriter(OutputStream out) {
            this.out = out;
        }

        public void write(List<Byte> dataList) {
            synchronized (data) {
                data.clear();
                for (Byte aByte : dataList) {
                    data.add(aByte);
                }
                isDataReady = true;
                data.notifyAll();
            }
        }

        public void run() {
            synchronized (data) {
                while (isConnected) {
                    while (!isDataReady) {
                        try {
                            data.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    byte[] outData = new byte[data.size()];
                    for (int i = 0; i < outData.length; i++) {
                        outData[i] = data.get(i);
                    }
                    try {
                        if (isConnected) {
                            out.write(outData);
                            isDataReady = false;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        public void disconnect() {
            isConnected = false;
            isDataReady = true;
            synchronized (data) {
                data.notifyAll();
            }
        }
    }
}
