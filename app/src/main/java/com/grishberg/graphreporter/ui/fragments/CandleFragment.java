package com.grishberg.graphreporter.ui.fragments;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
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
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.grishberg.graphreporter.R;
import com.grishberg.graphreporter.data.enums.ChartPeriod;
import com.grishberg.graphreporter.data.beans.DualChartContainer;
import com.grishberg.graphreporter.data.beans.FormulaChartContainer;
import com.grishberg.graphreporter.data.beans.FormulaContainer;
import com.grishberg.graphreporter.data.beans.ProductItem;
import com.grishberg.graphreporter.data.rest.RestConst;
import com.grishberg.graphreporter.di.DiManager;
import com.grishberg.graphreporter.mvp.presenter.CandlesChartPresenter;
import com.grishberg.graphreporter.mvp.view.CandlesChartView;
import com.grishberg.graphreporter.ui.activities.FormulaSettingsActivity;
import com.grishberg.graphreporter.ui.view.CombinedChartInitiable;
import com.grishberg.graphreporter.ui.view.LineFormulaDataSet;
import com.grishberg.graphreporter.ui.view.PeriodSelectorView;
import com.grishberg.graphreporter.ui.view.PointInfoView;
import com.grishberg.graphreporter.utils.ColorUtil;
import com.grishberg.graphreporter.utils.LogService;
import com.grishberg.graphreporter.utils.XAxisValueToDateFormatter;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import static com.github.mikephil.charting.charts.CombinedChart.DrawOrder.CANDLE;
import static com.github.mikephil.charting.charts.CombinedChart.DrawOrder.LINE;

public class CandleFragment extends MvpAppCompatFragment implements CandlesChartView, PeriodSelectorView.OnPeriodChangeListener {
    public static final float FORMULA_POINT_RADIUS = 3f;
    public static final float LEFT_BOUNDS = 0.0001F;
    public static final int FORMULA_SETTINGS_REQUEST_CODE = 1000;
    private static final String TAG = CandleFragment.class.getSimpleName();
    private static final String ARG_PRODUCT = "ARG_PRODUCT";
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy", Locale.US);

    @Inject
    LogService log;

    @InjectPresenter
    CandlesChartPresenter presenter;

    private XAxisValueToDateFormatter dateFormatter;

    private CombinedChartInitiable chart;
    private ProgressBar progressBar;
    private ProductItem productItem;
    private CombinedData combinedData;
    private LineData lineData;
    private PointInfoView pointInfoView;

    public CandleFragment() {
        // Required empty public constructor
    }

    public static CandleFragment newInstance(final ProductItem productItem) {
        final CandleFragment fragment = new CandleFragment();
        final Bundle args = new Bundle();
        args.putSerializable(ARG_PRODUCT, productItem);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DiManager.getAppComponent().inject(this);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            productItem = (ProductItem) getArguments().getSerializable(ARG_PRODUCT);
            Log.d(TAG, "onCreate: productItem " + productItem);
        }
        if (productItem != null && savedInstanceState == null) {
            presenter.onInitChartScreen(productItem.getId());
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
        initPeriodSelector(view);
        pointInfoView = (PointInfoView) view.findViewById(R.id.fragment_candle_point_info);
        return view;
    }

    private void initPeriodSelector(final View view) {
        final PeriodSelectorView periodSelector = (PeriodSelectorView) view.findViewById(R.id.fragment_candle_period_selector);
        periodSelector.setOnPeriodChangeListener(this);
    }

    private void initProgressBar(final View view) {
        progressBar = (ProgressBar) view.findViewById(R.id.fragment_candle_progress_bar);
    }

