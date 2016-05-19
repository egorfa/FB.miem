package com.hse_miem.fb_miem.server;

import com.squareup.okhttp.OkHttpClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by Egor on 18/05/16.
 */
@Module
@Singleton
public class RestModule {

    @Provides
    @Singleton
    fbAPI provideFbAPI() { return provideRetrofitBuilder().build().create(fbAPI.class); }

    private RestAdapter.Builder provideRetrofitBuilder() {
        OkHttpClient client = new OkHttpClient();

        return new RestAdapter.Builder()
                .setEndpoint(fbAPI.LINK_API)
                .setLogLevel(RestAdapter.LogLevel.FULL)
//                .setConverter(new GsonConverter(new GsonBuilder().create()))
                .setClient(new OkClient(client));
    }

}
