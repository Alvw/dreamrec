package com.github.dreamrec;


public class FirstDerivativeAbsFilter extends AbstractFilter<Short> {

    private int averagingBuffer = 1;

    public FirstDerivativeAbsFilter(Filter<Short> inputData, int averagingBuffer) {
        super(inputData);
        this.averagingBuffer = averagingBuffer;
    }

    @Override
    protected Short doFilter(int index) {
        throw new UnsupportedOperationException("todo");
    }
}
