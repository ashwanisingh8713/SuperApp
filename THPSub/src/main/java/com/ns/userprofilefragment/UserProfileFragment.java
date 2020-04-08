package com.ns.userprofilefragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.netoperation.model.TxnDataBean;
import com.netoperation.model.UserProfile;
import com.netoperation.net.ApiManager;
import com.netoperation.util.NetConstants;
import com.netoperation.util.THPPreferences;
import com.ns.alerts.Alerts;
import com.ns.clevertap.CleverTapUtil;
import com.ns.loginfragment.BaseFragmentTHP;
import com.ns.loginfragment.SubscriptionStep_3_Fragment;
import com.ns.thpremium.BuildConfig;
import com.ns.thpremium.R;
import com.ns.utils.FragmentUtil;
import com.ns.utils.IntentUtil;
import com.ns.utils.ResUtil;
import com.ns.utils.THPConstants;
import com.ns.utils.THPFirebaseAnalytics;
import com.ns.view.text.CustomTextView;
import com.twitter.sdk.android.core.TwitterCore;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;


public class UserProfileFragment extends BaseFragmentTHP {

    private String mFrom;

    private CustomTextView packName_Txt;
    private CustomTextView planValidity_Txt;
    private CustomTextView userName_Txt;
    private CustomTextView mobileNumber_Txt;

    private CustomTextView versionName_Txt;


    private UserProfile mUserProfile;
    private GoogleSignInClient mGoogleSignInClient;


