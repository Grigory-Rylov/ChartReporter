package com.grishberg.datafacade.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.grishberg.datafacade.OnItemSelectedListener;

/**
 * Created by grishberg on 31.12.16.
 */
public abstract class BaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private static final String TAG = BaseViewHolder.class.getSimpleName();
    private final OnItemSelectedListener clickListener;
    private int position;

    public BaseViewHolder(final View itemView, final OnItemSelectedListener clickListener) {
        super(itemView);
        this.clickListener = clickListener;
        if (clickListener != null) {
            itemView.setOnClickListener(this);
        }
    }

    public void bind(Object item, int position) {
        this.itemView.setTag(item);
        this.position = position;
    }

    @Override
    public void onClick(View view) {
        if (clickListener != null) {
            clickListener.onItemSelected(view.getTag(), position);
        }
    }
}
