package com.github.dreamrec;


public class FirstDerivativeAbsFilter extends AbstractFilter<Integer> {

    private int averagingBuffer = 1;

    public FirstDerivativeAbsFilter(Filter<Integer> inputData, int averagingBuffer) {
        super(inputData);
        this.averagingBuffer = averagingBuffer;
    }

    @Override
    protected Integer doFilter(int index) {
        if (index < averagingBuffer) {
            return 0;
        }
        int averageValue = 0;
        for (int i = 0; i < averagingBuffer; i++) {
              averageValue+= inputData.get(index - i);
        }
        averageValue/=averagingBuffer;
        return inputData.get(index) - averageValue;
    }
}
