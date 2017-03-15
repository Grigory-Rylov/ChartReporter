package com.grishberg.graphreporter.utils;

/**
 * Created by grishberg on 22.01.17.
 */

public interface TimerUtil {
    void startTimer(int duration);

    void stopTimer();

    void setHandler(Runnable handler);
}
