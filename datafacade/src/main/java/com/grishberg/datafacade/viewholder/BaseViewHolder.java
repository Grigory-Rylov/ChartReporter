package com.grishberg.datafacade.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.grishberg.datafacade.OnItemSelectedListener;

/**
 * Created by grishberg on 31.12.16.
 */
public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder implements View.OnClickListener {
    private final OnItemSelectedListener<T> clickListener;
    private int position;

    public BaseViewHolder(final View itemView, final OnItemSelectedListener<T> clickListener) {
        super(itemView);
        this.clickListener = clickListener;
        if (clickListener != null) {
            itemView.setOnClickListener(this);
        }
    }

    public void bind(final T item, final int position) {
        this.itemView.setTag(item);
        this.position = position;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onClick(final View view) {
        if (clickListener != null) {
            clickListener.onItemSelected((T) view.getTag());
        }
    }
}
