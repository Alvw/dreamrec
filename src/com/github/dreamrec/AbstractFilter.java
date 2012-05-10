package com.github.dreamrec;

/**
 *
 */
public abstract class AbstractFilter<T> implements Filter {
    protected int divider = 1;
    protected final Filter<T> inputData;

    public AbstractFilter(Filter<T> inputData) {
        this.inputData = inputData;        
    }

    public final int divider(){
            return divider * inputData.divider();
    }
 
    public final int size() {
        return inputData.size()/divider;        
    }

 
    public final T get(int index) {
        checkIndexBounds(index);
        return doFilter(index);
    }
    
    private void checkIndexBounds(int index){
        if(index > size() || index < 0 ){
            throw  new IndexOutOfBoundsException("index:  "+index+",size:  "+size());
        }
    }
    
    protected abstract T doFilter(int index);
}
