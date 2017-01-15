package com.grishberg.graphreporter.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.grishberg.datafacade.ListResultCloseable;
import com.grishberg.datafacade.adapters.BaseRecyclerViewAdapter;

/**
 * Created by grishberg on 15.01.17.
 */
public class ProductsListAdapter extends BaseRecyclerViewAdapter{
    private static final String TAG = ProductsListAdapter.class.getSimpleName();

    public ProductsListAdapter(final ListResultCloseable data) {
        super(data);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

    }
}
