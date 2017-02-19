package com.grishberg.graphreporter.ui.adapters;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.grishberg.datafacade.OnItemSelectedListener;
import com.grishberg.datafacade.adapters.BaseRecyclerViewAdapter;
import com.grishberg.datafacade.viewholder.BaseViewHolder;
import com.grishberg.graphreporter.R;
import com.grishberg.graphreporter.data.model.FormulaContainer;

/**
 * Created by grishberg on 18.02.17.
 */
public class FormulasListAdapter extends BaseRecyclerViewAdapter<FormulaContainer> {
    private final OnItemSelectedListener<FormulaContainer> selectedListener;
    @Nullable
    private OnItemSelectedListener<FormulaContainer> deleteListener;

    public FormulasListAdapter(final OnItemSelectedListener<FormulaContainer> selectedListener) {
        this.selectedListener = selectedListener;
    }

    public void setDeleteListener(final OnItemSelectedListener<FormulaContainer> deleteListener) {
        this.deleteListener = deleteListener;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        return new FormulaViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_formula_list, parent, false), selectedListener, deleteListener);
    }

    private static class FormulaViewHolder extends BaseViewHolder<FormulaContainer> {
        final TextView tvName;
        final View deleteButton;

        FormulaViewHolder(final View itemView,
                          final OnItemSelectedListener<FormulaContainer> clickListener,
                          final OnItemSelectedListener<FormulaContainer> deleteClickListener) {
            super(itemView, clickListener);
            tvName = (TextView) itemView.findViewById(R.id.item_formula_list_name);
            deleteButton = itemView.findViewById(R.id.item_formula_list_delete_button);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    if (deleteClickListener != null) {
                        deleteClickListener.onItemSelected((FormulaContainer) itemView.getTag());
                    }
                }
            });
        }

        @Override
        public void bind(final FormulaContainer item, final int position) {
            super.bind(item, position);
            tvName.setText(item.getName());
        }
    }
}
