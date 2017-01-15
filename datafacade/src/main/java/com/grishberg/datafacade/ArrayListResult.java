package com.grishberg.datafacade;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by grishberg on 15.01.17.
 */
public class ArrayListResult<T> extends ArrayList<T> implements ListResultCloseable<T> {

    public static <T> ListResultCloseable<T> fromList(final List<T> source) {
        final ListResultCloseable<T> list = new ArrayListResult<>(source);
        return list;
    }

    public ArrayListResult(final List<T> list) {
        super(list);
    }

    @Override
    public void close() throws IOException {
    }
}
