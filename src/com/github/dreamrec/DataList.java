package com.github.dreamrec;

import java.util.ArrayList;

/**
 *
 */
public class DataList<T> extends ArrayList<T> implements IFilter<T> {
    public int divider() {
        return 1;
    }
}
