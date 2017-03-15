package com.grishberg.graphreporter.mvp.presenter;

import com.grishberg.graphreporter.data.enums.ChartPeriod;
import com.grishberg.graphreporter.data.beans.DailyValue;
import com.grishberg.graphreporter.data.beans.FormulaChartContainer;
import com.grishberg.graphreporter.data.beans.FormulaContainer;

import org.junit.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

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
                formulaContainer);
        assertEquals(1, chart.getGrowPoints().size());
        assertEquals(1, chart.getFallPoints().size());
    }

    @Test
    public void testTimePacking() {
        final long now = 1452211200L;
        final float value = TimeUnit.MILLISECONDS.toMinutes(now * 1000);
        final long convertedTime = TimeUnit.MINUTES.toMillis((long) (value)) / 1000;
        assertEquals(now, convertedTime);
    }
}