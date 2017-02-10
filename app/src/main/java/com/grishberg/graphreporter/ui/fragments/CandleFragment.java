package com.grishberg.graphreporter.ui.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
import com.grishberg.graphreporter.data.model.DualChartContainer;
import com.grishberg.graphreporter.data.model.FormulaChartContainer;
import com.grishberg.graphreporter.data.model.FormulaContainer;
import com.grishberg.graphreporter.data.model.ProductItem;
import com.grishberg.graphreporter.di.DiManager;
import com.grishberg.graphreporter.mvp.presenter.CandlesChartPresenter;
import com.grishberg.graphreporter.mvp.view.CandlesChartView;
import com.grishberg.graphreporter.ui.dialogs.NewPointDialog;
import com.grishberg.graphreporter.ui.view.CombinedChartInitiable;
import com.grishberg.graphreporter.ui.view.LineFormulaDataSet;
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
    public static final float FORMULA_POINT_RADIUS = 3f;
    public static final int MAX_X_RANGE = 1000;
    private static final String TAG = CandleFragment.class.getSimpleName();
    private static final String ARG_PRODUCT = "ARG_PRODUCT";
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy", Locale.US);
    @Inject
    LogService log;

    @InjectPresenter
    CandlesChartPresenter presenter;

    private CombinedChartInitiable chart;
    private ProgressBar progressBar;
    private List<Long> dates;
    private ProductItem productItem;
    private CombinedData combinedData;
    private LineData lineData;

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
        }
        if (savedInstanceState == null) {
            presenter.onSelectedProduct(productItem.getId());
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
        final ImageButton addFormulaButton = (ImageButton) view.findViewById(R.id.fragment_candle_add_formula_button);
        addFormulaButton.setOnClickListener(this);
        return view;
    }

    private void initPeriodSelector(final View view) {
        final PeriodSelectorView periodSelector = (PeriodSelectorView) view.findViewById(R.id.fragment_candle_period_selector);
        periodSelector.setOnPeriodChangeListener(this);
        periodSelector.setMode(PeriodSelectorView.Mode.DAY);
    }

    private void initProgressBar(final View view) {
        progressBar = (ProgressBar) view.findViewById(R.id.fragment_candle_progress_bar);
    }

    private void initChart(final View view) {

        chart = (CombinedChartInitiable) view.findViewById(R.id.fragment_candle_chart);
        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(final Entry e, final Highlight h) {
                changeTitleValue(e);
            }

            @Override
            public void onNothingSelected() {
                resetTitleValue();
            }
        });
        initDualChartAxes(ChartPeriod.DAY);
    }

    private void resetTitleValue() {
        getActivity().setTitle(productItem.getName());
    }

    private void changeTitleValue(final Entry entry) {
        getActivity().setTitle(String.format(Locale.US, "%s : %f", productItem.getName(), entry.getY()));
    }

    @Override
    public void showChart(final DualChartContainer response) {
        if (response == null || response.getChartMode() == null) {
            log.e(TAG, "response is null");
            return;
        }
        initDualChartAxes(response.getPeriod());

        combinedData = new CombinedData();
        lineData = new LineData();

        switch (response.getChartMode()) {
            case LINE_MODE:
                dates = response.getEntryResponse().getDates();
                lineData.addDataSet(generateLineData(response.getEntryResponse().getEntries()));
                combinedData.setData(lineData);
                break;

            case CANDLE_MODE:
                dates = response.getCandleResponse().getDates();
                combinedData.setData(generateCandleData(response.getCandleResponse().getEntries()));
                break;

            default:
                dates = response.getCandleResponse().getDates();
                // добавить данные для линий
                lineData.addDataSet(generateLineData(response.getEntryResponse().getEntries()));
                combinedData.setData(lineData);
                // добавить данные для свечей
                combinedData.setData(generateCandleData(response.getCandleResponse().getEntries()));
                break;
        }
        combinedData.notifyDataChanged();

        chart.setData(combinedData);
        chart.setVisibleXRangeMaximum(MAX_X_RANGE);

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
        xAxis.setValueFormatter((value, axis) -> {
            final int index = (int) value;
            if (index >= dates.size() || index < 0) {
                log.e(TAG, "index " + index + " > dates.size " + dates.size() + " for axis " + axis);
                return "";
            }
            final Date date = new Date(dates.get(index));
            return dateFormat.format(date);
        });

        final YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setLabelCount(5, false);
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawAxisLine(false);
        leftAxis.setGranularity(1.0f);

        final YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);
    }

    private LineDataSet generateLineData(final List<Entry> entries) {

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

        return linesSet;
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
        linesSet.setAxisDependency(YAxis.AxisDependency.LEFT);
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
        linesSet.setAxisDependency(YAxis.AxisDependency.LEFT);
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
