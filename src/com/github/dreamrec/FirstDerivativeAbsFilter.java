package com.github.dreamrec;


public class FirstDerivativeAbsFilter extends AbstractFilter<Short> {

    public FirstDerivativeAbsFilter(Filter<Short> inputData) {
        super(inputData);
    }

    @Override
    protected Short doFilter(int index) {
        if (index == 0) {
            return 0;
        }
        return (short)(Math.abs(inputData.get(index) - inputData.get(index - 1)));
    }
}
