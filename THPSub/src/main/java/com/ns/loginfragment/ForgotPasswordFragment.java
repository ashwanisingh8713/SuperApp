package com.ns.loginfragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.netoperation.model.KeyValueModel;
import com.netoperation.net.ApiManager;
import com.netoperation.net.RequestCallback;
import com.netoperation.util.NetConstants;
import com.ns.activity.BaseAcitivityTHP;
import com.ns.alerts.Alerts;
import com.ns.clevertap.CleverTapUtil;
import com.ns.thpremium.BuildConfig;
import com.ns.thpremium.R;
import com.ns.utils.CommonUtil;
import com.ns.utils.FragmentUtil;
import com.ns.utils.ResUtil;
import com.ns.utils.StringUtils;
import com.ns.utils.THPConstants;
import com.ns.utils.THPFirebaseAnalytics;
import com.ns.view.CustomProgressBarWhite;
import com.ns.view.text.CustomTextView;
import com.ns.view.EmailMobileTextChangedListener;

public class ForgotPasswordFragment extends BaseFragmentTHP {

    public static ForgotPasswordFragment getInstance(String val) {
        ForgotPasswordFragment fragment = new ForgotPasswordFragment();
        Bundle bundle = new Bundle();
        bundle.putString("uid", val);
        fragment.setArguments(bundle);
        return fragment;
    }

    private EditText emailOrMobile_Et;
    private CustomTextView submit_Txt, textViewErrorEmail;

    private String email = "";
    private String contact = "";
    private String emailOrContact;

    private boolean isUserEnteredEmail;
    private boolean isUserEnteredMobile;

    private CustomProgressBarWhite progressBar;
    private String lastEnteredEmailOrMobile = "";

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_forgot_password;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null) {

        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        emailOrMobile_Et = view.findViewById(R.id.emailOrMobile_Et);
        progressBar = view.findViewById(R.id.progressBarVerify);
        submit_Txt = view.findViewById(R.id.submit_Txt);
        //Text views error
        textViewErrorEmail = view.findViewById(R.id.textViewErrorEmail);

        if (getArguments().getString("uid") != null) {
            emailOrMobile_Et.setText(getArguments().getString("uid"));
        }
        // Cross button click listener
        view.findViewById(R.id.backBtn).setOnClickListener(v->{
            FragmentUtil.clearSingleBackStack((AppCompatActivity) getActivity());
        });

        // DrawableRight action
        emailOrMobile_Et.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;

            if(event.getAction() == MotionEvent.ACTION_UP) {
                if(emailOrMobile_Et.getCompoundDrawables()[DRAWABLE_RIGHT] != null && event.getRawX() >= (emailOrMobile_Et.getRight() - emailOrMobile_Et.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    //Clear text
                    emailOrMobile_Et.setText("");
                    return false;
                }
            }
            return false;
        });
        // Input Text Change Listener
        emailOrMobile_Et.addTextChangedListener(new EmailMobileTextChangedListener(THPConstants.MOBILE_COUNTRY_CODE, emailOrMobile_Et, textViewErrorEmail));


        emailOrMobile_Et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    forgotPassword();
                    return true;
                }
                return false;
            }
        });


        // Submit button click listener
        submit_Txt.setOnClickListener(v->{
            forgotPassword();
        });


    }

    private void forgotPassword() {
        if(!BaseAcitivityTHP.sIsOnline) {
            noConnectionSnackBar(getView());
            return;
        }
        CommonUtil.hideKeyboard(emailOrMobile_Et);
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

        isUserEnteredMobile = false;
        isUserEnteredEmail = false;

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

        submit_Txt.setEnabled(false);

        progressBar.setVisibility(View.VISIBLE);
        submit_Txt.setText("");
        THPFirebaseAnalytics.setFirbaseAnalyticsEvent(getContext(), "Action", "Submit Button clicked", ForgotPasswordFragment.class.getSimpleName());

        CleverTapUtil.cleverTapEvent(getActivity(),THPConstants.CT_EVENT_FORGOT_PASSWORD,null);

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
                    Alerts.showAlertDialogOKBtn(getActivity(), "Sorry!", keyValueModel.getName());
                }
                else {
                    // Opening OTP Verification Screen
                    OTPVerificationFragment fragment = OTPVerificationFragment.getInstance(THPConstants.FROM_FORGOT_PASSWORD,
                            isUserEnteredEmail, email, mobile);
                    FragmentUtil.replaceFragmentAnim((AppCompatActivity)getActivity(), R.id.parentLayout, fragment,
                            FragmentUtil.FRAGMENT_ANIMATION, false);

                }

            }

            @Override
            public void onError(Throwable t, String str) {
                if(getActivity() != null && getView() != null) {
                    progressBar.setVisibility(View.GONE);
                    submit_Txt.setText("Submit");
                    submit_Txt.setEnabled(true);
                    Alerts.noConnectionSnackBar(getView(), (AppCompatActivity)getActivity());
//                    Alerts.showErrorDailog(getChildFragmentManager(), null, t.getLocalizedMessage());
                }
            }

            @Override
            public void onComplete(String str) {
                progressBar.setVisibility(View.GONE);
                submit_Txt.setText("Submit");
                submit_Txt.setEnabled(true);
            }
        }, emailStr, mobileStr, BuildConfig.SITEID, NetConstants.EVENT_FORGOT_PASSWORD);

    }


    @Override
    public void onResume() {
        super.onResume();
        THPFirebaseAnalytics.setFirbaseAnalyticsScreenRecord(getActivity(), "Forgot Password Screen", ForgotPasswordFragment.class.getSimpleName());
    }



}
