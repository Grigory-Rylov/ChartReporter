package com.grishberg.graphreporter.data.repository.values;

import com.grishberg.graphreporter.data.model.DailyValue;

import java.util.List;

/**
 * Created by grishberg on 21.01.17.
 */

public interface DailyDataStorage extends DailyDataRepository {
    void setDailyData(final long productId, List<DailyValue> values);
}
