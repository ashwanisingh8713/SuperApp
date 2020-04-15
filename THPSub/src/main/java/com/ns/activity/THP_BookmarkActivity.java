package com.ns.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.ns.contentfragment.THP_BookmarksFragment;
import com.ns.thpremium.R;
import com.ns.utils.FragmentUtil;
import com.ns.utils.THPFirebaseAnalytics;

public class THP_BookmarkActivity extends BaseAcitivityTHP {

    private String mFrom;
    private String mUserId;


    @Override
    public int layoutRes() {
        return R.layout.activity_anonymous_layout;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getIntent().getExtras() != null) {
            mFrom = getIntent().getStringExtra("from");
            mUserId = getIntent().getStringExtra("userId");
        }
        THP_BookmarksFragment fragment = THP_BookmarksFragment.getInstance(mUserId);
        FragmentUtil.replaceFragmentAnim(this, R.id.parentLayout, fragment, FragmentUtil.FRAGMENT_NO_ANIMATION, true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        THPFirebaseAnalytics.setFirbaseAnalyticsScreenRecord(this, "THP_BookmarkActivity Screen", THP_BookmarkActivity.class.getSimpleName());
    }
}
