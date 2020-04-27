package com.ns.contentfragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.main.AppAds;
import com.netoperation.db.THPDB;
import com.netoperation.default_db.DaoSection;
import com.netoperation.default_db.TableSection;
import com.netoperation.model.AdData;
import com.netoperation.model.SectionBean;
import com.ns.adapter.TopTabsAdapter;
import com.ns.callbacks.BackPressCallback;
import com.ns.callbacks.BackPressImpl;
import com.ns.callbacks.ToolbarChangeRequired;
import com.ns.loginfragment.BaseFragmentTHP;
import com.ns.thpremium.R;
import com.ns.utils.FragmentUtil;
import com.ns.view.CustomTabLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class TopTabsFragment extends BaseFragmentTHP {

    private String mFrom;
    private String mSectionId;
    private String mSubSectionId;
    private boolean mIsSubsection;

    public ViewPager mViewPager;
    private CustomTabLayout mTabLayout;

    private TopTabsAdapter mTopTabsAdapter;

    private List<TableSection> mTableSectionList;
    private List<SectionBean> mSubSectionList;
    private int mTabIndex;
    private String mParentSectionName;

    int mSelectedPagerIndex = 0;


    public static TopTabsFragment getInstance(int tabIndex, String from, String sectionId,
                                              boolean isSubsection, String subSectionId, String parentSectionName) {
        TopTabsFragment fragment = new TopTabsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("from", from);
        bundle.putString("sectionId", sectionId);
        bundle.putString("parentSectionName", parentSectionName);
        bundle.putString("subSectionId", subSectionId);
        bundle.putBoolean("isSubsection", isSubsection);
        bundle.putInt("tabIndex", tabIndex);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_sliding_section;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mFrom = getArguments().getString("from");
            mSectionId = getArguments().getString("sectionId");
            mSubSectionId = getArguments().getString("subSectionId");
            mParentSectionName = getArguments().getString("parentSectionName");
            mTabIndex = getArguments().getInt("tabIndex");
            mIsSubsection = getArguments().getBoolean("isSubsection");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewPager = view.findViewById(R.id.viewpager);
        mTabLayout = view.findViewById(R.id.tabs);

        final DaoSection section = THPDB.getInstance(getActivity()).daoSection();

        mDisposable.add(section.getSectionsOfTopBar()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(sectionList -> {
                    if(mIsSubsection) {
                        SectionBean subSectionBean = new SectionBean();
                        subSectionBean.setSecId(mSubSectionId);
                        for(TableSection tableSection : sectionList) {
                            List<SectionBean> subSection = tableSection.getSubSections();
                            mSelectedPagerIndex = subSection.indexOf(subSectionBean);
                            if(mSelectedPagerIndex != -1) {
                                mSubSectionList = subSection;
                                break;
                            }
                        }
                        mTopTabsAdapter = new TopTabsAdapter(getChildFragmentManager(), mFrom, null, mIsSubsection, mSubSectionList);
                        if(mSubSectionList.size() < 5) {
                            mTabLayout.setTabMode(TabLayout.MODE_FIXED);
                        }
                    }
                    else {
                        mTableSectionList = sectionList;
                        mTopTabsAdapter = new TopTabsAdapter(getChildFragmentManager(), mFrom, mTableSectionList, mIsSubsection, null);
                        if(mTableSectionList.size() < 5) {
                            mTabLayout.setTabMode(TabLayout.MODE_FIXED);
                        }
                    }
                    mViewPager.setAdapter(mTopTabsAdapter);
                    mTabLayout.setupWithViewPager(mViewPager);
                    mViewPager.setCurrentItem(mSelectedPagerIndex);
                }));


    }


    @Override
    public void onResume() {
        super.onResume();
        // ToolbarChangeRequired Event Post, It show Toolbar for Sub-Section
        if(mIsSubsection) {
            EventBus.getDefault().post(new ToolbarChangeRequired(mFrom, false, mTabIndex, mParentSectionName, ToolbarChangeRequired.SUB_SECTION));
        } else {
            EventBus.getDefault().post(new ToolbarChangeRequired(mFrom, true, mTabIndex, null, ToolbarChangeRequired.SECTION));
        }
        Log.i("TabFragment", "onResume() TabIndex = " + mTabIndex + " EventBus Registered, isSubSection :: "+mIsSubsection);
        EventBus.getDefault().register(this);
    }


    @Override
    public void onPause() {
        super.onPause();
        Log.i("TabFragment", "onPause() TabIndex = " + mTabIndex + " EventBus UnRegistered, isSubSection :: "+mIsSubsection);
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void handleEvent(TableSection tableSection) {
        Log.i("handleEvent", "Left Slider Section Pressed :: " + tableSection.getSecName());
        int index = mTableSectionList.indexOf(tableSection);
        mViewPager.setCurrentItem(index);
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void handleEvent(SectionBean tableSection) {
        Log.i("handleEvent", "Left Slider Sub-Section Pressed :: " + tableSection.getSecName() + "-" + tableSection.getSecId());

        // Opening Sub-Section Fragment
        TopTabsFragment topTabsFragment = TopTabsFragment.getInstance(mTabIndex, mFrom, mSectionId, true, tableSection.getSecId(), tableSection.getParentSecName());
        FragmentUtil.pushFragmentFromFragment(this, R.id.sectionLayout, topTabsFragment);

    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void handleEvent(BackPressImpl backPress) {
        Log.i("handleEvent", "Back Button Pressed :: TabIndex = " + mTabIndex);

        // ToolbarChangeRequired Event Post, It shows Toolbar for Section
        EventBus.getDefault().post(new ToolbarChangeRequired(mFrom, true, mTabIndex, null, ToolbarChangeRequired.SECTION));

        // Send Back to AppTabActivity.java => handleEvent(BackPressCallback backPressCallback)
        BackPressCallback backPressCallback = new BackPressImpl(this, mFrom, mTabIndex).onBackPressed();
        EventBus.getDefault().post(backPressCallback);

    }




}
