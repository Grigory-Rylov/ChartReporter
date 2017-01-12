package com.grishberg.datafacade.adapters;

import android.support.v7.widget.RecyclerView;

import com.grishberg.datafacade.ListResultCloseable;
import com.grishberg.datafacade.viewholder.BaseViewHolder;

import java.io.IOException;

/**
 * Created by grishberg on 31.12.16.
 */
public abstract class BaseRecyclerViewAdapter<T>
        extends RecyclerView.Adapter<BaseViewHolder> {

    private final ListResultCloseable<T> data;

    public BaseRecyclerViewAdapter(final ListResultCloseable<T> data) {
        this.data = data;
    }




    @Override
    public void onBindViewHolder(final BaseViewHolder holder, int position) {
        holder.bind(data.get(position), position);
        /*
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).id);
        holder.mContentView.setText(mValues.get(position).content);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTwoPanel) {
                    Bundle arguments = new Bundle();
                    arguments.putString(ReportItemDetailFragment.ARG_ITEM_ID, holder.mItem.id);
                    ReportItemDetailFragment fragment = new ReportItemDetailFragment();
                    fragment.setArguments(arguments);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.reportitem_detail_container, fragment)
                            .commit();
                } else {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, ReportItemDetailActivity.class);
                    intent.putExtra(ReportItemDetailFragment.ARG_ITEM_ID, holder.mItem.id);

                    context.startActivity(intent);
                }
            }
        });
        */
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    public void recycle(){
        if(data != null) {
            try {
                data.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}