package com.ns.model;

public class MyAddressModel {

    private int viewType;
    private String address;

    public MyAddressModel(int viewType, String address) {
        this.viewType = viewType;
        this.address = address;
    }


    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
