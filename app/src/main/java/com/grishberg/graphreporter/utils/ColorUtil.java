package com.grishberg.graphreporter.utils;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.LOLLIPOP;

/**
 * Created by grishberg on 22.01.17.
 */
public class ColorUtil {
    public static int getColor(final Context context, final int colorResId) {
        if (SDK_INT >= LOLLIPOP) {
            return ContextCompat.getColor(context, colorResId)
        } else {
            return context.getResources().getColor(colorResId);
        }
    }
}
