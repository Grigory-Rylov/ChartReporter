package com.grishberg.graphreporter.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.grishberg.graphreporter.R;

import java.text.DecimalFormat;

/**
 * Created by grishberg on 11.02.17.
 */
public class PointInfoView extends RelativeLayout {
    private final DecimalFormat formatter = new DecimalFormat("#0.0000");

    private final TextView openValue;
    private final TextView highValue;
    private final TextView lowValue;
    private final TextView closeValue;
    private final TextView pointValue;
    private final TextView candleDate;
    private final TextView pointDate;

    private final View candleContainer;
    private final View pointContainer;
    private boolean isSingleValueMode;

    public PointInfoView(final Context context) {
        this(context, null);
    }

    public PointInfoView(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PointInfoView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final View rootView = inflate(context, R.layout.view_point_info, this);
        openValue = (TextView) rootView.findViewById(R.id.point_info_open_value_tv);
        highValue = (TextView) rootView.findViewById(R.id.point_info_high_value_tv);
        lowValue = (TextView) rootView.findViewById(R.id.point_info_low_value_tv);
        closeValue = (TextView) rootView.findViewById(R.id.point_info_close_value_tv);
        pointValue = (TextView) rootView.findViewById(R.id.point_info_value_tv);
        candleContainer = rootView.findViewById(R.id.point_info_candle_value_container);
        pointContainer = rootView.findViewById(R.id.point_info_single_value_container);
        candleDate = (TextView) rootView.findViewById(R.id.point_info_candle_date);
        pointDate = (TextView) rootView.findViewById(R.id.point_info_single_date);
    }

    public void setValue(final float open,
                         final float high,
                         final float low,
                         final float close,
                         final String date) {
        openValue.setText(formatter.format(open));
        highValue.setText(formatter.format(high));
        lowValue.setText(formatter.format(low));
        closeValue.setText(formatter.format(close));
        candleDate.setText(date);
        if (isSingleValueMode) {
            candleContainer.setVisibility(VISIBLE);
            pointContainer.setVisibility(GONE);
            isSingleValueMode = false;
        }
    }

    public void setValue(final float y,
                         final String date) {
        pointValue.setText(formatter.format(y));
        pointDate.setText(date);
        if (!isSingleValueMode) {
            isSingleValueMode = true;
            candleContainer.setVisibility(GONE);
            pointContainer.setVisibility(VISIBLE);
        }
    }
}
