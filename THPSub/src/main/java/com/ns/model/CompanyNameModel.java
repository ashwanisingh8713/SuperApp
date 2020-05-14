package com.ns.model;

import java.util.List;

/**
 * Created by arvind on 22/1/16.
 */
public class CompanyNameModel {


    private int s;
    private String date;

    private List<CompanyData> data;

    public void setS(int s) {
        this.s = s;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setData(List<CompanyData> data) {
        this.data = data;
    }

    public int getS() {
        return s;
    }

    public String getDate() {
        return date;
    }

    public List<CompanyData> getData() {
        return data;
    }

}
