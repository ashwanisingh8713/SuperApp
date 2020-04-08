package com.ns.model;

import com.netoperation.model.TxnDataBean;
import com.ns.adapter.UserPlanAdapter;

import java.util.ArrayList;
import java.util.List;

public class SubscriptionPlanModel {

    private int vtType;
    private TxnDataBean txnDataBean;
    private ArrayList<TxnDataBean> userPlanDataBean;
    private String title;
    private String subTitle;


    public SubscriptionPlanModel(int vtType) {
        this.vtType = vtType;
        this.txnDataBean = txnDataBean;
        this.userPlanDataBean = userPlanDataBean;
    }

    public ArrayList<TxnDataBean> getUserPlanDataBean() {
        return userPlanDataBean;
    }

    public void setUserPlanDataBean(ArrayList<TxnDataBean> userPlanDataBean) {
        this.userPlanDataBean = userPlanDataBean;
    }

    public int getVtType() {
        return vtType;
    }

    public void setVtType(int vtType) {
        this.vtType = vtType;
    }

    public TxnDataBean getTxnDataBean() {
        return txnDataBean;
    }

    public void setTxnDataBean(TxnDataBean txnDataBean) {
        this.txnDataBean = txnDataBean;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }
}
