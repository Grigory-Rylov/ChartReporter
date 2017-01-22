package com.grishberg.graphreporter.ui.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.grishberg.graphreporter.R;
import com.grishberg.graphreporter.data.enums.ChartMode;
import com.grishberg.graphreporter.data.enums.ChartPeriod;
import com.grishberg.graphreporter.data.model.DualChartContainer;
import com.grishberg.graphreporter.di.DiManager;
import com.grishberg.graphreporter.mvp.presenter.CandlesChartPresenter;
import com.grishberg.graphreporter.mvp.view.CandlesChartView;
import com.grishberg.graphreporter.ui.dialogs.PeriodSelectDialog;
import com.grishberg.graphreporter.ui.view.CombinedChartInitiable;
import com.grishberg.graphreporter.utils.ColorUtil;
import com.grishberg.graphreporter.utils.LogService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import static com.github.mikephil.charting.charts.CombinedChart.DrawOrder.CANDLE;
import static com.github.mikephil.charting.charts.CombinedChart.DrawOrder.LINE;

public class CandleFragment extends MvpAppCompatFragment implements CandlesChartView {
    private static final String TAG = CandleFragment.class.getSimpleName();
    private static final String ARG_PRODUCT_ID = "ARG_PRODUCT_ID";
    @Inject
    LogService log;

    @InjectPresenter
    CandlesChartPresenter presenter;

    private CombinedChartInitiable chart;
    private ProgressBar progressBar;
    private List<Long> dates;
    private long productId;
    private ChartPeriod period = ChartPeriod.DAY;
    private ChartMode currentChartMode = ChartMode.CHART_MODE;

    public CandleFragment() {
        // Required empty public constructor
    }

    public static CandleFragment newInstance(final long productId) {
        final CandleFragment fragment = new CandleFragment();
        final Bundle args = new Bundle();
        args.putLong(ARG_PRODUCT_ID, productId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DiManager.getAppComponent().inject(this);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            productId = getArguments().getLong(ARG_PRODUCT_ID);
        }
        if (savedInstanceState == null) {
            presenter.recalculatePeriod(productId, currentChartMode, period);
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_candle, container, false);
        initProgressBar(view);
        initChart(view);
        return view;
    }

    private void initProgressBar(final View view) {
        progressBar = (ProgressBar) view.findViewById(R.id.fragment_candle_progress_bar);
    }

    private void initChart(final View view) {

        chart = (CombinedChartInitiable) view.findViewById(R.id.fragment_candle_chart);

        initDualChartAxes(ChartPeriod.DAY);
    }

    @Override
    public void showChart(final DualChartContainer response) {
        if (response == null || response.getChartMode() == null) {
            log.e(TAG, "response is null");
            return;
        }
        period = response.getPeriod();
        initDualChartAxes(period);
        final CombinedData combinedData = new CombinedData();

        switch (response.getChartMode()) {
            case LINE_MODE:
                dates = response.getEntryResponse().getDates();
                combinedData.setData(generateLineData(response.getEntryResponse().getEntries()));
                break;

            case CHART_MODE:
                dates = response.getCandleResponse().getDates();
                combinedData.setData(generateCandleData(response.getCandleResponse().getEntries()));
                break;

            default:
                dates = response.getCandleResponse().getDates();
                combinedData.setData(generateLineData(response.getEntryResponse().getEntries()));
                combinedData.setData(generateCandleData(response.getCandleResponse().getEntries()));
                break;
        }

        chart.setData(combinedData);
        chart.invalidate();
    }

    private void initDualChartAxes(final ChartPeriod period) {

        chart.setDoubleTapToZoomEnabled(false);
        chart.setScaleYEnabled(false);
        chart.setBackgroundColor(Color.WHITE);
        chart.getDescription().setEnabled(false);
        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        chart.setMaxVisibleValueCount(60);
        // scaling can now only be done on x- and y-axis separately
        chart.setPinchZoom(false);
        chart.setDrawGridBackground(false);
        chart.getLegend().setEnabled(false);

        // draw bars behind lines
        chart.setDrawOrder(new CombinedChart.DrawOrder[]{CANDLE, LINE});

        final Legend legend = chart.getLegend();
        legend.setWordWrapEnabled(true);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);

