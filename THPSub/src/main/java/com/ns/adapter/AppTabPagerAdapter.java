package com.ns.adapter;

import android.os.Bundle;
import android.os.Parcelable;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.netoperation.util.NetConstants;
import com.ns.contentfragment.AppTabListingFragment;
import com.ns.contentfragment.TopTabsFragment;
import com.ns.utils.THPConstants;

public class AppTabPagerAdapter extends FragmentStatePagerAdapter {

    private String mUserId;
    private String[] tabNames;
    public AppTabPagerAdapter(FragmentManager fm, String userId, String[] tabNames) {
        super(fm);
        mUserId = userId;
        this.tabNames = tabNames;
    }

    @Override
    public Fragment getItem(int i) {
        if(i==0) {
            return TopTabsFragment.getInstance(THPConstants.FROM_DEFAULT, NetConstants.RECO_HOME, false, "");
        }
        else if(i==1) {
            return AppTabListingFragment.getInstance(mUserId, NetConstants.BREIFING_ALL);
        }
        else if(i==2) {
            return AppTabListingFragment.getInstance(mUserId, NetConstants.RECO_Mystories);
        }
        else {
            return AppTabListingFragment.getInstance(mUserId, NetConstants.RECO_suggested);
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

    @Override
    public Parcelable saveState() {
        Bundle bundle = (Bundle) super.saveState();
        if (bundle != null) {
            bundle.putParcelableArray("states", null);
            return bundle;
        }
        return super.saveState();
    }


}
