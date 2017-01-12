package com.grishberg.graphreporter.utils;

/**
 * Created by grishberg on 12.01.17.
 */
public class StringUtils {
    private static final String TAG = StringUtils.class.getSimpleName();

    public static boolean isEmpty(final CharSequence charSequence) {
        return charSequence == null || charSequence.length() == 0;
    }
}
