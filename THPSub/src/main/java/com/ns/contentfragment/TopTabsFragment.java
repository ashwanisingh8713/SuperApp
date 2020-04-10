package com.ns.contentfragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.netoperation.db.THPDB;
import com.netoperation.default_db.DaoSection;
import com.ns.adapter.TopTabsAdapter;
import com.ns.loginfragment.BaseFragmentTHP;
import com.ns.thpremium.R;
import com.ns.utils.THPConstants;
import com.ns.view.CustomTabLayout;
import com.ns.view.CustomViewPager;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class TopTabsFragment extends BaseFragmentTHP {

    private String mFrom;
    private String mSectionId;
    private String mSubSectionId;
    private boolean mIsSubsection;

    public CustomViewPager mViewPager;
    private CustomTabLayout mTabLayout;

    private TopTabsAdapter mTopTabsAdapter;


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
                .map(sectionList -> {
                    return sectionList;
                })
                .subscribe(sectionList -> {
                    mTopTabsAdapter = new TopTabsAdapter(getChildFragmentManager(), mFrom, sectionList, mIsSubsection);
                }));

        /*Observable.just(mFrom)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(value->{

                    if(mFrom == null || mFrom.equalsIgnoreCase(THPConstants.FROM_DEFAULT)) {
                        DaoSection section = THPDB.getInstance(getActivity()).daoSection();


                    }
                    return "";
                })
                .subscribe();*/


    }


}
