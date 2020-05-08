package com.ns.loginfragment;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ns.thpremium.R;
import com.ns.utils.FragmentUtil;
import com.ns.utils.IntentUtil;
import com.ns.utils.ResUtil;
import com.ns.utils.THPConstants;
import com.ns.utils.THPFirebaseAnalytics;

public class AccountCreatedFragment extends BaseFragmentTHP {

    public static AccountCreatedFragment getInstance(String val) {
        AccountCreatedFragment fragment = new AccountCreatedFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public int getLayoutRes() {
        return R.layout.fragment_account_created;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null) {

        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayout shadowLayout = view.findViewById(R.id.shadowLayout);

        if(sIsDayTheme) {
            shadowLayout.setBackground(ResUtil.getBackgroundDrawable(getResources(), R.drawable.shadow_white_r12_s6_wh200_ltr));
        } else {
            shadowLayout.setBackground(ResUtil.getBackgroundDrawable(getResources(), R.drawable.shadow_dark_r12_s6_wh200_ltr));
        }

        view.findViewById(R.id.viewSubscriptionBtn_Txt).setOnClickListener(v->{
            FragmentUtil.clearSingleBackStack((AppCompatActivity) getActivity());
            IntentUtil.openSubscriptionActivity(getActivity(), THPConstants.FROM_USER_ACCOUNT_CREATED);
        });

        view.findViewById(R.id.setPreferenceBtn_Txt).setOnClickListener(v->{
            IntentUtil.openPersonaliseActivity(getActivity(), "");
            FragmentUtil.clearSingleBackStack((AppCompatActivity) getActivity());
        });

        view.findViewById(R.id.accoutCreatedParent).setOnClickListener((v-> {
            FragmentUtil.clearSingleBackStack((AppCompatActivity) getActivity());
        }));

        view.findViewById(R.id.otpLayout).setOnClickListener((v1-> {
        }));

        // Close button click listener
        view.findViewById(R.id.closeButton).setOnClickListener(v->
                FragmentUtil.clearSingleBackStack((AppCompatActivity) getActivity())
        );
    }

    @Override
    public void onResume() {
        super.onResume();
        THPFirebaseAnalytics.setFirbaseAnalyticsScreenRecord(getActivity(), "Account Created", AccountCreatedFragment.class.getSimpleName());
    }


}
