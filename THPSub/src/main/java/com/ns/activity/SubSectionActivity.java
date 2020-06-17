package com.ns.activity;

import com.ns.utils.THPFirebaseAnalytics;

public class SubSectionActivity extends BaseAcitivityTHP {

    @Override
    public int layoutRes() {
        return 0;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Firebase Analytics
        THPFirebaseAnalytics.setFirbaseAnalyticsScreenRecord(SubSectionActivity.this, "Sub Section Screen", SubSectionActivity.class.getSimpleName());
    }
}
