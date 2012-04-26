package com.github.dreamrec;

/**
 *
 */
public interface IListView<T> {

    public int size();

    public T get(int index);
    
    public int getDivider();

    public boolean isPositive();

    public void setInputDataList(IListView<T> inputDataList);

}
