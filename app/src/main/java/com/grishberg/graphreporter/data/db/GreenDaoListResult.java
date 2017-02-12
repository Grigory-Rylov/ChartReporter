package com.grishberg.graphreporter.data.db;

import android.support.annotation.NonNull;

import com.grishberg.datafacade.ListResultCloseable;

import org.greenrobot.greendao.query.LazyList;
import org.greenrobot.greendao.query.Query;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by grishberg on 12.02.17.
 */
public class GreenDaoListResult<T> implements ListResultCloseable<T> {

    private final LazyList<T> list;

    public GreenDaoListResult(@NonNull final Query<T> query) {
        this.list = query.listLazy();
    }

    @Override
    public boolean isClosed() {
        return list.isClosed();
    }

    @Override
    public void close() throws IOException {
        list.close();
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public boolean contains(final Object o) {
        return list.contains(o);
    }

    @NonNull
    @Override
    public Iterator<T> iterator() {
        return list.iterator();
    }

    @NonNull
    @Override
    public Object[] toArray() {
        return list.toArray();
    }

    @NonNull
    @Override
    public <T1> T1[] toArray(final T1[] a) {
        return list.toArray(a);
    }

    @Override
    public boolean add(final T t) {
        return list.add(t);
    }

    @Override
    public boolean remove(final Object o) {
        return list.remove(o);
    }

    @Override
    public boolean containsAll(final Collection<?> c) {
        return list.containsAll(c);
    }

    @Override
    public boolean addAll(final Collection<? extends T> c) {
        return list.addAll(c);
    }

    @Override
    public boolean addAll(final int index, final Collection<? extends T> c) {
        return list.addAll(index, c);
    }

    @Override
    public boolean removeAll(final Collection<?> c) {
        return list.removeAll(c);
    }

    @Override
    public boolean retainAll(final Collection<?> c) {
        return list.removeAll(c);
    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public T get(final int index) {
        return list.get(index);
    }

    @Override
    public T set(final int index, final T element) {
        return list.set(index, element);
    }

    @Override
    public void add(final int index, final T element) {
        list.add(index, element);
    }

    @Override
    public T remove(final int index) {
        return list.remove(index);
    }

    @Override
    public int indexOf(final Object o) {
        return list.indexOf(o);
    }

    @Override
    public int lastIndexOf(final Object o) {
        return list.lastIndexOf(o);
    }

    @Override
    public ListIterator<T> listIterator() {
        return list.listIterator();
    }

    @NonNull
    @Override
    public ListIterator<T> listIterator(final int index) {
        return list.listIterator(index);
    }

    @NonNull
    @Override
    public List<T> subList(final int fromIndex, final int toIndex) {
        return list.subList(fromIndex, toIndex);
    }
}
