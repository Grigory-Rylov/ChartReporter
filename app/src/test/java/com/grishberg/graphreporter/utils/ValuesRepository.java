package com.grishberg.graphreporter.utils;

import android.support.annotation.NonNull;

import com.grishberg.graphreporter.data.beans.DailyValue;

import java.util.ArrayList;
import java.util.List;

import static com.grishberg.graphreporter.utils.DateTimeUtils.getDate;

/**
 * Created by grishberg on 27.02.17.
 */

public class ValuesRepository {

    public static final int MINUTE = 60;
    public static final int DT_OFFSET = 0;

    @NonNull
    public static List<DailyValue> getDailyValues() {
        final List<DailyValue> dailyValues = new ArrayList<>();
        int i = 0;
        dailyValues.add(new DailyValue(i++, getDate(10, 6, 2016), 18.5f, 18.7f, 18.71f, 18.44f));
        dailyValues.add(new DailyValue(i++, getDate(13, 6, 2016), 18.01f, 18.1f, 18.1f, 18.0f));
        dailyValues.add(new DailyValue(i++, getDate(14, 6, 2016), 17.82f, 17.99f, 17.99f, 17.8f));
        dailyValues.add(new DailyValue(i++, getDate(15, 6, 2016), 17.78f, 17.8f, 17.95f, 17.75f));
        dailyValues.add(new DailyValue(i++, getDate(16, 6, 2016), 17.67f, 17.64f, 17.67f, 17.43f));
        dailyValues.add(new DailyValue(i++, getDate(17, 6, 2016), 17.61f, 17.5f, 17.56f, 17.5f));
        dailyValues.add(new DailyValue(i++, getDate(20, 6, 2016), 17.95f, 17.72f, 18.0f, 17.72f));
        dailyValues.add(new DailyValue(i++, getDate(21, 6, 2016), 17.99f, 17.88f, 18.1f, 17.88f));
        dailyValues.add(new DailyValue(i++, getDate(22, 6, 2016), 17.7f, 17.84f, 17.84f, 17.7f));
        dailyValues.add(new DailyValue(i++, getDate(23, 6, 2016), 17.83f, 17.85f, 17.88f, 17.75f));
        dailyValues.add(new DailyValue(i++, getDate(24, 6, 2016), 17.25f, 17.23f, 17.32f, 17.0f));
        dailyValues.add(new DailyValue(i++, getDate(27, 6, 2016), 16.81f, 16.95f, 17.07f, 16.77f));
        dailyValues.add(new DailyValue(i++, getDate(28, 6, 2016), 17.13f, 17.16f, 17.16f, 16.92f));
        dailyValues.add(new DailyValue(i++, getDate(29, 6, 2016), 17.78f, 17.81f, 17.81f, 17.72f));
        dailyValues.add(new DailyValue(i++, getDate(30, 6, 2016), 17.78f, 17.79f, 17.8f, 17.56f));
        dailyValues.add(new DailyValue(i++, getDate(1, 7, 2016), 17.75f, 17.7f, 17.83f, 17.7f));
        dailyValues.add(new DailyValue(i++, getDate(5, 7, 2016), 17.5f, 17.45f, 17.69f, 17.45f));
        dailyValues.add(new DailyValue(i++, getDate(6, 7, 2016), 17.63f, 17.71f, 17.71f, 17.45f));
        dailyValues.add(new DailyValue(i++, getDate(7, 7, 2016), 18.81f, 18.64f, 18.81f, 18.35f));
        dailyValues.add(new DailyValue(i++, getDate(8, 7, 2016), 20.73f, 20.57f, 20.85f, 20.47f));
        return dailyValues;
    }

    @NonNull
    public static List<DailyValue> getMinutesValues() {
        final List<DailyValue> dailyValues = new ArrayList<>();
        int i = 0;
        dailyValues.add(new DailyValue(i++, MINUTE * 0 + DT_OFFSET, 10, 12, 9, 11));
        dailyValues.add(new DailyValue(i++, MINUTE * 1 + DT_OFFSET, 11, 16, 8, 12));
        dailyValues.add(new DailyValue(i++, MINUTE * 2 + DT_OFFSET, 12, 14, 11, 13));

        dailyValues.add(new DailyValue(i++, MINUTE * 3 + DT_OFFSET, 20, 22, 19, 21));

        dailyValues.add(new DailyValue(i++, MINUTE * 7 + DT_OFFSET, 21, 26, 18, 22));
        dailyValues.add(new DailyValue(i++, MINUTE * 8 + DT_OFFSET, 22, 24, 21, 23));

        dailyValues.add(new DailyValue(i++, MINUTE * 11 + DT_OFFSET, 17.95f, 17.72f, 18.0f, 17.72f));
        dailyValues.add(new DailyValue(i++, MINUTE * 12 + DT_OFFSET, 17.99f, 17.88f, 18.1f, 17.88f));
        dailyValues.add(new DailyValue(i++, MINUTE * 14 + DT_OFFSET, 17.7f, 17.84f, 17.84f, 17.7f));
        return dailyValues;
    }
}
