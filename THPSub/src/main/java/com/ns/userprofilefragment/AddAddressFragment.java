package com.ns.userprofilefragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.netoperation.model.UserProfile;
import com.netoperation.net.ApiManager;
import com.netoperation.util.NetConstants;
import com.ns.alerts.Alerts;
import com.ns.clevertap.CleverTapUtil;
import com.ns.loginfragment.BaseFragmentTHP;
import com.ns.thpremium.BuildConfig;
import com.ns.thpremium.R;
import com.ns.utils.CommonUtil;
import com.ns.utils.FragmentUtil;
import com.ns.utils.THPConstants;
import com.ns.utils.THPFirebaseAnalytics;
import com.ns.view.CustomProgressBar;
import com.ns.view.CustomProgressBarWhite;
import com.ns.view.FontCache;
import com.ns.view.text.CustomTextView;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class AddAddressFragment extends BaseFragmentTHP {

    private String mFrom = "";

    public static AddAddressFragment getInstance(String from) {
        AddAddressFragment fragment = new AddAddressFragment();
        Bundle bundle = new Bundle();
        bundle.putString("from", from);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        if(mIsDayTheme) {
            return R.layout.fragment_add_address;
        } else {
            return R.layout.fragment_add_address_dark;
        }
    }

    private UserProfile mUserProfile;

    private TextInputEditText flatNoET;
    private TextInputEditText addressLine1ET;
    private TextInputEditText pincodeET;
    private TextInputEditText cityET;
    private TextInputEditText stateET;
    private TextInputEditText landmarkET;

    private CustomTextView homeBtn_Txt;
    private CustomTextView officeBtn_Txt;
    private CustomTextView othersBtn_Txt;
    private CustomTextView saveAddressBtn_Txt;
    private CustomProgressBar progressBar;
    private CustomProgressBarWhite progressBarWhite;

    private String mPrimaryAddress = "";


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

        flatNoET = view.findViewById(R.id.flatNoET);
        addressLine1ET = view.findViewById(R.id.addressLine1ET);
        pincodeET = view.findViewById(R.id.pincodeET);
        cityET = view.findViewById(R.id.cityET);
        stateET = view.findViewById(R.id.stateET);
        landmarkET = view.findViewById(R.id.landmarkET);

        homeBtn_Txt = view.findViewById(R.id.homeBtn_Txt);
        officeBtn_Txt = view.findViewById(R.id.officeBtn_Txt);
        othersBtn_Txt = view.findViewById(R.id.othersBtn_Txt);
        saveAddressBtn_Txt = view.findViewById(R.id.saveAddressBtn_Txt);
        progressBar = view.findViewById(R.id.progressBar);
        progressBarWhite = view.findViewById(R.id.progressBarVerify);

        stateET.setFocusable(false);
        stateET.setClickable(true);

        Typeface customFont = FontCache.getTypeface(getResources().getString(R.string.THP_FiraSans_Regular), getActivity());

        flatNoET.setTypeface(customFont);
        addressLine1ET.setTypeface(customFont);
        cityET.setTypeface(customFont);
        stateET.setTypeface(customFont);
        landmarkET.setTypeface(customFont);

        TextInputLayout flatLayout = view.findViewById(R.id.flatNoLayout);
        TextInputLayout addressLayout = view.findViewById(R.id.addressLine1Layout);
        TextInputLayout cityLayout = view.findViewById(R.id.cityLayout);
        TextInputLayout stateLayout = view.findViewById(R.id.stateLayout);
        TextInputLayout landmarkLayout = view.findViewById(R.id.landmarkLayout);

        flatLayout.setTypeface(customFont);
        addressLayout.setTypeface(customFont);
        cityLayout.setTypeface(customFont);
        stateLayout.setTypeface(customFont);
        landmarkLayout.setTypeface(customFont);


        stateET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0)
                cityET.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // Back button click listener
        view.findViewById(R.id.backBtn).setOnClickListener(v->{
            FragmentUtil.clearSingleBackStack((AppCompatActivity)getActivity());
        });

        homeBtn_Txt.setOnClickListener(v->{
            mPrimaryAddress = "Home";
            homeBtn_Txt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_ok, 0, 0, 0);
            officeBtn_Txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            othersBtn_Txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        });

        officeBtn_Txt.setOnClickListener(v->{
            mPrimaryAddress = "Office";
            homeBtn_Txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            officeBtn_Txt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_ok, 0, 0, 0);
            othersBtn_Txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        });

        othersBtn_Txt.setOnClickListener(v->{
            mPrimaryAddress = "Others";
            homeBtn_Txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            officeBtn_Txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            othersBtn_Txt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_ok, 0, 0, 0);
        });

        saveAddressBtn_Txt.setOnClickListener(v->{
            if(mIsOnline) {
                updateAddress();
                THPFirebaseAnalytics.setFirbaseAnalyticsEvent(getContext(), "Action", "Save Address Button clicked", AddAddressFragment.class.getSimpleName());
                //CleverTap
                HashMap<String,Object> map = new HashMap<>();
                map.put(THPConstants.CT_KEY_My_address,"Yes");
                CleverTapUtil.cleverTapEvent(getActivity(),THPConstants.CT_EVENT_PROFILE,map);
            } else {
                noConnectionSnackBar(getView());
            }
        });

        stateET.setOnClickListener(v->{
            CommonUtil.hideKeyboard(v);
            loadState("IN");
        });

        view.findViewById(R.id.clearAll_Txt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(getString(R.string.clear_all));
                builder.setMessage(getString(R.string.text_confirm_clear_all));
                builder.setPositiveButton("Yes", (dialogInterface, i) -> {
                    //Clear all the details after confirmation
                    mPrimaryAddress = "";
                    flatNoET.setText("");
                    addressLine1ET.setText("");
                    pincodeET.setText("");
                    cityET.setText("");
                    stateET.setText("");
                    landmarkET.setText("");
                    homeBtn_Txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    officeBtn_Txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    othersBtn_Txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    THPFirebaseAnalytics.setFirbaseAnalyticsEvent(getContext(), "Action", "Clear All clicked", AddAddressFragment.class.getSimpleName());
                });
                builder.setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.cancel());
                builder.create().show();

            }
        });

        loadUserProfileData();

        //Pin code, next focus to State Drop down
        pincodeET.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                //Send focus to State and load state names
                CommonUtil.hideKeyboard(textView);
                loadState("IN");
                return true;
            }
            return false;
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

                    String country = mUserProfile.getProfile_Country();
                    if(country == null || (!country.equalsIgnoreCase("india"))) {
                        stateET.setVisibility(View.GONE);
                        getView().findViewById(R.id.stateLayout).setVisibility(View.GONE);
                    } else {
                        stateET.setText(mUserProfile.getAddress_state());
                        getView().findViewById(R.id.stateLayout).setVisibility(View.VISIBLE);
                    }
                    flatNoET.setText(mUserProfile.getAddress_house_no());
                    addressLine1ET.setText(mUserProfile.getAddress_street());
                    pincodeET.setText(mUserProfile.getAddress_pincode());

                    cityET.setText(mUserProfile.getAddress_city());
                    landmarkET.setText(mUserProfile.getAddress_landmark());
                    mPrimaryAddress = mUserProfile.getAddress_default_option();

                    //Disable City field if State name is empty
                    /*if (stateET.getText().toString().length() == 0) {
                        cityET.setEnabled(false);
                    } else {
                        cityET.setEnabled(true);
                    }*/

                    if(mPrimaryAddress != null) {
                        if(mPrimaryAddress.equalsIgnoreCase("Home")) {
                            homeBtn_Txt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_ok, 0, 0, 0);
                        } else if(mPrimaryAddress.equalsIgnoreCase("Office")) {
                            officeBtn_Txt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_ok, 0, 0, 0);
                        } else if(mPrimaryAddress.equalsIgnoreCase("Others")) {
                            othersBtn_Txt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_ok, 0, 0, 0);
                        }
                    }

                    return "";
                })
                .subscribe(v -> {
                        },
                        t ->
                            Log.i("", "" + t)
                        ));
    }

    private void updateAddress() {

        String flatNo = flatNoET.getText().toString();
        String addressLine1 = addressLine1ET.getText().toString();
        String pincode = pincodeET.getText().toString();
        String city = cityET.getText().toString();
        String state = stateET.getText().toString();
        String landmark = landmarkET.getText().toString();

        /*if(TextUtils.isEmpty(flatNo) || TextUtils.isEmpty(addressLine1) || TextUtils.isEmpty(pincode)
                || TextUtils.isEmpty(city) || TextUtils.isEmpty(state)) {
            Alerts.showAlertDialogOKBtn(getActivity(), getResources().getString(R.string.empty_fields), "Required(*) fields are mandatory.");
            return;
        } else if (pincode.length() < 5) {
            Alerts.showAlertDialogOKBtn(getActivity(), getResources().getString(R.string.pincode_error), "Pincode length must be greater than or equal to 5.");
            return;
        } else if(mPrimaryAddress == null) {
            Alerts.showAlertDialogOKBtn(getActivity(), getResources().getString(R.string.primary_address), "Please select Primary Address.");
            return;
        }*/

        if(mPrimaryAddress != null && mPrimaryAddress.trim().length() > 0) {
            if (TextUtils.isEmpty(flatNo) && TextUtils.isEmpty(addressLine1) && TextUtils.isEmpty(pincode)
                    && TextUtils.isEmpty(city) && TextUtils.isEmpty(state) && TextUtils.isEmpty(landmark)) {
                Alerts.showToastAtCenter(getContext(), "Empty fields cannot be labeled as \""+mPrimaryAddress+"\"");
                return;
            }
        }

        progressBarWhite.setVisibility(View.VISIBLE);
        saveAddressBtn_Txt.setEnabled(false);
        saveAddressBtn_Txt.setText("");

        mDisposable.add(ApiManager.updateAddress(getActivity(), mUserProfile, BuildConfig.SITEID, flatNo, addressLine1,
                landmark, pincode, state, city, mPrimaryAddress, "")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(keyValueModel->{
                    if(keyValueModel.getState().equalsIgnoreCase(NetConstants.SUCCESS)) {
                        Alerts.showToast(getActivity(), getString(R.string.address_updated));
                        FragmentUtil.clearSingleBackStack((AppCompatActivity) getActivity());
                    }
                    else {
                        Alerts.showAlertDialogOKBtn(getActivity(), "Sorry!", keyValueModel.getName());
                    }
                }, throwable -> {
                    if (throwable instanceof ConnectException
                            || throwable instanceof SocketTimeoutException || throwable instanceof TimeoutException) {
                        Alerts.showErrorDailog(getChildFragmentManager(), getResources().getString(R.string.kindly), getResources().getString(R.string.please_check_ur_connectivity));
                    }
                    saveAddressBtn_Txt.setEnabled(true);
                }, () ->{
                    progressBarWhite.setVisibility(View.GONE);
                    saveAddressBtn_Txt.setEnabled(true);
                    saveAddressBtn_Txt.setText("Save Address");
                }));
    }


    private void loadState(String countryCode) {
        if(stateET.getVisibility() == View.GONE) {
            cityET.setEnabled(true);
            cityET.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mDisposable.add(ApiManager.getState(countryCode)
                .observeOn(AndroidSchedulers.mainThread())
                .map(states ->
                        states
                )
                .subscribe(value -> {
                    DropDownFragment fragment = DropDownFragment.getInstance("state", value);
                    FragmentUtil.addFragmentAnim((AppCompatActivity) getActivity(), R.id.parentLayout, fragment,
                            FragmentUtil.FRAGMENT_ANIMATION, false);
                    fragment.setOnDropdownitemSelection((from, stateModel) -> {
                        stateET.setText(stateModel.getState());
                        cityET.setEnabled(true);
                        cityET.requestFocus();
                        CommonUtil.showKeyboard(cityET);
                        FragmentUtil.clearSingleBackStack((AppCompatActivity) getActivity());
                    });
                    progressBar.setVisibility(View.GONE);

                }, throwable -> {
                    progressBar.setVisibility(View.GONE);
                }));
    }

    @Override
    public void onResume() {
        super.onResume();
        THPFirebaseAnalytics.setFirbaseAnalyticsScreenRecord(getActivity(), "Add Address Screen", AddAddressFragment.class.getSimpleName());
    }


}
