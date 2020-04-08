package com.netoperation.model;

import android.os.Parcel;
import android.os.Parcelable;

public class PaytmTransactionStatus implements Parcelable {
    private String ORDERID;
    public String TXNID;
    public String STATUS;
    public String RESPMSG;
    public String RESPCODE;
    private String PAYMENTMODE;
    private String GATEWAYNAME;
    private String BANKTXNID;
    private String TXNDATE;
    private String TXNAMOUNT;
    private String CURRENCY;

    protected PaytmTransactionStatus(Parcel in) {
        ORDERID = in.readString();
        TXNID = in.readString();
        STATUS = in.readString();
        RESPMSG = in.readString();
        RESPCODE = in.readString();
        PAYMENTMODE = in.readString();
        GATEWAYNAME = in.readString();
        BANKTXNID = in.readString();
        TXNDATE = in.readString();
        TXNAMOUNT = in.readString();
        CURRENCY = in.readString();
    }

    public static final Creator<PaytmTransactionStatus> CREATOR = new Creator<PaytmTransactionStatus>() {
        @Override
        public PaytmTransactionStatus createFromParcel(Parcel in) {
            return new PaytmTransactionStatus(in);
        }

        @Override
        public PaytmTransactionStatus[] newArray(int size) {
            return new PaytmTransactionStatus[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(ORDERID);
        parcel.writeString(TXNID);
        parcel.writeString(STATUS);
        parcel.writeString(RESPMSG);
        parcel.writeString(RESPCODE);
        parcel.writeString(PAYMENTMODE);
        parcel.writeString(GATEWAYNAME);
        parcel.writeString(BANKTXNID);
        parcel.writeString(TXNDATE);
        parcel.writeString(TXNAMOUNT);
        parcel.writeString(CURRENCY);
    }
}
