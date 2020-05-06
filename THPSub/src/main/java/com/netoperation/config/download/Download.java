package com.netoperation.config.download;


import android.os.Parcel;
import android.os.Parcelable;

public class Download implements Parcelable {

    public Download(){

    }

    private int status;
    private int currentFileSize;
    private int totalFileSize;
    private String url;
    private String localFilePath;
    private int requestNumber;

    public int getRequestNumber() {
        return requestNumber;
    }

    public void setRequestNumber(int requestNumber) {
        this.requestNumber = requestNumber;
    }

    public String getLocalFilePath() {
        return localFilePath;
    }

    public void setLocalFilePath(String localFilePath) {
        this.localFilePath = localFilePath;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCurrentFileSize() {
        return currentFileSize;
    }

    public void setCurrentFileSize(int currentFileSize) {
        this.currentFileSize = currentFileSize;
    }

    public int getTotalFileSize() {
        return totalFileSize;
    }

    public void setTotalFileSize(int totalFileSize) {
        this.totalFileSize = totalFileSize;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.status);
        dest.writeInt(this.currentFileSize);
        dest.writeInt(this.totalFileSize);
        dest.writeString(this.url);
        dest.writeString(this.localFilePath);
        dest.writeInt(this.requestNumber);
    }

    protected Download(Parcel in) {
        this.status = in.readInt();
        this.currentFileSize = in.readInt();
        this.totalFileSize = in.readInt();
        this.url = in.readString();
        this.localFilePath = in.readString();
        this.requestNumber = in.readInt();
    }

    public static final Creator<Download> CREATOR = new Creator<Download>() {
        @Override
        public Download createFromParcel(Parcel source) {
            return new Download(source);
        }

        @Override
        public Download[] newArray(int size) {
            return new Download[size];
        }
    };
}
