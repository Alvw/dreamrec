package com.github.dreamrec;

/**
 *
 */
public interface Filter<T> {
    public int size();
    public T get(int index);
    public int divider();
}
