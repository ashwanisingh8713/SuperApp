package com.ns.loginfragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
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
import com.netoperation.model.UserProfile;
import com.netoperation.net.ApiManager;
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

import io.reactivex.android.schedulers.AndroidSchedulers;

import static android.app.Activity.RESULT_OK;
import static com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes.SIGN_IN_CANCELLED;

public class SignInFragment extends BaseFragmentTHP implements SocialLoginUtil.SocialLoginCallbacks  {

    @SuppressLint("StaticFieldLeak")
    private static SignInFragment fragment;

    private String mFrom;
    public static boolean IS_FORGOT_PASSWORD_REDIRECT_PAYMENT;

    public static SignInFragment getInstance() {
        if (fragment == null) {
            fragment = new SignInFragment();
        }
        return fragment;
    }

    public static SignInFragment getInstance(String from) {
        fragment = new SignInFragment();
        Bundle bundle = new Bundle();
        bundle.putString("from", from);
        fragment.setArguments(bundle);
        return fragment;
    }

    public EditText emailOrMobile_Et;
    private EditText password_Et;
    private ImageButton passwordVisible_Btn;
    private boolean mIsPasswdVisible;

    private CustomTextView tc_Txt;
    private CustomTextView signIn_Txt;
    private CustomTextView textViewErrorEmail, textViewErrorPassword;

    private ImageButton googleBtn;
    private ImageButton tweeterBtn;
    private ImageButton facebookBtn;

    private boolean isUserEnteredEmail;
    private boolean isUserEnteredMobile;

    private CustomProgressBarWhite progressBar;

    private SignInAndUpActivity mActivity;


    private ProgressDialog mProgressDialog;

