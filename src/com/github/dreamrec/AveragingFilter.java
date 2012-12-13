package com.github.dreamrec;

/**
 *
 */
public class AveragingFilter extends AbstractFilter<Short> {

    public AveragingFilter(Filter<Short> inputData, int divider) {
        super(inputData);
        this.divider = divider;
    }

    @Override
    protected Short doFilter(int index) {
        int sum = 0;
        int incomingDataIndex = index*divider;
        for (int i = 0; i < divider; i++) {
            sum += Math.abs(inputData.get(incomingDataIndex+i));

        }
        return (short)(sum*100/divider);
    }
}
