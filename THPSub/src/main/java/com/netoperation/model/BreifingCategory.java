package com.netoperation.model;

import java.util.ArrayList;

public class BreifingCategory {

    private String time;
    private ArrayList<MorningBean> data;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public ArrayList<MorningBean> getData() {
        return data;
    }

    public void setData(ArrayList<MorningBean> data) {
        this.data = data;
    }
}
