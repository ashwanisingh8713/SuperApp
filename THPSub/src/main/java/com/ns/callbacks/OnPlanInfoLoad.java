package com.ns.callbacks;

import com.netoperation.model.TxnDataBean;

import java.util.List;

public interface OnPlanInfoLoad {

    void onPlansLoaded(List<TxnDataBean> planInfoList);
}
