package com.netoperation.config.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ImportantMsg implements Parcelable {

    private String title;
    private String msg;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.msg);
    }

    public ImportantMsg() {
    }

    protected ImportantMsg(Parcel in) {
        this.title = in.readString();
        this.msg = in.readString();
    }

    public static final Parcelable.Creator<ImportantMsg> CREATOR = new Parcelable.Creator<ImportantMsg>() {
        @Override
        public ImportantMsg createFromParcel(Parcel source) {
            return new ImportantMsg(source);
        }

        @Override
        public ImportantMsg[] newArray(int size) {
            return new ImportantMsg[size];
        }
    };
}
