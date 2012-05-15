package com.github.dreamrec;

/**
 *
 */
public enum Provider {
    DEBUG("Debug Provider"),
    EEG("EEG Provider");

    private String label;

    Provider(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
