package com.github.dreamrec;

/**
 *
 */
public class LoPassFilter extends AbstractFilter<Short>{

    private int bufferSize;

    public LoPassFilter(int bufferSize, Filter<Short> inputData) {
        super(inputData);
        this.bufferSize = bufferSize;
    }

    @Override
    protected Short doFilter(int index) {
        int sum = 0;
        if(index<bufferSize-1){
            return inputData.get(index);
        }else {
            for (int i = 0; i < bufferSize; i++) {
                 sum+=inputData.get(index - i);
            }
            return (short)(sum/bufferSize);
        }
    }
}