package com.github.dreamrec.gcomponent;

/**
 *
 */
public class PositionedLabel {
    private final int position;
    private final String label;

    PositionedLabel(int position, String label) {
        this.position = position;
        this.label = label;
    }

    public int getPosition() {
        return position;
    }

    public String getLabel() {
        return label;
    }
}