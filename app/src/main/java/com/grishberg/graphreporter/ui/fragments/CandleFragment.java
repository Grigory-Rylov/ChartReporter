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
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.grishberg.graphreporter.R;
import com.grishberg.graphreporter.data.model.ChartPeriod;
import com.grishberg.graphreporter.data.model.ChartResponseContainer;
import com.grishberg.graphreporter.di.DiManager;
import com.grishberg.graphreporter.mvp.presenter.CandlesChartPresenter;
import com.grishberg.graphreporter.mvp.view.CandlesChartView;
import com.grishberg.graphreporter.ui.dialogs.PeriodSelectDialog;
import com.grishberg.graphreporter.ui.view.CandleStickChartInitiable;
import com.grishberg.graphreporter.utils.LogService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

public class CandleFragment extends MvpAppCompatFragment implements CandlesChartView {
    private static final String TAG = CandleFragment.class.getSimpleName();
    private static final String ARG_PRODUCT_ID = "ARG_PRODUCT_ID";
    @Inject
    LogService log;

    @InjectPresenter
    CandlesChartPresenter presenter;

    private CandleStickChartInitiable chart;
    private ProgressBar progressBar;
    private List<Long> dates;
    private long productId;
    private ChartPeriod period = ChartPeriod.DAY;

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
            presenter.requestDailyValues(productId);
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

        chart = (CandleStickChartInitiable) view.findViewById(R.id.fragment_candle_chart);

        initAxes(ChartPeriod.DAY);
    }

    private void initAxes(final ChartPeriod period) {

        chart.init();
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

        final XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(true);
        xAxis.setGranularity(1f * period.getPeriod());
        xAxis.setValueFormatter(new IAxisValueFormatter() {

            private final SimpleDateFormat mFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.US);

            @Override
            public String getFormattedValue(final float value, final AxisBase axis) {
                final int index = (int) value;
                if (index > dates.size()) {
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

    @Override
    public void showChart(final ChartResponseContainer<CandleEntry> values) {
        period = values.getPeriod();
        initAxes(period);
        this.dates = values.getDates();
        initDataSet(values);
    }

    private void initDataSet(final ChartResponseContainer<CandleEntry> values) {
        final CandleDataSet set1 = new CandleDataSet(values.getEntries(), "Data Set");
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        set1.setShadowColor(Color.DKGRAY);
        set1.setShadowWidth(0.7f);
        set1.setDecreasingColor(Color.RED);
        set1.setDecreasingPaintStyle(Paint.Style.FILL);
        set1.setIncreasingColor(Color.rgb(122, 242, 84));
        set1.setIncreasingPaintStyle(Paint.Style.FILL);
        set1.setNeutralColor(Color.BLUE);

        final CandleData data = new CandleData(set1);
        chart.setData(data);
        chart.invalidate();
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
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        // Stuff to do, dependent on requestCode and resultCode
        if (requestCode == PeriodSelectDialog.PERIOD_SELECT_RESULT_CODE) {
            // This is the return result of your DialogFragment
            presenter.recalculatePeriod(productId, ChartPeriod.values()[resultCode]);
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
            default:
                return false;
        }
    }
}
