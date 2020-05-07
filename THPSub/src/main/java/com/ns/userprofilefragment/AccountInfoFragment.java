package com.ns.userprofilefragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.netoperation.net.ApiManager;
import com.ns.loginfragment.BaseFragmentTHP;
import com.ns.thpremium.R;
import com.ns.utils.FragmentUtil;
import com.ns.utils.THPFirebaseAnalytics;
import com.ns.view.text.CustomTextView;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class AccountInfoFragment extends BaseFragmentTHP {


    TextInputLayout mobileNumberLayout;
    TextInputLayout emailLayout;
    TextInputEditText mobileNumberET;
    TextInputEditText emailET;

    CustomTextView verifyViaOTPBtn_Txt;
    CustomTextView updateBtn_Txt;

    @Override
    public int getLayoutRes() {
        if(sIsDayTheme) {
            return R.layout.fragment_account_info;
        } else {
            return R.layout.fragment_account_info_dark;
        }
    }

    public static AccountInfoFragment getInstance(String from) {
        AccountInfoFragment fragment = new AccountInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putString("from", from);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mobileNumberET = view.findViewById(R.id.mobileNumberET);
        emailET = view.findViewById(R.id.emailET);
        emailLayout = view.findViewById(R.id.emailLayout);
        mobileNumberLayout = view.findViewById(R.id.mobileNumberLayout);

        mobileNumberET.setEnabled(false);
        emailET.setEnabled(false);


        // Back button click listener
        view.findViewById(R.id.backBtn).setOnClickListener(v->{
            FragmentUtil.clearSingleBackStack((AppCompatActivity)getActivity());
        });

        loadUserProfileData();

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

                    Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(),  getString(R.string.THP_FiraSans_Regular));

                    mobileNumberET.setTypeface(custom_font);
                    emailET.setTypeface(custom_font);

                    if(userProfile.getContact()!= null && !TextUtils.isEmpty(userProfile.getContact())) {
                        mobileNumberET.setText("+91 "+userProfile.getContact());
                    }
                    else {
                        mobileNumberLayout.setVisibility(View.GONE);
                    }
                    if(userProfile.getEmailId()!= null && !TextUtils.isEmpty(userProfile.getEmailId())) {
                        emailET.setText(userProfile.getEmailId());
                    }
                    else {
                        emailLayout.setVisibility(View.GONE);
                    }
                    return "";
                })
                .subscribe(v -> {
                        },
                        t -> {
                            Log.i("", "" + t);
                        }));
    }

    /*private boolean shouldShowError() {
        int textLength = editText.getText().length();
        return textLength > 0 && textLength < MIN_TEXT_LENGTH;
    }

    private void showError() {
        textInputLayout.setError(getString(R.string.error));
    }

    private void hideError() {
        textInputLayout.setError(EMPTY_STRING);
    }

    private static final class ActionListener implements TextView.OnEditorActionListener {
        private final WeakReference<MainActivity> mainActivityWeakReference;

        public static ActionListener newInstance(MainActivity mainActivity) {
            WeakReference<MainActivity> mainActivityWeakReference = new WeakReference<>(mainActivity);
            return new ActionListener(mainActivityWeakReference);
        }

        private ActionListener(WeakReference<MainActivity> mainActivityWeakReference) {
            this.mainActivityWeakReference = mainActivityWeakReference;
        }

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            MainActivity mainActivity = mainActivityWeakReference.get();
            if (mainActivity != null) {
                if (actionId == EditorInfo.IME_ACTION_GO && mainActivity.shouldShowError()) {
                    mainActivity.showError();
                } else {
                    mainActivity.hideError();
                }
            }
            return true;
        }
    }*/

    @Override
    public void onResume() {
        super.onResume();
        THPFirebaseAnalytics.setFirbaseAnalyticsScreenRecord(getActivity(), "Account Info Screen", AccountInfoFragment.class.getSimpleName());
    }

}
