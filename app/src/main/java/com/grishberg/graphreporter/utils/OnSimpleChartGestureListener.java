package com.grishberg.graphreporter.utils;

import android.view.MotionEvent;

import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;

/**
 * Created by grishberg on 11.02.17.
 */
public abstract class OnSimpleChartGestureListener implements OnChartGestureListener {
    @Override
    public void onChartGestureStart(final MotionEvent me, final ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartGestureEnd(final MotionEvent me, final ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartLongPressed(final MotionEvent me) {

    }

    @Override
    public void onChartDoubleTapped(final MotionEvent me) {

    }

    @Override
    public void onChartSingleTapped(final MotionEvent me) {

    }

    @Override
    public void onChartFling(final MotionEvent me1, final MotionEvent me2, final float velocityX,
                             final float velocityY) {

    }

    @Override
    public void onChartScale(final MotionEvent me, final float scaleX, final float scaleY) {

    }

    @Override
    public void onChartTranslate(final MotionEvent me, final float dX, final float dY) {

    }
}
