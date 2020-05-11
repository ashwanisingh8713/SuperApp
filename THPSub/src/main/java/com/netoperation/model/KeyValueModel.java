package com.netoperation.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

public class KeyValueModel implements Parcelable {


    /**
     * name : Afghanistan
     * code : AF
     */

    private String name;
    private String code;
    private String State;
    private String Code;

    private String userId;
    private String contact;
    private boolean isNewAccount;

    private String token;

    public String getToken() {
        return "Bearer "+token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isNewAccount() {
        return isNewAccount;
    }

    public void setNewAccount(boolean newAccount) {
        isNewAccount = newAccount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setState(String state) {
        State = state;
    }

    public String getState() {
        return State;
    }

    public String getStateCode() {
        return Code;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public void setName(String name) {
        this.name = name;
    }


    public KeyValueModel() {
    }


    @Override
    public boolean equals(@Nullable Object obj) {
        super.equals(obj);
        if(obj != null && obj instanceof KeyValueModel) {
            KeyValueModel model = (KeyValueModel) obj;
            if(model.code != null && code != null && model.code.equalsIgnoreCase(code)) {
                return true;
            }
            return model.Code != null && Code != null && model.Code.equalsIgnoreCase(Code);
        }
        return false;
    }

    @Override
    public int hashCode() {
        super.hashCode();
        return 31*9;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.code);
        dest.writeString(this.State);
        dest.writeString(this.Code);
        dest.writeString(this.userId);
        dest.writeString(this.contact);
        dest.writeByte(this.isNewAccount ? (byte) 1 : (byte) 0);
        dest.writeString(this.token);
    }

    protected KeyValueModel(Parcel in) {
        this.name = in.readString();
        this.code = in.readString();
        this.State = in.readString();
        this.Code = in.readString();
        this.userId = in.readString();
        this.contact = in.readString();
        this.isNewAccount = in.readByte() != 0;
        this.token = in.readString();
    }

    public static final Creator<KeyValueModel> CREATOR = new Creator<KeyValueModel>() {
        @Override
        public KeyValueModel createFromParcel(Parcel source) {
            return new KeyValueModel(source);
        }

        @Override
        public KeyValueModel[] newArray(int size) {
            return new KeyValueModel[size];
        }
    };
}