    private void initChart(final View view) {

        chart = (CombinedChartInitiable) view.findViewById(R.id.fragment_candle_chart);
        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(final Entry e, final Highlight h) {
                presenter.onChartValueSelected(e);
            }

            @Override
            public void onNothingSelected() {
                presenter.onNothingSelected();
            }
        });

        initDualChartAxes(ChartPeriod.DAY);
    }

    @Override
    public void showChart(final DualChartContainer response, final XAxisValueToDateFormatter dateFormatter) {
        if (response == null || response.getChartMode() == null) {
            log.e(TAG, "response is null");
            return;
        }
        this.dateFormatter = dateFormatter;
        initDualChartAxes(response.getPeriod());

        combinedData = new CombinedData();
        lineData = new LineData();

        switch (response.getChartMode()) {
            case LINE_MODE:
                lineData.addDataSet(generateLineData(response.getEntryResponse().getEntries()));
                combinedData.setData(lineData);
                break;

            case CANDLE_MODE:
                combinedData.setData(generateCandleData(response.getCandleResponse().getEntries()));
                break;

            default:
                // добавить данные для линий
                lineData.addDataSet(generateLineData(response.getEntryResponse().getEntries()));
                combinedData.setData(lineData);
                // добавить данные для свечей
                combinedData.setData(generateCandleData(response.getCandleResponse().getEntries()));
                break;
        }
        combinedData.notifyDataChanged();

        chart.setData(combinedData);
        chart.setVisibleXRangeMaximum(RestConst.MAX_POINTS_PER_SCREEN);

        chart.moveViewToX(combinedData.getEntryCount());
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
        xAxis.setGranularity(1f * period.getPeriod() / (60 * 24f));
        xAxis.setValueFormatter((value, axis) -> dateFormatter.getDateAsString(value, dateFormat));
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(true);
        xAxis.setCenterAxisLabels(true);

        final YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(true);
        rightAxis.setLabelCount(10, false);
        rightAxis.setDrawGridLines(true);
        rightAxis.setDrawAxisLine(true);
        rightAxis.setGranularity(period.getPartion());
        rightAxis.setDrawTopYLabelEntry(true);

        final YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setEnabled(false);
    }

    private LineDataSet generateLineData(final List<Entry> entries) {

        final LineDataSet linesSet = new LineDataSet(entries, "Line DataSet");
        linesSet.setColor(ColorUtil.getColor(getContext(), R.color.line_color));
        linesSet.setDrawCircles(false);
        linesSet.setLineWidth(1f);
        linesSet.setCircleColor(ColorUtil.getColor(getContext(), R.color.line_color));
        linesSet.setCircleRadius(1f);
        linesSet.setFillColor(ColorUtil.getColor(getContext(), R.color.line_color));
        linesSet.setMode(LineDataSet.Mode.LINEAR);
        linesSet.setDrawValues(false);
        linesSet.setValueTextSize(10f);
        linesSet.setValueTextColor(ColorUtil.getColor(getContext(), R.color.line_color));
        linesSet.setAxisDependency(YAxis.AxisDependency.RIGHT);

        return linesSet;
    }

    protected CandleData generateCandleData(final List<CandleEntry> entries) {

        final CandleData candleData = new CandleData();

        final CandleDataSet candleDataSet = new CandleDataSet(entries, "Candle DataSet");
        candleDataSet.setDrawValues(false);
        candleDataSet.setAxisDependency(YAxis.AxisDependency.RIGHT);
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
    public void formulaPoints(final FormulaChartContainer response) {
        lineData.addDataSet(generateFormulaGrowData(response.getGrowPoints(), response.getFormulaContainer()));
        lineData.addDataSet(generateFormulaFallData(response.getFallPoints(), response.getFormulaContainer()));
        combinedData.setData(lineData);
        chart.setData(combinedData);
        chart.invalidate();
    }

    private LineFormulaDataSet generateFormulaGrowData(final List<Entry> entries,
                                                       final FormulaContainer formulaContainer) {

        final LineFormulaDataSet linesSet = new LineFormulaDataSet(entries, "Line Grow DataSet");
        linesSet.setDrawCircles(true);
        linesSet.setCircleColor(formulaContainer.getGrowColor());
        linesSet.setCircleRadius(FORMULA_POINT_RADIUS);
        linesSet.setFillAlpha(0);
        linesSet.setMode(LineDataSet.Mode.LINEAR);
        linesSet.setDrawValues(false);
        linesSet.setValueTextSize(10f);
        linesSet.setValueTextColor(formulaContainer.getGrowColor());
        linesSet.setAxisDependency(YAxis.AxisDependency.RIGHT);
        linesSet.setCircleHoleRadius(1);
        linesSet.setDrawCircleHole(true);

        return linesSet;
    }

    private LineFormulaDataSet generateFormulaFallData(final List<Entry> entries,
                                                       final FormulaContainer formulaContainer) {

        final LineFormulaDataSet linesSet = new LineFormulaDataSet(entries, "Line Fall DataSet");
        linesSet.setDrawCircles(true);
        linesSet.setCircleColor(formulaContainer.getFallColor());
        linesSet.setCircleRadius(FORMULA_POINT_RADIUS);
        linesSet.setFillAlpha(0);
        linesSet.setMode(LineDataSet.Mode.LINEAR);
        linesSet.setDrawValues(false);
        linesSet.setValueTextSize(10f);
        linesSet.setValueTextColor(formulaContainer.getFallColor());
        linesSet.setAxisDependency(YAxis.AxisDependency.RIGHT);
        linesSet.setCircleHoleRadius(1);
        linesSet.setDrawCircleHole(true);

        return linesSet;
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

    public void onCandlesModeClicked() {
        presenter.onCandlesModeClicked();
    }

    public void onLinesModeClicked() {
        presenter.onLinesModeClicked();
    }

    public void onCandlesAndLinesMode() {
        presenter.onCandlesAndLinesMode();
    }

    @Override
    public void onPeriodChanged(final ChartPeriod selectedPeriod) {
        presenter.onPeriodChanged(selectedPeriod);
    }

    @Override
    public void showPointInfo(final float open,
                              final float high,
                              final float low,
                              final float close,
                              final String date) {
        pointInfoView.setValue(open, high, low, close, date);
        pointInfoView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showPointInfo(final float y, final String date) {
        pointInfoView.setValue(y, date);
        pointInfoView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hidePointInfo() {
        pointInfoView.setVisibility(View.GONE);
    }

    //---- menu -----

    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.candle_chart_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getItemId() == R.id.action_save) {
            presenter.onSaveSettingsClicked();
        } else if (item.getItemId() == R.id.action_formula_settings) {
            presenter.onShowFormulaSettings();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showSavingMessage() {
        Toast.makeText(getContext(), R.string.chart_saving_message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showFormulaSettingsScreen(final long currentProductId) {
        FormulaSettingsActivity.startForResult(getActivity(), currentProductId, FORMULA_SETTINGS_REQUEST_CODE);
    }

    public void onFormulaSettingsClosed() {
        presenter.onFormulaSettingsClosed();
    }
}
