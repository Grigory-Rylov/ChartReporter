package com.grishberg.graphreporter.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.grishberg.datafacade.ListResultCloseable;
import com.grishberg.datafacade.OnItemSelectedListener;
import com.grishberg.graphreporter.R;
import com.grishberg.graphreporter.data.beans.ProductItem;
import com.grishberg.graphreporter.mvp.presenter.ProductsPresenter;
import com.grishberg.graphreporter.mvp.view.ProductsView;
import com.grishberg.graphreporter.ui.adapters.ProductsListAdapter;

public class ProductsFragment extends MvpAppCompatFragment implements ProductsView,
        OnItemSelectedListener<ProductItem> {
    @InjectPresenter
    ProductsPresenter presenter;
    private ProductsListAdapter adapter;
    private ProgressBar progressBar;

    private ProductsInteractionListener listener;

    public ProductsFragment() {
        // Required empty public constructor
    }

    public static ProductsFragment newInstance() {
        final ProductsFragment fragment = new ProductsFragment();
        final Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.requestProducts();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_products, container, false);
        initRecyclerView(view);
        progressBar = (ProgressBar) view.findViewById(R.id.products_list_progress);
        return view;
    }

    private void initRecyclerView(final View parent) {
        adapter = new ProductsListAdapter(this);
        final RecyclerView recyclerView = (RecyclerView) parent.findViewById(R.id.products_list_recycler);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        if (context instanceof ProductsInteractionListener) {
            listener = (ProductsInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ProductsInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onItemSelected(final ProductItem item) {
        getActivity().setTitle(item.getName());
        listener.onProductSelected(item);
    }

    @Override
    public void showProducts(final ListResultCloseable<ProductItem> productItems) {
        adapter.update(productItems);
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showFail(final String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @FunctionalInterface
    public interface ProductsInteractionListener {

        void onProductSelected(ProductItem item);
    }
}
