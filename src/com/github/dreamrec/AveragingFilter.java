package com.github.dreamrec;

/**
 *
 */
public class AveragingFilter extends AbstractFilter<Integer> {

    public AveragingFilter(Filter<Integer> inputData, int divider) {
        super(inputData);
        this.divider = divider;
    }

    @Override
    protected Integer doFilter(int index) {
        int sum = 0;
        int incomingDataIndex = index*divider;
        for (int i = 0; i < divider; i++) {
            sum += inputData.get(incomingDataIndex+i);

        }
        return sum/divider;
    }
}
