package com.ns.loginfragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.netoperation.model.KeyValueModel;
import com.netoperation.model.UserProfile;
import com.netoperation.net.ApiManager;
import com.netoperation.net.RequestCallback;
import com.netoperation.util.NetConstants;
import com.netoperation.util.PremiumPref;
import com.ns.activity.BaseAcitivityTHP;
import com.ns.activity.SignInAndUpActivity;
import com.ns.alerts.Alerts;
import com.ns.clevertap.CleverTapUtil;
import com.ns.thpremium.BuildConfig;
import com.ns.thpremium.R;
import com.ns.utils.CommonUtil;
import com.ns.utils.FragmentUtil;
import com.ns.utils.IntentUtil;
import com.ns.utils.ResUtil;
import com.ns.utils.SocialLoginUtil;
import com.ns.utils.StringUtils;
import com.ns.utils.THPConstants;
import com.ns.utils.THPFirebaseAnalytics;
import com.ns.utils.TextSpanCallback;
import com.ns.view.CustomProgressBarWhite;
import com.ns.view.text.CustomTextView;
import com.ns.view.EmailMobileTextChangedListener;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import java.util.HashMap;

import static android.app.Activity.RESULT_OK;
import static com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes.SIGN_IN_CANCELLED;

public class SignUpFragment extends BaseFragmentTHP implements SocialLoginUtil.SocialLoginCallbacks {
    private static SignUpFragment fragment;
    private String mFrom;

    public static SignUpFragment getInstance() {
        if (fragment == null) {
            fragment = new SignUpFragment();
        }
        return fragment;
    }

    public static SignUpFragment getInstance(String from) {
        fragment = new SignUpFragment();
        Bundle bundle = new Bundle();
        bundle.putString("from", from);
        fragment.setArguments(bundle);
        return fragment;
    }

    public EditText emailOrMobile_Et;
    private CustomProgressBarWhite progressBar;

    private CustomTextView tc_Txt;
    private CustomTextView faq_Txt;
    private CustomTextView signUp_Txt, textViewErrorEmail;

    private ImageButton googleBtn;
    private ImageButton tweeterBtn;
    private ImageButton facebookBtn;

    private boolean isUserEnteredEmail;
    private boolean isUserEnteredMobile;

    private SignInAndUpActivity mActivity;

