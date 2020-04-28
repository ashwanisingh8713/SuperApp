package com.ns.model;

import com.netoperation.model.AdData;
import com.netoperation.model.ArticleBean;
import com.netoperation.model.MeBean;

public class AppTabContentModel {

    private int viewType;
    private ArticleBean bean;
    private String uniqueIdForView;
    private AdData adData;
    private MeBean meBean;

    public AppTabContentModel(int viewType) {
        this.viewType = viewType;
    }

    public AppTabContentModel(int viewType, String uniqueIdForView) {
        this.viewType = viewType;
        this.uniqueIdForView = uniqueIdForView;
    }

    public MeBean getMeBean() {
        return meBean;
    }

    public void setMeBean(MeBean meBean) {
        this.meBean = meBean;
    }

    public int getViewType() {
        return viewType;
    }

    public ArticleBean getBean() {
        return bean;
    }

    public String getUniqueIdForView() {
        return uniqueIdForView;
    }

    public void setBean(ArticleBean bean) {
        this.bean = bean;
    }

    public AdData getAdData() {
        return adData;
    }

    public void setAdData(AdData adData) {
        this.adData = adData;
    }

    @Override
    public boolean equals(Object obj) {
        super.equals(obj);
        if(obj != null & obj instanceof AppTabContentModel) {
            AppTabContentModel model = (AppTabContentModel) obj ;
            if(model.getUniqueIdForView() != null) {
                return uniqueIdForView != null && model.uniqueIdForView != null && uniqueIdForView.equals(model.uniqueIdForView);
            }
            else if(bean != null) {
                return bean != null && model.bean != null && bean.getArticleId().equals(model.bean.getArticleId());
            }

        }
        return false;
    }

    @Override
    public int hashCode() {
        super.hashCode();
        if(uniqueIdForView != null) {
            return uniqueIdForView.hashCode();
        }
        return 31;
    }
}
