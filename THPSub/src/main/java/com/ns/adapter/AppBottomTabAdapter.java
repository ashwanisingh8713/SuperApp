package com.ns.adapter;

import android.util.SparseArray;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.netoperation.config.model.TabsBean;
import com.netoperation.util.NetConstants;
import com.ns.contentfragment.AppTabListingFragment;
import com.ns.contentfragment.IndicesTabFragment;
import com.ns.contentfragment.TopTabsFragment;
import com.ns.contentfragment.WebFragment;
import com.ns.thpremium.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppBottomTabAdapter extends FragmentStatePagerAdapter {


    private List<TabsBean> mTabsBean;

    SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

    public AppBottomTabAdapter(FragmentManager fm, List<TabsBean> tabsBean ) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.mTabsBean = tabsBean;
    }

    @Override
    public Fragment getItem(int i) {
        return getPageSourceTypeFragment(mTabsBean.get(i), i);
    }

    private Fragment getPageSourceTypeFragment(TabsBean tabsBean, int tabIndex) {

        switch (tabsBean.getPageSource()) {
            case NetConstants.PS_GROUP_DEFAULT_SECTIONS:
                return TopTabsFragment.getInstance(tabIndex, tabsBean.getPageSource(), NetConstants.RECO_HOME_TAB, false, "", null);
            case NetConstants.PS_Briefing:
            case NetConstants.PS_My_Stories:
            case NetConstants.PS_Suggested:
                return AppTabListingFragment.getInstance(tabIndex, tabsBean.getPageSource());

            case NetConstants.PS_Profile:
                return new Fragment(); // pageSource="Profile" opens in different Activity, and it's tab click is handled
            case NetConstants.PS_Url:
                return WebFragment.getInstance(tabIndex, tabsBean);
            case NetConstants.PS_ADD_ON_SECTION:
                return TopTabsFragment.getInstance(tabIndex, tabsBean.getPageSource(), tabsBean.getSection().getSecId(), true, tabsBean.getSection().getSecId(), tabsBean.getSection().getSecName());
            case NetConstants.PS_SENSEX:
                return IndicesTabFragment.getInstance(tabIndex, tabsBean.getGroup());
        }

        return new Fragment();
    }

    @Override
    public int getCount() {
        return mTabsBean.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mTabsBean.get(position).getTitle();
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