package com.ns.adapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.netoperation.default_db.TableSection;
import com.ns.contentfragment.SectionFragment;

import java.util.List;

public class TopTabsAdapter extends FragmentStatePagerAdapter {

    private String mFrom;
    private List<TableSection> mSectionList;
    private boolean mIsSubsection;

    public TopTabsAdapter(FragmentManager fm, String from, List<TableSection> sectionList, boolean isSubsection) {
        super(fm);
        this.mFrom = from;
        this.mSectionList = sectionList;
        this.mIsSubsection = isSubsection;

    }

    @Override
    public Fragment getItem(int position) {
        return SectionFragment.getInstance(mFrom, mSectionList.get(position).getSecId(), mSectionList.get(position).getType(), mSectionList.get(position).getSecName(), mIsSubsection);
    }

    @Override
    public int getCount() {
        return mSectionList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mSectionList.get(position).getSecName();
    }
}
