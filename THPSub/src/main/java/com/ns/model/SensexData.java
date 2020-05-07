package com.ns.model;

/**
 * Created by 9920 on 7/20/2017.
 */

public class SensexData {

    private NSEData mNSEData;
    private BSEData mBSEData;


    public NSEData getNSEData() {
        return mNSEData;
    }

    public void setNSEData(NSEData mNSEData) {
        this.mNSEData = mNSEData;
    }

    public BSEData getmBSEData() {
        return mBSEData;
    }

    public void setBSEData(BSEData mBSEData) {
        this.mBSEData = mBSEData;
    }
}
