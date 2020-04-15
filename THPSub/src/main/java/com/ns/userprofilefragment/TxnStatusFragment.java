package com.ns.userprofilefragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.netoperation.net.ApiManager;
import com.ns.loginfragment.BaseFragmentTHP;
import com.ns.loginfragment.SubscriptionStep_3_Fragment;
import com.ns.thpremium.R;
import com.ns.utils.FragmentUtil;
import com.ns.utils.IntentUtil;
import com.ns.utils.ResUtil;
import com.ns.utils.THPConstants;
import com.ns.utils.THPFirebaseAnalytics;
import com.ns.view.text.CustomTextView;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class TxnStatusFragment extends BaseFragmentTHP {

    private String mStatus, mMessage;

    public static TxnStatusFragment getInstance(String status, String message) {
        TxnStatusFragment fragment = new TxnStatusFragment();
        Bundle bundle = new Bundle();
        bundle.putString("status", status);
        bundle.putString("message", message);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        if (mStatus.equalsIgnoreCase("success")) {
            return R.layout.fragment_transaction_successful;
        } else if (mStatus.equalsIgnoreCase("failed")) {
            return R.layout.fragment_transaction_failed;
        } else {//Pending
            return R.layout.fragment_transaction_pending;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mStatus = getArguments().getString("status");
            mMessage = getArguments().getString("message");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayout txnMainLayout = view.findViewById(R.id.txnMainLayout);
        ConstraintLayout transactionParentLayout = view.findViewById(R.id.transactionParentLayout);
        if(txnMainLayout != null) {
            if (mIsDayTheme) {
                txnMainLayout.setBackground(ResUtil.getBackgroundDrawable(getResources(), R.drawable.apptab_transaction_bg));
            } else {
                txnMainLayout.setBackground(ResUtil.getBackgroundDrawable(getResources(), R.drawable.apptab_transaction_bg_dark));
                transactionParentLayout.setBackground(null);
            }
        }

        // Read User Data from local DB
        loadUserData();

        if (mStatus.equalsIgnoreCase("success")) {
            view.findViewById(R.id.viewSubscriptionBtn_Txt).setOnClickListener(view1 ->
            {
                SubscriptionStep_3_Fragment fragment = SubscriptionStep_3_Fragment.getInstance(THPConstants.FROM_PROFILE_VIEWALL);
                FragmentUtil.replaceFragmentAnim((AppCompatActivity)getActivity(), R.id.parentLayout, fragment,
                        FragmentUtil.FRAGMENT_ANIMATION, true);
            });
            view.findViewById(R.id.setPreferenceBtn_Txt).setOnClickListener(v ->
            {
                IntentUtil.openPersonaliseActivity(getActivity(), "");
                getActivity().finish();
            });
            view.findViewById(R.id.checkoutMyDashboard).setOnClickListener(view12 -> {
                IntentUtil.openContentListingActivity(getContext(), "TxnStatus");
                getActivity().finish();
            });
        }

        if (mStatus.equalsIgnoreCase("failed")) {
            CustomTextView textMessage = view.findViewById(R.id.textViewFailedMessage);
            textMessage.setText(mMessage);
            view.findViewById(R.id.textViewTryAgain).setOnClickListener(view14 -> {
                //IntentUtil.openSubscriptionActivity(getActivity(), THPConstants.FROM_SUBSCRIPTION_EXPLORE);
                //getActivity().finish();
                if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0)
                    FragmentUtil.clearSingleBackStack((AppCompatActivity) getActivity());
                else {
                    getActivity().onBackPressed();
                }
            });
            view.findViewById(R.id.textViewDismissWindow).setOnClickListener(view13 -> {

                if (THPConstants.sISMAIN_ACTIVITY_LAUNCHED || THPConstants.IS_FROM_MP_BLOCKER) {
                    THPConstants.IS_FROM_MP_BLOCKER = false;
                    getActivity().finish();
                } else
                getActivity().onBackPressed();
            });
        }

        if (mStatus.equalsIgnoreCase("pending")) {
            CustomTextView textMessage = view.findViewById(R.id.textViewMessagePending);
            textMessage.setText(mMessage);
            view.findViewById(R.id.checkSubscriptionBtn_Txt).setOnClickListener(view1 ->
            {
                SubscriptionStep_3_Fragment fragment = SubscriptionStep_3_Fragment.getInstance(THPConstants.FROM_PROFILE_VIEWALL);
                FragmentUtil.replaceFragmentAnim((AppCompatActivity)getActivity(), R.id.parentLayout, fragment,
                        FragmentUtil.FRAGMENT_ANIMATION, true);
            });
            view.findViewById(R.id.textViewDismissWindow).setOnClickListener(view13 -> {
                //IntentUtil.openContentListingActivity(getContext(), "TxnStatus");
                //getActivity().finish();
                if (THPConstants.sISMAIN_ACTIVITY_LAUNCHED || THPConstants.IS_FROM_MP_BLOCKER) {
                    THPConstants.IS_FROM_MP_BLOCKER = false;
                    getActivity().finish();
                }else
                    getActivity().onBackPressed();
            });
        }
    }


    private void redirection() {
        if(mIsUserLoggedIn && (mHasSubscriptionPlan || mHasFreePlan) ) {
            IntentUtil.openContentListingActivity(getActivity(), "");
        }
        else if(mIsUserLoggedIn && mHasFreePlan) {
            IntentUtil.openSubscriptionActivity(getActivity(), THPConstants.FROM_SUBSCRIPTION_EXPLORE);
        }
        else if(mIsUserLoggedIn && !mHasFreePlan) { // THis is for sort time, may be in future in logic or implementation will come
            IntentUtil.openSubscriptionActivity(getActivity(), THPConstants.FROM_SUBSCRIPTION_EXPLORE);
        }
        else {
            IntentUtil.openMemberActivity(getActivity(), "");
        }
    }


    private boolean mIsUserLoggedIn;
    private boolean mHasFreePlan;
    private boolean mHasSubscriptionPlan;

    private void loadUserData() {
        ApiManager.getUserProfile(getActivity())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userProfile -> {
                    mUserId = userProfile.getUserId();
                    mIsUserLoggedIn = false;
                    if(userProfile != null && !TextUtils.isEmpty(userProfile.getFullName())) {
                        mIsUserLoggedIn = true;
                    } else if(userProfile != null && !TextUtils.isEmpty(userProfile.getEmailId())) {
                        mIsUserLoggedIn = true;
                    } else mIsUserLoggedIn = userProfile != null && !TextUtils.isEmpty(userProfile.getContact());


                    mHasFreePlan = userProfile.isHasFreePlan();
                    mHasSubscriptionPlan = userProfile.isHasSubscribedPlan();

                });
    }

    @Override
    public void onResume() {
        super.onResume();
        THPFirebaseAnalytics.setFirbaseAnalyticsScreenRecord(getActivity(), "Transaction Status Screen", TxnStatusFragment.class.getSimpleName());
    }


}
