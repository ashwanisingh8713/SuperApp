package com.ns.adapter;

import android.util.SparseArray;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.netoperation.default_db.TableSection;
import com.netoperation.model.SectionBean;
import com.ns.contentfragment.SectionFragment;

import java.util.List;

public class TopTabsAdapter extends FragmentStatePagerAdapter {

    private String mPageSource;
    private List<TableSection> mSectionList;
    private List<SectionBean> mSubSectionList;
    private boolean mIsSubsection;

    SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();


    public TopTabsAdapter(FragmentManager fm, String pageSource, List<TableSection> sectionList, boolean isSubsection, List<SectionBean> subSectionList) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.mPageSource = pageSource;
        this.mSectionList = sectionList;
        this.mSubSectionList = subSectionList;
        this.mIsSubsection = isSubsection;

    }

    @Override
    public Fragment getItem(int position) {
        if(mIsSubsection) {
            return SectionFragment.getInstance(mPageSource, mSubSectionList.get(position).getSecId(), mSubSectionList.get(position).getType(), mSubSectionList.get(position).getSecName(), mIsSubsection);
        }
        return SectionFragment.getInstance(mPageSource, mSectionList.get(position).getSecId(), mSectionList.get(position).getType(), mSectionList.get(position).getSecName(), mIsSubsection);
    }

    @Override
    public int getCount() {
        if(mSectionList != null) {
            return mSectionList.size();
        } else if (mSubSectionList != null) {
            return mSubSectionList.size();
        } else {
            return 0;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if(mSectionList != null) {
            return mSectionList.get(position).getSecName();
        } else if (mSubSectionList != null) {
            return mSubSectionList.get(position).getSecName();
        } else {
            return "";
        }
    }

    /**
     * On each Fragment instantiation we are saving the reference of that Fragment in a Map
     * It will help us to retrieve the Fragment by position
     *
     * @param container
     * @param position
     * @return
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    /**
     * Remove the saved reference from our Map on the Fragment destroy
     *
     * @param container
     * @param position
     * @param object
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }


    /**
     * Get the Fragment by position
     *
     * @param position tab position of the fragment
     * @return
     */
    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }
}
