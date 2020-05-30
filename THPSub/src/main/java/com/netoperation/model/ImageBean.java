package com.netoperation.model;

import io.realm.RealmObject;

/**
 * Created by root on 24/8/16.
 */

public class ImageBean extends RealmObject {
    private String im;
    private String ca;
    private String im_v2;

    public String getIm_v2() {
        return im_v2;
    }

    public void setIm_v2(String im_v2) {
        this.im_v2 = im_v2;
    }

    public String getIm() {
        return im;
    }

    public void setIm(String im) {
        this.im = im;
    }

    public String getCa() {
        return ca;
    }

    public void setCa(String ca) {
        this.ca = ca;
    }
}
