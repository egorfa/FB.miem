package com.hse_miem.fb_miem.fragments.ShopCartMapTabs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.hse_miem.fb_miem.R;
import com.hse_miem.fb_miem.fragments.BaseFragment;
import com.hse_miem.fb_miem.model.Product;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Egor on 19/05/16.
 */
public class ShopListFragment extends BaseFragment{

    @Bind(R.id.text_balance)        TextView balance;
    @Bind(R.id.recycler_products)   RecyclerView recyclerView;

    ArrayList<Pair<Product, Integer>> mData;

    public static ShopListFragment newInstance(ArrayList<Pair<Product, Integer>> data) {

        Bundle args = new Bundle();
        args.putSerializable("Data", data);

        ShopListFragment fragment = new ShopListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getActivity();
        View view = inflater.inflate(R.layout.fragment_shop_list, container, false);
        ButterKnife.bind(this, view);
        mData = new ArrayList<>();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initializeRecyclerView();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (getArguments() != null)
            mData = (ArrayList<Pair<Product, Integer>>) getArguments().getSerializable("Data");

        balance.setText(String.valueOf(getBalance()));

        recyclerView.setAdapter(new ProductListAdapter());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_list_products, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_done_all:

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initializeRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(mLayoutManager);
    }


    private double getBalance() {
        double balance = -1;
        for (Pair<Product, Integer> p: mData){
            balance += p.second * p.first.getPrice();
        }
        if (mData.size() != 0) return balance;
        else return 0;
    }

    private class ProductListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ProductViewHolder(LayoutInflater.from(context).inflate(R.layout.item_product_list, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((ProductViewHolder) holder).bind(mData.get(position));
        }
        @Override
        public int getItemCount() {
            return mData.size();
        }

    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.text_name)   TextView name;
        @Bind(R.id.text_price)  TextView price;
        @Bind(R.id.text_num)    TextView numbers;

        @Bind(R.id.checkbox)
        CheckBox checkBox;

        public ProductViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                mData.remove(getAdapterPosition());
                recyclerView.getAdapter().notifyItemRemoved(getAdapterPosition());
                recyclerView.requestLayout();
            });
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
