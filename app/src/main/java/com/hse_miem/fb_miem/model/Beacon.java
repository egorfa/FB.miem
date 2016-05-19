package com.hse_miem.fb_miem.model;

import android.bluetooth.BluetoothDevice;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Egor on 18/05/16.
 */
public class Beacon {

    @Getter @Setter
    private Coordinates coordinates;

    @Getter @Setter
    private String name;

    @Getter @Setter
    private Integer major;
    @Getter @Setter
    private Integer minor;

    @Getter @Setter
    BluetoothDevice BLdevice;
    @Getter @Setter
    private String UUID;
    @Getter @Setter
    private int TxPower;
    @Getter @Setter
    private int Rssi;

    public Beacon(BluetoothDevice BLdevice, String name, String UUID, Integer major, Integer minor, int txPower, int rssi) {
        this.name = name;
        this.major = major;
        this.minor = minor;
        this.BLdevice = BLdevice;
        this.UUID = UUID;
        TxPower = txPower;
        Rssi = rssi;
    }

    private class Coordinates {
        @Getter @Setter
        private double x;
        @Getter @Setter
        private double y;
    }
}
