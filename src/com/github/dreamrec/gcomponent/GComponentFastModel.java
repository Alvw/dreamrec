package com.github.dreamrec.gcomponent;

import com.github.dreamrec.IListView;
import com.github.dreamrec.Model;

/**
 *
 */
public class GComponentFastModel extends GComponentModel{

    public GComponentFastModel(Model model, IListView<Integer> dataView) {
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
