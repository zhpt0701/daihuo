package com.example.xl.foursling.unity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/4/1 0001.
 */
public class Province implements Parcelable{
    private int privinceid;
    private String princename;

    public int getPrivinceid() {
        return privinceid;
    }

    public void setPrivinceid(int privinceid) {
        this.privinceid = privinceid;
    }

    public String getPrincename() {
        return princename;
    }

    public void setPrincename(String princename) {
        this.princename = princename;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}
