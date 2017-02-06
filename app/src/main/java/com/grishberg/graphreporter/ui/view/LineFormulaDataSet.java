package com.grishberg.graphreporter.ui.view;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.List;

/**
 * Created by grishberg on 06.02.17.
 */
public class LineFormulaDataSet extends LineDataSet {

    public LineFormulaDataSet(final List<Entry> yVals, final String label) {
        super(yVals, label);
    }
}
