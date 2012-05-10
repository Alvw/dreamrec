package com.github.dreamrec;

/**
 *
 */
public class ChainListView implements IListView<Integer> {
    IListView<Integer> firstListView;
    IListView<Integer> lastListView;
    int divider = 1;


    public ChainListView(IListView<Integer>... listViews) {
        firstListView = listViews[0];
        divider *= listViews[0].getDivider();
        lastListView = listViews[listViews.length-1];
        IListView<Integer> prevListView = firstListView;
        if (listViews.length > 1) {
            for (int i = 1; i < listViews.length; i++) {
                listViews[i].setInputDataList(prevListView);
                prevListView = listViews[i];
                divider *= listViews[i].getDivider();
            }
        }
    }

    @Override
    public void setInputDataList(IListView<Integer> inputDataList) {
        firstListView.setInputDataList(inputDataList);

    }


    @Override
    public int size() {
        return lastListView.size();
    }

    @Override
    public Integer get(int index) {
        return lastListView.get(index);

    }

    @Override
    public int getDivider() {
        return divider;
    }

    @Override
    public boolean isPositive() {
        return true;
    }
}

