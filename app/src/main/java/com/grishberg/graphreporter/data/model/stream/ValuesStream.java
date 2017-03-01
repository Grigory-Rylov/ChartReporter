package com.grishberg.graphreporter.data.model.stream;

/**
 * Created by grishberg on 27.02.17.
 */

public interface ValuesStream<T> {
    int getSize();

    T getNextElement() throws NoMoreItemException;

    class NoMoreItemException extends Throwable {
    }
}
