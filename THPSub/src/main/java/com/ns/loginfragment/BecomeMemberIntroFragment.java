package com.ns.loginfragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.ns.adapter.BecomeMemberPagerAdapter;
import com.ns.thpremium.R;
import com.ns.utils.THPConstants;
import com.ns.utils.THPFirebaseAnalytics;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

public class BecomeMemberIntroFragment extends BaseFragmentTHP {


    public static BecomeMemberIntroFragment getInstance(String from) {
        BecomeMemberIntroFragment fragment = new BecomeMemberIntroFragment();
        Bundle bundle = new Bundle();
        bundle.putString("from", from);
        fragment.setArguments(bundle);
        return fragment;
    }

    private ViewPager mViewPager;
    private String mFrom;

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_become_member_intro;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null) {
            mFrom = getArguments().getString("from");
        }
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewPager = view.findViewById(R.id.becomeMember_ViewPager);
        DotsIndicator tabLayout = view.findViewById(R.id.dots_indicator);

        mViewPager.setAdapter(new BecomeMemberPagerAdapter(getActivity(), mFrom));

        if(mFrom != null && mFrom.equalsIgnoreCase(THPConstants.FROM_BecomeMemberActivity)) {
            tabLayout.setViewPager(mViewPager);
        } else {
            tabLayout.setVisibility(View.GONE);
        }

    }


    @Override
    public void onResume() {
        super.onResume();
        THPFirebaseAnalytics.setFirbaseAnalyticsScreenRecord(getActivity(), "Become a Member", BecomeMemberIntroFragment.class.getSimpleName());
    }
}
