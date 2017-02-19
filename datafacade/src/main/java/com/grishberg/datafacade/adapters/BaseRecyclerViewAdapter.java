package com.grishberg.datafacade.adapters;

import android.support.v7.widget.RecyclerView;

import com.grishberg.datafacade.ListResultCloseable;
import com.grishberg.datafacade.viewholder.BaseViewHolder;

import java.io.IOException;
import java.util.List;

/**
 * Created by grishberg on 31.12.16.
 */
public abstract class BaseRecyclerViewAdapter<T>
        extends RecyclerView.Adapter<BaseViewHolder> {

    private List<T> data;

    public BaseRecyclerViewAdapter() {
    }

    public void update(final List<T> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final BaseViewHolder holder, final int position) {
        holder.bind(data.get(position), position);
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }
}