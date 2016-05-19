package com.hse_miem.fb_miem.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hse_miem.fb_miem.R;
import com.hse_miem.fb_miem.dagger.InjectorClass;
import com.hse_miem.fb_miem.model.Beacon;
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
 * Created by Egor on 18/05/16.
 */
public class TestFragment extends BaseFragment {

    @Inject fbAPI fbAPI;
    ArrayList<Beacon> mBeacons;
    Subscription subscriptionBeacon;

    @Bind(R.id.recycler_beacons) RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test, container, false);
        ButterKnife.bind(this, view);
        InjectorClass.getRestComponent().inject(this);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("TestFragment");
        initializeRecycler();
    }

    private void initializeRecycler() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(mLayoutManager);
    }

    @Override
    public void onResume() {
        super.onResume();
        mBeacons = new ArrayList<>();
        subscriptionBeacon = fbAPI.getBeacons()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<Beacon>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("ERROR GET_BEACON", e.getMessage());
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(ArrayList<Beacon> beacons) {
                        mBeacons = beacons;
                    }
                });


    }


    @Override
    public void onPause() {
        super.onPause();
        unsubscribe(subscriptionBeacon);
    }
}