        final XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(true);
        xAxis.setGranularity(1f * period.getPeriod());
        xAxis.setValueFormatter(new IAxisValueFormatter() {

            private final SimpleDateFormat mFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.US);

            @Override
            public String getFormattedValue(final float value, final AxisBase axis) {
                final int index = (int) value;
                if (index >= dates.size()) {
                    log.e(TAG, "index " + index + " > dates.size " + dates.size() + " for axis " + axis);
                    return "";
                }
                return mFormat.format(new Date(dates.get(index)));
            }
        });

        final YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setLabelCount(5, false);
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawAxisLine(false);

        final YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);
    }

    private LineData generateLineData(final List<Entry> entries) {

        final LineData lineData = new LineData();

        final LineDataSet set = new LineDataSet(entries, "Line DataSet");
        set.setColor(ColorUtil.getColor(getContext(), R.color.line_color));
        set.setLineWidth(2f);
        set.setCircleColor(ColorUtil.getColor(getContext(), R.color.line_color));
        set.setCircleRadius(2f);
        set.setFillColor(ColorUtil.getColor(getContext(), R.color.line_color));
        set.setMode(LineDataSet.Mode.LINEAR);
        set.setDrawValues(false);
        set.setValueTextSize(10f);
        set.setValueTextColor(ColorUtil.getColor(getContext(), R.color.line_color));

        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineData.addDataSet(set);

        return lineData;
    }

    protected CandleData generateCandleData(final List<CandleEntry> entries) {

        final CandleData candleData = new CandleData();

        final CandleDataSet candleDataSet = new CandleDataSet(entries, "Candle DataSet");
        candleDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        candleDataSet.setShadowColor(Color.DKGRAY);
        candleDataSet.setShadowWidth(0.7f);
        candleDataSet.setDecreasingColor(ColorUtil.getColor(getContext(), R.color.candle_decreasing_color));
        candleDataSet.setDecreasingPaintStyle(Paint.Style.FILL);
        candleDataSet.setIncreasingColor(ColorUtil.getColor(getContext(), R.color.candle_increasing_color));
        candleDataSet.setIncreasingPaintStyle(Paint.Style.FILL);
        candleDataSet.setNeutralColor(ColorUtil.getColor(getContext(), R.color.candle_neutral_color));
        candleDataSet.setBarSpace(0f);
        candleData.addDataSet(candleDataSet);
        return candleData;
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        chart.setVisibility(View.GONE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
        chart.setVisibility(View.VISIBLE);
    }

    @Override
    public void showFail(final String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showEmptyDataError() {
        showFail(getString(R.string.candle_screen_empty_data));
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode,
                                 final Intent data) {
        // Stuff to do, dependent on requestCode and resultCode
        if (requestCode == PeriodSelectDialog.PERIOD_SELECT_RESULT_CODE) {
            // This is the return result of your DialogFragment
            presenter.recalculatePeriod(productId, currentChartMode, ChartPeriod.values()[resultCode]);
        }
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
        inflater.inflate(R.menu.candle_chart_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.change_period:
                PeriodSelectDialog.showDialog(getActivity().getSupportFragmentManager(),
                        this,
                        period.ordinal());
                return true;

            case R.id.candle_chart_mode:
                currentChartMode = ChartMode.CHART_MODE;
                presenter.recalculatePeriod(productId, currentChartMode, period);
                return true;

            case R.id.line_chart_mode:
                currentChartMode = ChartMode.LINE_MODE;
                presenter.recalculatePeriod(productId, currentChartMode, period);
                return true;

            case R.id.candle_and_line_mode:
                currentChartMode = ChartMode.CANDLE_AND_LINE_MODE;
                presenter.recalculatePeriod(productId, currentChartMode, period);
                return true;

            default:
                return false;
        }
    }
}
