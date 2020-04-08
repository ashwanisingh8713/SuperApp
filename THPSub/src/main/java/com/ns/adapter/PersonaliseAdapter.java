package com.ns.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

public class PersonaliseAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> mPersonaliseFragments;
    String mTopics, mCities, mAuthors;

    public PersonaliseAdapter(FragmentManager fm, List<Fragment> personaliseFragments, String topics, String cities, String authors) {
        super(fm);
        mPersonaliseFragments = personaliseFragments;
        mTopics=topics;
        mCities=cities;
        mAuthors=authors;
    }

    @Override
    public Fragment getItem(int i) {
        return mPersonaliseFragments.get(i);
    }

    @Override
    public int getCount() {
        return mPersonaliseFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return mTopics;
            case 1:
                return mCities;
            case 2:
                return mAuthors;
        }
        return null;
    }
}
