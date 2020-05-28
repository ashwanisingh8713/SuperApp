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

public class BookmarkActivity extends BaseAcitivityTHP {

    private TabLayout tabLayout;
    private ViewPager bookmarkViewPager;
    private MergedBookmarkPagerAdapter pagerAdapter;
    private String mGroupTypeDisplay ;

    @Override
    public int layoutRes() {
        return R.layout.activity_tab_bookmark_merged;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGroupTypeDisplay = getIntent().getExtras().getString("groupType");

        getDetailToolbar().setTitle("Read Later");
        getDetailToolbar().showNotificationAndBookmarkeIcons(false);

        bookmarkViewPager = findViewById(R.id.bookmarkViewPager);
        tabLayout = findViewById(R.id.tabLayout);

        if(mGroupTypeDisplay.equals(NetConstants.BOOKMARK_IN_ONE)) {
            tabLayout.setVisibility(View.GONE);
            //Show overflow menu
            getDetailToolbar().showNotificationAndBookmarkeIcons(true);
        }

        pagerAdapter = new MergedBookmarkPagerAdapter(getSupportFragmentManager(), sIsDayTheme, mGroupTypeDisplay);
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
                pagerAdapter.SetOnSelectView(BookmarkActivity.this, tabLayout, pos);
                bookmarkViewPager.setCurrentItem(pos);
                THPFirebaseAnalytics.setFirbaseAnalyticsScreenRecord(BookmarkActivity.this, "Read Later : " + tab.getText(), AppTabFragment.class.getSimpleName());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                pagerAdapter.SetUnSelectView(BookmarkActivity.this, tabLayout, pos);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        //AppFirebaseAnalytics.setFirbaseAnalyticsScreenRecord(BookmarkMergedActivity.this, "BookmarkMergedActivity Screen", BookmarkMergedActivity.class.getSimpleName());
    }
}
