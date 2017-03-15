package com.grishberg.graphreporter.data.beans;

import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.grishberg.graphreporter.data.enums.ChartMode;
import com.grishberg.graphreporter.data.enums.ChartPeriod;

/**
 * Created by grishberg on 19.01.17.
 * Контейнер для свечей и линейного графика
 */
public class DualChartContainer {
    private ChartMode chartMode;
    private ChartPeriod period;

    private ChartResponseContainer<Entry> entryResponse;
    private ChartResponseContainer<CandleEntry> candleResponse;

    private DualChartContainer() {
    }

    public static DualChartContainer makeCandleAndLine(final ChartPeriod period,
                                                       final ChartResponseContainer<Entry> entryResponse,
                                                       final ChartResponseContainer<CandleEntry> candleResponse) {
        final DualChartContainer container = new DualChartContainer();
        container.period = period;
        container.chartMode = ChartMode.CANDLE_AND_LINE_MODE;
        container.entryResponse = entryResponse;
        container.candleResponse = candleResponse;
        return container;
    }

    public static DualChartContainer makeLine(final ChartPeriod period,
                                              final ChartResponseContainer<Entry> entryResponse) {
        final DualChartContainer container = new DualChartContainer();
        container.period = period;
        container.chartMode = ChartMode.LINE_MODE;
        container.entryResponse = entryResponse;
        container.candleResponse = null;
        return container;
    }

    public static DualChartContainer makeCandle(final ChartPeriod period,
                                                final ChartResponseContainer<CandleEntry> candleResponse) {
        final DualChartContainer container = new DualChartContainer();
        container.period = period;
        container.chartMode = ChartMode.CANDLE_MODE;
        container.entryResponse = null;
        container.candleResponse = candleResponse;
        return container;
    }

    public ChartMode getChartMode() {
        return chartMode;
    }

    public ChartResponseContainer<Entry> getEntryResponse() {
        return entryResponse;
    }

    public ChartResponseContainer<CandleEntry> getCandleResponse() {
        return candleResponse;
    }

    public ChartPeriod getPeriod() {
        return period;
    }
}