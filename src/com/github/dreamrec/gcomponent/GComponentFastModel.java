package com.github.dreamrec.gcomponent;

import com.github.dreamrec.IFilter;
import com.github.dreamrec.Model;

/**
 *
 */
public class GComponentFastModel extends GComponentModel{

    public GComponentFastModel(Model model, IFilter<Integer> dataView) {
        super(model, dataView);
    }

    @Override
    public int getStartIndex() {
        return model.getFastGraphIndex();
    }

    @Override
    public double getFrequency() {
        return model.getFrequency();
    }

}
