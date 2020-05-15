package com.ns.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by arvind on 25/1/16.
 */
public class CompanyNseData implements Parcelable {
    @SerializedName("cp")
    double cp;
    @SerializedName("ch")
    double ch;
    @SerializedName("per")
    double per;
    @SerializedName("dop")
    double dop;
    @SerializedName("dhi")
    double dhi;
    @SerializedName("dlo")
    double dlo;
    @SerializedName("prepr")
    double bsep;
    @SerializedName("nse52h")
    double bse52h;
    @SerializedName("nse52l")
    double bse52l;

    @SerializedName("snapshot")
    SnapShot snapShot;

    public double getCp() {
        return cp;
    }

    public double getCh() {
        return ch;
    }

    public double getPer() {
        return per;
    }

    public double getDop() {
        return dop;
    }

    public double getDhi() {
        return dhi;
    }

    public double getDlo() {
        return dlo;
    }

    public double getBsep() {
        return bsep;
    }

    public double getBse52h() {
        return bse52h;
    }

    public double getBse52l() {
        return bse52l;
    }

    public SnapShot getSnapShot() {
        return snapShot;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.cp);
        dest.writeDouble(this.ch);
        dest.writeDouble(this.per);
        dest.writeDouble(this.dop);
        dest.writeDouble(this.dhi);
        dest.writeDouble(this.dlo);
        dest.writeDouble(this.bsep);
        dest.writeDouble(this.bse52h);
        dest.writeDouble(this.bse52l);
        dest.writeParcelable(this.snapShot, flags);
    }

    public CompanyNseData() {
    }

    protected CompanyNseData(Parcel in) {
        this.cp = in.readDouble();
        this.ch = in.readDouble();
        this.per = in.readDouble();
        this.dop = in.readDouble();
        this.dhi = in.readDouble();
        this.dlo = in.readDouble();
        this.bsep = in.readDouble();
        this.bse52h = in.readDouble();
        this.bse52l = in.readDouble();
        this.snapShot = in.readParcelable(SnapShot.class.getClassLoader());
    }

    public static final Parcelable.Creator<CompanyNseData> CREATOR = new Parcelable.Creator<CompanyNseData>() {
        public CompanyNseData createFromParcel(Parcel source) {
            return new CompanyNseData(source);
        }

        public CompanyNseData[] newArray(int size) {
            return new CompanyNseData[size];
        }
    };
}
