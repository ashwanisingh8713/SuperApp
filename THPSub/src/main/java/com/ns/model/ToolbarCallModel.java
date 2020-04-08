package com.ns.model;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ashwanisingh on 05/10/18.
 */

public class ToolbarCallModel implements Parcelable {

    private String from;
    private Bundle bundle;

    public ToolbarCallModel(String from, Bundle bundle) {
        this.from = from;
        this.bundle = bundle;
    }

    public String getFrom() {
        return from;
    }

    public Bundle getBundle() {
        return bundle;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.from);
        dest.writeBundle(this.bundle);
    }

    protected ToolbarCallModel(Parcel in) {
        this.from = in.readString();
        this.bundle = in.readBundle();
    }

    public static final Creator<ToolbarCallModel> CREATOR = new Creator<ToolbarCallModel>() {
        @Override
        public ToolbarCallModel createFromParcel(Parcel source) {
            return new ToolbarCallModel(source);
        }

        @Override
        public ToolbarCallModel[] newArray(int size) {
            return new ToolbarCallModel[size];
        }
    };
}
