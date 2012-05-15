package com.github.dreamrec;

/**
 *
 */
public enum Provider {
    DEBUG("Debug Provider"),
    EEG("EEG Provider");

    private String name;

    Provider(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
