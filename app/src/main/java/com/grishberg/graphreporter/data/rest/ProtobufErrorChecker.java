package com.grishberg.graphreporter.data.rest;

import com.grishberg.graphreporter.common.data.rest.SoftErrorDelegate;
import com.grishberg.graphreporter.data.beans.DailyValueProtos.DailyValueContainer;

/**
 * Created by grishberg on 29.04.17.
 */

public class ProtobufErrorChecker implements SoftErrorDelegate<DailyValueContainer> {
    @Override
    public Throwable checkSoftError(DailyValueContainer body) {
        return null;
    }
}
