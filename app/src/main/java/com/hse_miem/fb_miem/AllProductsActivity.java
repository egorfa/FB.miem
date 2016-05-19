package com.hse_miem.fb_miem;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hse_miem.fb_miem.dagger.InjectorClass;
import com.hse_miem.fb_miem.model.Product;
import com.hse_miem.fb_miem.server.fbAPI;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Egor on 19/05/16.
 */
public class AllProductsActivity extends AppCompatActivity implements View.OnClickListener {


    private ArrayList<Product> mProducts;
    @Bind(R.id.recyclerview)    RecyclerView recyclerView;
    Subscription getAllProductsSubscription;

    @Inject fbAPI fBapi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_products);
        ButterKnife.bind(this);
        InjectorClass.getRestComponent().inject(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        initializeRecyclerView();
        getAllProducts();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (getAllProductsSubscription != null && !getAllProductsSubscription.isUnsubscribed())
            getAllProductsSubscription.unsubscribe();
    }

    private void getAllProducts() {
        getAllProductsSubscription = fBapi.getAllProducts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<Product>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(this.getClass().toString(), e.getMessage());
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(ArrayList<Product> products) {
                        mProducts = products;
                        recyclerView.setAdapter(new AllProductsAdapter(AllProductsActivity.this, AllProductsActivity.this));
                    }
                });
    }

    private void initializeRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

    }

    @Override
    public void onClick(View v) {
        int position = recyclerView.getChildAdapterPosition(v);
        Intent intent = new Intent();
        intent.putExtra("Product", mProducts.get(position));
        setResult(RESULT_OK, intent);
        finish();
    }


    private class AllProductsAdapter extends RecyclerView.Adapter {

        Context context;
        View.OnClickListener listener;

        public AllProductsAdapter(Context context, View.OnClickListener listener) {
            this.context = context;
            this.listener = listener;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ProductViewHolder(LayoutInflater.from(context).inflate(R.layout.item_all_products, parent, false), listener);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((ProductViewHolder)holder).bind(mProducts.get(position), context);
        }

        @Override
        public int getItemCount() {
            return mProducts.size();
        }
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.image)       ImageView img;
        @Bind(R.id.text_name)   TextView name;
        @Bind(R.id.text_price)  TextView price;

        public ProductViewHolder(View itemView, View.OnClickListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(listener);
        }
        public void bind(Product product, Context context) {
            name.setText(product.getName());
            price.setText(product.getPrice().toString() + " \u20BD");
            Glide.with(context)
                    .load(product.getImage())
                    .fitCenter()
                    .crossFade()
                    .into(img);
        }

    }
}
