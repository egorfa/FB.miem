package com.hse_miem.fb_miem.fragments.ShopCartMapTabs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hse_miem.fb_miem.OnPinClickListener;
import com.hse_miem.fb_miem.R;
import com.hse_miem.fb_miem.dagger.InjectorClass;
import com.hse_miem.fb_miem.fragments.BaseFragment;
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
public class ShopMapFragment extends BaseFragment implements MapViewListener {

    private Bitmap bitmap;
    private ArrayList<Product> mProducts;
    private ArrayList<Pin> mPins;
    private List<PointF> nodes;
    private List<PointF> nodesContract;
    private List<PointF> marks;

    @Bind(R.id.mapview)     MapView map;

    private MarkLayer markLayer;
    private RouteLayer routeLayer;

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

        bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(getActivity().getAssets().open("map.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }


        mProducts = new ArrayList<>();
        mPins = new ArrayList<>();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        nodes = TestData.getNodes();
        nodesContract = TestData.getNodesContacts();
        MapUtils.init(nodes.size(), nodesContract.size());
        mProducts = ((ShopCartMapTabFragment)getParentFragment()).getProducts();
        loadPins();

        final BluetoothManager bluetoothManager = (BluetoothManager) getActivity().getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void loadPins() {
        subscriptionPin = fbApi.getAllPins()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMapIterable(pins -> pins)
                .flatMap(ShopMapFragment::getItemPin)
                .filter(pin -> {
                    final boolean[] flag = {false};
                    Observable.from(mProducts)
                            .subscribe(product -> {
                                if (product.getId() == pin.getId())
                                    flag[0] = true;
                            });
                    return flag[0];
                })
                .subscribe(new Observer<Pin>() {
                    @Override
                    public void onCompleted() {
                        setupPins();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(getClass().getName(), e.getMessage());
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Pin pin) {
                        mPins.add(pin);
                        scanLeDevice(true);
                    }
                });
    }

    private void setupPins() {
        map.loadMap(bitmap);
        map.setMapViewListener(this);
    }


    private static rx.Observable<Pin> getItemPin(Pin pin) {
        Pin pin1 = pin;
        return rx.Observable.just(pin1);
    }

    @Override
    public void onPause() {
        super.onPause();
        unsubscribe(subscriptionPin);
    }

    @Override
    public void onMapLoadSuccess() {

        List<PointF> marks = new ArrayList<>();
        List<String> marksNames = new ArrayList<>();
                Observable.from(mPins)
                .flatMap(pin -> Observable.just(Pair.create(pin.getPointF(), pin.getName())))
                .subscribe(new Observer<Pair<PointF, String>>() {
                    @Override
                    public void onCompleted() {
                        marks.add(new PointF(190,173));
                        markLayer = new MarkLayer(map, marks, marksNames);
                        routeLayer = new RouteLayer(map);

                        markLayer.setMarkIsClickListener(num -> {
                            PointF target = new PointF(marks.get(num).x, marks.get(num).y);
                            List<Integer> routeList = MapUtils.getShortestDistanceBetweenTwoPoints(marks.get(marks.size()-1), target, nodes, nodesContract);
                            routeLayer.setNodeList(nodes);
                            routeLayer.setRouteList(routeList);
                            map.refresh();
                        });
                        map.addLayer(markLayer);
                        map.addLayer(routeLayer);
                        map.refresh();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Pair<PointF, String> pointFStringPair) {
                        marks.add(pointFStringPair.first);
                        marksNames.add(pointFStringPair.second);
                    }
                });

    }


    @Override
    public void onMapLoadFail() {
        Snackbar.make(getView().findViewById(R.id.clayout), "Не удалось загрузить карту", Snackbar.LENGTH_SHORT).show();
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
