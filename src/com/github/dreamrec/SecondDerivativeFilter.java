package com.github.dreamrec;

/**
 *
 */
public class SecondDerivativeFilter extends AbstractFilter<Short> {

    public SecondDerivativeFilter(Filter<Short> inputData) {
        super(inputData);
    }

    @Override
    protected Short doFilter(int index) {
        if (index < 8) {
            return 0;
        }
        int sum = inputData.get(index - 8) +
                inputData.get(index - 7) * 2 +
                inputData.get(index - 6) * 4 +
                inputData.get(index - 5) * 8 +
                inputData.get(index - 3) * 8 +
                inputData.get(index - 2) * 4 +
                inputData.get(index - 1) * 2 +
                inputData.get(index);
        return (short)(inputData.get(index - 4) - sum / 30);
    }
}