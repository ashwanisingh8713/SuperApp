package com.ns.contentfragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.netoperation.db.THPDB;
import com.netoperation.default_db.DaoSection;
import com.netoperation.default_db.TableSection;
import com.netoperation.model.SectionBean;
import com.ns.adapter.TopTabsAdapter;
import com.ns.alerts.Alerts;
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

    int mSelectedPagerIndex = 0;


    public static TopTabsFragment getInstance(int tabIndex, String from, String sectionId,
                                              boolean isSubsection, String subSectionId) {
        TopTabsFragment fragment = new TopTabsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("from", from);
        bundle.putString("sectionId", sectionId);
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
            mTabIndex = getArguments().getInt("tabIndex");
            mIsSubsection = getArguments().getBoolean("isSubsection");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewPager = view.findViewById(R.id.viewpager);
        mTabLayout = view.findViewById(R.id.tabs);

        if (mIsSubsection) {
            TextView ttt = view.findViewById(R.id.tttt);
            ttt.setText("SUB-Section");
        }

        final DaoSection section = THPDB.getInstance(getActivity()).daoSection();


        mDisposable.add(section.getSectionsOfBurger(true)
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
                    }
                    else {
                        mTableSectionList = sectionList;
                        mTopTabsAdapter = new TopTabsAdapter(getChildFragmentManager(), mFrom, mTableSectionList, mIsSubsection, null);
                    }
                    mViewPager.setAdapter(mTopTabsAdapter);
                    mTabLayout.setupWithViewPager(mViewPager);
                    mViewPager.setCurrentItem(mSelectedPagerIndex);
                }));

    }


    @Override
    public void onResume() {
        super.onResume();
        Log.i("TabFragment", "onResume() TabIndex = " + mTabIndex + " EventBus Registered");
        EventBus.getDefault().register(this);
    }


    @Override
    public void onPause() {
        super.onPause();
        Log.i("TabFragment", "onPause() TabIndex = " + mTabIndex + " EventBus UnRegistered");
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        Log.i("TabFragment", "onDestroyView() TabIndex = " + mTabIndex + " EventBus UnRegistered");
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
        TopTabsFragment topTabsFragment = TopTabsFragment.getInstance(mTabIndex, mFrom, mSectionId, true, tableSection.getSecId());
        FragmentUtil.pushFragmentFromFragment(this, R.id.sectionLayout, topTabsFragment);

        // ToolbarChangeRequired Event Post
        EventBus.getDefault().post(new ToolbarChangeRequired(mFrom, false, mTabIndex, tableSection.getParentSecName(), ToolbarChangeRequired.SUB_SECTION));
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void handleEvent(BackPressImpl backPress) {
        Log.i("handleEvent", "Back Button Pressed :: TabIndex = " + mTabIndex);

        // ToolbarChangeRequired Event Post
        EventBus.getDefault().post(new ToolbarChangeRequired(mFrom, true, mTabIndex, null, ToolbarChangeRequired.SECTION));

        // Back Press Event Post
        EventBus.getDefault().post(new BackPressImpl(this, mFrom, mTabIndex).onBackPressed());
    }


}
