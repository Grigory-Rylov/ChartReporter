package com.grishberg.graphreporter.ui.view.color;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * github
 */
public class HSVColorWheel extends View {

    private static final float SCALE = 2f;
    private static final float FADE_OUT_FRACTION = 0.03f;

    private static final int POINTER_LINE_WIDTH_DP = 2;
    private static final int POINTER_LENGTH_DP = 10;

    private final Context context;
    private final Point selectedPoint = new Point();
    private final Paint pointerPaint = new Paint();
    float[] colorHsv = {0f, 0f, 1f};
    private Rect rect;
    private Bitmap bitmap;
    private int[] pixels;
    private float innerCircleRadius;
    private float fullCircleRadius;
    private int scaledWidth;
    private int scaledHeight;
    private int[] scaledPixels;
    private float scaledInnerCircleRadius;
    private float scaledFullCircleRadius;
    private float scaledFadeOutSize;
    private int screenScale;
    private int pointerLength;
    private int innerPadding;
    private OnColorSelectedListener listener;
    private int selectedColor;

    public HSVColorWheel(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        init();
    }

    public HSVColorWheel(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public HSVColorWheel(final Context context) {
        super(context);
        this.context = context;
        init();
    }

    private void init() {
        final float density = context.getResources().getDisplayMetrics().density;
        screenScale = (int) (density * SCALE);
        pointerLength = (int) (density * POINTER_LENGTH_DP);
        pointerPaint.setStrokeWidth((int) (density * POINTER_LINE_WIDTH_DP));
        innerPadding = pointerLength / 2;
    }

    public void setColor(final int color) {
        selectedColor = color;
        Color.colorToHSV(color, colorHsv);
        invalidate();
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        if (bitmap != null) {
            canvas.drawBitmap(bitmap, null, rect, null);
            final float hueInPiInterval = colorHsv[0] / 180f * (float) Math.PI;

            selectedPoint.x = rect.left + (int) (-Math.cos(hueInPiInterval) * colorHsv[1] * innerCircleRadius + fullCircleRadius);
            selectedPoint.y = rect.top + (int) (-Math.sin(hueInPiInterval) * colorHsv[1] * innerCircleRadius + fullCircleRadius);

            canvas.drawLine(selectedPoint.x - pointerLength, selectedPoint.y, selectedPoint.x + pointerLength, selectedPoint.y, pointerPaint);
            canvas.drawLine(selectedPoint.x, selectedPoint.y - pointerLength, selectedPoint.x, selectedPoint.y + pointerLength, pointerPaint);
        }
    }

    @Override
    protected void onSizeChanged(final int w, final int h, final int oldw, final int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        rect = new Rect(innerPadding, innerPadding, w - innerPadding, h - innerPadding);
        bitmap = Bitmap.createBitmap(w - 2 * innerPadding, h - 2 * innerPadding, Bitmap.Config.ARGB_8888);

        fullCircleRadius = Math.min(rect.width(), rect.height()) / 2;
        innerCircleRadius = fullCircleRadius * (1 - FADE_OUT_FRACTION);

        scaledWidth = rect.width() / screenScale;
        scaledHeight = rect.height() / screenScale;
        scaledFullCircleRadius = Math.min(scaledWidth, scaledHeight) / 2f;
        scaledInnerCircleRadius = scaledFullCircleRadius * (1 - FADE_OUT_FRACTION);
        scaledFadeOutSize = scaledFullCircleRadius - scaledInnerCircleRadius;
        scaledPixels = new int[scaledWidth * scaledHeight];
        pixels = new int[rect.width() * rect.height()];

        createBitmap();
    }

    private void createBitmap() {
        final int w = rect.width();
        final int h = rect.height();

        final float[] hsv = new float[]{0f, 0f, 1f};
        int alpha;

        int x = (int) -scaledFullCircleRadius;
        int y = (int) -scaledFullCircleRadius;
        for (int i = 0; i < scaledPixels.length; i++) {
            if (i % scaledWidth == 0) {
                x = (int) -scaledFullCircleRadius;
                y++;
            } else {
                x++;
            }

            final double centerDist = Math.sqrt(x * x + y * y);
            if (centerDist <= scaledFullCircleRadius) {
                hsv[0] = (float) (Math.atan2(y, x) / Math.PI * 180f) + 180;
                hsv[1] = (float) (centerDist / scaledInnerCircleRadius);
                if (centerDist <= scaledInnerCircleRadius) {
                    alpha = 255;
                } else {
                    alpha = 255 - (int) ((centerDist - scaledInnerCircleRadius) / scaledFadeOutSize * 255);
                }
                scaledPixels[i] = Color.HSVToColor(alpha, hsv);
            } else {
                scaledPixels[i] = 0x00000000;
            }
        }

        int scaledX, scaledY;
        for (x = 0; x < w; x++) {
            scaledX = x / screenScale;
            if (scaledX >= scaledWidth) {
                scaledX = scaledWidth - 1;
            }
            for (y = 0; y < h; y++) {
                scaledY = y / screenScale;
                if (scaledY >= scaledHeight) {
                    scaledY = scaledHeight - 1;
                } 
                pixels[x * h + y] = scaledPixels[scaledX * scaledHeight + scaledY];
            }
        }

        bitmap.setPixels(pixels, 0, w, 0, 0, w, h);

        invalidate();
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        final int maxWidth = MeasureSpec.getSize(widthMeasureSpec);
        final int maxHeight = MeasureSpec.getSize(heightMeasureSpec);

        final int width;
        final int height;
            /*
             * Make the view quadratic, with height and width equal and as large as possible
			 */
        width = height = Math.min(maxWidth, maxHeight);

        setMeasuredDimension(width, height);
    }

    public int getColorForPoint(int x, int y, final float[] hsv) {
        x -= fullCircleRadius;
        y -= fullCircleRadius;
        final double centerDist = Math.sqrt(x * x + y * y);
        hsv[0] = (float) (Math.atan2(y, x) / Math.PI * 180f) + 180;
        hsv[1] = Math.max(0f, Math.min(1f, (float) (centerDist / innerCircleRadius)));
        hsv[2] = 255;
        return Color.HSVToColor(hsv);
    }

    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        final int action = event.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                if (listener != null) {
                    selectedColor = getColorForPoint((int) event.getX(), (int) event.getY(), colorHsv);
                    listener.colorSelected(selectedColor);
                }
                invalidate();
                return true;
        }
        return super.onTouchEvent(event);
    }

    public int getSelectedColor() {
        return selectedColor;
    }

    public void setColorSelectedListener(final OnColorSelectedListener colorSelectedListener) {
        this.listener = colorSelectedListener;
    }

    @FunctionalInterface
    public interface OnColorSelectedListener {
        void colorSelected(int color);
    }
}
