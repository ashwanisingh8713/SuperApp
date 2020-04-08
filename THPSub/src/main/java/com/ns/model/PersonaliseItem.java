package com.ns.model;


import androidx.annotation.Nullable;

public class PersonaliseItem {

    private String name;
    private String value;


    public PersonaliseItem(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        super.equals(obj);
        if(obj != null && obj instanceof PersonaliseItem) {
            PersonaliseItem pr = (PersonaliseItem) obj;
            return pr.getValue().equalsIgnoreCase(value) || value.equalsIgnoreCase(pr.getValue());
        }
        return false;
    }

    @Override
    public int hashCode() {
        super.hashCode();
        if(value != null) {
            return value.hashCode();
        } else {
            return 31;
        }
    }
}
