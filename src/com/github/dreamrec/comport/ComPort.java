package com.github.dreamrec.comport;

import com.github.dreamrec.Ads1292DataProvider;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;

public class ComPort implements SerialPortEventListener {

    private static Log log = LogFactory.getLog(ComPort.class);

    private InputStream inputStream;
    private SerialPort serialPort;
    private OutputStream outputStream;
    private boolean isConnected;
    Ads1292DataProvider dataProvider;
    private static ComPort instance = new ComPort();
    private String comPortName;

    private ComPort() {
    }

    public static ComPort getInstance() {
        return instance;
    }

    public void connect(String comPortName) {
        if (isConnected) {
            return;
        }
        this.comPortName = comPortName;
        try {
            serialPort = (SerialPort) getPortId().open(comPortName, 2000);
            inputStream = serialPort.getInputStream();
            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);
            serialPort.setSerialPortParams(230400, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            outputStream = serialPort.getOutputStream();
            serialPort.notifyOnOutputEmpty(true);
            isConnected = true;
        } catch (Exception e) {
            String msg = "Can't establish com port connection";
            log.error(msg, e);
            throw new ComPortException(msg, e);
        }
    }

    private CommPortIdentifier getPortId() throws IOException {
        Enumeration<CommPortIdentifier> portList = CommPortIdentifier.getPortIdentifiers();
        while (portList.hasMoreElements()) {
            CommPortIdentifier portId = portList.nextElement();
            if (checkThePort(portId)) {
                return portId;
            }
        }
        throw new ComPortException("No serial port with name: " + comPortName);
    }

    private boolean checkThePort(CommPortIdentifier portId) throws IOException {
        return portId.getPortType() == CommPortIdentifier.PORT_SERIAL && portId.getName().equals(comPortName);
    }

    public void disconnect() {
        if (!isConnected)
            return;
        try {
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            log.error(e);
        }
        serialPort.close();
        serialPort.removeEventListener();
        isConnected = false;
    }

    public void writetoport(byte[] bytes) {
        if (!isConnected) {
            connect(comPortName);
        }
        try {
            outputStream.write(bytes);
        } catch (IOException e) {
            log.error("Error writing data to com port", e);
        }
    }

    public void serialEvent(SerialPortEvent event) {
        if (event.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            try {
                while (inputStream.available() > 0) {
                    dataProvider.receiveSample(inputStream.read());
                }
            } catch (IOException e) {
                log.error("Error reading data from com port", e);
            }
        }
    }

    public void addDataProvider(Ads1292DataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }
}
