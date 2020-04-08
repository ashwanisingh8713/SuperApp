package com.ns.userprofilefragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.netoperation.net.ApiManager;
import com.ns.alerts.Alerts;
import com.ns.clevertap.CleverTapUtil;
import com.ns.loginfragment.BaseFragmentTHP;
import com.ns.thpremium.R;
import com.ns.utils.FragmentUtil;
import com.ns.utils.ResUtil;
import com.ns.utils.THPConstants;
import com.ns.utils.THPFirebaseAnalytics;
import com.ns.view.CustomProgressBarWhite;
import com.ns.view.text.CustomTextView;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class ChangePasswordFragment extends BaseFragmentTHP {

    private String mFrom = "";

    private CustomProgressBarWhite progressBar;
    private CustomTextView updatePasswordBtn_Txt,
    errorTextConfirm, errorTextNew, errorTextCurrent;

    private TextInputEditText currentPasswordET;
    private TextInputEditText newPasswordET;
    private TextInputEditText confirmPasswordET;
    private ImageButton passwordVisibleBtnCurrent, passwordVisibleBtnNew, passwordVisibleBtnConfirm;
    private boolean mIsPasswordVisibleInCurrent, mIsPasswordVisibleInNew, mIsPasswordVisibleInConfirm;

    @Override
    public int getLayoutRes() {
        if(mIsDayTheme) {
            return R.layout.fragment_change_passwd;
        } else {
            return R.layout.fragment_change_passwd_dark;
        }
    }

    public static ChangePasswordFragment getInstance(String from) {
        ChangePasswordFragment fragment = new ChangePasswordFragment();
        Bundle bundle = new Bundle();
        bundle.putString("from", from);
        fragment.setArguments(bundle);
        return fragment;
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

        progressBar = view.findViewById(R.id.progressBarVerify);
        currentPasswordET = view.findViewById(R.id.currentPasswordET);
        newPasswordET = view.findViewById(R.id.newPasswordET);
        confirmPasswordET = view.findViewById(R.id.confirmPasswordET);
        updatePasswordBtn_Txt = view.findViewById(R.id.updatePasswordBtn_Txt);
        passwordVisibleBtnCurrent = view.findViewById(R.id.passwordVisible_Btn_current);
        passwordVisibleBtnNew = view.findViewById(R.id.passwordVisible_Btn_newPassword);
        passwordVisibleBtnConfirm = view.findViewById(R.id.passwordVisible_Btn_confirmPassword);

        //CleverTap
        HashMap<String,Object> map = new HashMap<>();
        map.put(THPConstants.CT_KEY_Change_pasword,"No");
        CleverTapUtil.cleverTapEvent(getActivity(),THPConstants.CT_EVENT_PROFILE,map);

        //Error text view objects
        errorTextCurrent = view.findViewById(R.id.textViewError_currentPassword);
        errorTextConfirm = view.findViewById(R.id.textViewError_ConfirmPassword);
        errorTextNew = view.findViewById(R.id.textViewError_NewPassword);

        // Back button click listener
        view.findViewById(R.id.backBtn).setOnClickListener(v->{
            FragmentUtil.clearSingleBackStack((AppCompatActivity)getActivity());
        });

        updatePasswordBtn_Txt.setOnClickListener(v->{
            if(!mIsOnline) {
                noConnectionSnackBar(getView());
                return;
            }
            updatePassword();
        });

        if(mIsDayTheme) {
            passwordVisibleBtnCurrent.setImageResource(R.drawable.ic_show_password);
        } else {
            passwordVisibleBtnCurrent.setImageResource(R.drawable.ic_show_password_dark);
        }

        // Password show / hide button click listener
        // Current Password
        passwordVisibleBtnCurrent.setOnClickListener(v -> {
            if (mIsPasswordVisibleInCurrent) {
                currentPasswordET.setTransformationMethod(new PasswordTransformationMethod());
                //Set Selection cursor to last character
                currentPasswordET.setSelection(currentPasswordET.getText().length());
                if(mIsDayTheme) {
                    passwordVisibleBtnCurrent.setImageResource(R.drawable.ic_show_password);
                } else {
                    passwordVisibleBtnCurrent.setImageResource(R.drawable.ic_show_password_dark);
                }
                mIsPasswordVisibleInCurrent = false;
            } else {
                currentPasswordET.setTransformationMethod(null);
                if(mIsDayTheme) {
                    passwordVisibleBtnCurrent.setImageResource(R.drawable.ic_hide_password);
                } else {
                    passwordVisibleBtnCurrent.setImageResource(R.drawable.ic_hide_password_dark);
                }
                //Set Selection cursor to last character
                currentPasswordET.setSelection(currentPasswordET.getText().length());
                mIsPasswordVisibleInCurrent = true;
            }
        });

        if(mIsDayTheme) {
            passwordVisibleBtnNew.setImageResource(R.drawable.ic_show_password);
        } else {
            passwordVisibleBtnNew.setImageResource(R.drawable.ic_show_password_dark);
        }

        // New Password
        passwordVisibleBtnNew.setOnClickListener(v -> {
            if (mIsPasswordVisibleInNew) {
                newPasswordET.setTransformationMethod(new PasswordTransformationMethod());
                //Set Selection cursor to last character
                newPasswordET.setSelection(newPasswordET.getText().length());
                if(mIsDayTheme) {
                    passwordVisibleBtnNew.setImageResource(R.drawable.ic_show_password);
                } else {
                    passwordVisibleBtnNew.setImageResource(R.drawable.ic_show_password_dark);
                }
                mIsPasswordVisibleInNew = false;
            } else {
                newPasswordET.setTransformationMethod(null);
                //Set Selection cursor to last character
                newPasswordET.setSelection(newPasswordET.getText().length());
                if(mIsDayTheme) {
                    passwordVisibleBtnNew.setImageResource(R.drawable.ic_hide_password);
                } else {
                    passwordVisibleBtnNew.setImageResource(R.drawable.ic_hide_password_dark);
                }
                mIsPasswordVisibleInNew = true;
            }
        });

        if(mIsDayTheme) {
            passwordVisibleBtnConfirm.setImageResource(R.drawable.ic_show_password);
        } else {
            passwordVisibleBtnConfirm.setImageResource(R.drawable.ic_show_password_dark);
        }

        // Confirm Password
        passwordVisibleBtnConfirm.setOnClickListener(v -> {
            if (mIsPasswordVisibleInConfirm) {
                confirmPasswordET.setTransformationMethod(new PasswordTransformationMethod());
                confirmPasswordET.setSelection(confirmPasswordET.getText().length());
                //Set Selection cursor to last character
                if(mIsDayTheme) {
                    passwordVisibleBtnConfirm.setImageResource(R.drawable.ic_show_password);
                } else {
                    passwordVisibleBtnConfirm.setImageResource(R.drawable.ic_show_password_dark);
                }
                mIsPasswordVisibleInConfirm = false;
            } else {
                confirmPasswordET.setTransformationMethod(null);
                confirmPasswordET.setSelection(confirmPasswordET.getText().length());
                //Set Selection cursor to last character
                if(mIsDayTheme) {
                    passwordVisibleBtnConfirm.setImageResource(R.drawable.ic_hide_password);
                } else {
                    passwordVisibleBtnConfirm.setImageResource(R.drawable.ic_hide_password_dark);
                }
                mIsPasswordVisibleInConfirm = true;
            }
        });

        //Text change listener
        currentPasswordET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if(s != null && s.length() > 0){
                    errorTextCurrent.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        newPasswordET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if(s != null && s.length() > 0){
                    errorTextNew.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        confirmPasswordET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if(s != null && s.length() > 0){
                    errorTextConfirm.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void updatePassword() {
        String currentPassword = currentPasswordET.getText().toString();
        String newPassword = newPasswordET.getText().toString();
        String confirmPassword = confirmPasswordET.getText().toString();

        if(TextUtils.isEmpty(currentPassword) || TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmPassword)) {
            //Alerts.showAlertDialogOKBtn(getActivity(), getString(R.string.empty_fields), getString(R.string.all_field_mandatory));
            if (TextUtils.isEmpty(currentPassword)) {
                errorTextCurrent.setVisibility(View.VISIBLE);
                errorTextCurrent.setText("Please enter your Current Password");
            }
            if (TextUtils.isEmpty(newPassword)) {
                errorTextNew.setVisibility(View.VISIBLE);
                errorTextNew.setText("Please set your New Password");
            }
            if (TextUtils.isEmpty(confirmPassword)) {
                errorTextConfirm.setVisibility(View.VISIBLE);
                errorTextConfirm.setText("Please enter your New Password");
            }
        }
        else if(newPassword.length() < 5) {
            //Alerts.showAlertDialogOKBtn(getActivity(), getString(R.string.alert), "New password & Confirm password doesn't match");
            errorTextNew.setVisibility(View.VISIBLE);
            errorTextNew.setText(R.string.password_constraints);
            return;
        }
        else if(!newPassword.equals(confirmPassword)) {
            //Alerts.showAlertDialogOKBtn(getActivity(), getString(R.string.alert), getString(R.string.passwd_length_err_msg));
            errorTextConfirm.setVisibility(View.VISIBLE);
            errorTextConfirm.setText("New password & Confirm password doesn't match");
            return;
        } else if(!ResUtil.isValidPassword(confirmPassword)) {
            Alerts.showAlertDialogOKBtn(getActivity(), getString(R.string.alert), getString(R.string.password_constraints));
            return;
        } else {
            progressBar.setVisibility(View.VISIBLE);
            updatePasswordBtn_Txt.setEnabled(false);
            updatePasswordBtn_Txt.setText("");
            mDisposable.add(ApiManager.getUserProfile(getActivity())
                    .subscribe(userProfile ->
                                    ApiManager.updatePassword(userProfile.getUserId(), currentPassword, confirmPassword)
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(keyValueModel -> {
                                                if(keyValueModel.getState() != null && keyValueModel.getState().equalsIgnoreCase("failed")) {
                                                    if (keyValueModel.getName() != null && keyValueModel.getName().contains("incorrect") ||
                                                            keyValueModel.getName().contains("Incorrect") ||
                                                            keyValueModel.getName().contains("Invalid")) {
                                                        errorTextCurrent.setVisibility(View.VISIBLE);
                                                        errorTextCurrent.setText(ResUtil.capitalizeFirstLetter(keyValueModel.getName()));

                                                    }
//                                                    Alerts.showAlertDialogOKBtn(getActivity(), "Failed to change password", "Invalid current password.");
                                                }
                                                else {
                                                    Alerts.showToastAtCenter(getActivity(), "Password changed successfully");
                                                    THPFirebaseAnalytics.setFirbaseAnalyticsEvent(getContext(), "Action", "Update Password Button clicked", ChangePasswordFragment.class.getSimpleName());

                                                    //CleverTap
                                                    HashMap<String,Object> map = new HashMap<>();
                                                    map.put(THPConstants.CT_KEY_Change_pasword,"Yes");
                                                    CleverTapUtil.cleverTapEvent(getActivity(),THPConstants.CT_EVENT_PROFILE,map);

//                                                    Alerts.showSnackbar(getActivity(), "Password changed successfully");
                                                    FragmentUtil.clearSingleBackStack((AppCompatActivity) getActivity());
                                                }
                                            }, throwable -> {
                                                progressBar.setVisibility(View.GONE);
                                                updatePasswordBtn_Txt.setEnabled(true);
                                                updatePasswordBtn_Txt.setText("Update Password");
                                                Alerts.showErrorDailog(getChildFragmentManager(), getResources().getString(R.string.kindly), getResources().getString(R.string.please_check_ur_connectivity));
                                            }, () -> {
                                                progressBar.setVisibility(View.GONE);
                                                updatePasswordBtn_Txt.setEnabled(true);
                                                updatePasswordBtn_Txt.setText("Update Password");
                                            })
                            , throwable -> {

                            }));

        }







    }

    @Override
    public void onResume() {
        super.onResume();
        THPFirebaseAnalytics.setFirbaseAnalyticsScreenRecord(getActivity(), "Change Password Screen", ChangePasswordFragment.class.getSimpleName());
    }

}
