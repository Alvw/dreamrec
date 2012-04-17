package com.github.dreamrec;


public class ListViewChain<T> implements IListView<T>{
    ListView<T> lastView;
    int divider;

    public int size() {
        return lastView.size();
    }

    public T get(int index) {
        return lastView.get(index);
    }

    public int getTotalDivider() {
        return divider;
    }
    
    public void addView(ListView<T> listView){

        
    }
}


