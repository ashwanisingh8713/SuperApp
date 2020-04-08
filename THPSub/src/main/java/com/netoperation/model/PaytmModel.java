package com.netoperation.model;

import android.os.Parcel;
import android.os.Parcelable;

/*
Paytm Payment Gateway
* Response object for Generating Checksum API*/
public class PaytmModel implements Parcelable {

    public String CALLBACK_URL;
    public String CHANNEL_ID;
    public String CHECKSUMHASH;
    public String CUST_ID;
    public String INDUSTRY_TYPE_ID;
    public String MID;
    public String ORDER_ID;
    public String TXN_AMOUNT;
    public String WEBSITE;

    public PaytmModel(Parcel in) {
        CALLBACK_URL = in.readString();
        CHANNEL_ID = in.readString();
        CHECKSUMHASH = in.readString();
        CUST_ID = in.readString();
        INDUSTRY_TYPE_ID = in.readString();
        MID = in.readString();
        ORDER_ID = in.readString();
        TXN_AMOUNT = in.readString();
        WEBSITE = in.readString();
    }

    public PaytmModel() {

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(CALLBACK_URL);
        dest.writeString(CHANNEL_ID);
        dest.writeString(CHECKSUMHASH);
        dest.writeString(CUST_ID);
        dest.writeString(INDUSTRY_TYPE_ID);
        dest.writeString(MID);
        dest.writeString(ORDER_ID);
        dest.writeString(TXN_AMOUNT);
        dest.writeString(WEBSITE);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PaytmModel> CREATOR = new Creator<PaytmModel>() {
        @Override
        public PaytmModel createFromParcel(Parcel in) {
            return new PaytmModel(in);
        }

        @Override
        public PaytmModel[] newArray(int size) {
            return new PaytmModel[size];
        }
    };
}
