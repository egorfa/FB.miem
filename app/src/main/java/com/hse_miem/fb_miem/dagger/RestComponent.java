package com.hse_miem.fb_miem.dagger;

import com.hse_miem.fb_miem.AllProductsActivity;
import com.hse_miem.fb_miem.fragments.TestFragment;
import com.hse_miem.fb_miem.server.RestModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Egor on 18/05/16.
 */
@Singleton
@Component(modules = RestModule.class)
public interface RestComponent {
    void inject(TestFragment testFragment);
    void inject(AllProductsActivity allProductsActivity);
}
