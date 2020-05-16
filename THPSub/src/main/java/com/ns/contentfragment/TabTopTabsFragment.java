package com.ns.contentfragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.netoperation.db.THPDB;
import com.netoperation.default_db.TableSection;
import com.netoperation.model.SectionBean;
import com.netoperation.net.DefaultTHApiManager;
import com.netoperation.util.NetConstants;
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

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class TabTopTabsFragment extends BaseFragmentTHP {

    private String mPageSource;
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


    public static TabTopTabsFragment getInstance(int tabIndex, String pageSource, String sectionId,
                                                 boolean isSubsection, String subSectionId, String parentSectionName) {
        TabTopTabsFragment fragment = new TabTopTabsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("pageSource", pageSource);
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
        return R.layout.fragment_top_tabs;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPageSource = getArguments().getString("pageSource");
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

        if(mPageSource.equals(NetConstants.PS_ADD_ON_SECTION)) {
            load_PS_ADD_ON_SECTION();
        }
        else {
            load_PS_GROUP_DEFAULT_SECTIONS();
        }
    }

    private void load_PS_ADD_ON_SECTION() {
        // Need to handle offline and section, sub-section
        mDisposable.add(DefaultTHApiManager.isSectionOrSubsection(getActivity(), mSectionId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(value->{
                    if(value instanceof SectionBean) {
                        Log.i("", "");

                    }
                    else if(value instanceof TableSection) {
                        Log.i("", "");
                        TableSection tableSection = (TableSection)value;
                        mSubSectionList = new ArrayList<>();
                        mSubSectionList.add(tableSection.getSection());
                        mSubSectionList.addAll(tableSection.getSubSections());
                        mTopTabsAdapter = new TopTabsAdapter(getChildFragmentManager(), mPageSource, null, mIsSubsection, mSubSectionList);

                        mViewPager.setAdapter(mTopTabsAdapter);
                        mTabLayout.setupWithViewPager(mViewPager);
                        mViewPager.setCurrentItem(mSelectedPagerIndex);
                    }
                    else {
                        Log.i("", "");
                    }

                    if(mSubSectionList == null || mSubSectionList.size() == 1) {
                        mTabLayout.setVisibility(View.GONE);
                    }

                }));
    }


    private void load_PS_GROUP_DEFAULT_SECTIONS() {
        Single<List<TableSection>> tableSectionObservable = null;
        if(mSubSectionId.equals("998")) {// Opens News-Digest
            tableSectionObservable =  THPDB.getInstance(getActivity()).daoSection().getNewsDigestTableSection();
            mTabLayout.setVisibility(View.GONE);
        }
        else if(mIsSubsection) {
            tableSectionObservable =  THPDB.getInstance(getActivity()).daoSection().getAllSections();
        } else {
            tableSectionObservable =  THPDB.getInstance(getActivity()).daoSection().getSectionsOfTopBar();
        }
        mDisposable.add(tableSectionObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(sectionList -> {
                    if(mSubSectionId.equals("998")) {
                        mSubSectionList = new ArrayList<>();
                        mSubSectionList.add(sectionList.get(0).getSection());
                        mTopTabsAdapter = new TopTabsAdapter(getChildFragmentManager(), mPageSource, null, mIsSubsection, mSubSectionList);
                    }
                    else if (mIsSubsection) {
                        SectionBean subSectionBean = new SectionBean();
                        subSectionBean.setSecId(mSubSectionId);
                        for (TableSection tableSection : sectionList) {
                            List<SectionBean> subSection = tableSection.getSubSections();
                            mSelectedPagerIndex = subSection.indexOf(subSectionBean);
                            if (mSelectedPagerIndex != -1) {
                                mSubSectionList = subSection;
                                break;
                            }
                        }
                        if (mSubSectionList == null) {
                            mSubSectionList = new ArrayList<>();
                        }
                        mTopTabsAdapter = new TopTabsAdapter(getChildFragmentManager(), mPageSource, null, mIsSubsection, mSubSectionList);
                        if (mSubSectionList.size() < 5) {
                            mTabLayout.setTabMode(TabLayout.MODE_FIXED);
                        }
                    } else {
                        mTableSectionList = sectionList;
                        mTopTabsAdapter = new TopTabsAdapter(getChildFragmentManager(), mPageSource, mTableSectionList, mIsSubsection, null);
                        if (mTableSectionList.size() < 5) {
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
        // It sends Event in AppTabActivity.java=> handleEvent(ToolbarChangeRequired toolbarChangeRequired)
        // ToolbarChangeRequired Event Post, It show Toolbar for Sub-Section
        if(mIsSubsection) {
            EventBus.getDefault().post(new ToolbarChangeRequired(mPageSource, false, mTabIndex, mParentSectionName, ToolbarChangeRequired.SUB_SECTION_TOPBAR));
        } else {
            EventBus.getDefault().post(new ToolbarChangeRequired(mPageSource, true, mTabIndex, null, ToolbarChangeRequired.SECTION_TOPBAR));
        }
        Log.i("handleEvent", "register() ::  "+mPageSource+" :: "+mTabIndex);
        EventBus.getDefault().register(this);
    }


    @Override
    public void onPause() {
        super.onPause();
        Log.i("handleEvent", "unregister() ::  "+mPageSource+" :: "+mTabIndex);
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
        if(index != -1) {
            mViewPager.setCurrentItem(index);
        }
        else if(tableSection.getType().equalsIgnoreCase("static") && tableSection.getSecId().equalsIgnoreCase("998")) {
            // Opening News Digest Fragment
            TabTopTabsFragment tabTopTabsFragment = TabTopTabsFragment.getInstance(mTabIndex, mPageSource, mSectionId, true, tableSection.getSecId(), tableSection.getSecName());
            FragmentUtil.pushFragmentFromFragment(this, R.id.sectionLayout, tabTopTabsFragment);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void handleEvent(SectionBean tableSection) {
        Log.i("handleEvent", "Left Slider Sub-Section Pressed :: " + tableSection.getSecName() + "-" + tableSection.getSecId());

        // Opening Sub-Section Fragment
        TabTopTabsFragment tabTopTabsFragment = TabTopTabsFragment.getInstance(mTabIndex, mPageSource, mSectionId, true, tableSection.getSecId(), tableSection.getParentSecName());
        FragmentUtil.pushFragmentFromFragment(this, R.id.sectionLayout, tabTopTabsFragment);

    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void handleEvent(BackPressImpl backPress) {
        Log.i("handleEvent", "Back Button Pressed ::  "+mPageSource+" :: "+mTabIndex);

        // ToolbarChangeRequired Event Post, It shows Toolbar for Section
        EventBus.getDefault().post(new ToolbarChangeRequired(mPageSource, true, mTabIndex, null, ToolbarChangeRequired.SECTION_TOPBAR));

        // Send Back to AppTabActivity.java => handleEvent(BackPressCallback backPressCallback)
        BackPressCallback backPressCallback = new BackPressImpl(this, mPageSource, mTabIndex).onBackPressed();
        EventBus.getDefault().post(backPressCallback);

    }




}
