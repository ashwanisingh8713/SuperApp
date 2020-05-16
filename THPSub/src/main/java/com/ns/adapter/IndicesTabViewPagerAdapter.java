package com.ns.adapter;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.ns.contentfragment.ForexFragment;
import com.ns.contentfragment.GoldFragment;
import com.ns.contentfragment.StockDetailsFragment;
import com.ns.model.IndicesSection;

import java.util.List;


public class IndicesTabViewPagerAdapter extends FragmentStatePagerAdapter {

    private List<IndicesSection> mIndicesSection;

    public IndicesTabViewPagerAdapter(FragmentManager fm, List<IndicesSection> mIndicesSection) {
        super(fm);
        this.mIndicesSection = mIndicesSection;
    }

    @Override
    public Fragment getItem(int position) {

        switch (mIndicesSection.get(position).getViewType()) {
            case 0:
                return StockDetailsFragment.newInstance(0, "percentageChange", "desc");
            case 1:
                return StockDetailsFragment.newInstance(1, "percentageChange", "desc");
            case 2:
                return new ForexFragment();
            case 3:
                return new GoldFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return mIndicesSection.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mIndicesSection.get(position).getSectionName();
    }
}
