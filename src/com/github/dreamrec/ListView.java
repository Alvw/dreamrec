package com.github.dreamrec;

import java.util.ArrayList;

/**
 *
 */
public class ListView<T> extends ArrayList<T> implements IListView<T>{
    @Override
    public int getDivider() {
        return 1;
    }

    @Override
    public boolean isPositive() {
        return false;
    }

    @Override
    public void setInputDataList(IListView<T> inputDataList) {

    }
}
