package com.hse_miem.fb_miem.model;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Egor on 18/05/16.
 */
public class Product implements Parcelable {

    @Getter @Setter
    private int id;
    @Getter @Setter
    private String name;
    @Getter @Setter
    private Double price;
    @Getter @Setter
    private long vendor_code;
    @Getter @Setter
    private String category;
    @Getter @Setter
    private String image;

    public Product() {
    }

    protected Product(Parcel in) {
        id = in.readInt();
        name = in.readString();
        price = in.readDouble();
        vendor_code = in.readLong();
        category = in.readString();
        image = in.readString();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }



    @Override
    public void writeToParcel(Parcel dest, int f) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeDouble(price);
        dest.writeLong(vendor_code);
        dest.writeString(category);
        dest.writeString(image);

    }

}
