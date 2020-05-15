package com.ns.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by ARVIND on 1/23/2016.
 */
public class CompanyData extends RealmObject{
    @PrimaryKey
    private int id;
    private String name;
    private int bse;
    private int nse;
    private String gp;

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBse(int bse) {
        this.bse = bse;
    }

    public void setNse(int nse) {
        this.nse = nse;
    }

    public void setGp(String gp) {
        this.gp = gp;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getBse() {
        return bse;
    }

    public int getNse() {
        return nse;
    }

    public String getGp() {
        return gp;
    }
}
