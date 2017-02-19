package com.grishberg.graphreporter.data.storage;

import com.grishberg.graphreporter.data.model.DailyValue;
import com.grishberg.graphreporter.data.repository.values.DailyDataRepository;

import java.util.List;

/**
 * Created by grishberg on 21.01.17.
 */

public interface DailyDataStorage extends DailyDataRepository {
    void setDailyData(long productId, List<DailyValue> values);

    void appendDailyData(long productId, List<DailyValue> values);
}
