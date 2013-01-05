package com.github.dreamrec.comport;

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

    private void write(int code){
        comPort.writeToPort(new byte[]{(byte)code});
    }

    private void write(int param, int code){
        byte[] commandFrame = new byte[3];
        commandFrame[0] = (byte) (BYTE_0_MARKER | (param >> 4));
        commandFrame[1] = (byte) (BYTE_1_MARKER | (param & 0x0F));
        commandFrame[2] = (byte)code;
        comPort.writeToPort(commandFrame);
    }

    private void write(int param1, int param2, int code){
        byte[] commandFrame = new byte[5];
        commandFrame[0] = (byte) (BYTE_0_MARKER | (param1 >> 4));
        commandFrame[1] = (byte) (BYTE_1_MARKER | (param1 & 0x0F));
        commandFrame[2] = (byte) (BYTE_2_MARKER | (param2 >> 4));
        commandFrame[3] = (byte) (BYTE_3_MARKER | (param2 & 0x0F));
        commandFrame[4] = (byte) code;

        comPort.writeToPort(commandFrame);
    }

    public void startPinHi(){
        write(START_PIN_HI_CODE);
    }
    
    public void startPinLo() {
        write(START_PIN_LO_CODE);
    }
    
    public void writeCommand(int command){
        write(command, WRITE_COMMAND_CODE);
    }
    
    public void writeRegister(int address, int value){
        write(address, value, WRITE_REGISTER_CODE);
    }
    
    public void writeDividerForChannel(int chanelNumber, int divider){
        write(chanelNumber, divider, SET_CHANEL_DIVIDER_CODE);
    }

    public void writeSps(short sps){
        write((byte) ((sps) >> 8), (byte) (sps & 0xFF), WRITE_SPS_CODE);
    }
}
