package com.ns.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by arvind on 18/1/16.
 */
public class NSEData implements SensexStatus {

    private int status = NONE;

    @SerializedName("cp")
    private double cp;

    @SerializedName("da")
    private String da;

    @SerializedName("per")
    private double per;

    @SerializedName("ch")
    private double ch;

    @SerializedName("snapshot")
    SnapShot snapShot;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDa() {
        return da;
    }

    public double getCp() {
        return cp;
    }

    public double getPer() {
        return per;
    }

    public double getCh() {
        return ch;
    }

    public SnapShot getSnapShot() {
        return snapShot;
    }

    public void setCp(double cp) {
        this.cp = cp;
    }

    public void setDa(String da) {
        this.da = da;
    }

    public void setPer(double per) {
        this.per = per;
    }

    public void setCh(double ch) {
        this.ch = ch;
    }

    public void setSnapShot(SnapShot snapShot) {
        this.snapShot = snapShot;
    }
}
