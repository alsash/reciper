package com.alsash.reciper.app.lib;

import java.util.concurrent.TimeUnit;

/**
 * Yet another time converter
 */
public class TimeLong {

    public static int[] getHoursAndMinutes(long millis) {
        long h = TimeUnit.MILLISECONDS.toHours(millis);
        long m = TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(h);
        if (h >= 24) h = 0;
        return new int[]{Math.round(h), Math.round(m)};
    }

    public static long getTimeFromHoursAndMinutes(int hours24, int minutes) {
        return TimeUnit.HOURS.toMillis(hours24) + TimeUnit.MINUTES.toMillis(minutes);
    }
}
