package com.ns.contentfragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.ns.adapter.IndicesTabViewPagerAdapter;
import com.ns.callbacks.ToolbarChangeRequired;
import com.ns.loginfragment.BaseFragmentTHP;
import com.ns.model.IndicesSection;
import com.ns.thpremium.R;
import com.ns.view.CustomTabLayout;

import org.greenrobot.eventbus.EventBus;

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
        // It sends Event in AppTabActivity.java=> handleEvent(ToolbarChangeRequired toolbarChangeRequired)
        EventBus.getDefault().post(new ToolbarChangeRequired(mPageSource, false, mTabIndex, mParentSectionName, ToolbarChangeRequired.OTHER_TOPBAR));
    }
}
