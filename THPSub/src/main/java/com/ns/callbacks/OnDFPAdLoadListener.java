package com.ns.callbacks;

import com.netoperation.model.AdData;

public interface OnDFPAdLoadListener {
    void onDFPAdLoadSuccess(AdData adData);
    void onDFPAdLoadFailure(AdData adData);
    void onAdClose();
}