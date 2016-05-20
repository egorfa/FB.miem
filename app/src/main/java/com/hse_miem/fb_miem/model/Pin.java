package com.hse_miem.fb_miem.model;

import android.graphics.PointF;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Egor on 20/05/16.
 */
public class Pin {

    @Getter @Setter
    private int id;
    @Getter @Setter
    private String name;
    @Getter @Setter @SerializedName("point")
    private PointF pointF;


}
