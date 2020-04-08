package com.ns.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import com.ns.clevertap.CleverTapUtil;
import com.ns.loginfragment.BecomeMemberIntroFragment;
import com.ns.thpremium.R;
import com.ns.utils.IntentUtil;
import com.ns.utils.NetUtils;
import com.ns.utils.ResUtil;
import com.ns.utils.THPConstants;
import com.ns.utils.THPFirebaseAnalytics;
import com.ns.utils.TextSpanCallback;
import com.ns.view.text.CustomTextView;

public class BecomeMemberActivity extends BaseAcitivityTHP {

    private CustomTextView signUpFor30Days_Txt;
    private CustomTextView subscribeNowForExclusive_Txt;
    private CustomTextView exploreSubscriptionPlans_Txt;
    private CustomTextView signIn_Txt;
    private boolean isInAleadySignUPIN;

    private BroadcastReceiver broadcastFinishActivity = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase(THPConstants.BROADCAST_ACTION_KILL_BECOME_MEMBER_PAGE)) {
                //Just finish this activity here.
                if (!BecomeMemberActivity.this.isFinishing()) {
                    BecomeMemberActivity.this.finish();
                }
            }
        }
    };


    @Override
    public int layoutRes() {
        return R.layout.activity_becomemember;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BecomeMemberIntroFragment fragment = BecomeMemberIntroFragment.getInstance(THPConstants.FROM_BecomeMemberActivity);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.part1, fragment);
        ft.commit();

        signUpFor30Days_Txt = findViewById(R.id.signUpFor30Days_Txt);
        subscribeNowForExclusive_Txt = findViewById(R.id.subscribeNowForExclusive_Txt);
        exploreSubscriptionPlans_Txt = findViewById(R.id.exploreSubscriptionPlans_Txt);
        signIn_Txt = findViewById(R.id.signIn_Txt);


//        signUpFor30Days_Txt.applyCustomFont(this, getResources().getString(R.string.THP_FiraSans_Bold));
//        subscribeNowForExclusive_Txt.applyCustomFont(this, getResources().getString(R.string.THP_TundraOffc_Bold));
//        exploreSubscriptionPlans_Txt.applyCustomFont(this, getResources().getString(R.string.THP_FiraSans_Bold));
//        signIn_Txt.applyCustomFont(this, getResources().getString(R.string.THP_FiraSans_Regular));


        // Sign Up Click Listener
        signUpFor30Days_Txt.setOnClickListener(v-> {
                    if(!isInAleadySignUPIN) {
                        isInAleadySignUPIN = true;
                        IntentUtil.openSignInOrUpActivity(BecomeMemberActivity.this, THPConstants.FROM_USER_SignUp);
                        THPFirebaseAnalytics.setFirbaseAnalyticsEvent(BecomeMemberActivity.this, "Action", "Sign Up for 30 days Free Trial Button clicked", BecomeMemberActivity.class.getSimpleName());
                    }
                }

        );

        // Back button Click Listener
        findViewById(R.id.backBtn).setOnClickListener(v->
            finish()
        );

        // Explore Our Subscription Click Listener
        exploreSubscriptionPlans_Txt.setOnClickListener(v-> {
                    if(NetUtils.isConnected(this)) {
                        IntentUtil.openSubscriptionActivity(BecomeMemberActivity.this, THPConstants.FROM_SUBSCRIPTION_EXPLORE);
                        THPFirebaseAnalytics.setFirbaseAnalyticsEvent(BecomeMemberActivity.this, "Action", "Explore our Subscription Plans Button clicked", BecomeMemberActivity.class.getSimpleName());
                    } else {
                        noConnectionSnackBar(exploreSubscriptionPlans_Txt);
                    }
                }
        );

        // Sign In Click Listener
        ResUtil.doClickSpanForString(this, "Already have an account? ", "Sign In",
                signIn_Txt, R.color.blueColor_1, new TextSpanCallback() {
                    @Override
                    public void onTextSpanClick() {
                        if(!isInAleadySignUPIN) {
                            isInAleadySignUPIN = true;
                            IntentUtil.openSignInOrUpActivity(BecomeMemberActivity.this, "signIn");
//                            finish();
                            THPFirebaseAnalytics.setFirbaseAnalyticsEvent(BecomeMemberActivity.this, "Action", "Sign In Text clicked", BecomeMemberActivity.class.getSimpleName());

                        }
                    }
                });

        //Register Become a member Killing broadcast receiver
        registerReceiver(broadcastFinishActivity, new IntentFilter(THPConstants.BROADCAST_ACTION_KILL_BECOME_MEMBER_PAGE));
        //Send CT Event - From where this screen appears
        if (THPConstants.FLOW_TAB_CLICK != null) {
            String value = "";
            if (THPConstants.TAP_CROWN.equalsIgnoreCase(THPConstants.FLOW_TAB_CLICK)) {
                value = THPConstants.TAP_CROWN;
            } else if (THPConstants.TAP_BANNER_SUBSCRIPTION.equalsIgnoreCase(THPConstants.FLOW_TAB_CLICK)) {
                value = THPConstants.TAP_BANNER_SUBSCRIPTION;
            } else if (THPConstants.TAB_1.equalsIgnoreCase(THPConstants.FLOW_TAB_CLICK)) {
                value = THPConstants.TAP_BRIEFING;
            } else if (THPConstants.TAB_2.equalsIgnoreCase(THPConstants.FLOW_TAB_CLICK)) {
                value = THPConstants.TAP_STORIES;
            } else if (THPConstants.TAB_3.equalsIgnoreCase(THPConstants.FLOW_TAB_CLICK)) {
                value = THPConstants.TAP_SUGGESTED;
            } else if (THPConstants.TAB_4.equalsIgnoreCase(THPConstants.FLOW_TAB_CLICK)) {
                value = THPConstants.TAP_PROFILE;
            }
            if (!TextUtils.isEmpty(value)) {
                CleverTapUtil.cleverTapEventBenefitsPage(this, THPConstants.CT_KEY_Clicked_Section, value);
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //Register Become a member Killing broadcast receiver
        registerReceiver(broadcastFinishActivity, new IntentFilter(THPConstants.BROADCAST_ACTION_KILL_BECOME_MEMBER_PAGE));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            //Unregister receiver
            unregisterReceiver(broadcastFinishActivity);
        }catch (Exception ignore) {}
    }

    @Override
    protected void onResume() {
        super.onResume();
        isInAleadySignUPIN = false;
        THPFirebaseAnalytics.setFirbaseAnalyticsScreenRecord(this, "BecomeMemberActivity Screen", BecomeMemberActivity.class.getSimpleName());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("", "");
        if(data != null) {
            boolean isKillToBecomeMemberActivity = data.getExtras().getBoolean("isKillToBecomeMemberActivity");
            if(isKillToBecomeMemberActivity) {
                finish();
            }

        }
    }

}
