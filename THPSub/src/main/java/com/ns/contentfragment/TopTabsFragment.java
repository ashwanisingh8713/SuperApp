package com.ns.contentfragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.netoperation.db.THPDB;
import com.netoperation.default_db.DaoSection;
import com.netoperation.default_db.DaoWidget;
import com.netoperation.default_db.TableSection;
import com.netoperation.model.SectionBean;
import com.ns.adapter.TopTabsAdapter;
import com.ns.alerts.Alerts;
import com.ns.loginfragment.BaseFragmentTHP;
import com.ns.thpremium.R;
import com.ns.utils.THPConstants;
import com.ns.view.CustomTabLayout;
import com.ns.view.CustomViewPager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import io.reactivex.Observable;
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


    public static TopTabsFragment getInstance(String from, String sectionId,
                                              boolean isSubsection, String subSectionId) {
        TopTabsFragment fragment = new TopTabsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("from", from);
        bundle.putString("sectionId", sectionId);
        bundle.putString("subSectionId", subSectionId);
        bundle.putBoolean("isSubsection", isSubsection);
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
            mIsSubsection = getArguments().getBoolean("isSubsection");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewPager = view.findViewById(R.id.viewpager);
        mTabLayout = view.findViewById(R.id.tabs);


        DaoSection section = THPDB.getInstance(getActivity()).daoSection();

        mDisposable.add(section.getSectionsOfBurger(true)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(sectionList -> {
                    mTableSectionList = sectionList;
                    mTopTabsAdapter = new TopTabsAdapter(getChildFragmentManager(), mFrom, sectionList, mIsSubsection);
                    mViewPager.setAdapter(mTopTabsAdapter);
                    mTabLayout.setupWithViewPager(mViewPager);
                }));

    }


    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void handleEvent(TableSection tableSection) {
        Alerts.showToast(getActivity(), "Section");
        int index = mTableSectionList.indexOf(tableSection);
        mViewPager.setCurrentItem(index);

    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void handleEvent(SectionBean tableSection) {
        Alerts.showToast(getActivity(), "SubSection");
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}
