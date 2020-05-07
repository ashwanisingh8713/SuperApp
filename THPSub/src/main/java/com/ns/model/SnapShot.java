package com.ns.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by arvind on 25/1/16.
 */
public class SnapShot implements Parcelable {

    String bsec;
    String nses;
    String scn;
    String lcn;
    String isin;
    String csn;

    public String getBsec() {
        return bsec;
    }

    public String getNses() {
        return nses;
    }

    public String getScn() {
        return scn;
    }

    public String getLcn() {
        return lcn;
    }

    public String getIsin() {
        return isin;
    }

    public String getCsn() {
        return csn;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.bsec);
        dest.writeString(this.nses);
        dest.writeString(this.scn);
        dest.writeString(this.lcn);
        dest.writeString(this.isin);
        dest.writeString(this.csn);
    }

    public SnapShot() {
    }

    protected SnapShot(Parcel in) {
        this.bsec = in.readString();
        this.nses = in.readString();
        this.scn = in.readString();
        this.lcn = in.readString();
        this.isin = in.readString();
        this.csn = in.readString();
    }

    public static final Parcelable.Creator<SnapShot> CREATOR = new Parcelable.Creator<SnapShot>() {
        public SnapShot createFromParcel(Parcel source) {
            return new SnapShot(source);
        }

        public SnapShot[] newArray(int size) {
            return new SnapShot[size];
        }
    };
}
