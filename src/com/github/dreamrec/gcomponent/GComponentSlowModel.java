package com.github.dreamrec.gcomponent;

import com.github.dreamrec.AbstractFilter;
import com.github.dreamrec.Filter;
import com.github.dreamrec.Model;

/**
 *
 */
public class GComponentSlowModel extends GComponentModel implements ICursorPainterModel{


    public GComponentSlowModel(Model model, Filter<Short>... dataView) {
        super(model, dataView);
    }

    public int getCursorPosition() {
        return model.getCursorPosition();
    }

    public int getCursorWidth() {
        return getXSize()/Model.DIVIDER;
    }

    @Override
    public int getStartIndex() {
        return model.getSlowGraphIndex();
    }

    @Override
    public int getFrequency() {
        return model.getFrequency()/Model.DIVIDER;
    }

}
