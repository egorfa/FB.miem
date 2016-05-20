package com.hse_miem.fb_miem.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.hse_miem.fb_miem.R;
import com.hse_miem.fb_miem.fragments.ShopCartMapTabs.ShopListFragment;
import com.hse_miem.fb_miem.fragments.ShopCartMapTabs.ShopMapFragment;
import com.hse_miem.fb_miem.model.Product;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;

/**
 * Created by Egor on 19/05/16.
 */
public class ShopCartMapTabFragment extends BaseFragment {

    @Bind(R.id.pager)       ViewPager viewPager;
    @Bind(R.id.tabLayout)   TabLayout tabLayout;

    private ShopCartMapTabAdapter adapter;

    private ArrayList<Pair<Product, Integer>> mData;

    public static Fragment newInstance(ArrayList<Pair<Product, Integer>> mData) {
        Bundle args = new Bundle();
        args.putSerializable("Data", mData);

        ShopCartMapTabFragment fragment = new ShopCartMapTabFragment();
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
        View view = inflater.inflate(R.layout.fragment_tab_card_map, container, false);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.header1));

        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new ShopCartMapTabAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public void onResume() {
        super.onResume();

        if (getArguments() != null) {
            mData = (ArrayList<Pair<Product, Integer>>) getArguments().getSerializable("Data");
        }
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

    public ArrayList<Product> getProducts() {
        ArrayList<Product> products = new ArrayList<>();
        Observable.from(mData)
                .subscribe(productIntegerPair -> {
                    products.add(productIntegerPair.first);
                });
        return  products;
    }

    private class ShopCartMapTabAdapter extends FragmentPagerAdapter{
        private Fragment _currentPrimaryItem;
        private Fragment _lastCurentPrimaryItem;

        private String[] titles = new String[]{
                getResources().getString(R.string.tab_map),
                getResources().getString(R.string.tab_cart)
        };

        public ShopCartMapTabAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new ShopMapFragment();
                case 1:
                    return ShopListFragment.newInstance(mData);

            }
            return null;
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);

            _currentPrimaryItem = (Fragment) object;
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position < titles.length) {
                return titles[position];
            }

            return null;
        }
    }
}
