package com.ns.loginfragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.ns.adapter.SignInAndUpPagerAdapter;
import com.ns.thpremium.R;

public class SignInAndUpFragment extends BaseFragmentTHP {


    @Override
    public int getLayoutRes() {
        return R.layout.fragment_sign_in_and_up;
    }

    public static SignInAndUpFragment getInstance(String from) {
        SignInAndUpFragment fragment = new SignInAndUpFragment();
        Bundle bundle = new Bundle();
        bundle.putString("from", from);
        fragment.setArguments(bundle);
        return fragment;
    }


    private String mFrom;

    private ViewPager mSignInUpViewPager;
    private TabLayout mTabLayout;
    SignInAndUpPagerAdapter pagerAdapter;

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

        int selectedPosition = 0;

        if(mFrom != null && mFrom.contains("signIn")) {
            selectedPosition = 1;
        } else {
            selectedPosition = 0;
        }

        mSignInUpViewPager = view.findViewById(R.id.signInUpViewPager);
        mTabLayout = view.findViewById(R.id.tabLayout);

        if(pagerAdapter == null) {
            pagerAdapter = new SignInAndUpPagerAdapter(getChildFragmentManager(), mFrom, sIsDayTheme);
        }
        mSignInUpViewPager.setAdapter(pagerAdapter);

        mTabLayout.setupWithViewPager(mSignInUpViewPager, true);

        // Iterate over all tabs and set the custom view
        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = mTabLayout.getTabAt(i);
            tab.setCustomView(pagerAdapter.getTabView(i, getActivity(), false));
        }

        // To select default tab
        pagerAdapter.SetOnSelectView(getActivity(), mTabLayout, selectedPosition);

        mSignInUpViewPager.setCurrentItem(selectedPosition);

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                pagerAdapter.SetOnSelectView(getActivity(), mTabLayout, pos);
                mSignInUpViewPager.setCurrentItem(pos);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                pagerAdapter.SetUnSelectView(getActivity(), mTabLayout, pos);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        view.findViewById(R.id.backBtn).setOnClickListener(v->{
            getActivity().finish();
        });
    }

    public void switchToSignInOrSignUp(int position) {
        mSignInUpViewPager.setCurrentItem(position, true);
    }
}