    private ProgressDialog mProgressDialog;

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_signup;
    }

    private void enableButton(boolean isEnable, boolean isFromSocialLogin) {
        if(isEnable) {
            if(mProgressDialog != null) {
                mProgressDialog.dismiss();
            }
            progressBar.setVisibility(View.GONE);
            signUp_Txt.setText("Sign Up for Free Trial");
        }
        else if(!isFromSocialLogin) {
            progressBar.setVisibility(View.VISIBLE);
            signUp_Txt.setText("");
        }
        signUp_Txt.setEnabled(isEnable);
        googleBtn.setEnabled(isEnable);
        tweeterBtn.setEnabled(isEnable);
        facebookBtn.setEnabled(isEnable);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (SignInAndUpActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            mFrom = getArguments().getString("from");
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        emailOrMobile_Et = view.findViewById(R.id.emailOrMobile_Et);
        Typeface typefaceFiraSansRegular = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/THP_FiraSans-Regular.ttf");
        emailOrMobile_Et.setTypeface(typefaceFiraSansRegular);
        tc_Txt = view.findViewById(R.id.tc_Txt);
        faq_Txt = view.findViewById(R.id.faq_Txt);
        signUp_Txt = view.findViewById(R.id.signUp_Txt);
        progressBar = view.findViewById(R.id.progressBarVerify);

        googleBtn = view.findViewById(R.id.googleBtn);
        tweeterBtn = view.findViewById(R.id.tweeterBtn);
        tweeterBtn.setEnabled(true);
        facebookBtn = view.findViewById(R.id.facebookBtn);
        textViewErrorEmail = view.findViewById(R.id.textViewErrorEmail);

        //Focus change listener
        faq_Txt.setOnClickListener(v->{
            if(!BaseAcitivityTHP.sIsOnline) {
                noConnectionSnackBar(getView());
                return;
            }
            TCFragment fragment = TCFragment.getInstance(THPConstants.FAQ_URL, "crossBackImg");
            FragmentUtil.replaceFragmentAnim((AppCompatActivity)getActivity(), R.id.parentLayout,
                    fragment, FragmentUtil.FRAGMENT_NO_ANIMATION, false);
            THPFirebaseAnalytics.setFirbaseAnalyticsEvent(getContext(), "Action", "FAQ clicked", SignUpFragment.class.getSimpleName());
        });

        // Terms and Conditions Click Listener
        ResUtil.doClickSpanForString(getActivity(), "By signing up, you agree to our  ",
                "Terms and Conditions",
                tc_Txt, R.color.blueColor_1, new TextSpanCallback() {
                    @Override
                    public void onTextSpanClick() {
                        if(!BaseAcitivityTHP.sIsOnline) {
                            noConnectionSnackBar(getView());
                            return;
                        }
                        TCFragment fragment = TCFragment.getInstance(THPConstants.TnC_URL, "crossBackImg");
                        FragmentUtil.replaceFragmentAnim((AppCompatActivity)getActivity(), R.id.parentLayout,
                                fragment, FragmentUtil.FRAGMENT_NO_ANIMATION, false);
                        THPFirebaseAnalytics.setFirbaseAnalyticsEvent(getContext(), "Action", "Terms and Conditions clicked", SignUpFragment.class.getSimpleName());
                    }
                });

        signUp_Txt.setOnClickListener( v-> {
            if(!BaseAcitivityTHP.sIsOnline) {
                noConnectionSnackBar(getView());
                return;
            }
            String emailOrMobile = emailOrMobile_Et.getText().toString();
            String mobileStr = "";
            String emailStr = "";
            String prefix = THPConstants.MOBILE_COUNTRY_CODE;

            boolean hasPrefix = StringUtils.startsWithIgnoreCase(emailOrMobile, prefix);
            emailOrMobile = StringUtils.trimAllWhitespace(emailOrMobile);

            if(hasPrefix) {
                emailOrMobile = StringUtils.delete(emailOrMobile, prefix);
                // This removed extra space and set values after triming space
                emailOrMobile_Et.setText(THPConstants.MOBILE_COUNTRY_CODE+" "+emailOrMobile);
                emailOrMobile_Et.setSelection(emailOrMobile.length()+THPConstants.MOBILE_COUNTRY_CODE.length()+1);
            } else {
                // This removed extra space and set values after triming space
                emailOrMobile_Et.setText(emailOrMobile);
                emailOrMobile_Et.setSelection(emailOrMobile.length());
            }

            if (ResUtil.isEmpty(emailOrMobile)) {
                textViewErrorEmail.setVisibility(View.VISIBLE);
                textViewErrorEmail.setText(R.string.please_enter_valid);
                return;
            }
            else if(hasPrefix && !ResUtil.isValidMobile(emailOrMobile)) {
                textViewErrorEmail.setVisibility(View.VISIBLE);
                textViewErrorEmail.setText("Please enter valid Mobile Number");
                return;
            }
            else if(!hasPrefix && !ResUtil.isValidEmail(emailOrMobile)) {
                textViewErrorEmail.setVisibility(View.VISIBLE);
                textViewErrorEmail.setText("Please enter valid Email Address");
                return;
            }

            isUserEnteredMobile = false;
            isUserEnteredEmail = false;

            if(ResUtil.isValidMobile(emailOrMobile)) {
                isUserEnteredMobile = true;
                isUserEnteredEmail = false;
                mobileStr = emailOrMobile;
            }

            if(ResUtil.isValidEmail(emailOrMobile)) {
                isUserEnteredEmail = true;
                isUserEnteredMobile = false;
                emailStr = emailOrMobile;
            }

            if(!isUserEnteredMobile && !isUserEnteredEmail) {
                //Alerts.showAlertDialogNoBtnWithCancelable(getActivity(), "", "\nPlease enter valid Email or Mobile \n");
                textViewErrorEmail.setVisibility(View.VISIBLE);
                textViewErrorEmail.setText(R.string.please_enter_valid);
                return;
            }


            signUp_Txt.setEnabled(false);

            progressBar.setVisibility(View.VISIBLE);
            signUp_Txt.setText("");
            THPFirebaseAnalytics.setFirbaseAnalyticsEvent(getContext(), "Action", "Sign Up for free Trial  Button clicked", SignUpFragment.class.getSimpleName());

            String mobile = mobileStr;
            String email = emailStr;



            // Hide SoftKeyboard
            CommonUtil.hideKeyboard(getView());

            ApiManager.userVerification(new RequestCallback<KeyValueModel>() {
                @Override
                public void onNext(KeyValueModel keyValueModel) {
                    if(getActivity() == null && getView() == null) {
                        return;
                    }

                    if(keyValueModel.getState() != null && !keyValueModel.getState().equalsIgnoreCase("success")) {
                        if(keyValueModel.getName() != null && keyValueModel.getName().equalsIgnoreCase("User email already exist with social signin")) {
                            textViewErrorEmail.setText(keyValueModel.getName());
                            textViewErrorEmail.setVisibility(View.VISIBLE);
                        }
                        else if (keyValueModel.getName() != null && keyValueModel.getName().contains("already exist")) {
                            SignInFragment.getInstance().emailOrMobile_Et.setText(emailOrMobile_Et.getText().toString());
                            SignInFragment.getInstance().emailOrMobile_Et.setSelection(SignInFragment.getInstance().emailOrMobile_Et.getText().length());
                            Alerts.showAlertDialogOKListener(getActivity(), "Sorry!", keyValueModel.getName(),
                                    (dialogInterface, i) -> {
                                        for (Fragment fragment : getActivity().getSupportFragmentManager().getFragments()) {
                                            if (fragment instanceof SignInAndUpFragment) {
                                                ((SignInAndUpFragment) fragment).switchToSignInOrSignUp(1); //Switch to Sign In requirement by THG
                                                break;
                                            }
                                        }
                                    });
                        } else {
                            Alerts.showAlertDialogOKBtn(getActivity(), "Sorry!", keyValueModel.getName());
                        }
                    }
                    else {
                        // Opening OTP Verification Screen
                        String from = mActivity.mFrom;
                        if(mActivity.mFrom != null && mActivity.mFrom.contains("signIn")) {
                            from = THPConstants.FROM_USER_SignUp;
                        }
                        OTPVerificationFragment fragment = OTPVerificationFragment.getInstance(from,
                                isUserEnteredEmail, email, mobile);
                        FragmentUtil.replaceFragmentAnim((AppCompatActivity)getActivity(), R.id.parentLayout, fragment,
                                FragmentUtil.FRAGMENT_ANIMATION, false);

                    }

                }

                @Override
                public void onError(Throwable t, String str) {
                    if(getActivity() != null && getView() != null) {
                        progressBar.setVisibility(View.GONE);
                        signUp_Txt.setText("Sign Up for Free Trial");
                        signUp_Txt.setEnabled(true);
                        Alerts.showErrorDailog(getChildFragmentManager(), null, t.getLocalizedMessage());
                    }
                }

                @Override
                public void onComplete(String str) {
                    progressBar.setVisibility(View.GONE);
                    signUp_Txt.setText("Sign Up for Free Trial");
                    signUp_Txt.setEnabled(true);
                }
            }, emailStr, mobileStr, BuildConfig.SITEID, NetConstants.EVENT_SIGNUP);



        });


        // Google Sign in click listener
        googleBtn.setOnClickListener(v->{
            if(!BaseAcitivityTHP.sIsOnline) {
                noConnectionSnackBar(getView());
                return;
            }
            mProgressDialog = Alerts.showProgressDialog(getActivity());
            //mProgressDialog.setCancelable(true);
            mSocialLoginUtil = new SocialLoginUtil(getActivity(), "google", mProgressDialog, this);
            THPFirebaseAnalytics.setFirbaseAnalyticsEvent(getContext(), "Action", "Google Icon clicked", SignUpFragment.class.getSimpleName());

        });

        // Facebook Sign in click listener
        facebookBtn.setOnClickListener(v->{
            if(!BaseAcitivityTHP.sIsOnline) {
                noConnectionSnackBar(getView());
                return;
            }

            mProgressDialog = Alerts.showProgressDialog(getActivity());
            //mProgressDialog.setCancelable(true);
            mSocialLoginUtil = new SocialLoginUtil(getActivity(), this,"facebook", mProgressDialog, this);
            THPFirebaseAnalytics.setFirbaseAnalyticsEvent(getContext(), "Action", "Facebook Icon clicked", SignUpFragment.class.getSimpleName());
        });


        //Twitter Sign in click listener
        tweeterBtn.setOnClickListener(v->{
            if(!BaseAcitivityTHP.sIsOnline) {
                noConnectionSnackBar(getView());
                return;
            }
            mProgressDialog = Alerts.showProgressDialog(getActivity());
            //mProgressDialog.setCancelable(true);
            mSocialLoginUtil = new SocialLoginUtil(getActivity(), this,"twitter", mProgressDialog, this);
            THPFirebaseAnalytics.setFirbaseAnalyticsEvent(getContext(), "Action", "Twitter Icon clicked", SignUpFragment.class.getSimpleName());
        });

        // DrawableRight action
        emailOrMobile_Et.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(emailOrMobile_Et.getCompoundDrawables()[DRAWABLE_RIGHT] != null && event.getRawX() >= (emailOrMobile_Et.getRight() - emailOrMobile_Et.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        //Clear text
                        emailOrMobile_Et.setText("");
                        return false;
                    }
                }
                return false;
            }
        });

        // Text Change Listener
        emailOrMobile_Et.addTextChangedListener(new EmailMobileTextChangedListener(THPConstants.MOBILE_COUNTRY_CODE, emailOrMobile_Et, textViewErrorEmail));


    }


    private SocialLoginUtil mSocialLoginUtil;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == TwitterAuthConfig.DEFAULT_AUTH_REQUEST_CODE) {
            if (mSocialLoginUtil != null && mSocialLoginUtil.getTwitterAuthClient() != null)
                mSocialLoginUtil.getTwitterAuthClient().onActivityResult(requestCode, resultCode, data);

        }else {
            //if the requestCode is the Google Sign In code that we defined at starting
            if (requestCode == SocialLoginUtil.RC_SIGN_IN) {

                //Getting the GoogleSignIn Task
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                try {
                    //Google Sign In was successful, authenticate with Firebase
                    GoogleSignInAccount account = task.getResult(ApiException.class);

                    //authenticating with firebase
                    mSocialLoginUtil.firebaseAuthWithGoogle(account);
                } catch (ApiException e) {
                    if (mProgressDialog != null) {
                        enableButton(true, false);
                    }
                    if(e.getStatusCode()!=SIGN_IN_CANCELLED)
                        Alerts.showToast(getActivity(), e.getMessage());
                }
            }else {
                /*For Facebook callback*/
                /*For Facebook callback*/
                if (mSocialLoginUtil != null && mSocialLoginUtil.getCallbackManager() != null)
                    mSocialLoginUtil.getCallbackManager().onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fragment = null;
    }

    private void openContentListingActivity(boolean isKillToBecomeMemberActivity, UserProfile userProfile, String from, boolean isNewAccount) {
        //Always set True if Successful Login
        PremiumPref.getInstance(getContext()).setIsRelogginSuccess(true);
        Intent intent = new Intent();
        intent.putExtra("isKillToBecomeMemberActivity", isKillToBecomeMemberActivity);
        if (mActivity.mFrom != null && !TextUtils.isEmpty(mActivity.mFrom) && mActivity.mFrom.contains(THPConstants.PAYMENT)) {
            intent.putExtra("isRequestPayment", true);
            PremiumPref.getInstance(getActivity()).setIsRefreshRequired(true);
        } else {
            if(from != null) {
                if(from.equalsIgnoreCase("facebook")) {
                    Alerts.showToast(getActivity(), "Successfully Logged In with Facebook.");
                } else if(from.equalsIgnoreCase("google")) {
                    Alerts.showToast(getActivity(), "Successfully Logged In with Gmail.");
                } else if(from.equalsIgnoreCase("twitter")) {
                    Alerts.showToast(getActivity(), "Successfully Logged In with Twitter.");
                }
            }

            if (userProfile != null && (userProfile.isHasFreePlan() || userProfile.isHasSubscribedPlan())) {
                // TODO, process for user sign - In
                // When user logged in then we need this to refresh the default TH listing page.
                PremiumPref.getInstance(getActivity()).setIsRefreshRequired(true);

                if (THPConstants.IS_FROM_MP_BLOCKER) {
                    //Disable the MP constant
                    THPConstants.IS_FROM_MP_BLOCKER = false;
                } else if(THPConstants.FLOW_TAB_CLICK != null && THPConstants.FLOW_TAB_CLICK.equals(THPConstants.TAB_4)) {
                    if(isNewAccount) {
                        IntentUtil.openContentListingActivity(getActivity(), THPConstants.FROM_USER_SignUp);
                    } else {
                        IntentUtil.openUserProfileActivity(getActivity(), THPConstants.FROM_USER_PROFILE);
                    }
                } else {
                    if(isNewAccount) {
                        IntentUtil.openContentListingActivity(getActivity(), THPConstants.FROM_USER_SignUp);
                    } else {
                        IntentUtil.openContentListingActivity(getActivity(), "");
                    }
                }
            } else if (userProfile != null && !(userProfile.isHasFreePlan() && userProfile.isHasSubscribedPlan())) {
                // When user logged in then we need this to refresh the default TH listing page.
                PremiumPref.getInstance(getActivity()).setIsRefreshRequired(true);
                if (THPConstants.IS_FROM_MP_BLOCKER) {
                    //Disable the MP constant
                    THPConstants.IS_FROM_MP_BLOCKER = false;
                } else // Redirect in Subcription plan Page
                if(mActivity.mFrom != null &&
                        (mActivity.mFrom.equalsIgnoreCase(THPConstants.FROM_SignUpAndPayment) || mActivity.mFrom.equalsIgnoreCase(THPConstants.FROM_START_30_DAYS_TRAIL))) {
                    // TODO, Currently we hope nothing to do
                } else {
                    IntentUtil.openSubscriptionActivity(getActivity(), THPConstants.FROM_SUBSCRIPTION_EXPLORE);
                }
            }

            // Send User SignIn Profile to CleverTap
            String loginSource = THPConstants.DIRECT_LOGIN;
            if(from != null) {
                if(from.equalsIgnoreCase("facebook")) {
                    loginSource = THPConstants.FACEBOOK_LOGIN;
                } else if(from.equalsIgnoreCase("google")) {
                    loginSource = THPConstants.GOOGLE_LOGIN;
                } else if(from.equalsIgnoreCase("twitter")) {
                    loginSource = THPConstants.TWITTER_LOGIN;
                }
            }
            PremiumPref.getInstance(getContext()).setLoginSource(loginSource);
            CleverTapUtil.cleverTapUpdateProfile(getActivity(), true, userProfile, userProfile.isHasSubscribedPlan(), userProfile.isHasFreePlan());
        }

        getActivity().setResult(RESULT_OK, intent);
        getActivity().finish();
        //Send Broadcast to Kill Become a member screen once user Signed up or Signed In
        CommonUtil.sendBroadcastToKillBecomeAMemberScreen(getActivity());

    }


    @Override
    public void onStartSocialLogin() {
        enableButton(false, true);
    }

    @Override
    public void onFinishSocialLogin(boolean isKillToBecomeMemberActivity, UserProfile userProfile, String from, boolean isNewAccount) {
        if(mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        openContentListingActivity(isKillToBecomeMemberActivity, userProfile, from, isNewAccount);
        //CleverTap
        HashMap<String,Object> map = new HashMap<>();
        map.put(THPConstants.CT_Custom_KEY_Email,userProfile.getEmailId());
        map.put(THPConstants.CT_KEY_Login_Source, PremiumPref.getInstance(getContext()).getLoginSource());
        CleverTapUtil.cleverTapEvent(getActivity(),THPConstants.CT_EVENT_SIGN_UP,map);

        //Send CleverTap Event for Free Trial Subscription
        if (isNewAccount) {
            CleverTapUtil.cleverTapEventFreeTrial(getContext());
            THPFirebaseAnalytics.setFirbaseFreeTrialEvent(getContext());
        }
    }

    @Override
    public void onErrorSocialLogin() {
        if(mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        enableButton(true, true);
    }

    @Override
    public void initGoogleLogin(Intent signInIntent) {
        startActivityForResult(signInIntent, SocialLoginUtil.RC_SIGN_IN);
    }

    @Override
    public void initFacebookLogin(Intent signInIntent) {

    }


    @Override
    public void onResume() {
        super.onResume();
        THPFirebaseAnalytics.setFirbaseAnalyticsScreenRecord(getActivity(), "SignUp Screen", SignUpFragment.class.getSimpleName());
    }
}
