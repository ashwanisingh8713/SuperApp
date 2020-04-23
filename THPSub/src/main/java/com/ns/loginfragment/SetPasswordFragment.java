package com.ns.loginfragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.netoperation.model.KeyValueModel;
import com.netoperation.model.UserProfile;
import com.netoperation.net.ApiManager;
import com.netoperation.net.RequestCallback;
import com.netoperation.util.PremiumPref;
import com.ns.alerts.Alerts;
import com.ns.clevertap.CleverTapUtil;
import com.ns.thpremium.BuildConfig;
import com.ns.thpremium.R;
import com.ns.utils.CommonUtil;
import com.ns.utils.FragmentUtil;
import com.ns.utils.IntentUtil;
import com.ns.utils.ResUtil;
import com.ns.utils.THPConstants;
import com.ns.utils.THPFirebaseAnalytics;
import com.ns.view.CustomProgressBarWhite;
import com.ns.view.text.CustomTextView;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;

import static android.app.Activity.RESULT_OK;

public class SetPasswordFragment extends BaseFragmentTHP {

    private String mFrom;
    private CustomTextView errorTextPassword;

    public static SetPasswordFragment getInstance(String from) {
        SetPasswordFragment fragment = new SetPasswordFragment();
        Bundle bundle = new Bundle();
        bundle.putString("from", from);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static SetPasswordFragment getInstance(String from, boolean isUserEnteredEmail, String email, String contact, String otp) {
        SetPasswordFragment fragment = new SetPasswordFragment();
        Bundle bundle = new Bundle();
        bundle.putString("from", from);
        bundle.putBoolean("isUserEnteredEmail", isUserEnteredEmail);
        bundle.putString("email", email);
        bundle.putString("contact", contact);
        bundle.putString("otp", otp);
        fragment.setArguments(bundle);
        return fragment;
    }

    private EditText password_Et;
    private ImageButton passwordVisible_Btn;
    private boolean mIsPasswdVisible;
    private TextView setPasswdTitle_Txt;
    private TextView submit_Txt;

    private CustomProgressBarWhite progressBar;

    private String email;
    private String contact;
    private String otp;
    private boolean isUserEnteredEmail;

    private String mCountryCode = "+91";

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_set_password;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mFrom = getArguments().getString("from");
            isUserEnteredEmail = getArguments().getBoolean("isUserEnteredEmail");
            email = getArguments().getString("email");
            contact = getArguments().getString("contact");
            otp = getArguments().getString("otp");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayout shadowLayout = view.findViewById(R.id.shadowLayout);

        if(mIsDayTheme) {
            shadowLayout.setBackground(ResUtil.getBackgroundDrawable(getResources(), R.drawable.shadow_white_r12_s6_wh200_ltr));
        } else {
            shadowLayout.setBackground(ResUtil.getBackgroundDrawable(getResources(), R.drawable.shadow_dark_r12_s6_wh200_ltr));
        }

        progressBar = view.findViewById(R.id.progressBarVerify);
        password_Et = view.findViewById(R.id.password_Et);
        passwordVisible_Btn = view.findViewById(R.id.passwordVisible_Btn);
        setPasswdTitle_Txt = view.findViewById(R.id.setPasswdTitle_Txt);
        submit_Txt = view.findViewById(R.id.submit_Txt);
        //Error Text password
        errorTextPassword = view.findViewById(R.id.textViewError_Password);

        if (mFrom != null && mFrom.equalsIgnoreCase(THPConstants.FROM_FORGOT_PASSWORD)) {
            setPasswdTitle_Txt.setText("Reset Password");
            password_Et.setHint("New Password");
        } else {
            setPasswdTitle_Txt.setText("Set Password");
            password_Et.setHint("Password");
        }

        // Cross button click listener
        view.findViewById(R.id.backBtn).setVisibility(View.GONE);
        view.findViewById(R.id.backBtn).setOnClickListener(v -> {
            FragmentUtil.clearSingleBackStack((AppCompatActivity) getActivity());
        });

        // Disable Back Press
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                return keyCode == KeyEvent.KEYCODE_BACK;
            }
        });

        if(mIsDayTheme) {
            passwordVisible_Btn.setImageResource(R.drawable.ic_show_password);
        } else {
            passwordVisible_Btn.setImageResource(R.drawable.ic_show_password_dark);
        }

        // Password show / hide button click listener
        passwordVisible_Btn.setOnClickListener(v -> {
            if (mIsPasswdVisible) {
                password_Et.setTransformationMethod(new PasswordTransformationMethod());
                if(mIsDayTheme) {
                    passwordVisible_Btn.setImageResource(R.drawable.ic_show_password);
                } else {
                    passwordVisible_Btn.setImageResource(R.drawable.ic_show_password_dark);
                }
                mIsPasswdVisible = false;
                //Set Selection cursor to last character
                password_Et.setSelection(password_Et.getText().length());
            } else {
                password_Et.setTransformationMethod(null);
                if(mIsDayTheme) {
                    passwordVisible_Btn.setImageResource(R.drawable.ic_hide_password);
                } else {
                    passwordVisible_Btn.setImageResource(R.drawable.ic_hide_password_dark);
                }
                mIsPasswdVisible = true;
                //Set Selection cursor to last character
                password_Et.setSelection(password_Et.getText().length());
            }
        });

        // Submit button click listener
        submit_Txt.setOnClickListener(v -> {
            String password = password_Et.getText().toString();
            if (TextUtils.isEmpty(password)) {
                errorTextPassword.setVisibility(View.VISIBLE);
                if (mFrom != null && mFrom.equalsIgnoreCase(THPConstants.FROM_FORGOT_PASSWORD)) {
                    errorTextPassword.setText(R.string.please_enter__new_password);
                } else {
                    errorTextPassword.setText(R.string.please_enter_password);
                }
                return;
            } else if (password.length() < 5) {
                errorTextPassword.setVisibility(View.VISIBLE);
                errorTextPassword.setText(R.string.passwd_length_err_msg);
                return;
            } else if (!ResUtil.isValidPassword(password)) {
                errorTextPassword.setVisibility(View.VISIBLE);
                errorTextPassword.setText(R.string.password_constraints);
                return;
            }
            if (mFrom != null && mFrom.equalsIgnoreCase(THPConstants.FROM_FORGOT_PASSWORD)) {
                resetPassword();
            } else {
                sigupApiRequest();
            }
        });

        getView().findViewById(R.id.cancel_Txt).setOnClickListener(v -> {
            if (mFrom != null && mFrom.equalsIgnoreCase(THPConstants.FROM_FORGOT_PASSWORD)) {
                FragmentUtil.clearAllBackStack((AppCompatActivity) getActivity());
                THPFirebaseAnalytics.setFirbaseAnalyticsEvent(getContext(), "Action", "Cancel Button clicked", "Reset Password Screen");
            } else {
                //getActivity().finish();
                //FragmentUtil.clearSingleBackStack((AppCompatActivity) getActivity());
                FragmentUtil.clearAllBackStack((AppCompatActivity) getActivity());
                THPFirebaseAnalytics.setFirbaseAnalyticsEvent(getContext(), "Action", "Cancel Button clicked", "Set Password Screen");
            }
        });

        password_Et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if (s != null && s.length() > 0) {
                    errorTextPassword.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    private void resetPassword() {
        String password = password_Et.getText().toString();
        /* Moved before this method calls
        if (TextUtils.isEmpty(password)) {
            Alerts.showErrorDailog(getChildFragmentManager(), "Alert", "Please enter password.");
            return;
        } else if (password.length() < 5) {
            Alerts.showErrorDailog(getChildFragmentManager(), "Alert", getString(R.string.passwd_length_err_msg));
            return;
        } else if (!ResUtil.isValidPassword(password)) {
            Alerts.showErrorDailog(getChildFragmentManager(), "Alert", getString(R.string.passwd_char_err_msg));
            return;
        }*/

        submit_Txt.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
        submit_Txt.setText("");
        CommonUtil.hideKeyboard(getView());
        THPFirebaseAnalytics.setFirbaseAnalyticsEvent(getContext(), "Action", "Submit Button clicked", "Reset Password Screen");

        ApiManager.resetPassword(new RequestCallback<KeyValueModel>() {
            @Override
            public void onNext(KeyValueModel keyValueModel) {
                if (getActivity() == null && getView() == null) {
                    return;
                }

                if (keyValueModel.getState() != null && !keyValueModel.getState().equalsIgnoreCase("success")) {
                    Alerts.showAlertDialogOKBtn(getActivity(), "Sorry!", keyValueModel.getName());
                    progressBar.setVisibility(View.GONE);
                    submit_Txt.setText("Submit");
                    submit_Txt.setEnabled(true);
                } else {
                    //Alerts.showToast(getActivity(), getString(R.string.password_reset_success));
                    //FragmentUtil.clearAllBackStack((AppCompatActivity) getActivity());
                    /*if (SignInFragment.getInstance().emailOrMobile_Et != null) {
                        SignInFragment.getInstance().emailOrMobile_Et.setText(TextUtils.isEmpty(email) ? contact : email);
                        SignInFragment.getInstance().emailOrMobile_Et.clearFocus();
                    }*/
                    //Signed In API call - Requirement 27/09/19
                    signInUser(password);
                }

            }

            @Override
            public void onError(Throwable t, String str) {
                if (getActivity() != null && getView() != null) {
                    progressBar.setVisibility(View.GONE);
                    submit_Txt.setText("Submit");
                    submit_Txt.setEnabled(true);
                    Alerts.showErrorDailog(getChildFragmentManager(), null, t.getLocalizedMessage());
                }
            }

            @Override
            public void onComplete(String str) {
                /*progressBar.setVisibility(View.GONE);
                submit_Txt.setText("Submit");
                submit_Txt.setEnabled(true);*/
            }
        }, otp, password, mCountryCode, email, BuildConfig.SITEID, BuildConfig.ORIGIN_URL, contact);
    }

    private void sigupApiRequest() {
        String password = password_Et.getText().toString();

        /*if (TextUtils.isEmpty(password)) {
            Alerts.showErrorDailog(getChildFragmentManager(), "Alert", "Please enter password.");
            return;
        } else if (password.length() <= 5) {
            Alerts.showErrorDailog(getChildFragmentManager(), "Alert", getString(R.string.passwd_length_err_msg));
            return;
        } else if (!ResUtil.isValidPassword(password)) {
            Alerts.showErrorDailog(getChildFragmentManager(), "Alert", getString(R.string.passwd_char_err_msg));
            return;
        }*/

        submit_Txt.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
        submit_Txt.setText("");
        CommonUtil.hideKeyboard(getView());
        THPFirebaseAnalytics.setFirbaseAnalyticsEvent(getContext(), "Action", "Submit Button clicked", "Set Password Screen");

        mDisposable.add(ApiManager.userSignUp(getActivity(), otp, mCountryCode, password, email, contact, ResUtil.getDeviceId(getActivity()), BuildConfig.SITEID, BuildConfig.ORIGIN_URL)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(keyValueModel -> {
                    if (getActivity() == null && getView() == null) {
                        return;
                    }

                    progressBar.setVisibility(View.VISIBLE);
                    submit_Txt.setEnabled(false);
                    submit_Txt.setText("");

                    if (keyValueModel.getState() != null && !keyValueModel.getState().equalsIgnoreCase("success")) {
                        progressBar.setVisibility(View.INVISIBLE);
                        submit_Txt.setEnabled(true);
                        submit_Txt.setText("Submit");
                        Alerts.showAlertDialogOKBtn(getActivity(), "Sorry!", keyValueModel.getName());
                    } else {
                        //cleverTap signup
                        //CleverTap
                        HashMap<String,Object> map = new HashMap<>();
                        map.put(THPConstants.CT_KEY_Login_Source, THPConstants.DIRECT_LOGIN);
                        if(!TextUtils.isEmpty(contact)){
                            map.put(THPConstants.CT_KEY_UserId,contact);
                            map.put(THPConstants.CT_KEY_Mobile_Number,contact);
                            CleverTapUtil.cleverTapEvent(getActivity(),THPConstants.CT_EVENT_SIGN_UP,map);
                        }else if(!TextUtils.isEmpty(email)){
                            map.put(THPConstants.CT_KEY_UserId,email);
                            map.put(THPConstants.CT_Custom_KEY_Email,email);
                            CleverTapUtil.cleverTapEvent(getActivity(),THPConstants.CT_EVENT_SIGN_UP,map);
                        }
                        progressBar.setVisibility(View.VISIBLE);
                        submit_Txt.setEnabled(false);
                        // Create free plan
                        freePlan(keyValueModel.getUserId(), keyValueModel.getContact());
                        /*Intent intent = new Intent();
                        intent.putExtra("isKillToBecomeMemberActivity", true);
                        if (mFrom != null && !TextUtils.isEmpty(mFrom) && mFrom.contains(THPConstants.PAYMENT)) {
                            intent.putExtra("isRequestPayment", true);
                        } else {
                            // When user logged in then we need this to refresh the default TH listing page.
                            PremiumPref.getInstance(getActivity()).setIsRefreshRequired(true);
                            //Always open Briefing screen - Requirement 14 Sept,2019
                            IntentUtil.openContentListingActivity(getActivity(), THPConstants.FROM_USER_SignUp);
                        }

                        getActivity().setResult(RESULT_OK, intent);
                        getActivity().finish();*/
                        //Send Broadcast to Kill Become a member screen once user Signed up or Signed In
                        //CommonUtil.sendBroadcastToKillBecomeAMemberScreen(getActivity());
                    }
                }, throwable -> {
                    submit_Txt.setEnabled(true);
                }));

    }

    private void signInUser(String password) {
        submit_Txt.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
        submit_Txt.setText("");
        mDisposable.add(ApiManager.userLogin(getActivity(), email, contact, BuildConfig.SITEID, password, ResUtil.getDeviceId(getActivity()), BuildConfig.ORIGIN_URL, true)
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
                                Alerts.showToast(getActivity(), getString(R.string.password_reset_success));
                                FragmentUtil.clearAllBackStack((AppCompatActivity) getActivity());
                                if (SignInFragment.getInstance().emailOrMobile_Et != null) {
                                    SignInFragment.getInstance().emailOrMobile_Et.setText(TextUtils.isEmpty(email) ? contact : email);
                                    SignInFragment.getInstance().emailOrMobile_Et.clearFocus();
                                }
                            } else {
                                String emailOrMobile = "";
                                if (!TextUtils.isEmpty(email)) {
                                    emailOrMobile = email;
                                } else {
                                    emailOrMobile = contact;
                                }
                                // Making server request to get User Info
                                mDisposable.add(ApiManager.getUserInfoObject(getActivity(), BuildConfig.SITEID, ResUtil.getDeviceId(getActivity()), userId, emailOrMobile, password)
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(userProfile -> {
                                            if (getView() == null || getActivity() == null) {
                                                return;
                                            }
                                            if (userProfile != null && userProfile.getUserId() != null
                                                    && !ResUtil.isEmpty(userProfile.getUserId())) {
                                                Alerts.showToast(getActivity(), getString(R.string.password_reset_success));
                                                //Process for user sign - In
                                                openContentListingActivity(userProfile);
                                            } else {
                                                Alerts.showToast(getActivity(), getString(R.string.password_reset_success));
                                                //Send to SignIn Screen
                                                if (getView() != null && getActivity() != null) {
                                                    FragmentUtil.clearAllBackStack((AppCompatActivity) getActivity());
                                                    if (SignInFragment.getInstance().emailOrMobile_Et != null) {
                                                        SignInFragment.getInstance().emailOrMobile_Et.setText(TextUtils.isEmpty(email) ? contact : email);
                                                        SignInFragment.getInstance().emailOrMobile_Et.clearFocus();
                                                    }
                                                }
                                            }

                                        }, throwable -> {
                                            //Send to SignIn Screen
                                            if (getView() != null && getActivity() != null) {
                                                Alerts.showToast(getActivity(), getString(R.string.password_reset_success));
                                                FragmentUtil.clearAllBackStack((AppCompatActivity) getActivity());
                                                if (SignInFragment.getInstance().emailOrMobile_Et != null) {
                                                    SignInFragment.getInstance().emailOrMobile_Et.setText(TextUtils.isEmpty(email) ? contact : email);
                                                    SignInFragment.getInstance().emailOrMobile_Et.clearFocus();
                                                }
                                            }
                                        }, () -> {
                                            //Do Something if required
                                        }));


                            }
                        }, throwable -> {
                            if (getView() != null && getActivity() != null) {
                                Alerts.showToast(getActivity(), getString(R.string.password_reset_success));
                                FragmentUtil.clearAllBackStack((AppCompatActivity) getActivity());
                                if (SignInFragment.getInstance().emailOrMobile_Et != null) {
                                    SignInFragment.getInstance().emailOrMobile_Et.setText(TextUtils.isEmpty(email) ? contact : email);
                                    SignInFragment.getInstance().emailOrMobile_Et.clearFocus();
                                }
                            }
                        },
                        () -> {
                        }));
    }

    private void openContentListingActivity(UserProfile userProfile) {
        //Always set True if Successful Login
        PremiumPref.getInstance(getContext()).setIsRelogginSuccess(true);
        Intent intent = new Intent();
        intent.putExtra("isKillToBecomeMemberActivity", true);
        //CleverTap
        PremiumPref.getInstance(getContext()).setLoginSource(THPConstants.DIRECT_LOGIN);
        CleverTapUtil.cleverTapUpdateProfile(getActivity(), true, userProfile, userProfile.isHasSubscribedPlan(), userProfile.isHasFreePlan());

        if (mFrom != null && !TextUtils.isEmpty(mFrom) && mFrom.contains(THPConstants.PAYMENT) || SignInFragment.IS_FORGOT_PASSWORD_REDIRECT_PAYMENT) {
            intent.putExtra("isRequestPayment", true);
            PremiumPref.getInstance(getActivity()).setIsRefreshRequired(true);
        } else {
            if (userProfile != null && (userProfile.isHasFreePlan() || userProfile.isHasSubscribedPlan())) {
                // Process for user sign - In
                // When user logged in then we need this to refresh the default TH listing page.
                PremiumPref.getInstance(getActivity()).setIsRefreshRequired(true);
                if (THPConstants.IS_FROM_MP_BLOCKER) {
                    //Disable the MP constant
                    THPConstants.IS_FROM_MP_BLOCKER = false;
                } else if (THPConstants.FLOW_TAB_CLICK != null && THPConstants.FLOW_TAB_CLICK.equals(THPConstants.TAB_4)) {
                    IntentUtil.openUserProfileActivity(getActivity(), THPConstants.FROM_USER_PROFILE);
                } else {
                    IntentUtil.openContentListingActivity(getActivity(), "");
                }
            } else if (userProfile != null && !(userProfile.isHasFreePlan() && userProfile.isHasSubscribedPlan())) {
                // When user logged in then we need this to refresh the default TH listing page.
                PremiumPref.getInstance(getActivity()).setIsRefreshRequired(true);
                if (THPConstants.IS_FROM_MP_BLOCKER) {
                    //Disable the MP constant
                    THPConstants.IS_FROM_MP_BLOCKER = false;
                } else
                // Redirect in Subscription plan Page
                if (mFrom != null &&
                        (mFrom.equalsIgnoreCase(THPConstants.FROM_SignUpAndPayment) || mFrom.equalsIgnoreCase(THPConstants.FROM_START_30_DAYS_TRAIL))) {
                    //Currently we hope nothing to do
                } else {
                    IntentUtil.openSubscriptionActivity(getActivity(), THPConstants.FROM_SUBSCRIPTION_EXPLORE);
                }
            }
        }
        getActivity().setResult(RESULT_OK, intent);
        getActivity().finish();
        //Send Broadcast to Kill Become a member screen once user Signed up or Signed In
        CommonUtil.sendBroadcastToKillBecomeAMemberScreen(getActivity());

    }

    private void freePlan(String userId, String contact) {
        ApiManager.freePlanF(userId, contact, BuildConfig.SITEID)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(value -> {

                    if (value) {
                        //CleverTap
                        mDisposable.add(ApiManager.getUserProfile(getActivity())
                                .observeOn(AndroidSchedulers.mainThread())
                                .map(userProfile -> {
                                    if (userProfile == null) {
                                        return "";
                                    }
                                    PremiumPref.getInstance(getContext()).setLoginSource(THPConstants.DIRECT_LOGIN);
                                    userProfile.setHasFreePlan(true);
                                    CleverTapUtil.cleverTapUpdateProfile(getActivity(), true, userProfile, userProfile.isHasSubscribedPlan(), userProfile.isHasFreePlan());
                                    return "";
                                })
                                .subscribe(v -> {
                                }, t -> {
                                    Log.i("", "" + t);
                                }));
                        //Always set True if Successful Login
                        PremiumPref.getInstance(getContext()).setIsRelogginSuccess(true);
                        // When user logged in then we need this to refresh the default TH listing page.
                        PremiumPref.getInstance(getActivity()).setIsRefreshRequired(true);
                        Intent intent = new Intent();
                        intent.putExtra("isKillToBecomeMemberActivity", true);
                        if (mFrom != null && !TextUtils.isEmpty(mFrom) && mFrom.contains(THPConstants.PAYMENT)) {
                            intent.putExtra("isRequestPayment", true);
                        } else if (THPConstants.IS_FROM_MP_BLOCKER) {
                            //Disable the MP constant
                            THPConstants.IS_FROM_MP_BLOCKER = false;
                        } else {
                            //Always open Briefing screen - Requirement 14 Sept,2019
                            IntentUtil.openContentListingActivity(getActivity(), THPConstants.FROM_USER_SignUp);
                        }
                        //Send CleverTap Event for Free Trial Subscription
                        CleverTapUtil.cleverTapEventFreeTrial(getContext());
                        THPFirebaseAnalytics.setFirbaseFreeTrialEvent(getContext());

                        getActivity().setResult(RESULT_OK, intent);
                        getActivity().finish();
                        //Send Broadcast to Kill Become a member screen once user Signed up or Signed In
                        CommonUtil.sendBroadcastToKillBecomeAMemberScreen(getActivity());
                    } else {
                        progressBar.setVisibility(View.INVISIBLE);
                        submit_Txt.setEnabled(true);
                        submit_Txt.setText("Submit");
                        Alerts.showAlertDialogOKListener(getActivity(), "Sorry!", "Free trial activation failed", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // When user logged in then we need this to refresh the default TH listing page.
                                PremiumPref.getInstance(getActivity()).setIsRefreshRequired(true);
                                Intent intent = new Intent();
                                intent.putExtra("isKillToBecomeMemberActivity", true);
                                if (mFrom != null && !TextUtils.isEmpty(mFrom) && mFrom.contains(THPConstants.PAYMENT)) {
                                    intent.putExtra("isRequestPayment", true);
                                } else if (THPConstants.IS_FROM_MP_BLOCKER) {
                                    //Disable the MP constant
                                    THPConstants.IS_FROM_MP_BLOCKER = false;
                                } else {
                                    IntentUtil.openContentListingActivity(getActivity(), "");
                                }
                                getActivity().setResult(RESULT_OK, intent);
                                getActivity().finish();
                                //Send Broadcast to Kill Become a member screen once user Signed up or Signed In
                                CommonUtil.sendBroadcastToKillBecomeAMemberScreen(getActivity());
                            }
                        });
                    }
                }, throwable -> {
                    progressBar.setVisibility(View.INVISIBLE);
                    submit_Txt.setEnabled(true);
                    submit_Txt.setText("Submit");
                    Alerts.showAlertDialogOKListener(getActivity(), "Sorry!", "Free trial activation failed", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // When user logged in then we need this to refresh the default TH listing page.
                            PremiumPref.getInstance(getActivity()).setIsRefreshRequired(true);
                            Intent intent = new Intent();
                            intent.putExtra("isKillToBecomeMemberActivity", true);
                            if (mFrom != null && !TextUtils.isEmpty(mFrom) && mFrom.contains(THPConstants.PAYMENT)) {
                                intent.putExtra("isRequestPayment", true);
                            } else if (THPConstants.IS_FROM_MP_BLOCKER) {
                                //Disable the MP constant
                                THPConstants.IS_FROM_MP_BLOCKER = false;
                            } else {
                                IntentUtil.openContentListingActivity(getActivity(), "");
                            }
                            getActivity().setResult(RESULT_OK, intent);
                            getActivity().finish();
                            //Send Broadcast to Kill Become a member screen once user Signed up or Signed In
                            CommonUtil.sendBroadcastToKillBecomeAMemberScreen(getActivity());
                        }
                    });
                });

    }


    @Override
    public void onResume() {
        super.onResume();
        if (mFrom != null && mFrom.equalsIgnoreCase(THPConstants.FROM_FORGOT_PASSWORD)) {
            THPFirebaseAnalytics.setFirbaseAnalyticsScreenRecord(getActivity(), "Reset Password Screen", SetPasswordFragment.class.getSimpleName());
        } else {
            THPFirebaseAnalytics.setFirbaseAnalyticsScreenRecord(getActivity(), "Set Password Screen", SetPasswordFragment.class.getSimpleName());
        }
    }
}
