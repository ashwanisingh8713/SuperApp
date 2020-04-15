package com.ns.adapter;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.netoperation.util.NetConstants;
import com.ns.contentfragment.AppTabListingFragment;
import com.ns.contentfragment.TopTabsFragment;

public class AppTabPagerAdapter extends FragmentStatePagerAdapter {

    private String mUserId;
    private String[] tabNames;

    SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

    public AppTabPagerAdapter(FragmentManager fm, String userId, String[] tabNames) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mUserId = userId;
        this.tabNames = tabNames;
    }

    @Override
    public Fragment getItem(int i) {
        if(i==0) {
            return TopTabsFragment.getInstance(i, NetConstants.RECO_DEFAULT_SECTIONS, NetConstants.RECO_HOME_TAB, false, "", null);
        }
        else if(i==1) {
            return AppTabListingFragment.getInstance(i, mUserId, NetConstants.BREIFING_ALL);
        }
        else if(i==2) {
            return AppTabListingFragment.getInstance(i, mUserId, NetConstants.RECO_Mystories);
        }
        else {
            return AppTabListingFragment.getInstance(i, mUserId, NetConstants.RECO_suggested);
        }
    }

    @Override
    public int getCount() {
        return tabNames.length;
    }



    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabNames[position];
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
