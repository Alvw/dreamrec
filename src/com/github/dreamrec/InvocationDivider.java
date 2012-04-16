package com.github.dreamrec;

/**
 *
 */
public abstract class InvocationDivider{

    private int invocationCounter;
    private int divider;

    public InvocationDivider(int divider) {
        this.divider = divider;
    }

    public void count(){
        invocationCounter++;
        if(invocationCounter % divider == 0){
            invoke();
        }
    }

    protected abstract void invoke();
}
