package com.example.xl.foursling.unity;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/4/12 0012.
 */
public class Record implements Parcelable{
    private int recordid;
    private String recordnickname;
    private String recorduserid;
    private String recordaddress;
    private String IP;
    private String winner;
    private String winnerid;
    private String productid;
    private String ID;
    private String recordlucknumber;
    private String recordpoplenumber;
    private String recordgoodsname;
    private String recordgoodstimepublish;
    private String recordimage;
    private String PNumber;
    private String State;
    private String TradingCount;

    public String getPNumber() {
        return PNumber;
    }

    public void setPNumber(String PNumber) {
        this.PNumber = PNumber;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getTradingCount() {
        return TradingCount;
    }

    public void setTradingCount(String tradingCount) {
        TradingCount = tradingCount;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public String getWinnerid() {
        return winnerid;
    }

    public void setWinnerid(String winnerid) {
        this.winnerid = winnerid;
    }

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public String getRecordimage() {
        return recordimage;
    }

    public void setRecordimage(String recordimage) {
        this.recordimage = recordimage;
    }

    public int getRecordid() {
        return recordid;
    }

    public void setRecordid(int recordid) {
        this.recordid = recordid;
    }

    public String getRecordnickname() {
        return recordnickname;
    }

    public void setRecordnickname(String recordnickname) {
        this.recordnickname = recordnickname;
    }

    public String getRecorduserid() {
        return recorduserid;
    }

    public void setRecorduserid(String recorduserid) {
        this.recorduserid = recorduserid;
    }

    public String getRecordaddress() {
        return recordaddress;
    }

    public void setRecordaddress(String recordaddress) {
        this.recordaddress = recordaddress;
    }

    public String getRecordlucknumber() {
        return recordlucknumber;
    }

    public void setRecordlucknumber(String recordlucknumber) {
        this.recordlucknumber = recordlucknumber;
    }

    public String getRecordpoplenumber() {
        return recordpoplenumber;
    }

    public void setRecordpoplenumber(String recordpoplenumber) {
        this.recordpoplenumber = recordpoplenumber;
    }

    public String getRecordgoodsname() {
        return recordgoodsname;
    }

    public void setRecordgoodsname(String recordgoodsname) {
        this.recordgoodsname = recordgoodsname;
    }

    public String getRecordgoodstimepublish() {
        return recordgoodstimepublish;
    }

    public void setRecordgoodstimepublish(String recordgoodstimepublish) {
        this.recordgoodstimepublish = recordgoodstimepublish;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
