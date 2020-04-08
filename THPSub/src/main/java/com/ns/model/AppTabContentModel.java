package com.ns.model;

import com.netoperation.model.RecoBean;

public class AppTabContentModel {

    private int viewType;
    private RecoBean bean;
    private String uniqueIdForView;

    public AppTabContentModel(int viewType) {
        this.viewType = viewType;
    }

    public AppTabContentModel(int viewType, String uniqueIdForView) {
        this.viewType = viewType;
        this.uniqueIdForView = uniqueIdForView;
    }

    public int getViewType() {
        return viewType;
    }

    public RecoBean getBean() {
        return bean;
    }

    public void setBean(RecoBean bean) {
        this.bean = bean;
    }

    @Override
    public boolean equals(Object obj) {
        super.equals(obj);
        if(obj != null & obj instanceof AppTabContentModel) {
            AppTabContentModel model = (AppTabContentModel) obj ;
            if(uniqueIdForView != null) {
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
