package com.grishberg.graphreporter.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.grishberg.datafacade.OnItemSelectedListener;
import com.grishberg.datafacade.adapters.BaseRecyclerViewAdapter;
import com.grishberg.datafacade.viewholder.BaseViewHolder;
import com.grishberg.graphreporter.R;
import com.grishberg.graphreporter.data.model.ProductItem;

/**
 * Created by grishberg on 15.01.17.
 */
public class ProductsListAdapter extends BaseRecyclerViewAdapter<ProductItem> {
    private static final String TAG = ProductsListAdapter.class.getSimpleName();

    private final OnItemSelectedListener<ProductItem> selectedListener;

    public ProductsListAdapter(final OnItemSelectedListener<ProductItem> selectedListener) {
        this.selectedListener = selectedListener;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        return new ProductViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_chart_nav_view, parent, false), selectedListener);
    }

    private static class ProductViewHolder extends BaseViewHolder {
        public final TextView tvName;

        public ProductViewHolder(final View itemView, final OnItemSelectedListener clickListener) {
            super(itemView, clickListener);
            tvName = (TextView) itemView.findViewById(R.id.chart_nav_view_name);
        }

        @Override
        public void bind(final Object item, final int position) {
            super.bind(item, position);
            tvName.setText(((ProductItem) item).getName());
        }
    }
}