    private SocialLoginUtil mSocialLoginUtil;

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_signin;
    }

    private void enableButton(boolean isEnable, boolean isFromSocialLogin) {
        if(isEnable) {
            if(mProgressDialog != null) {
                mProgressDialog.dismiss();
            }
            progressBar.setVisibility(View.GONE);
            signIn_Txt.setText("Sign In");
        } else if(!isFromSocialLogin) {
            progressBar.setVisibility(View.VISIBLE);
            signIn_Txt.setText("");
        }
        signIn_Txt.setEnabled(isEnable);
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
        password_Et = view.findViewById(R.id.password_Et);
        password_Et.setTypeface(typefaceFiraSansRegular);
        passwordVisible_Btn = view.findViewById(R.id.passwordVisible_Btn);

        tc_Txt = view.findViewById(R.id.tc_Txt);

        googleBtn = view.findViewById(R.id.googleBtn);
        tweeterBtn = view.findViewById(R.id.tweeterBtn);
        tweeterBtn.setEnabled(true);
        facebookBtn = view.findViewById(R.id.facebookBtn);
        signIn_Txt = view.findViewById(R.id.signIn_Txt);
        progressBar = view.findViewById(R.id.progressBarVerify);

        //Text views error
        textViewErrorEmail = view.findViewById(R.id.textViewErrorEmail);
        textViewErrorPassword = view.findViewById(R.id.textViewErrorPassword);

        //Focus change listener
        password_Et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if(s != null && s.length() > 0){
                    textViewErrorPassword.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // Text Change Listener
        emailOrMobile_Et.addTextChangedListener(new EmailMobileTextChangedListener(THPConstants.MOBILE_COUNTRY_CODE, emailOrMobile_Et, password_Et, textViewErrorEmail));

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



        // Terms and Conditions click listener
        ResUtil.doClickSpanForString(getActivity(), "By signing in, you agree to our  ", "Terms and Conditions",
                tc_Txt, R.color.blueColor_1, new TextSpanCallback() {
                    @Override
                    public void onTextSpanClick() {
                        if(!BaseAcitivityTHP.sIsOnline) {
                            Alerts.noConnectionSnackBar(getView(), (AppCompatActivity)getActivity());
                            return;
                        }
                        TCFragment fragment = TCFragment.getInstance(THPConstants.TnC_URL, "crossBackImg");
                        FragmentUtil.replaceFragmentAnim((AppCompatActivity) getActivity(), R.id.parentLayout,
                                fragment, FragmentUtil.FRAGMENT_NO_ANIMATION, false);
                        THPFirebaseAnalytics.setFirbaseAnalyticsEvent(getContext(), "Action", "Terms and Conditions clicked", SignInFragment.class.getSimpleName());
                    }
                });

        if(sIsDayTheme) {
            passwordVisible_Btn.setImageResource(R.drawable.ic_show_password);
        } else {
            passwordVisible_Btn.setImageResource(R.drawable.ic_show_password_dark);
        }

        // Password show / hide button click listener
        passwordVisible_Btn.setOnClickListener(v -> {
            if (mIsPasswdVisible) {
                password_Et.setTransformationMethod(new PasswordTransformationMethod());
                //Set Selection cursor to last character
                password_Et.setSelection(password_Et.getText().length());
                if(sIsDayTheme) {
                    passwordVisible_Btn.setImageResource(R.drawable.ic_show_password);
                } else {
                    passwordVisible_Btn.setImageResource(R.drawable.ic_show_password_dark);
                }
                mIsPasswdVisible = false;
            } else {
                password_Et.setTransformationMethod(null);
                if(sIsDayTheme) {
                    passwordVisible_Btn.setImageResource(R.drawable.ic_hide_password);
                } else {
                    passwordVisible_Btn.setImageResource(R.drawable.ic_hide_password_dark);
                }
                //Set Selection cursor to last character
                password_Et.setSelection(password_Et.getText().length());
                mIsPasswdVisible = true;
            }
        });

        // Sign In button click listener
        signIn_Txt.setOnClickListener(v -> {
            if(!BaseAcitivityTHP.sIsOnline) {
                Alerts.noConnectionSnackBar(getView(), (AppCompatActivity)getActivity());
                return;
            }
            String emailOrMobilee = emailOrMobile_Et.getText().toString();
            String mobile = "";
            String email = "";
            String passwd = password_Et.getText().toString();
            String prefix = THPConstants.MOBILE_COUNTRY_CODE;
            boolean hasPrefix = StringUtils.startsWithIgnoreCase(emailOrMobilee, prefix);
            emailOrMobilee = StringUtils.trimAllWhitespace(emailOrMobilee);
            /*if(hasPrefix) {
                emailOrMobilee = StringUtils.delete(emailOrMobilee, prefix);
            }*/


            if(hasPrefix) {
                emailOrMobilee = StringUtils.delete(emailOrMobilee, prefix);
                // This removed extra space and set values after triming space
                emailOrMobile_Et.setText(THPConstants.MOBILE_COUNTRY_CODE+" "+emailOrMobilee);
                emailOrMobile_Et.setSelection(emailOrMobilee.length()+THPConstants.MOBILE_COUNTRY_CODE.length()+1);
            } else {
                // This removed extra space and set values after triming space
                emailOrMobile_Et.setText(emailOrMobilee);
                emailOrMobile_Et.setSelection(emailOrMobilee.length());
            }

            final String emailOrMobile = emailOrMobilee;


            if (ResUtil.isEmpty(emailOrMobile)) {
                textViewErrorEmail.setVisibility(View.VISIBLE);
                textViewErrorEmail.setText(R.string.please_enter_valid);
                return;
            }
            else if(hasPrefix && !ResUtil.isValidMobile(emailOrMobile)) {
                textViewErrorEmail.setVisibility(View.VISIBLE);
                textViewErrorEmail.setText("Please enter valid Mobile Number");
                return;
            } else if(!hasPrefix && !ResUtil.isValidEmail(emailOrMobile)) {
                textViewErrorEmail.setVisibility(View.VISIBLE);
                textViewErrorEmail.setText("Please enter valid Email Address");
                return;
            }


            if (ResUtil.isEmpty(passwd)) {
                //Alerts.showAlertDialogNoBtnWithCancelable(getActivity(), "", "\nPlease enter your Password.\n");
                textViewErrorPassword.setVisibility(View.VISIBLE);
                textViewErrorPassword.setText(R.string.please_enter_password);
                return;
            }

            if (ResUtil.isValidMobile(emailOrMobile)) {
                isUserEnteredMobile = true;
                isUserEnteredEmail = false;
                mobile = emailOrMobile;
            }

            if (ResUtil.isValidEmail(emailOrMobile)) {
                isUserEnteredEmail = true;
                isUserEnteredMobile = false;
                email = emailOrMobile;
            }

            if (!isUserEnteredMobile && !isUserEnteredEmail) {
                //Alerts.showAlertDialogNoBtnWithCancelable(getActivity(), "", "\nPlease enter your Mobile Number/Email Address\n");
                textViewErrorEmail.setVisibility(View.VISIBLE);
                textViewErrorEmail.setText(R.string.please_enter_valid);
                return;
            }

            enableButton(false, false);

            // Hide SoftKeyboard
            CommonUtil.hideKeyboard(getView());
            THPFirebaseAnalytics.setFirbaseAnalyticsEvent(getContext(), "Action", "Sign In Button clicked", SignInFragment.class.getSimpleName());

            String deviceId = ResUtil.getDeviceId(getActivity());

            String finalMobile = mobile;
            String finalEmail = email;
            mDisposable.add(ApiManager.userLogin(getActivity(), email, mobile, BuildConfig.SITEID, passwd, deviceId, BuildConfig.ORIGIN_URL, true)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(keyValueModel -> {
                                if (getActivity() == null && getView() == null) {
                                    return;
                                }

                                String userId = "";

                                if (keyValueModel.getState() != null && keyValueModel.getState().equalsIgnoreCase("success")) {
                                    userId = keyValueModel.getCode();
                                }

                                if (TextUtils.isEmpty(userId)) {
                                        SignUpFragment.getInstance().emailOrMobile_Et.setText(emailOrMobile_Et.getText().toString().trim());
                                        SignUpFragment.getInstance().emailOrMobile_Et.setSelection(SignUpFragment.getInstance().emailOrMobile_Et.getText().length());
                                        if(keyValueModel.getName() != null && keyValueModel.getName().equalsIgnoreCase("Invalid  password")) {
                                            textViewErrorPassword.setText(keyValueModel.getName());
                                            textViewErrorPassword.setVisibility(View.VISIBLE);
                                        } else if(keyValueModel.getName() != null && keyValueModel.getName().equalsIgnoreCase("User email already exist with social signin")) {
                                            textViewErrorEmail.setText(keyValueModel.getName());
                                            textViewErrorEmail.setVisibility(View.VISIBLE);
                                        } else {
                                            Alerts.showAlertDialogOKListener(getActivity(), "Sorry!", keyValueModel.getName(),
                                                    (dialogInterface, i) -> {
                                                        for (Fragment fragment : getActivity().getSupportFragmentManager().getFragments()) {
                                                            if (fragment instanceof SignInAndUpFragment) {
                                                                ((SignInAndUpFragment) fragment).switchToSignInOrSignUp(0); //Switch to Sign Up requirement by THG
                                                                break;
                                                            }
                                                        }
                                                    });
                                        }
                                    enableButton(true, false);
                                } else {
                                    // Making server request to get User Info
//                                    mDisposable.add(ApiManager.getUserInfo(getActivity(), BuildConfig.SITEID, ResUtil.getDeviceId(getActivity()), userId, emailOrMobile, passwd)
                                    mDisposable.add(ApiManager.getUserInfoObject(getActivity(), BuildConfig.SITEID, ResUtil.getDeviceId(getActivity()), userId, emailOrMobile, passwd)
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(userProfile -> {

                                                if (getView() == null || getActivity() == null) {
                                                    return;
                                                }

                                                if (userProfile != null && userProfile.getUserId() != null
                                                        && !ResUtil.isEmpty(userProfile.getUserId())) {
                                                    // TODO, process for user sign - In
                                                    //CleverTap
                                                    HashMap<String,Object> map = new HashMap<>();
                                                    map.put(THPConstants.CT_KEY_Login_Source, THPConstants.DIRECT_LOGIN);
                                                    if(!TextUtils.isEmpty(finalMobile)){
                                                        map.put(THPConstants.CT_KEY_Mobile_Number, finalMobile);
                                                        CleverTapUtil.cleverTapEvent(getActivity(),THPConstants.CT_EVENT_SIGN_IN,map);
                                                    }else if(!TextUtils.isEmpty(finalEmail)){
                                                        map.put(THPConstants.CT_Custom_KEY_Email, finalEmail);
                                                        CleverTapUtil.cleverTapEvent(getActivity(),THPConstants.CT_EVENT_SIGN_IN,map);
                                                    }

                                                    openContentListingActivity(true, userProfile, null, false);
                                                } else {
                                                    Alerts.showErrorDailog(getChildFragmentManager(), "Sorry",
                                                            "We are fetching some technical problem.\n Please try later.");
                                                }

                                            }, throwable -> {
                                                enableButton(true, false);
                                                if(getView() != null && getActivity() != null) {
                                                    Alerts.noConnectionSnackBar(getView(), (AppCompatActivity) getActivity());
                                                }
                                            }, () -> {
                                                enableButton(true, false);
                                            }));


                                }
                            }, throwable -> {
                                if (getActivity() != null && getView() != null) {
                                    enableButton(true, false);
                                    if(getView() != null && getActivity() != null) {
                                        Alerts.showSnackbar(getActivity(), getResources().getString(R.string.something_went_wrong));
                                    }
                                }
                            },
                            () -> {

                            }));

        });


        // Forgot Password button click listener
        view.findViewById(R.id.forgotPassword_Txt).setOnClickListener(v -> {

            if(!BaseAcitivityTHP.sIsOnline) {
                noConnectionSnackBar(getView());
                return;
            }

            String emailOrMobile = emailOrMobile_Et.getText().toString();
            String prefix = "+91";
            boolean hasPrefix = StringUtils.startsWithIgnoreCase(emailOrMobile, prefix);
            emailOrMobile = StringUtils.trimAllWhitespace(emailOrMobile);
            if(hasPrefix) {
                emailOrMobile = StringUtils.delete(emailOrMobile, prefix);
            }

            isUserEnteredMobile = false;
            isUserEnteredEmail = false;


            if (ResUtil.isEmpty(emailOrMobile)) {
                isUserEnteredMobile = false;
                isUserEnteredEmail = false;
            }

            if (ResUtil.isValidMobile(emailOrMobile)) {
                isUserEnteredMobile = true;
                isUserEnteredEmail = false;
                emailOrMobile = prefix+" "+emailOrMobile;
            }

            if (ResUtil.isValidEmail(emailOrMobile)) {
                isUserEnteredEmail = true;
                isUserEnteredMobile = false;
            }


            if (!isUserEnteredMobile && !isUserEnteredEmail) {
                emailOrMobile = "";
                // This code is commented to disable forget password, pre filled email/mobile condition
                /*textViewErrorEmail.setVisibility(View.VISIBLE);
                textViewErrorEmail.setText(R.string.please_enter_valid);
                return;*/
            }
            IS_FORGOT_PASSWORD_REDIRECT_PAYMENT = mFrom != null && mFrom.contains(THPConstants.PAYMENT);
            ForgotPasswordFragment fragment = ForgotPasswordFragment.getInstance(emailOrMobile);
            FragmentUtil.replaceFragmentAnim((AppCompatActivity) getActivity(), R.id.parentLayout,
                    fragment, FragmentUtil.FRAGMENT_ANIMATION, false);
            THPFirebaseAnalytics.setFirbaseAnalyticsEvent(getContext(), "Action", "Forgot Password clicked", SignInFragment.class.getSimpleName());
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
            THPFirebaseAnalytics.setFirbaseAnalyticsEvent(getContext(), "Action", "Google Icon clicked", SignInFragment.class.getSimpleName());

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
            THPFirebaseAnalytics.setFirbaseAnalyticsEvent(getContext(), "Action", "Facebook Icon clicked", SignInFragment.class.getSimpleName());
        });


        // Twitter Sign in click listener
        tweeterBtn.setOnClickListener(v->{
            if(!BaseAcitivityTHP.sIsOnline) {
                noConnectionSnackBar(getView());
                return;
            }
            mProgressDialog = Alerts.showProgressDialog(getActivity());
            //mProgressDialog.setCancelable(true);
            mSocialLoginUtil = new SocialLoginUtil(getActivity(), this,"twitter", mProgressDialog, this);
            THPFirebaseAnalytics.setFirbaseAnalyticsEvent(getContext(), "Action", "Twitter icon clicked", SignInFragment.class.getSimpleName());
        });
    }


    @Override
    public void onStart() {
        super.onStart();
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == TwitterAuthConfig.DEFAULT_AUTH_REQUEST_CODE) {
            if (mSocialLoginUtil != null && mSocialLoginUtil.getTwitterAuthClient() != null)
                mSocialLoginUtil.getTwitterAuthClient().onActivityResult(requestCode, resultCode, data);
        } else {
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
                    if(mProgressDialog != null) {
                        enableButton(true, false);
                    }
                    if(e.getStatusCode()!=SIGN_IN_CANCELLED)
                        Alerts.showToast(getActivity(), e.getMessage());
                }
            }else {

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
                }// Redirect in Subcription plan Page
                else if(mActivity.mFrom != null &&
                        (mActivity.mFrom.equalsIgnoreCase(THPConstants.FROM_SignUpAndPayment) || mActivity.mFrom.equalsIgnoreCase(THPConstants.FROM_START_30_DAYS_TRAIL))) {
                    //Currently we hope nothing to do, Fallback.
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
        CleverTapUtil.cleverTapEvent(getActivity(),THPConstants.CT_EVENT_SIGN_IN,map);

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
        THPFirebaseAnalytics.setFirbaseAnalyticsScreenRecord(getActivity(), "SignIn Screen", SignUpFragment.class.getSimpleName());
    }
}
