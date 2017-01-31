package com.grishberg.graphreporter.ui.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.grishberg.graphreporter.R;
import com.grishberg.graphreporter.data.enums.ChartPeriod;
import com.grishberg.graphreporter.data.model.DualChartContainer;
import com.grishberg.graphreporter.data.model.FormulaChartContainer;
import com.grishberg.graphreporter.data.model.FormulaContainer;
import com.grishberg.graphreporter.di.DiManager;
import com.grishberg.graphreporter.mvp.presenter.CandlesChartPresenter;
import com.grishberg.graphreporter.mvp.view.CandlesChartView;
import com.grishberg.graphreporter.ui.dialogs.NewPointDialog;
import com.grishberg.graphreporter.ui.view.CombinedChartInitiable;
import com.grishberg.graphreporter.ui.view.PeriodSelectorView;
import com.grishberg.graphreporter.utils.ColorUtil;
import com.grishberg.graphreporter.utils.LogService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import static com.github.mikephil.charting.charts.CombinedChart.DrawOrder.CANDLE;
import static com.github.mikephil.charting.charts.CombinedChart.DrawOrder.LINE;

public class CandleFragment extends MvpAppCompatFragment implements CandlesChartView, PeriodSelectorView.OnPeriodChangeListener, View.OnClickListener {
    private static final String TAG = CandleFragment.class.getSimpleName();
    private static final String ARG_PRODUCT_ID = "ARG_PRODUCT_ID";
    private final SimpleDateFormat SIMPLE_DATE_FORMAT_DATE = new SimpleDateFormat("dd.MM.yy", Locale.US);
    @Inject
    LogService log;

    @InjectPresenter
    CandlesChartPresenter presenter;
    private ImageButton addFormulaButton;

    private CombinedChartInitiable chart;
    private ProgressBar progressBar;
    private PeriodSelectorView periodSelector;
    private List<Long> dates;
    private long productId;

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
            presenter.onSelectedProduct(productId);
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
        addFormulaButton = (ImageButton) view.findViewById(R.id.fragment_candle_add_formula_button);
        addFormulaButton.setOnClickListener(this);
        return view;
    }

    private void initPeriodSelector(final View view) {
        periodSelector = (PeriodSelectorView) view.findViewById(R.id.fragment_candle_period_selector);
        periodSelector.setOnPeriodChangeListener(this);
        periodSelector.setMode(PeriodSelectorView.Mode.DAY);
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
        initDualChartAxes(response.getPeriod());
        final CombinedData combinedData = new CombinedData();

        switch (response.getChartMode()) {
            case LINE_MODE:
                dates = response.getEntryResponse().getDates();
                combinedData.setData(generateLineData(response.getEntryResponse().getEntries()));
                break;

            case CANDLE_MODE:
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

        final ChartTouchListener<CombinedChartInitiable> chartTouchListener = new ChartTouchListener<CombinedChartInitiable>(chart) {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {

                final Entry entryByTouchPoint = chart.getEntryByTouchPoint(event.getX(), event.getY());
                Toast.makeText(getContext(), String.format(Locale.US, "%f", entryByTouchPoint.getY()), Toast.LENGTH_SHORT).show();
                return false;
            }
        };
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
        chart.setOnTouchListener(chartTouchListener);

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
        xAxis.setValueFormatter(new IAxisValueFormatter() {

            @Override
            public String getFormattedValue(final float value, final AxisBase axis) {
                final int index = (int) value;
                if (index >= dates.size()) {
                    log.e(TAG, "index " + index + " > dates.size " + dates.size() + " for axis " + axis);
                    return "";
                }
                final Date date = new Date(dates.get(index));
                return SIMPLE_DATE_FORMAT_DATE.format(date);
            }
        });

        final YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setLabelCount(5, false);
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawAxisLine(false);
        leftAxis.setGranularity(1.0f);

        final YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);
    }

    private LineData generateLineData(final List<Entry> entries) {

        final LineData lineData = new LineData();

        final LineDataSet linesSet = new LineDataSet(entries, "Line DataSet");
        linesSet.setColor(ColorUtil.getColor(getContext(), R.color.line_color));
        linesSet.setDrawCircles(false);
        linesSet.setLineWidth(2f);
        linesSet.setCircleColor(ColorUtil.getColor(getContext(), R.color.line_color));
        linesSet.setCircleRadius(1f);
        linesSet.setFillColor(ColorUtil.getColor(getContext(), R.color.line_color));
        linesSet.setMode(LineDataSet.Mode.LINEAR);
        linesSet.setDrawValues(false);
        linesSet.setValueTextSize(10f);
        linesSet.setValueTextColor(ColorUtil.getColor(getContext(), R.color.line_color));
        linesSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineData.addDataSet(linesSet);

        return lineData;
    }

    protected CandleData generateCandleData(final List<CandleEntry> entries) {

        final CandleData candleData = new CandleData();

        final CandleDataSet candleDataSet = new CandleDataSet(entries, "Candle DataSet");
        candleDataSet.setDrawValues(false);
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
    public void formulaPoints(final FormulaChartContainer response) {

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

    /**
     * Клик по кнопке добавления точки
     */
    @Override
    public void onClick(final View view) {
        NewPointDialog.showDialog(getFragmentManager(), this);
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NewPointDialog.NEW_POINT_RESULT_CODE) {
            final FormulaContainer formulaContainer = NewPointDialog.getResult(data);
            presenter.addNewFormula(formulaContainer);
        }
    }
}
