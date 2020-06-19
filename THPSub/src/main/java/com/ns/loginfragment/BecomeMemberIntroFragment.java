package com.ns.loginfragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.netoperation.net.DefaultTHApiManager;
import com.netoperation.net.RequestCallback;
import com.ns.adapter.BecomeMemberPagerAdapter;
import com.ns.adapter.BecomeMember_URL_PagerAdapter;
import com.ns.thpremium.R;
import com.ns.utils.THPConstants;
import com.ns.utils.THPFirebaseAnalytics;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.util.List;

public class BecomeMemberIntroFragment extends BaseFragmentTHP {


    public static BecomeMemberIntroFragment getInstance(String from) {
        BecomeMemberIntroFragment fragment = new BecomeMemberIntroFragment();
        Bundle bundle = new Bundle();
        bundle.putString("from", from);
        fragment.setArguments(bundle);
        return fragment;
    }

    private ViewPager mViewPager;
    private LinearLayout emptyLayout;
    private TextView msgText;
    private Button refreshBtn;
    private DotsIndicator dotIndicator;
    private ProgressBar progressBar;
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
        emptyLayout = view.findViewById(R.id.emptyLayout);
        msgText = view.findViewById(R.id.msgText);
        refreshBtn = view.findViewById(R.id.refreshBtn);
        dotIndicator = view.findViewById(R.id.dots_indicator);
        progressBar = view.findViewById(R.id.progressBar);

        refreshBtn.setOnClickListener(v->{
            progressBar.setVisibility(View.VISIBLE);
            emptyLayout.setVisibility(View.GONE);
            emptyLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    uspRequest();
                }
            }, 300);

        });

        uspRequest();

        //mViewPager.setAdapter(new BecomeMemberPagerAdapter(getActivity(), mFrom));

    }

    private void uspRequest() {
        DefaultTHApiManager.getUPS(getActivity(), new RequestCallback<List<String>>() {
            @Override
            public void onNext(List<String> strings) {
                if(strings != null && strings.size() > 0) {
                    progressBar.setVisibility(View.GONE);
                    emptyLayout.setVisibility(View.GONE);
                    mViewPager.setVisibility(View.VISIBLE);
                    mViewPager.setAdapter(new BecomeMember_URL_PagerAdapter(getActivity(), strings));
                }
                else {
                    emptyLayout.setVisibility(View.VISIBLE);
                    mViewPager.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                }

            }

            @Override
            public void onError(Throwable t, String str) {
                emptyLayout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onComplete(String str) {
                if(mFrom != null && mFrom.equalsIgnoreCase(THPConstants.FROM_BecomeMemberActivity)) {
                    dotIndicator.setViewPager(mViewPager);
                } else {
                    dotIndicator.setVisibility(View.GONE);
                }
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        THPFirebaseAnalytics.setFirbaseAnalyticsScreenRecord(getActivity(), "Become a Member", BecomeMemberIntroFragment.class.getSimpleName());
    }
}
