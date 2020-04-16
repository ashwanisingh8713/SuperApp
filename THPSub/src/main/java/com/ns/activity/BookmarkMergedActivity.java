package com.ns.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.netoperation.util.NetConstants;
import com.ns.adapter.MergedBookmarkPagerAdapter;
import com.ns.contentfragment.AppTabFragment;
import com.ns.thpremium.R;
import com.ns.utils.THPFirebaseAnalytics;
import com.ns.view.text.CustomTextView;

public class BookmarkMergedActivity extends BaseAcitivityTHP {

    String mUserId = "";

    private TabLayout tabLayout;
    private ViewPager bookmarkViewPager;
    private MergedBookmarkPagerAdapter pagerAdapter;

    private int mBookmarkDisplay = NetConstants.BOOKMARK_DEFAULT_PREMIUM_IN_TAB;
//    private int mBookmarkDisplay = NetConstants.BOOKMARK_DEFAULT_PREMIUM_IN_ONE;


    @Override
    public int layoutRes() {
        return R.layout.activity_tab_bookmark_merged;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getIntent() != null) {
            mUserId = getIntent().getStringExtra("userId");
        }

        CustomTextView title_tv = findViewById(R.id.title_tv_merged);
        title_tv.setText("Read Later");

        bookmarkViewPager = findViewById(R.id.bookmarkViewPager);
        tabLayout = findViewById(R.id.tabLayout);

        if(mBookmarkDisplay == NetConstants.BOOKMARK_DEFAULT_PREMIUM_IN_ONE) {
            tabLayout.setVisibility(View.GONE);
        }

        pagerAdapter = new MergedBookmarkPagerAdapter(getSupportFragmentManager(), mUserId, mIsDayTheme, mBookmarkDisplay);
        bookmarkViewPager.setAdapter(pagerAdapter);

        tabLayout.setupWithViewPager(bookmarkViewPager, true);

        // Iterate over all tabs and set the custom view
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(pagerAdapter.getTabView(i, this));
        }

        // To select default tab
        pagerAdapter.SetOnSelectView(this, tabLayout, 0);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                pagerAdapter.SetOnSelectView(BookmarkMergedActivity.this, tabLayout, pos);
                bookmarkViewPager.setCurrentItem(pos);
                THPFirebaseAnalytics.setFirbaseAnalyticsScreenRecord(BookmarkMergedActivity.this, "Read Later : " + tab.getText(), AppTabFragment.class.getSimpleName());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                pagerAdapter.SetUnSelectView(BookmarkMergedActivity.this, tabLayout, pos);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // Back button click listener
        findViewById(R.id.backBtn).setOnClickListener(v->{
            finish();
        });



    }


    @Override
    protected void onResume() {
        super.onResume();
        //AppFirebaseAnalytics.setFirbaseAnalyticsScreenRecord(BookmarkMergedActivity.this, "BookmarkMergedActivity Screen", BookmarkMergedActivity.class.getSimpleName());

    }
}
