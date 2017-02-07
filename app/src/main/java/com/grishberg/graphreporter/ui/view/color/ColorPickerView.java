package com.grishberg.graphreporter.ui.view.color;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.grishberg.graphreporter.R;

/**
 * Created by grishberg on 07.02.17.
 * Вью выбора цвета, за основу взята https://github.com/jesperborgstrup/buzzingandroid/blob/master/src/com/buzzingandroid/ui/HSVColorPickerDialog.java
 */
public class ColorPickerView extends RelativeLayout {

    private static final int CONTROL_SPACING_DP = 20;
    private static final int SELECTED_COLOR_HEIGHT_DP = 50;
    private static final int BORDER_DP = 1;
    private static final int BORDER_COLOR = Color.BLACK;
    private final View selectedColorView;
    private int selectedColor;
    private HSVColorWheel colorWheel;
    private HSVValueSlider valueSlider;
    private OnColorSelectedListener colorSelectedListener;

    public ColorPickerView(final Context context) {
        this(context, null);
    }

    public ColorPickerView(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorPickerView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.selectedColor = 0;

        colorWheel = new HSVColorWheel(context);
        valueSlider = new HSVValueSlider(context);
        final int borderSize = (int) (context.getResources().getDisplayMetrics().density * BORDER_DP);

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.bottomMargin = (int) (context.getResources().getDisplayMetrics().density * CONTROL_SPACING_DP);
        colorWheel.setListener(color -> valueSlider.setColor(color, true));
        colorWheel.setColor(selectedColor);
        colorWheel.setId(R.id.color_picker_wheel);
        addView(colorWheel, lp);

        final int selectedColorHeight = (int) (context.getResources().getDisplayMetrics().density * SELECTED_COLOR_HEIGHT_DP);

        final FrameLayout valueSliderBorder = new FrameLayout(context);
        valueSliderBorder.setBackgroundColor(BORDER_COLOR);
        valueSliderBorder.setPadding(borderSize, borderSize, borderSize, borderSize);
        valueSliderBorder.setId(R.id.color_picker_slider);
        lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, selectedColorHeight + 2 * borderSize);
        lp.bottomMargin = (int) (context.getResources().getDisplayMetrics().density * CONTROL_SPACING_DP);
        lp.addRule(RelativeLayout.BELOW, R.id.color_picker_wheel);
        addView(valueSliderBorder, lp);

        valueSlider.setColor(selectedColor, false);
        valueSlider.setListener(new OnColorSelectedListener() {
            @Override
            public void colorSelected(final int color) {
                selectedColor = color;
                selectedColorView.setBackgroundColor(color);
                if (colorSelectedListener != null) {
                    colorSelectedListener.colorSelected(color);
                }
            }
        });
        valueSliderBorder.addView(valueSlider);

        final FrameLayout selectedColorborder = new FrameLayout(context);
        selectedColorborder.setBackgroundColor(BORDER_COLOR);
        lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, selectedColorHeight + 2 * borderSize);
        selectedColorborder.setPadding(borderSize, borderSize, borderSize, borderSize);
        lp.addRule(RelativeLayout.BELOW, R.id.color_picker_slider);
        addView(selectedColorborder, lp);

        selectedColorView = new View(context);
        selectedColorView.setBackgroundColor(selectedColor);
        selectedColorborder.addView(selectedColorView);
    }

    public int getSelectedColor() {
        return selectedColor;
    }

    public void setColorSelectedListener(final OnColorSelectedListener colorSelectedListener) {
        this.colorSelectedListener = colorSelectedListener;
    }

    @FunctionalInterface
    public interface OnColorSelectedListener {
        void colorSelected(int color);
    }
}
