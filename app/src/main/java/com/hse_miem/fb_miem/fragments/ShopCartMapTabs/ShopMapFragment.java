package com.hse_miem.fb_miem.fragments.ShopCartMapTabs;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hse_miem.fb_miem.LeScanCallback;
import com.hse_miem.fb_miem.OnPinClickListener;
import com.hse_miem.fb_miem.R;
import com.hse_miem.fb_miem.dagger.InjectorClass;
import com.hse_miem.fb_miem.fragments.BaseFragment;
import com.hse_miem.fb_miem.model.Beacon;
import com.hse_miem.fb_miem.model.Pin;
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
 * Created by Egor on 20/05/16.
 */
public class ShopMapFragment extends BaseFragment implements OnPinClickListener{

    private ArrayList<Product> mProducts;
    private ArrayList<Pin> mPins;

    @Bind(R.id.map)     ImageView map;

    @Inject fbAPI fbApi;

    private Subscription subscriptionPin;

    private BluetoothAdapter mBluetoothAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getActivity();
        View view = inflater.inflate(R.layout.fragment_shop_map, container, false);
        ButterKnife.bind(this, view);
        InjectorClass.getRestComponent().inject(this);
        mProducts = new ArrayList<>();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        final BluetoothManager bluetoothManager = (BluetoothManager) getActivity().getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
    }

    @Override
    public void onResume() {
        super.onResume();

        loadPins();
    }

    private void loadPins() {
        subscriptionPin = fbApi.getAllPins()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<Pin>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(this.getClass().getName(), e.getMessage());
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(ArrayList<Pin> pins) {
                        mPins = pins;
                        setPinOnMap();
                        scanLeDevice(true);
                    }
                });
    }

    private void setPinOnMap() {
//        map.onDraw);
//        Bitmap map = BitmapFactory.decodeResource(getResources(), R.drawable.map);
//        canvas.drawBitmap(map, 0, 0, null);
//
//        Bitmap marker = BitmapFactory.decodeResource(getResources(), R.drawable.marker);
//        canvas.drawBitmap(marker, xPositionFor1stMarker, yPositionFor1stMarker, null);
//        canvas.drawBitmap(marker, xPositionFor2ndMarker, yPositionFor2ndMarker, null);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onPinClicked(int position) {

    }

    private void scanLeDevice(final boolean enable) {
        Log.d("LeScan", "scan started");
        final LeScanCallback callback = new LeScanCallback() {
            @Override
            public void onDeviceFound(Beacon device) {
                Log.d("LeScan", "major = " + device.getMajor());
                Log.d("LeScan", "minor = " + device.getMinor());
            }
        };

        mBluetoothAdapter.startLeScan(callback);
    }
}
