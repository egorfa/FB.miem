package com.hse_miem.fb_miem.server;

import com.hse_miem.fb_miem.model.Beacon;
import com.hse_miem.fb_miem.model.Pin;
import com.hse_miem.fb_miem.model.Product;

import java.util.ArrayList;

import retrofit.http.GET;
import rx.Observable;

/**
 * Created by Egor on 18/05/16.
 */
public interface fbAPI {

    String LINK_API = "https://dl.dropbox.com/s";

    @GET("/it3mmcwitc6ydrz/beacons.json?dl=0")
    Observable<ArrayList<Beacon>> getBeacons();

    @GET("/to3z6ag4pucfnk5/allproducts.json?dl=0")
    Observable<ArrayList<Product>> getAllProducts();

    @GET("/scnnscxildwtnzs/pins.json?dl=0")
    Observable<ArrayList<Pin>> getAllPins();
}
