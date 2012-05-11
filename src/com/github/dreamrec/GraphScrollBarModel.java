package com.github.dreamrec;

/**
 *
 */
public interface GraphScrollBarModel {

    public int graphSize();
    public int graphIndex();
    public int screenSize();
    public void addModelUpdateListener(ModelUpdateListener listener);
}