    public static UserProfileFragment getInstance(String from) {
        UserProfileFragment fragment = new UserProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putString("from", from);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_userprofile;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null) {
            mFrom = getArguments().getString("from");
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        packName_Txt = view.findViewById(R.id.packName_Txt);
        planValidity_Txt = view.findViewById(R.id.planValidity_Txt);
        versionName_Txt = view.findViewById(R.id.versionName_Txt);
        userName_Txt = view.findViewById(R.id.userName_Txt);
        mobileNumber_Txt = view.findViewById(R.id.mobileNumber_Txt);


            // Subscribe Button Click Listener
            view.findViewById(R.id.subscribeBtn_Txt).setOnClickListener(v -> {
                if(!mIsOnline) {
                    noConnectionSnackBar(getView());
                    return;
                }
                IntentUtil.openSubscriptionActivity(getActivity(), THPConstants.FROM_SUBSCRIPTION_EXPLORE);
            });
            view.findViewById(R.id.subscribeLayout).setOnClickListener(v -> {
                if(!mIsOnline) {
                    noConnectionSnackBar(getView());
                    return;
                }
                IntentUtil.openSubscriptionActivity(getActivity(), THPConstants.FROM_SUBSCRIPTION_EXPLORE);
            });
            view.findViewById(R.id.subsCloseImg).setOnClickListener(v -> {
                THPPreferences.getInstance(getActivity()).setIsSubscribeClose(true);
                view.findViewById(R.id.subscribeLayout).setVisibility(View.GONE);
            });


        loadUserProfileData();


        // Back button click listener
        view.findViewById(R.id.backBtn).setOnClickListener(v->{
            getActivity().finish();
        });

        // Change Button click listener
        view.findViewById(R.id.viewAllBtn_Txt).setOnClickListener(v->{
            if(!mIsOnline) {
                noConnectionSnackBar(getView());
                return;
            }
            SubscriptionStep_3_Fragment fragment = SubscriptionStep_3_Fragment.getInstance(THPConstants.FROM_PROFILE_VIEWALL);
            FragmentUtil.pushFragmentAnim((AppCompatActivity)getActivity(), R.id.parentLayout, fragment,
                    FragmentUtil.FRAGMENT_ANIMATION, false);
        });

        //View All
        view.findViewById(R.id.part2Layout).setOnClickListener(view12 -> {
            if(!mIsOnline) {
                noConnectionSnackBar(getView());
                return;
            }
            SubscriptionStep_3_Fragment fragment = SubscriptionStep_3_Fragment.getInstance(THPConstants.FROM_PROFILE_VIEWALL);
            FragmentUtil.pushFragmentAnim((AppCompatActivity)getActivity(), R.id.parentLayout, fragment,
                    FragmentUtil.FRAGMENT_ANIMATION, false);
        });


        // User Next Btn button click listener
        view.findViewById(R.id.part1Layout).setOnClickListener(v->{
            AccountInfoFragment fragment = AccountInfoFragment.getInstance("");
            FragmentUtil.pushFragmentAnim((AppCompatActivity) getActivity(), R.id.parentLayout,
                    fragment, FragmentUtil.FRAGMENT_NO_ANIMATION, false);
            THPFirebaseAnalytics.setFirbaseAnalyticsEvent(getContext(), "Action", "Hi User clicked", UserProfileFragment.class.getSimpleName());
        });

        // Personal Info Row click listener - 1
        view.findViewById(R.id.personalInfo_Row).setOnClickListener(v->{
            PersonalInfoFragment fragment = PersonalInfoFragment.getInstance("");
            FragmentUtil.pushFragmentAnim((AppCompatActivity) getActivity(), R.id.parentLayout,
                    fragment, FragmentUtil.FRAGMENT_NO_ANIMATION, false);
            THPFirebaseAnalytics.setFirbaseAnalyticsEvent(getContext(), "Action", "Personal Info clicked", UserProfileFragment.class.getSimpleName());
        });

        // My Address Row click listener - 2
        view.findViewById(R.id.myAddress_Row).setOnClickListener(v->{
            MyAddressFragment fragment = MyAddressFragment.getInstance("");
            FragmentUtil.pushFragmentAnim((AppCompatActivity) getActivity(), R.id.parentLayout,
                    fragment, FragmentUtil.FRAGMENT_NO_ANIMATION, false);
            THPFirebaseAnalytics.setFirbaseAnalyticsEvent(getContext(), "Action", "My Address clicked", UserProfileFragment.class.getSimpleName());
        });

        // Notification Row click listener - 3
        view.findViewById(R.id.notification_Row).setVisibility(View.GONE);
        view.findViewById(R.id.notification_Row).setOnClickListener(v->{
            NotificationFragment fragment = NotificationFragment.getInstance("");
            FragmentUtil.pushFragmentAnim((AppCompatActivity) getActivity(), R.id.parentLayout,
                    fragment, FragmentUtil.FRAGMENT_NO_ANIMATION, false);
            THPFirebaseAnalytics.setFirbaseAnalyticsEvent(getContext(), "Action", "Notifications clicked", UserProfileFragment.class.getSimpleName());
        });

        // Transaction History Row click listener - 4
        view.findViewById(R.id.transactionHistory_Row).setOnClickListener(v->{
            TransactionHistoryFragment fragment = TransactionHistoryFragment.getInstance("");
            FragmentUtil.pushFragmentAnim((AppCompatActivity) getActivity(), R.id.parentLayout,
                    fragment, FragmentUtil.FRAGMENT_NO_ANIMATION, false);
            THPFirebaseAnalytics.setFirbaseAnalyticsEvent(getContext(), "Action", "Transaction History clicked", UserProfileFragment.class.getSimpleName());
        });


        // Change Password Row click listener - 5
        view.findViewById(R.id.changePassword_Row).setOnClickListener(v->{
            String loginSource = mUserProfile.getLoginSource();
            // Because if user logged in by normal login then we get response "app_direct"
            if(loginSource != null && loginSource.contains("direct")) {
                ChangePasswordFragment fragment = ChangePasswordFragment.getInstance("");
                FragmentUtil.pushFragmentAnim((AppCompatActivity) getActivity(), R.id.parentLayout,
                        fragment, FragmentUtil.FRAGMENT_NO_ANIMATION, false);
                THPFirebaseAnalytics.setFirbaseAnalyticsEvent(getContext(), "Action", "Change Password clicked", UserProfileFragment.class.getSimpleName());
            } else {
                Alerts.showAlertDialogOKBtn(getActivity(), "Change Password", "You are unable to change your password as you have signed in using a social media account");
            }
        });

        // Manage Your Address Row click listener - 6
        view.findViewById(R.id.manageYourAccounts_Row).setOnClickListener(v->{
            ManageAccountsFragment fragment = ManageAccountsFragment.getInstance("");
            FragmentUtil.pushFragmentAnim((AppCompatActivity) getActivity(), R.id.parentLayout,
                    fragment, FragmentUtil.FRAGMENT_NO_ANIMATION, false);
        });

        // Sign Out Row click listener - 7
        view.findViewById(R.id.signOut_Row).setOnClickListener(v -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(getString(R.string.sign_out));
                    builder.setMessage(getString(R.string.text_confirm_signout));
                    builder.setPositiveButton("Yes", (dialogInterface, i) -> confirmedToSignOut());
                    builder.setNegativeButton("Cancel", (dialogInterface, i) -> {dialogInterface.cancel(); signOutCTNo();});
                    builder.create().show();
            THPFirebaseAnalytics.setFirbaseAnalyticsEvent(getContext(), "Action", "Sign Out clicked", UserProfileFragment.class.getSimpleName());


                }
        );

