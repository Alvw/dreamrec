package com.github.dreamrec.comport;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class AdsManager {
    private static final int BYTE_0_MARKER = 0x00;
    private static final int BYTE_1_MARKER = 0x10;
    private static final int BYTE_2_MARKER = 0x20;
    private static final int BYTE_3_MARKER = 0x30;
    private static final int START_PIN_HI_CODE = 0xF0;
    private static final int START_PIN_LO_CODE = 0xF1;
    private static final int WRITE_COMMAND_CODE = 0xF2;
    private static final int WRITE_REGISTER_CODE = 0xF3;
    private static final int SET_CHANEL_DIVIDER_CODE = 0xF4;
    private static final int WRITE_SPS_CODE = 0xF5;

    private ComPort comPort = ComPort.getInstance();

    private List<Byte> write(int code){
        List<Byte> result = new ArrayList<Byte>();
        result.add((byte)code);
        return result;
    }

    private List<Byte> write(int param, int code){
        List<Byte> result = new ArrayList<Byte>();
        result.add((byte) (BYTE_0_MARKER | (param >> 4)));
        result.add((byte) (BYTE_1_MARKER | (param & 0x0F)));
        result.add((byte)code);
        return result;
    }

    private List<Byte> write(int param1, int param2, int code){
        List<Byte> result = new ArrayList<Byte>();
        result.add((byte) (BYTE_0_MARKER | (param1 >> 4)));
        result.add((byte) (BYTE_1_MARKER | (param1 & 0x0F)));
        result.add((byte) (BYTE_2_MARKER | (param2 >> 4)));
        result.add((byte) (BYTE_3_MARKER | (param2 & 0x0F)));
        result.add((byte) code);
        return result;
    }

    public List<Byte> startPinHi(){
        return write(START_PIN_HI_CODE);
    }
    
    public List<Byte> startPinLo() {
        return write(START_PIN_LO_CODE);
    }
    
    public List<Byte> writeCommand(int command){
        return write(command, WRITE_COMMAND_CODE);
    }
    
    public List<Byte> writeRegister(int address, int value){
        return write(address, value, WRITE_REGISTER_CODE);
    }
    
    public List<Byte> writeDividerForChannel(int chanelNumber, int divider){
        return write(chanelNumber, divider, SET_CHANEL_DIVIDER_CODE);
    }

    public List<Byte> writeSps(short sps){
        return write((byte) ((sps) >> 8), (byte) (sps & 0xFF), WRITE_SPS_CODE);
    }
}
