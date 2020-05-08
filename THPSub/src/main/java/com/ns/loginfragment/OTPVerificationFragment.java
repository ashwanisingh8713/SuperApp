package com.ns.loginfragment;

import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.netoperation.model.KeyValueModel;
import com.netoperation.net.ApiManager;
import com.netoperation.net.RequestCallback;
import com.netoperation.util.NetConstants;
import com.ns.activity.BaseAcitivityTHP;
import com.ns.alerts.Alerts;
import com.ns.clevertap.CleverTapUtil;
import com.ns.helper.OtpReceivedInterface;
import com.ns.receiver.SmsBroadcastReceiver;
import com.ns.thpremium.BuildConfig;
import com.ns.thpremium.R;
import com.ns.utils.CommonUtil;
import com.ns.utils.FragmentUtil;
import com.ns.utils.ResUtil;
import com.ns.utils.StringUtils;
import com.ns.utils.THPConstants;
import com.ns.utils.THPFirebaseAnalytics;
import com.ns.view.text.CustomTextView;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class OTPVerificationFragment extends BaseFragmentTHP implements GoogleApiClient.ConnectionCallbacks,
        OtpReceivedInterface, GoogleApiClient.OnConnectionFailedListener {

    public OnOtpVerification mOnOtpVerification;
    private static int count = 0;

    GoogleApiClient mGoogleApiClient;
    SmsBroadcastReceiver mSmsBroadcastReceiver;
    protected static boolean isVisible = false;


    public void setOtpVerification(OnOtpVerification onOtpVerification) {
        mOnOtpVerification = onOtpVerification;
    }

    public interface OnOtpVerification {
        void onOtpVerification(boolean isOtpVerified, String otp);
    }

    public static OTPVerificationFragment getInstance(String from, boolean isUserEnteredEmail, String email, String contact) {
        OTPVerificationFragment fragment = new OTPVerificationFragment();
        Bundle bundle = new Bundle();
        bundle.putString("from", from);
        bundle.putBoolean("isUserEnteredEmail", isUserEnteredEmail);
        bundle.putString("email", email);
        bundle.putString("contact", contact);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static OTPVerificationFragment getInstance(String from) {
        OTPVerificationFragment fragment = new OTPVerificationFragment();
        Bundle bundle = new Bundle();
        bundle.putString("from", from);
        fragment.setArguments(bundle);
        return fragment;
    }

    private PinEntryEditText pinEntry;
    private TextView resend_Txt,verify_Txt;
    private ProgressBar progressBar,progressBarVerify;
    private CustomTextView otpSendTitle_TV;

    private String mFrom;
    private boolean isUserEnteredEmail;
    private String email = "";
    private String contact = "";
    private String emailOrContact;

    private String mEventType;


    @Override
    public int getLayoutRes() {
        if(mFrom != null && (mFrom.equalsIgnoreCase(THPConstants.FROM_AccountInfoFragment)
        || mFrom.equalsIgnoreCase(THPConstants.FROM_PersonalInfoFragment))) {
            return R.layout.fragment_otp_verification_userprofile;
        } else {
            return R.layout.fragment_otp_verification;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null) {
            mFrom = getArguments().getString("from");
            email = getArguments().getString("email");
            contact = getArguments().getString("contact");
            isUserEnteredEmail = getArguments().getBoolean("isUserEnteredEmail");
        }


        // init broadcast receiver
        mSmsBroadcastReceiver = new SmsBroadcastReceiver();

        mSmsBroadcastReceiver.setOnOtpListeners(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION);
        getActivity().registerReceiver(mSmsBroadcastReceiver, intentFilter);

        startSMSListener();
    }


    public void startSMSListener() {
        SmsRetrieverClient mClient = SmsRetriever.getClient(getActivity());
        Task<Void> mTask = mClient.startSmsRetriever();
        mTask.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override public void onSuccess(Void aVoid) {
               // Toast.makeText(getActivity(), "SMS Retriever starts", Toast.LENGTH_LONG).show();
            }
        });
        mTask.addOnFailureListener(new OnFailureListener() {
            @Override public void onFailure(@NonNull Exception e) {
              //  Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override public void onConnected(@Nullable Bundle bundle) {

    }

    @Override public void onConnectionSuspended(int i) {

    }

    @Override public void onOtpReceived(String otp) {
       // Toast.makeText(getActivity(), "Otp Received " + otp, Toast.LENGTH_LONG).show();
        if(pinEntry!=null && otp.matches("^[0-9]+$"))
        pinEntry.setText(otp);
       // Log.e("OTP",""+otp);
    }

    @Override public void onOtpTimeout() {
      //  Toast.makeText(getActivity(), "Time out, please resend", Toast.LENGTH_LONG).show();
    }

    @Override public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onResume()
    {
        super.onResume();
        isVisible = true;
        THPFirebaseAnalytics.setFirbaseAnalyticsScreenRecord(getActivity(), "OTP Varification Screen", OTPVerificationFragment.class.getSimpleName());
    }



    @Override
    public void onPause()
    {
        super.onPause();
        isVisible = false;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        getActivity().unregisterReceiver(mSmsBroadcastReceiver);
    }

    public void countDownTimer(){
        count = count+1; //count Resent OTP click
        //time count code
        new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                resend_Txt.setText("Resend OTP in "+ millisUntilFinished / 1000+" Seconds");
                resend_Txt.setTextColor(Color.parseColor("#818181"));
                enableDisableResentBtn(false);

                //here you can have your logic to set text to edittext
            }
            public void onFinish() {
                resend_Txt.setText("Resend OTP");
                resend_Txt.setTextColor(Color.parseColor("#1b528e"));
                enableDisableResentBtn(true);
            }
        }.start();
    }

    public void enableDisableResentBtn(Boolean boln){
        resend_Txt.setClickable(boln);
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

        pinEntry = view.findViewById(R.id.pinEntry_ET);
        resend_Txt = view.findViewById(R.id.resend_Txt);
        verify_Txt = view.findViewById(R.id.verify_Txt);
        progressBar = view.findViewById(R.id.progressBar);
        progressBarVerify = view.findViewById(R.id.progressBarVerify);
        otpSendTitle_TV = view.findViewById(R.id.otpSendTitle_TV);


        countDownTimer();



        if(isUserEnteredEmail) {
            emailOrContact = email;
            otpSendTitle_TV.setText("Enter OTP sent to your email address");
        } else {
            emailOrContact = contact;
            otpSendTitle_TV.setText("Enter OTP sent to your mobile number");
        }

        emailOrContact = StringUtils.trimAllWhitespace(emailOrContact);

        view.findViewById(R.id.otpParentLayout).setOnTouchListener((v, e)->{
            return true;
        });

        // OTP Entered Verification Listener
        pinEntry.setOnPinEnteredListener(pinEntryValue->{
            if(isVisible) {
                CommonUtil.hideKeyboard(getView());
                validateOTP(pinEntry.getText().toString(), emailOrContact);
            }
        });



        // Cross button click listener
        view.findViewById(R.id.backBtn).setOnClickListener(v->
            FragmentUtil.clearSingleBackStack((AppCompatActivity) getActivity())
        );

        // Verify button click listener
        view.findViewById(R.id.verify_Txt).setOnClickListener(v-> {
                    if (BaseAcitivityTHP.sIsOnline) {
                        validateOTP(pinEntry.getText().toString(), emailOrContact);
                    } else {
                        noConnectionSnackBar(getView());
                    }
                }
        );

        // Resend button click listener
        view.findViewById(R.id.resend_Txt).setOnClickListener(v-> {
                 if(BaseAcitivityTHP.sIsOnline) {
                     reSendSignupOtpReq();
                     startSMSListener();
                 } else {
                     noConnectionSnackBar(getView());
                 }
                }
        );

        if(mFrom != null && (mFrom.equalsIgnoreCase(THPConstants.FROM_DELETE_ACCOUNT)
                || mFrom.equalsIgnoreCase(THPConstants.FROM_SUSPEND_ACCOUNT))) {

            mEventType = NetConstants.EVENT_CHANGE_ACCOUNT_STATUS;

            ApiManager.getUserProfile(getActivity())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(userProfile -> {
                        email = userProfile.getEmailId();

                        if(email != null && !TextUtils.isEmpty(email)) {
                            emailOrContact = email;
                            otpSendTitle_TV.setText("Enter OTP sent to your email address");
                            isUserEnteredEmail = true;
                        }
                        else if(userProfile.getContact() != null && !TextUtils.isEmpty(userProfile.getContact())) {
                            emailOrContact = contact;
                            otpSendTitle_TV.setText("Enter OTP sent to your mobile number");
                            contact = userProfile.getContact();
                        }

                        generateOTP();
                        return userProfile;
                    })
                    .subscribe();
        }
        else if(mFrom != null && (mFrom.equalsIgnoreCase(THPConstants.FROM_FORGOT_PASSWORD))) {
            mEventType = NetConstants.EVENT_FORGOT_PASSWORD;
        }
        else if(mFrom != null && (mFrom.equalsIgnoreCase(THPConstants.FROM_USER_SignUp))) {
            mEventType = NetConstants.EVENT_SIGNUP;
        }

    }

    private void validateOTP(String otp, String emailOrContact) {

        ApiManager.validateOTP(new RequestCallback<Boolean>() {
            @Override
            public void onNext(Boolean bool) {
                if(getActivity() == null && getView() == null) {
                    return;
                }

                if(mOnOtpVerification != null) {
                    mOnOtpVerification.onOtpVerification(bool, otp);
                }

                progressBar.setVisibility(View.INVISIBLE);
                if(!bool) {
                    pinEntry.setError(true);
                    pinEntry.postDelayed(()-> pinEntry.setText(null), 1000);
                    if(isUserEnteredEmail) {
                        Alerts.showAlertDialogOKBtn(getActivity(), "", "Please provide a correct 4 digit OTP received on your Email Address");
                    } else {
                        Alerts.showAlertDialogOKBtn(getActivity(), "", "Please provide a correct 4 digit OTP received on your Mobile Number");
                    }

                    //CleverTap
                    HashMap<String,Object> map = new HashMap<>();
                    map.put(THPConstants.CT_KEY_SOURCE,"Fail");
                    CleverTapUtil.cleverTapEvent(getActivity(),THPConstants.CT_EVENT_OTP_VARIFICATION,map);
                }
                else {
                    Alerts.showToastAtCenter(getContext(), getContext().getString(R.string.toast_otp_verified));
                    THPFirebaseAnalytics.setFirbaseAnalyticsEvent(getContext(), "Action", "Verify Button clicked", OTPVerificationFragment.class.getSimpleName());
                    if(mFrom != null && mFrom.equalsIgnoreCase(THPConstants.FROM_USER_SignUp)
                            || mFrom.equalsIgnoreCase(THPConstants.FROM_SignUpAndPayment)
                            || mFrom.equalsIgnoreCase(THPConstants.FROM_START_30_DAYS_TRAIL) || THPConstants.IS_FROM_MP_BLOCKER) {
                        FragmentUtil.clearSingleBackStack((AppCompatActivity)getActivity());
                        SetPasswordFragment fragment = SetPasswordFragment.getInstance(mFrom, isUserEnteredEmail, email, contact, otp);
                        FragmentUtil.replaceFragmentAnim((AppCompatActivity) getActivity(),
                                R.id.parentLayout, fragment,
                                FragmentUtil.FRAGMENT_NO_ANIMATION, false);

                    }
                    else if(mFrom != null && (mFrom.equalsIgnoreCase(THPConstants.FROM_DELETE_ACCOUNT)
                            || mFrom.equalsIgnoreCase(THPConstants.FROM_SUSPEND_ACCOUNT))) {
                        FragmentUtil.clearSingleBackStack((AppCompatActivity)getActivity());
                    }
                    else if(mFrom != null && (mFrom.equalsIgnoreCase(THPConstants.FROM_FORGOT_PASSWORD))) {
                        FragmentUtil.clearSingleBackStack((AppCompatActivity)getActivity());
                        SetPasswordFragment fragment = SetPasswordFragment.getInstance(mFrom, isUserEnteredEmail, email, contact, otp);
                        FragmentUtil.replaceFragmentAnim((AppCompatActivity) getActivity(),
                                R.id.parentLayout, fragment,
                                FragmentUtil.FRAGMENT_NO_ANIMATION, false);
                    }
                    //CleverTap
                    HashMap<String,Object> map = new HashMap<>();
                    map.put(THPConstants.CT_KEY_SOURCE,"Success");
                    CleverTapUtil.cleverTapEvent(getActivity(),THPConstants.CT_EVENT_OTP_VARIFICATION,map);
                }
            }

            @Override
            public void onError(Throwable t, String str) {
                if(getActivity() != null && getView() != null) {
                    progressBar.setVisibility(View.INVISIBLE);
                    pinEntry.setError(true);
                    Alerts.showToast(getActivity(), "FAIL");
                    pinEntry.postDelayed(()-> pinEntry.setText(null), 1000);
                }
            }

            @Override
            public void onComplete(String str) {

            }
        }, otp, emailOrContact);
    }

    /**
     * Resends OTP request to server
     */
    private void reSendSignupOtpReq() {
        Log.i("Counter", "reSendSignupOtpReq: "+count);
        if(count<=5) {
            progressBar.setVisibility(View.VISIBLE);
            resend_Txt.setText("");
            enableDisableResentBtn(false);
            THPFirebaseAnalytics.setFirbaseAnalyticsEvent(getContext(), "Action", "Resend OTP Button clicked", OTPVerificationFragment.class.getSimpleName());
            CleverTapUtil.cleverTapEvent(getActivity(),THPConstants.CT_EVENT_RESENT_OTP_FP,null);
            ApiManager.userVerification(new RequestCallback<KeyValueModel>() {
                @Override
                public void onNext(KeyValueModel keyValueModel) {
                    if (getActivity() == null && getView() == null) {
                        return;
                    }
                    progressBar.setVisibility(View.INVISIBLE);
                    resend_Txt.setText("Resend OTP");
                    if (keyValueModel.getState() != null && !keyValueModel.getState().equalsIgnoreCase("success")) {
                        Alerts.showAlertDialogOKBtn(getActivity(), "Sorry!", keyValueModel.getName());
                    } else {
                        // TODO, Nothing
                    }
                    countDownTimer();
                }

                @Override
                public void onError(Throwable t, String str) {
                    if (getActivity() != null && getView() != null) {
                        progressBar.setVisibility(View.INVISIBLE);
                        resend_Txt.setText("Resend OTP");
                        enableDisableResentBtn(true);
                        try {
                            Alerts.showErrorDailog(getChildFragmentManager(), null, t.getLocalizedMessage());
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onComplete(String str) {

                }
            }, email, contact, BuildConfig.SITEID, mEventType);
        }else{
            Toast.makeText(getActivity(),"Try again later",Toast.LENGTH_SHORT).show();
        }
    }

    private void generateOTP() {
        progressBar.setVisibility(View.VISIBLE);
        ApiManager.generateOtp(email, contact, BuildConfig.SITEID, mEventType)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aBoolean -> {
                    if(getActivity() == null && getView() == null) {
                        return;
                    }
                    progressBar.setVisibility(View.INVISIBLE);
                    if(!aBoolean) {
                        Alerts.showAlertDialogOKBtn(getActivity(), "Sorry!", "OTP couldn't generated please try again.");
                    }
                }, throwable -> {

                }, () -> {
                    progressBar.setVisibility(View.INVISIBLE);
                });
    }


}
