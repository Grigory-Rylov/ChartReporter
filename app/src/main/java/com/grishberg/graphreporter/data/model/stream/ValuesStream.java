package com.grishberg.graphreporter.data.model.stream;

import com.grishberg.graphreporter.data.model.stream.DateTimeHolder;

/**
 * Created by grishberg on 27.02.17.
 */

public interface ValuesStream<T extends DateTimeHolder> {
    int getSize();

    T getNextElement();
}
