package com.github.dreamrec;

/**
 *
 */
public interface IFilter<T> {
    public int size();
    public T get(int index);
    public int divider();
}
