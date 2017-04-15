package com.grishberg.graphreporter.data.storage;

import com.grishberg.graphreporter.data.beans.DailyValueProtos;
import com.grishberg.graphreporter.data.repository.values.DailyDataRepository;

import java.util.List;

/**
 * Created by grishberg on 21.01.17.
 */

public interface DailyDataStorage extends DailyDataRepository {
    void appendData(long productId, List<DailyValueProtos.DailyValue> values);
}
