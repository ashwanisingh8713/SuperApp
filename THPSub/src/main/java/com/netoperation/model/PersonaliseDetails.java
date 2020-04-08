package com.netoperation.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class PersonaliseDetails implements Parcelable {
    private String name;
    private ArrayList<PersonaliseModel> values=new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<PersonaliseModel> getValues() {
        return values;
    }

    public void addPersonalise(PersonaliseModel model) {
        values.add(model);
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeTypedList(this.values);
    }

    public PersonaliseDetails() {
    }

    protected PersonaliseDetails(Parcel in) {
        this.name = in.readString();
        this.values = in.createTypedArrayList(PersonaliseModel.CREATOR);
    }

    public static final Creator<PersonaliseDetails> CREATOR = new Creator<PersonaliseDetails>() {
        @Override
        public PersonaliseDetails createFromParcel(Parcel source) {
            return new PersonaliseDetails(source);
        }

        @Override
        public PersonaliseDetails[] newArray(int size) {
            return new PersonaliseDetails[size];
        }
    };
}