        view.findViewById(R.id.feedback).setOnClickListener(view1 -> sendFeedbackViaEmail());

        versionName_Txt.setText(ResUtil.getVersionName(getActivity()));

    }

    private void signOutCTNo() {
        //CleverTap
        HashMap<String,Object> map = new HashMap<>();
        map.put("No",null);
        CleverTapUtil.cleverTapEvent(getActivity(),THPConstants.CT_EVENT_SIGN_OUT,map);
    }

    private void sendFeedbackViaEmail() {
        try {
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"customersupport@thehindu.co.in"});
            emailIntent.putExtra(Intent.EXTRA_TEXT, "");
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback for The Hindu app");
            emailIntent.putExtra(Intent.EXTRA_CC, "");
            emailIntent.setType("message/rfc822");
            final PackageManager pm = getActivity().getPackageManager();
            final List<ResolveInfo> matches = pm.queryIntentActivities(emailIntent, 0);
            ResolveInfo best = null;
            for (final ResolveInfo info : matches)
                if (info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail"))
                    best = info;
            if (best != null)
                emailIntent.setClassName(best.activityInfo.packageName, best.activityInfo.name);
            startActivity(Intent.createChooser(emailIntent, "Send Feedback"));
            THPFirebaseAnalytics.setFirbaseAnalyticsEvent(getContext(), "Action", "Feedback clicked", UserProfileFragment.class.getSimpleName());
            //CleverTap Event Capture
            HashMap<String,Object> map = new HashMap<>();
            map.put(THPConstants.CT_KEY_Feedback,"Yes");
            CleverTapUtil.cleverTapEvent(getActivity(), THPConstants.CT_EVENT_PROFILE, map);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void confirmedToSignOut() {
        if(!mIsOnline) {
            noConnectionSnackBar(getView());
            return;
        }
        progressDialog = Alerts.showProgressDialog(getActivity());
        ApiManager.logout(getActivity(), mUserProfile.getUserId(), BuildConfig.SITEID, ResUtil.getDeviceId(getActivity()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(keyValueModel->{
                    if(keyValueModel.getState() != null && keyValueModel.getState().equalsIgnoreCase(NetConstants.SUCCESS)) {
                        clearDatabase();
                    } else if (keyValueModel.getName() != null && keyValueModel.getName().equalsIgnoreCase("1")) {
                        clearDatabase();
                    } else {
                        Alerts.showToast(getActivity(), "Could not log out, please try again later");
                    }
                }, throwable -> {
                    progressDialog.cancel();
                    Alerts.showToast(getActivity(), throwable.getMessage());
                }, () ->{
                    progressDialog.cancel();
                    //Alerts.showToast(getActivity(), "Could not logged out, please try again later");
                });
    }


    private ProgressDialog progressDialog;
    private void clearDatabase() {
        // CleverTap Tracking
      //  CleverTapUtil.cleverTapLogoutEvent(getActivity(), mUserProfile);

        Observable observable = ApiManager.clearDatabaseAsync(getActivity());
        observable.map(v->{

            // Clear THP Preference
            THPPreferences.getInstance(getActivity()).clearPref();

            // Facebook Logout
            if(AccessToken.getCurrentAccessToken() != null) {
                AccessToken.setCurrentAccessToken(null);
            }
            try {
                // Twitter Logout
                if (TwitterCore.getInstance().getSessionManager().getActiveSession() != null) {
                    CookieSyncManager.createInstance(getActivity());
                    CookieManager cookieManager = CookieManager.getInstance();
                    cookieManager.removeSessionCookie();
                    TwitterCore.getInstance().getSessionManager().clearActiveSession();
                }
            } catch (Exception ignore) {
                Log.i("", "");
            }
            // Google Logout
            if(mGoogleSignInClient != null) {
                GoogleSignInAccount googleAccount = GoogleSignIn.getLastSignedInAccount(getActivity());
                if(googleAccount!=null) {
                    mGoogleSignInClient.signOut()
                            .addOnCompleteListener(getActivity(), task -> {
                                //Signed out
                            });
                }

            }

            // To track user logout in CleverTap
            // CleverTapUtil.cleverTapLogoutEvent(getActivity(), mUserProfile);
            // CleverTap
            HashMap<String,Object> map = new HashMap<>();
            map.put("Yes",null);
            CleverTapUtil.cleverTapEvent(getActivity(),THPConstants.CT_EVENT_SIGN_OUT,map);

            //Clear User Id value in CleverTapUtil
            CleverTapUtil.clearUserId();

            return "";
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(v->{
                    Alerts.showToast(getActivity(), "Logged out successfully.");
                    // Opens TheHindu Default Sections Screen
                    Intent intent = new Intent();
                    intent.putExtra("isKillToAppTabActivity", true);
                    getActivity().setResult(Activity.RESULT_OK, intent);
                    getActivity().finish();
                    progressDialog.dismiss();

                    // Need to refresh the Default Hindu Screens for Ads
                    THPPreferences.getInstance(getActivity()).setIsRefreshRequired(true);
                });

    }

    /**
     * // Loads User Profile Data from local Database
     */
    private void loadUserProfileData() {
        mDisposable.add(ApiManager.getUserProfile(getActivity())
                .observeOn(AndroidSchedulers.mainThread())
                .map(userProfile -> {
                    if (userProfile == null) {
                        return "";
                    }

                    mUserProfile = userProfile;
                    String userName = mUserProfile.getFullNameForProfile();
                    userName_Txt.setText("Hi "+userName);
                    //Display subscribed plan info
                    if (mUserProfile.getUserPlanList() != null && mUserProfile.getUserPlanList().size() > 0) {
                        for (TxnDataBean plan : mUserProfile.getUserPlanList()) {
                            if (plan.getIsActive() == 1) {
                                packName_Txt.setText(plan.getPlanName());
                                planValidity_Txt.setText("Valid Till - "+plan.geteDate());
                            }
                        }
                    } else {
                        planValidity_Txt.setText("Subscription plans");
                    }
                    if(mUserProfile.getContact()!= null && !TextUtils.isEmpty(mUserProfile.getContact())) {
                        mobileNumber_Txt.setText("+91"+mUserProfile.getContact());
                    } else {
                        mobileNumber_Txt.setText(mUserProfile.getEmailId());
                    }

                    boolean hasFreePlan = userProfile.isHasFreePlan();
                    boolean hasSubscriptionPlan = userProfile.isHasSubscribedPlan();


                    if(hasSubscriptionPlan) {
                        getView().findViewById(R.id.subscribeLayout).setVisibility(View.GONE);
                    } else if(THPPreferences.getInstance(getActivity()).isSubscribeClose()) {
                        getView().findViewById(R.id.subscribeLayout).setVisibility(View.GONE);
                    } else {
                        getView().findViewById(R.id.subscribeLayout).setVisibility(View.VISIBLE);
                    }

                    return "";
                })
                .subscribe(v -> {
                        },
                        t -> {
                            Log.i("", "" + t);
                        }));
    }

    @Override
    public void onResume() {
        super.onResume();
        THPFirebaseAnalytics.setFirbaseAnalyticsScreenRecord(getActivity(), "Users Profile Screen", UserProfileFragment.class.getSimpleName());
    }

}
