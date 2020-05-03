package com.ns.adapter;


import android.util.SparseArray;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.netoperation.default_db.TableSection;
import com.netoperation.model.ArticleBean;
import com.ns.contentfragment.THP_DetailFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;


public class DetailPagerAdapter extends FragmentStatePagerAdapter {

    private List<ArticleBean> articleList;
    private String mFrom;
    private String mUserId;

    //private SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

    public DetailPagerAdapter(@NonNull FragmentManager fm, List<ArticleBean> articleList, String usedId, String from) {
        super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.articleList = articleList;
        this.mFrom = from;
        this.mUserId = usedId;
    }

    public ArticleBean getArticleBean(int position) {
        return articleList.get(position);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return THP_DetailFragment.getInstance(articleList.get(position), articleList.get(position).getArticleId(), mUserId, mFrom);
    }

    @Override
    public int getCount() {
        return articleList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "";
    }

    /**
     * On each Fragment instantiation we are saving the reference of that Fragment in a Map
     * It will help us to retrieve the Fragment by position
     *
     * @param container
     * @param position
     * @return
     */
    /*@Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }*/

    /**
     * Remove the saved reference from our Map on the Fragment destroy
     *
     * @param container
     * @param position
     * @param object
     */
    /*@Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }*/


    /**
     * Get the Fragment by position
     *
     * @param position tab position of the fragment
     * @return
     */
    /*public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }*/

}
