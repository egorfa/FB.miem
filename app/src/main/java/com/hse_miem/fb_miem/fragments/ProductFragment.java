package com.hse_miem.fb_miem.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.util.Pair;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.hse_miem.fb_miem.AllProductsActivity;
import com.hse_miem.fb_miem.R;
import com.hse_miem.fb_miem.model.Product;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.hse_miem.fb_miem.MainActivity.ALL_PRODUCTS_CODE;

/**
 * Created by Egor on 18/05/16.
 */
public class ProductFragment extends BaseFragment{

    ArrayList<Pair<Product, Integer>> mProducts;

    @Bind(R.id.recycler_products) RecyclerView recyclerView;
    @Bind(R.id.fab)               FloatingActionButton fab;
    @Bind(R.id.text_balance)      TextView balance;
    @Bind(R.id.img_empty)         ImageView imgEmpty;


    public static ProductFragment newInstance(Product product) {

        Bundle args = new Bundle();
        args.putParcelable("Product", product);

        ProductFragment fragment = new ProductFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getActivity();
        View view = inflater.inflate(R.layout.fragment_products, container, false);
        ButterKnife.bind(this, view);
        mProducts = new ArrayList<>();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initializeRecyclerView();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mProducts.size() == 0) {
            imgEmpty.setVisibility(View.VISIBLE);
            Snackbar.make(getActivity().findViewById(R.id.clayout), getString(R.string.empty_message), Snackbar.LENGTH_SHORT).show();
        } else {
            imgEmpty.setVisibility(View.GONE);
        }


        balance.setText(String.valueOf(getBalance()) + " \u20BD");

        if (getArguments() != null) {
            mProducts.add(getArguments().getParcelable("Product"));
            recyclerView.getAdapter().notifyDataSetChanged();
            recyclerView.requestLayout();
        }
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AllProductsActivity.class);
            getActivity().startActivityForResult(intent, ALL_PRODUCTS_CODE);
        });

        recyclerView.setAdapter(new ProductAdapter());
    }

    private double getBalance() {
        double balance = -1;
        for (Pair<Product, Integer> p: mProducts){
            balance += p.second * p.first.getPrice();
        }
        if (mProducts.size() != 0) return balance;
        else return 0;
    }


    private void initializeRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(mLayoutManager);
    }

    public void updateArray(Product productNew) {
        int positionYes = -1;
        for(int i=0 ; i < mProducts.size(); i++) {
            if (mProducts.get(i).first.getId() == (productNew.getId()))
                positionYes = i;
        }
        if (positionYes > -1) {
            mProducts.set(positionYes, Pair.create(mProducts.get(positionYes).first, mProducts.get(positionYes).second+1));
            recyclerView.getAdapter().notifyItemChanged(positionYes);
            recyclerView.requestLayout();
        } else {
            mProducts.add(Pair.create(productNew, 1));
            recyclerView.getAdapter().notifyDataSetChanged();
            recyclerView.requestLayout();
        }
        balance.setText(String.valueOf(getBalance()) + " \u20BD");
    }


    private class ProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ProductViewHolder(LayoutInflater.from(context).inflate(R.layout.item_product, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((ProductViewHolder) holder).bind(mProducts.get(position));
        }
        @Override
        public int getItemCount() {
            return mProducts.size();
        }

    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.text_name)   TextView name;
        @Bind(R.id.text_price)  TextView price;
        @Bind(R.id.text_num)    TextView numbers;

        @Bind(R.id.checkbox)    CheckBox checkBox;

        public ProductViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(Pair<Product, Integer> product) {
            name.setText(product.first.getName());
            String strNum = "x" + String.valueOf(product.second);
            numbers.setText(strNum);
            String strPrice = product.first.getPrice().toString() + "\u20BD";
            price.setText(strPrice);
        }

    }


}
