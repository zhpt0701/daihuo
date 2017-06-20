package com.example.xl.foursling.unity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by admin on 2017/1/3.
 */

public class ProvinceAndCity implements Parcelable{
    /**
     * city
     * city_id
     * province
     */
    public String city;
    public String province;
    public String city_id;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
