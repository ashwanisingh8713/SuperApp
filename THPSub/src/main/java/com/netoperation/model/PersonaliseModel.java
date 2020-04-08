package com.netoperation.model;

import android.os.Parcel;
import android.os.Parcelable;

public class PersonaliseModel implements Parcelable {
    private String pId;
    private String title;
    private String image;
    private String value;
    private boolean isSelected;

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.pId);
        dest.writeString(this.title);
        dest.writeString(this.image);
        dest.writeString(this.value);
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
    }

    public PersonaliseModel() {
    }

    protected PersonaliseModel(Parcel in) {
        this.pId = in.readString();
        this.title = in.readString();
        this.image = in.readString();
        this.value = in.readString();
        this.isSelected = in.readByte() != 0;
    }

    public static final Creator<PersonaliseModel> CREATOR = new Creator<PersonaliseModel>() {
        @Override
        public PersonaliseModel createFromParcel(Parcel source) {
            return new PersonaliseModel(source);
        }

        @Override
        public PersonaliseModel[] newArray(int size) {
            return new PersonaliseModel[size];
        }
    };

    @Override
    public boolean equals( Object obj) {
        super.equals(obj);
        if(obj instanceof  PersonaliseModel) {
            PersonaliseModel model = (PersonaliseModel) obj;
            return model.getValue().equalsIgnoreCase(getValue());
        }
        return false;
    }

    @Override
    public int hashCode() {
        super.hashCode();
        if(getValue() == null) {
            return 31*41;
        }
        return getValue().hashCode();
    }
}
