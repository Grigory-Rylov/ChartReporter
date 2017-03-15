package com.grishberg.graphreporter.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.grishberg.datafacade.OnItemSelectedListener;
import com.grishberg.graphreporter.R;
import com.grishberg.graphreporter.data.beans.FormulaContainer;
import com.grishberg.graphreporter.mvp.presenter.FormulaListPresenter;
import com.grishberg.graphreporter.mvp.view.FormulasListView;
import com.grishberg.graphreporter.ui.adapters.FormulasListAdapter;
import com.grishberg.graphreporter.ui.dialogs.NewPointDialog;

import java.util.List;

/**
 * Created by grishberg on 18.02.17.
 */
public class FormulasListFragment extends MvpAppCompatFragment implements FormulasListView, OnItemSelectedListener<FormulaContainer> {

    public static final String ARG_PRODUCT_ID = "ARG_PRODUCT_ID";
    @InjectPresenter
    FormulaListPresenter presenter;
    private RecyclerView formulaListRecyclerView;
    private FormulasListAdapter adapter;
    private long productId;

    public static FormulasListFragment newInstance(final long productId) {
        final FormulasListFragment fragment = new FormulasListFragment();
        final Bundle args = new Bundle();
        args.putLong(ARG_PRODUCT_ID, productId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            productId = getArguments().getLong(ARG_PRODUCT_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater,
                             @Nullable final ViewGroup container,
                             @Nullable final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_formulas_list, container, false);
        initRecyclerView(view);
        presenter.onInitScreen(productId);
        return view;
    }

    private void initRecyclerView(final View view) {
        adapter = new FormulasListAdapter(this);
        adapter.setDeleteListener(new OnItemSelectedListener<FormulaContainer>() {
            @Override
            public void onItemSelected(final FormulaContainer item) {
                presenter.onFormulaDeleteClicked(item);
            }
        });
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.fragment_formulas_list);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public void showFormulasList(final List<FormulaContainer> formulaContainers) {
        adapter.update(formulaContainers);
    }

    @Override
    public void onItemSelected(final FormulaContainer item) {
        presenter.onFormulaItemSelected(item);
    }

    @Override
    public void showFormulaDescription(final FormulaContainer formulaContainer) {
        NewPointDialog.showEditDialog(formulaContainer, getFragmentManager(), this);
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NewPointDialog.POINT_UPDATE_RESULT_CODE) {
            final FormulaContainer formulaContainer = NewPointDialog.getResult(data);
            presenter.saveFormula(formulaContainer);
        } else if (requestCode == NewPointDialog.POINT_NEW_RESULT_CODE) {
            final FormulaContainer formulaContainer = NewPointDialog.getResult(data);
            presenter.addNewFormula(formulaContainer);
            presenter.onInitScreen(productId);
        }
    }

    //---- menu -----

    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.formula_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getItemId() == R.id.action_add_formula) {
            NewPointDialog.showDialog(getFragmentManager(), this);
        }
        return super.onOptionsItemSelected(item);
    }
}
