package com.grishberg.graphreporter.utils;

import java.text.SimpleDateFormat;

/**
 * Created by grishberg on 11.02.17.
 */

public interface XAxisValueToDateFormatter {
    String getDateAsString(float x);

    String getDateAsString(float x, SimpleDateFormat dateFormat);
}
