package com.grishberg.graphreporter.ui.view.color;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by grishberg on 07.02.17.
 * Слайдер для выбора цвета
 */
public class HSVValueSlider extends View {
    float[] colorHsv = {0f, 0f, 1f};
    private Rect srcRect;
    private Rect dstRect;
    private Bitmap bitmap;
    private int[] pixels;
    private ColorPickerView.OnColorSelectedListener listener;

    public HSVValueSlider(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
    }

    public HSVValueSlider(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public HSVValueSlider(final Context context) {
        super(context);
    }

    public void setListener(final ColorPickerView.OnColorSelectedListener listener) {
        this.listener = listener;
    }

    public void setColor(final int color, final boolean keepValue) {
        final float oldValue = colorHsv[2];
        Color.colorToHSV(color, colorHsv);
        if (keepValue) {
            colorHsv[2] = oldValue;
        }
        if (listener != null) {
            listener.colorSelected(Color.HSVToColor(colorHsv));
        }

        createBitmap();
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        if (bitmap != null) {
            canvas.drawBitmap(bitmap, srcRect, dstRect, null);
        }
    }

    @Override
    protected void onSizeChanged(final int w, final int h, final int oldw, final int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        srcRect = new Rect(0, 0, w, 1);
        dstRect = new Rect(0, 0, w, h);
        bitmap = Bitmap.createBitmap(w, 1, Bitmap.Config.ARGB_8888);
        pixels = new int[w];

        createBitmap();
    }

    private void createBitmap() {
        if (bitmap == null) {
            return;
        }
        final int w = getWidth();

        final float[] hsv = new float[]{colorHsv[0], colorHsv[1], 1f};

        final int selectedX = (int) (colorHsv[2] * w);

        float value = 0;
        final float valueStep = 1f / w;
        for (int x = 0; x < w; x++) {
            value += valueStep;
            if (x >= selectedX - 1 && x <= selectedX + 1) {
                final int intVal = 0xFF - (int) (value * 0xFF);
                final int color = intVal * 0x010101 + 0xFF000000;
                pixels[x] = color;
            } else {
                hsv[2] = value;
                pixels[x] = Color.HSVToColor(hsv);
            }
        }

        bitmap.setPixels(pixels, 0, w, 0, 0, w, 1);

        invalidate();
    }

    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        final int action = event.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                final int x = Math.max(0, Math.min(bitmap.getWidth() - 1, (int) event.getX()));
                final float value = x / (float) bitmap.getWidth();
                if (colorHsv[2] != value) {
                    colorHsv[2] = value;
                    if (listener != null) {
                        listener.colorSelected(Color.HSVToColor(colorHsv));
                    }
                    createBitmap();
                    invalidate();
                }
                return true;
        }
        return super.onTouchEvent(event);
    }
}