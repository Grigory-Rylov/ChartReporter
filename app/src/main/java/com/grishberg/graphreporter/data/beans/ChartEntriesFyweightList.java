package com.grishberg.graphreporter.data.beans;

import android.support.annotation.NonNull;

import com.github.mikephil.charting.data.Entry;

import java.io.Closeable;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by grishberg on 29.04.17.
 */

public class ChartEntriesFyweightList implements List<Entry>, Closeable {
    private int count;
    private final int windowSize;


    public ChartEntriesFyweightList(int windowSize) {
        this.windowSize = windowSize;
    }

    void silentClose() {
        try {
            close();
        } catch (IOException e) {

        }
    }

    @Override
    public void close() throws IOException {

    }

    // List implementation

    @Override
    public int size() {
        return count;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @NonNull
    @Override
    public Iterator<Entry> iterator() {
        return null;
    }

    @NonNull
    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @NonNull
    @Override
    public <T> T[] toArray(@NonNull T[] a) {
        return null;
    }

    @Override
    public boolean add(Entry entry) {
        return false;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(@NonNull Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(@NonNull Collection<? extends Entry> c) {
        return false;
    }

    @Override
    public boolean addAll(int index, @NonNull Collection<? extends Entry> c) {
        return false;
    }

    @Override
    public boolean removeAll(@NonNull Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(@NonNull Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public Entry get(int index) {
        return null;
    }

    @Override
    public Entry set(int index, Entry element) {
        return null;
    }

    @Override
    public void add(int index, Entry element) {

    }

    @Override
    public Entry remove(int index) {
        return null;
    }

    @Override
    public int indexOf(Object o) {
        return 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        return 0;
    }

    @Override
    public ListIterator<Entry> listIterator() {
        return null;
    }

    @NonNull
    @Override
    public ListIterator<Entry> listIterator(int index) {
        return null;
    }

    @NonNull
    @Override
    public List<Entry> subList(int fromIndex, int toIndex) {
        return null;
    }
}
