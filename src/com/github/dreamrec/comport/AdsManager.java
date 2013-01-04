package com.github.dreamrec.comport;

import java.nio.ByteBuffer;

/**
 *
 */
public class AdsManager {
    private static final byte BYTE_0_MARKER = 0x00;
    private static final byte BYTE_1_MARKER = 0x10;
    private static final byte BYTE_2_MARKER = 0x20;
    private static final byte BYTE_3_MARKER = 0x30;
    private static final byte START_PIN_HI_CODE = (byte)0xF0;
    private static final byte START_PIN_LO_CODE = (byte)0xF1;
    private static final byte WRITE_COMMAND_CODE = (byte)0xF2;
    private static final byte WRITE_REGISTER_CODE = (byte)0xF3;
    private static final byte SET_CHANEL_DIVIDER_CODE = (byte)0xF4;
    private static final byte WRITE_SPS_CODE = (byte)0xF5;

    private ComPort comPort;
    
    private void write(byte code){
        comPort.writeToPort(new byte[]{code});
    }

    private void write(byte code, byte param){
        byte[] commandFrame = new byte[3];
        commandFrame[0] = (byte) (BYTE_0_MARKER + param >> 4);
        commandFrame[1] = (byte) (BYTE_1_MARKER + param & 0x0F);
        commandFrame[2] = code;
        comPort.writeToPort(commandFrame);
    }

    private void write(byte code, byte param1, byte param2){
        byte[] commandFrame = new byte[5];
        commandFrame[0] = (byte) (BYTE_0_MARKER + param1 >> 4);
        commandFrame[1] = (byte) (BYTE_1_MARKER + param1 & 0x0F);
        commandFrame[2] = (byte) (BYTE_2_MARKER + param2 >> 4);
        commandFrame[3] = (byte) (BYTE_3_MARKER + param2 & 0x0F);
        commandFrame[4] = code;
        comPort.writeToPort(commandFrame);
    }

    public void startPinHi(){
        write(START_PIN_HI_CODE);
    }
    
    public void startPinLo() {
        write(START_PIN_LO_CODE);
    }
    
    public void writeCommand(byte command){
        write(WRITE_COMMAND_CODE, command);
    }
    
    public void writeRegister(byte address, byte value){
        write(WRITE_REGISTER_CODE, address, value);
    }
    
    public void writeDividerForChannel(byte chanelNumber, byte divider){
        write(SET_CHANEL_DIVIDER_CODE, chanelNumber, divider);
    }

    public void writeSps(short sps){
        write(WRITE_SPS_CODE, (byte)((sps) >> 8), (byte)(sps & 0xFF));
    }
}
