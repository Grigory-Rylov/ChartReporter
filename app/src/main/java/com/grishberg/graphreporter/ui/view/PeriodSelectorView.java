package com.grishberg.graphreporter.ui.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.grishberg.graphreporter.R;
import com.grishberg.graphreporter.data.enums.ChartPeriod;

/**
 * Created by grishberg on 28.01.17.
 */
public class PeriodSelectorView extends LinearLayout implements View.OnClickListener {

    private final SparseArray<ButtonPeriodHolder> buttons = new SparseArray<>();
    private int triggeredButtonId = -1;
    @Nullable
    private OnPeriodChangeListener onPeriodChangeListener;

    public PeriodSelectorView(final Context context) {
        this(context, null);
    }

    public PeriodSelectorView(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PeriodSelectorView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final View rootView = inflate(context, R.layout.view_period_selector, this);

        buttons.put(R.id.view_period_selector_3m, new
                ButtonPeriodHolder(ChartPeriod.MINUTE_3, (Button) rootView.findViewById(R.id.view_period_selector_3m)));
        buttons.put(R.id.view_period_selector_15m, new
                ButtonPeriodHolder(ChartPeriod.MINUTE_15, (Button) rootView.findViewById(R.id.view_period_selector_15m)));
        buttons.put(R.id.view_period_selector_30m, new
                ButtonPeriodHolder(ChartPeriod.MINUTE_30, (Button) rootView.findViewById(R.id.view_period_selector_30m)));
        buttons.put(R.id.view_period_selector_1h, new
                ButtonPeriodHolder(ChartPeriod.HOUR, (Button) rootView.findViewById(R.id.view_period_selector_1h)));
        buttons.put(R.id.view_period_selector_1D, new
                ButtonPeriodHolder(ChartPeriod.DAY, (Button) rootView.findViewById(R.id.view_period_selector_1D)));
        buttons.put(R.id.view_period_selector_1W, new
                ButtonPeriodHolder(ChartPeriod.WEEK, (Button) rootView.findViewById(R.id.view_period_selector_1W)));
        buttons.put(R.id.view_period_selector_1M, new
                ButtonPeriodHolder(ChartPeriod.MONTH, (Button) rootView.findViewById(R.id.view_period_selector_1M)));
        buttons.put(R.id.view_period_selector_1Y, new
                ButtonPeriodHolder(ChartPeriod.YEAR, (Button) rootView.findViewById(R.id.view_period_selector_1Y)));

        for (int i = 0, len = buttons.size(); i < len; i++) {
            final ButtonPeriodHolder button = buttons.get(buttons.keyAt(i));
            button.button.setOnClickListener(this);
        }

        onClick(buttons.get(R.id.view_period_selector_1D).button);
    }

    public void setOnPeriodChangeListener(final OnPeriodChangeListener onPeriodChangeListener) {
        this.onPeriodChangeListener = onPeriodChangeListener;
    }

    @Override
    public void onClick(final View view) {
        changePressedState(false);
        triggeredButtonId = view.getId();
        final ChartPeriod period = changePressedState(true);
        if (onPeriodChangeListener != null) {
            onPeriodChangeListener.onPeriodChanged(period);
        }
    }

    private ChartPeriod changePressedState(final boolean isPressed) {
        if (buttons.get(triggeredButtonId) != null) {
            buttons.get(triggeredButtonId).button.setPressed(isPressed);
            buttons.get(triggeredButtonId).button.setSelected(isPressed);
            return buttons.get(triggeredButtonId).period;
        }
        return ChartPeriod.DAY;
    }

    @FunctionalInterface
    public interface OnPeriodChangeListener {
        void onPeriodChanged(ChartPeriod selectedPeriod);
    }

    private static class ButtonPeriodHolder {
        ChartPeriod period;
        Button button;

        ButtonPeriodHolder(final ChartPeriod period, final Button button) {
            this.period = period;
            this.button = button;
        }
    }
}
