package com.grishberg.graphreporter.data.enums;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created on 13.03.17.
 *
 * @author g
 */
public enum ChartRange {
    YEAR {
        @Override
        public long getStartDate() {
            final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
            calendar.add(Calendar.YEAR, -1);
            return calendar.getTimeInMillis() / 1000L;
        }
    },
    DAY {
        @Override
        public long getStartDate() {
            final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
            calendar.add(Calendar.DAY_OF_YEAR, -1);
            return calendar.getTimeInMillis() / 1000L;
        }
    },
    WEEK {
        @Override
        public long getStartDate() {
            final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
            calendar.add(Calendar.DAY_OF_YEAR, -7);
            return calendar.getTimeInMillis() / 1000L;
        }
    },
    MONTH {
        @Override
        public long getStartDate() {
            final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
            calendar.add(Calendar.MONTH, -1);
            return calendar.getTimeInMillis() / 1000L;
        }
    },
    HALF_YEAR {
        @Override
        public long getStartDate() {
            final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
            calendar.add(Calendar.MONTH, -6);
            return calendar.getTimeInMillis() / 1000L;
        }
    },
    TWO_YEAR {
        @Override
        public long getStartDate() {
            final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
            calendar.add(Calendar.YEAR, -2);
            return calendar.getTimeInMillis() / 1000L;
        }
    };

    public abstract long getStartDate();
}
