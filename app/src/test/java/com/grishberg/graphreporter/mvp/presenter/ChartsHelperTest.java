package com.grishberg.graphreporter.mvp.presenter;

import android.support.annotation.NonNull;

import com.github.mikephil.charting.data.Entry;
import com.grishberg.graphreporter.data.enums.ChartPeriod;
import com.grishberg.graphreporter.data.model.ChartResponseContainer;
import com.grishberg.graphreporter.data.model.DailyValue;
import com.grishberg.graphreporter.data.model.FormulaChartContainer;
import com.grishberg.graphreporter.data.model.FormulaContainer;
import com.grishberg.graphreporter.utils.DateTimeUtils;
import com.grishberg.graphreporter.utils.ValuesRepository;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.grishberg.graphreporter.utils.DateTimeUtils.getDate;
import static com.grishberg.graphreporter.utils.ValuesRepository.getDailyValues;
import static org.junit.Assert.*;

/**
 * Created by grishberg on 31.01.17.
 */
public class ChartsHelperTest {
    @Test
    public void getFormulaDataForPeriod() throws Exception {
        final List<DailyValue> dailyValues = getDailyValues();

        final FormulaContainer formulaContainer = new FormulaContainer("test",
                1,
                7D, true, 0xff,
                7D, true, 0xff);
        final ChartsHelper chartsHelper = new ChartsHelper();
        final FormulaChartContainer chart = chartsHelper.getFormulaDataForPeriod(ChartPeriod.DAY,
                dailyValues,
                formulaContainer,
                true);
        assertEquals(1, chart.getGrowPoints().size());
        assertEquals(1, chart.getFallPoints().size());
    }

}