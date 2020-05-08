package com.ns.contentfragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.ns.adapter.IndicesTabViewPagerAdapter;
import com.ns.loginfragment.BaseFragmentTHP;
import com.ns.model.IndicesSection;
import com.ns.thpremium.R;
import com.ns.view.CustomTabLayout;

import java.util.ArrayList;
import java.util.List;

public class IndicesTabFragment extends BaseFragmentTHP {

    private String mFrom;
    private int tabIndex = 0;
    private int selectedTabPosition = 0;

    public ViewPager mViewPager;
    private CustomTabLayout mTabLayout;

    private IndicesTabViewPagerAdapter mIndicesTabViewPagerAdapter;

    public static IndicesTabFragment getInstance(int tabIndex, String from) {
        IndicesTabFragment fragment = new IndicesTabFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("tabIndex", tabIndex);
        bundle.putString("from", from);
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
            tabIndex = getArguments().getInt("tabIndex");
            mFrom = getArguments().getString("from");
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
}
