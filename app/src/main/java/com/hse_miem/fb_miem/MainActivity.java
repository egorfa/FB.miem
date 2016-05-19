package com.hse_miem.fb_miem;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.hse_miem.fb_miem.fragments.ProductFragment;
import com.hse_miem.fb_miem.fragments.ShopCartMapTabFragment;
import com.hse_miem.fb_miem.model.Product;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    public static int ALL_PRODUCTS_CODE = 1001;

    private ArrayList<Pair<Product, Integer>> mData;

    @Bind(R.id.drawer_layout)       DrawerLayout mDrawerLayout;
    @Bind(R.id.navigation_view)     NavigationView navigationView;
    @Bind(R.id.toolbar)             Toolbar mToolbar;

    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setupToolBar();
        setupNavigationDrawerContent(navigationView);
        navigationView.getMenu().performIdentifierAction(R.id.item_header_2, 0);
    }

    private void setupToolBar() {
        mToolbar.setTitleTextColor(ContextCompat.getColor(getBaseContext(), R.color.white));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white_36dp);

    }

    private void setupNavigationDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                menuItem -> {
                    switch (menuItem.getItemId()) {
                        case R.id.item_header_1:
                            setSelectedItem(0);
                            break;
                        case R.id.item_header_2:
                            setSelectedItem(1);
                            break;
//                        case R.id.item_header_3:
//                            setSelectedItem(2);
//                            break;
//                        case R.id.item_header_4:
//                            break;
                    }
                    menuItem.setChecked(true);

                    return true;
                });
    }

    private void setSelectedItem(int position) {
        Fragment fragment = null;
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch (position){
            case 0:
                if (mData != null && !mData.isEmpty())
                    fragment = ShopCartMapTabFragment.newInstance(mData);
                else fragment = new ShopCartMapTabFragment();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
//                fragment = new TestFragment();
                break;
            case 1:
                fragment = new ProductFragment();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
//                fragment = new CreatePurchaseFragment();
                break;
            case 2:
                break;
            case 3:
                break;
        }
        currentFragment = fragment;

        mDrawerLayout.closeDrawer(GravityCompat.START);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(resultCode) {
            case RESULT_OK:
                if (requestCode == ALL_PRODUCTS_CODE) {
                    if (currentFragment instanceof ProductFragment)
                        ((ProductFragment)currentFragment).updateArray(data.getExtras().getParcelable("Product"));
                }
        }

    }


    public void goToMap(ArrayList<Pair<Product, Integer>> mProductsChoosen) {
        if (mProductsChoosen!= null && mProductsChoosen.size() > 0)
            mData = mProductsChoosen;
        setSelectedItem(0);
    }
}
