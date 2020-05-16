package com.ns.contentfragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.ns.adapter.IndicesTabViewPagerAdapter;
import com.ns.callbacks.BackPressCallback;
import com.ns.callbacks.BackPressImpl;
import com.ns.callbacks.ToolbarChangeRequired;
import com.ns.loginfragment.BaseFragmentTHP;
import com.ns.model.IndicesSection;
import com.ns.thpremium.R;
import com.ns.view.CustomTabLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class TabIndicesFragment extends BaseFragmentTHP {

    private String mPageSource;
    private String mParentSectionName;
    private int mTabIndex = 0;
    private int selectedTabPosition = 0;

    public ViewPager mViewPager;
    private CustomTabLayout mTabLayout;

    private IndicesTabViewPagerAdapter mIndicesTabViewPagerAdapter;

    public static TabIndicesFragment getInstance(int tabIndex, String pageSource, String parentSectionName) {
        TabIndicesFragment fragment = new TabIndicesFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("tabIndex", tabIndex);
        bundle.putString("pageSource", pageSource);
        bundle.putString("parentSectionName", parentSectionName);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_indicestab;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTabIndex = getArguments().getInt("tabIndex");
            mPageSource = getArguments().getString("pageSource");
            mParentSectionName = getArguments().getString("parentSectionName");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewPager = view.findViewById(R.id.viewpager);
        mTabLayout = view.findViewById(R.id.tabs);

        List<IndicesSection> mIndicesSection = new ArrayList<>();
        mIndicesSection.add(new IndicesSection(0, "BSE"));
        mIndicesSection.add(new IndicesSection(1, "NSE"));
        mIndicesSection.add(new IndicesSection(2, "Currency"));
        mIndicesSection.add(new IndicesSection(3, "Commodities"));

        mIndicesTabViewPagerAdapter = new IndicesTabViewPagerAdapter(getChildFragmentManager(), mIndicesSection);
        mViewPager.setAdapter(mIndicesTabViewPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setCurrentItem(selectedTabPosition, true);

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("handleEvent", "register() ::  "+mPageSource+" :: "+mTabIndex);
        EventBus.getDefault().register(this);
        // It sends Event in AppTabActivity.java=> handleEvent(ToolbarChangeRequired toolbarChangeRequired)
        EventBus.getDefault().post(new ToolbarChangeRequired(mPageSource, false, mTabIndex, mParentSectionName, ToolbarChangeRequired.OTHER_TOPBAR));
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("handleEvent", "unregister() ::  "+mPageSource+" :: "+mTabIndex);
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void handleEvent(BackPressImpl backPress) {
        Log.i("handleEvent", "Back Button Pressed ::  "+mPageSource+" :: "+mTabIndex);

        // ToolbarChangeRequired Event Post, It shows Toolbar for Section
        EventBus.getDefault().post(new ToolbarChangeRequired(mPageSource, true, mTabIndex, null, ToolbarChangeRequired.SECTION_TOPBAR));

        // Send Back to AppTabActivity.java => handleEvent(BackPressCallback backPressCallback)
        BackPressCallback backPressCallback = new BackPressImpl(this, mPageSource, mTabIndex).onBackPressed();
        EventBus.getDefault().post(backPressCallback);
    }

}
