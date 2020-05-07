package com.ns.userprofilefragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.netoperation.model.KeyValueModel;
import com.netoperation.model.UserProfile;
import com.netoperation.net.ApiManager;
import com.netoperation.util.NetConstants;
import com.ns.activity.BaseAcitivityTHP;
import com.ns.activity.THPUserProfileActivity;
import com.ns.alerts.Alerts;
import com.ns.callbacks.AppLocationListener;
import com.ns.clevertap.CleverTapUtil;
import com.ns.loginfragment.BaseFragmentTHP;
import com.ns.loginfragment.OTPVerificationFragment;
import com.ns.thpremium.BuildConfig;
import com.ns.thpremium.R;
import com.ns.utils.CommonUtil;
import com.ns.utils.FragmentUtil;
import com.ns.utils.NetUtils;
import com.ns.utils.ResUtil;
import com.ns.utils.THPConstants;
import com.ns.utils.THPFirebaseAnalytics;
import com.ns.view.CustomProgressBar;
import com.ns.view.CustomProgressBarWhite;
import com.ns.view.FontCache;
import com.ns.view.text.CustomTextView;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class PersonalInfoFragment extends BaseFragmentTHP {

    private String mFrom;

    private TextInputLayout nameLayout;
    private TextInputEditText nameET;

    private TextInputLayout dobLayout;
    private TextInputEditText dobLayoutET;

    private TextInputLayout genderLayout;
    private TextInputEditText genderLayoutET;

    private TextInputLayout countryLayout;
    private TextInputEditText countryET;

    private TextInputLayout stateLayout;
    private TextInputEditText stateET;

    private CustomProgressBar progressBar;
    private CustomProgressBarWhite progressBarWhite;
    private CustomTextView currentLocationBtn_Txt;

    private TextView updateBtn_Txt;

    private ArrayList<KeyValueModel> mCountryModels;
    private KeyValueModel mSelectedCountryModel;

    private UserProfile mUserProfile;

    private THPUserProfileActivity mActivity;

    @Override
    public int getLayoutRes() {
        if(sIsDayTheme) {
            return R.layout.fragment_personal_info;
        } else {
            return R.layout.fragment_personal_info_dark;
        }
    }

    public static PersonalInfoFragment getInstance(String from) {
        PersonalInfoFragment fragment = new PersonalInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putString("from", from);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof THPUserProfileActivity) {
            mActivity = (THPUserProfileActivity) context;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof THPUserProfileActivity) {
            mActivity = (THPUserProfileActivity) activity;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mFrom = getArguments().getString("from");
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        currentLocationBtn_Txt = view.findViewById(R.id.currentLocationBtn_Txt);
        progressBar = view.findViewById(R.id.progressBar);
        progressBarWhite = view.findViewById(R.id.progressBarVerify);
        updateBtn_Txt = view.findViewById(R.id.updateBtn_Txt);

        nameLayout = view.findViewById(R.id.nameLayout);
        nameET = view.findViewById(R.id.nameET);

        dobLayout = view.findViewById(R.id.dobLayout);
        dobLayoutET = view.findViewById(R.id.dobLayoutET);

        if(sIsDayTheme) {
            dobLayoutET.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_dob_wrapper, 0);
        } else {
            dobLayoutET.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_dob_wrapper_dark, 0);
        }

        genderLayout = view.findViewById(R.id.genderLayout);
        genderLayoutET = view.findViewById(R.id.genderLayoutET);

        countryLayout = view.findViewById(R.id.countryLayout);
        countryET = view.findViewById(R.id.countryET);

        stateLayout = view.findViewById(R.id.stateLayout);
        stateET = view.findViewById(R.id.stateET);

        dobLayoutET.setFocusable(false);
        dobLayoutET.setClickable(true);

        genderLayoutET.setFocusable(false);
        genderLayoutET.setClickable(true);

        countryET.setFocusable(false);
        countryET.setClickable(true);

        stateET.setFocusable(false);
        stateET.setClickable(true);

        Typeface customFont = FontCache.getTypeface(getResources().getString(R.string.THP_FiraSans_Regular), getActivity());

        nameLayout.setTypeface(customFont);
        dobLayout.setTypeface(customFont);
        genderLayout.setTypeface(customFont);
        stateLayout.setTypeface(customFont);
        countryLayout.setTypeface(customFont);

        nameET.setTypeface(customFont);
        dobLayoutET.setTypeface(customFont);
        genderLayoutET.setTypeface(customFont);
        stateET.setTypeface(customFont);
        countryET.setTypeface(customFont);

        //EditorListener
        nameET.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                //Send focus to State and load state names
                CommonUtil.hideKeyboard(textView);
                //Date picker dialog
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
                //Date picker dialog
                DatePickerDialog picker = new DatePickerDialog(getActivity(), R.style.DatePickerDialogTheme,
                        (view1, year1, monthOfYear, dayOfMonth) -> {
                            SimpleDateFormat df = new SimpleDateFormat(THPConstants.date_dd_MM_yyyy);
                            Calendar cal = Calendar.getInstance();
                            cal.set(year1, monthOfYear, dayOfMonth);
                            dobLayoutET.setText(df.format(cal.getTime()));
                        }, year, month, day);
                picker.getDatePicker().setMaxDate(new Date().getTime());
                picker.show();
                return true;
            }
            return false;
        });
        //Input Text Change Listener
        nameET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (nameET.getText().toString().length() > 1) {
                    nameET.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_clear, 0);
                } else {
                    nameET.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
            }
        });

        // DrawableRight action
        nameET.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;

            if(event.getAction() == MotionEvent.ACTION_UP) {
                if(nameET.getCompoundDrawables()[DRAWABLE_RIGHT] != null && event.getRawX() >= (nameET.getRight() - nameET.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    //Clear text
                    nameET.setText("");
                    return false;
                }
            }
            return false;
        });

        TextView infoTextView = view.findViewById(R.id.info_Txt);
        if(sIsDayTheme) {
            infoTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_info_wrapper, 0, 0, 0);
        } else {
            infoTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_info_wrapper_white, 0, 0, 0);
        }

        infoTextView.setOnClickListener(v->{
            Alerts.showAlertDialogOKBtn(getActivity(), "",
                    "Your profile details will enable us to tailor the stories that we bring to better suit your interests and tastes.");
        });

         // Update Profile Button Click Listener
        updateBtn_Txt.setOnClickListener(v->{
            /*if (nameET.getText().length() == 0 || dobLayoutET.getText().length() == 0 ||
                    genderLayoutET.getText().length() == 0 || countryET.getText().length() == 0) {
                Alerts.showAlertDialogOKBtn(getActivity(), getResources().getString(R.string.empty_fields), "Required(*) fields are mandatory.");
                return;
            }*/
            if(BaseAcitivityTHP.sIsOnline) {
                disableAllView(false);
                progressBarWhite.setVisibility(View.VISIBLE);
                updateBtn_Txt.setText("");
                updateProfile();
                THPFirebaseAnalytics.setFirbaseAnalyticsEvent(getContext(), "Action", "Update Profile Button clicked", PersonalInfoFragment.class.getSimpleName());
            } else {
                noConnectionSnackBar(getView());
            }
        });

        // Back button click listener
        view.findViewById(R.id.backBtn).setOnClickListener(v -> {
            CommonUtil.hideKeyboard(v);
            FragmentUtil.clearSingleBackStack((AppCompatActivity) getActivity());
        });

        // Verify OTP button click listener
        view.findViewById(R.id.verifyViaOTPBtn_Txt).setOnClickListener(v -> {
            CommonUtil.hideKeyboard(v);
            OTPVerificationFragment fragment = OTPVerificationFragment.getInstance(THPConstants.FROM_PersonalInfoFragment);
            FragmentUtil.addFragmentAnim((AppCompatActivity) getActivity(), R.id.parentLayout, fragment,
                    FragmentUtil.FRAGMENT_ANIMATION, false);
        });

        //Clear All fields
        view.findViewById(R.id.clearAll_Txt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(getString(R.string.clear_all));
                builder.setMessage(getString(R.string.text_confirm_clear_all));
                builder.setPositiveButton("Yes", (dialogInterface, i) -> {
                    //Clear all the details after confirmation
                    nameET.setText("");
                    dobLayoutET.setText("");
                    genderLayoutET.setText("");
                    countryET.setText("");
                    stateET.setText("");
                    enableStateField(true);
                    mCountryModels = null;
                    mSelectedCountryModel = null;

                    THPFirebaseAnalytics.setFirbaseAnalyticsEvent(getContext(), "Action", "Clear All clicked", PersonalInfoFragment.class.getSimpleName());
                });
                builder.setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.cancel());
                builder.create().show();
            }
        });

        // Date of birth Click Listener
        dobLayoutET.setOnClickListener(v -> {
            CommonUtil.hideKeyboard(v);
            /*CalendarFragment fragment = CalendarFragment.getInstance();

            FragmentUtil.addFragmentAnim((AppCompatActivity) getActivity(),
                    R.id.parentLayout, fragment, FragmentUtil.FRAGMENT_NO_ANIMATION, false);

            fragment.setOnCalendarDateClickListener(date -> {
                SimpleDateFormat df = new SimpleDateFormat(THPConstants.date_dd_MM_yyyy);
                dobLayoutET.setText(df.format(date));

                // Clearing Calendar Fragment
                FragmentUtil.clearSingleBackStack((AppCompatActivity) getActivity());

            });*/

            //Date picker dialog
            final Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);
            //Date picker dialog
            DatePickerDialog picker = new DatePickerDialog(getActivity(), R.style.DatePickerDialogTheme,
                    (view1, year1, monthOfYear, dayOfMonth) -> {
                        SimpleDateFormat df = new SimpleDateFormat(THPConstants.date_dd_MM_yyyy);
                        Calendar cal = Calendar.getInstance();
                        cal.set(year1, monthOfYear, dayOfMonth);
                        dobLayoutET.setText(df.format(cal.getTime()));
                    }, year, month, day);
            picker.getDatePicker().setMaxDate(new Date().getTime());
            picker.show();
        });

        //Gender Button Click Listener
        genderLayoutET.setOnClickListener(v -> {
            CommonUtil.hideKeyboard(v);
            ArrayList<KeyValueModel> mList = new ArrayList<>();
            KeyValueModel model = new KeyValueModel();
            model.setName("Male");
            mList.add(model);

            model = new KeyValueModel();
            model.setName("Female");
            mList.add(model);

            model = new KeyValueModel();
            model.setName("Others");
            mList.add(model);

            DropDownFragment fragment = DropDownFragment.getInstance("gender", mList);

            FragmentUtil.addFragmentAnim((AppCompatActivity) getActivity(), R.id.parentLayout, fragment,
                    FragmentUtil.FRAGMENT_ANIMATION, false);
            fragment.setOnDropdownitemSelection((from, genderModel) -> {
                genderLayoutET.setText(ResUtil.capitalizeFirstLetter(genderModel.getName()));
                FragmentUtil.clearSingleBackStack((AppCompatActivity) getActivity());
            });
        });

        // Country Click Listener
        countryET.setOnClickListener(v -> {
            CommonUtil.hideKeyboard(v);
            if (mCountryModels == null) {
                loadCountry();
            } else {
                DropDownFragment fragment = DropDownFragment.getInstance("country", mCountryModels);
                FragmentUtil.addFragmentAnim((AppCompatActivity) getActivity(), R.id.parentLayout, fragment,
                        FragmentUtil.FRAGMENT_ANIMATION, false);
                fragment.setOnDropdownitemSelection((from, countryModel) -> {
                    mSelectedCountryModel = countryModel;
                    countryET.setText(countryModel.getName());
                    if (countryET.getText().toString().equalsIgnoreCase("India"))
                        enableStateField(true);
                    else
                        enableStateField(false);
                    FragmentUtil.clearSingleBackStack((AppCompatActivity) getActivity());

                    //loadState(countryModel.getCode());
                });
            }
        });

        // State Click Listener
        stateET.setOnClickListener(v -> {
            CommonUtil.hideKeyboard(v);
            if (countryET.getText().length() > 0 && countryET.getText().toString().trim().equalsIgnoreCase("India")) {
                loadState("IN");
            } else if (mCountryModels == null || mSelectedCountryModel == null) {
                Alerts.showAlertDialogOKBtn(getActivity(), "Select country", "Please select country first.");
                return;
            } else if (!mSelectedCountryModel.getCode().equalsIgnoreCase("IN")) {
                Alerts.showAlertDialogOKBtn(getActivity(), "Warning!", "Currently, We are supporting state list only for India.");
                return;
            } else {
                loadState(mSelectedCountryModel.getCode());
            }

        });

        // Current Location Click Listener
        currentLocationBtn_Txt.setOnClickListener(v -> {
            CommonUtil.hideKeyboard(v);
            if (!ResUtil.isGooglePlayServicesAvailable(getActivity())) {
                Alerts.showToast(getActivity(), "Re-Install Google Play Services App");
                return;
            }
            else if(mActivity != null) {
                mActivity.getLocation(new AppLocationListener() {
                    @Override
                    public void locationReceived(String maxAddress, String pin, String state,
                                                 String city, String subCity, String countryCode, String countryName) {
                        /*if(mCountryModels == null) {
                            mCountryModels = new ArrayList<>();
                        }*/

                        mSelectedCountryModel = new KeyValueModel();
                        mSelectedCountryModel.setCode(countryCode);
                        mSelectedCountryModel.setName(countryName);
                        //mCountryModels.add(mSelectedCountryModel);

                        countryET.setText(countryName);
                        stateET.setText(state);

                        if (countryET.getText().toString().contains("India"))
                            enableStateField(true);
                        else
                            enableStateField(false);

                    }

                    @Override
                    public void locationFailed() {
                        currentLocationBtn_Txt.setText("Failed to get current location");
                    }
                });
            }


        });

        // Load User Profile Data from local Database
        loadData();
    }


    /**
     * @param isEnable
     */
    private void enableStateField(boolean isEnable) {
        if (isEnable) {
            stateET.setVisibility(View.VISIBLE);
            stateLayout.setVisibility(View.VISIBLE);
        } else {
            stateET.setVisibility(View.GONE);
            stateLayout.setVisibility(View.GONE);
        }

    }


    /**
     * // Loads User Profile Data from local Database
     */
    private void loadData() {
        mDisposable.add(ApiManager.getUserProfile(getActivity())
                .observeOn(AndroidSchedulers.mainThread())
                .map(userProfile -> {
                    if (userProfile == null) {
                        return "";
                    }

                    mUserProfile = userProfile;

                    nameET.setText(mUserProfile.getFullNameForPersonalInfo());
                    dobLayoutET.setText(mUserProfile.getDOB());
                    genderLayoutET.setText(ResUtil.capitalizeFirstLetter(mUserProfile.getGender()));
                    countryET.setText(mUserProfile.getProfile_Country());
                    stateET.setText(mUserProfile.getProfile_State());


                    //CleverTap
                    if (TextUtils.isEmpty(mUserProfile.getFullNameForPersonalInfo()) && TextUtils.isEmpty(mUserProfile.getDOB()) &&
                            TextUtils.isEmpty(mUserProfile.getGender())&&TextUtils.isEmpty(mUserProfile.getProfile_Country())&&
                            TextUtils.isEmpty(mUserProfile.getProfile_State())) {

                        HashMap<String, Object> map = new HashMap<>();
                        map.put(THPConstants.CT_KEY_User_info, "No");
                        CleverTapUtil.cleverTapEvent(getActivity(), THPConstants.CT_EVENT_PROFILE, map);

                        HashMap<String, Object> map2 = new HashMap<>();
                        map2.put("No", "No");
                        CleverTapUtil.cleverTapEvent(getActivity(), THPConstants.CT_EVENT_PERSONAL_INFO, map2);
                    }

                    if (countryET.getText().length() == 0 || countryET.getText().toString().contains("India"))
                        enableStateField(true);
                    else
                        enableStateField(false);
                    return "";
                })
                .subscribe(v -> {
                        },
                        t -> {
                            Log.i("", "" + t);
                        }));
    }

    private void loadCountry() {
        if (!NetUtils.isConnected(getActivity())) {
            Alerts.showToastAtCenter(getContext(), getString(R.string.please_check_ur_connectivity));
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mDisposable.add(ApiManager.getCountry()
                .observeOn(AndroidSchedulers.mainThread())
                .map(countries -> {
                    return countries;
                })
                .subscribe(value -> {
                            mCountryModels = value;
                            progressBar.setVisibility(View.GONE);
                            DropDownFragment fragment = DropDownFragment.getInstance("country", value);
                            FragmentUtil.addFragmentAnim((AppCompatActivity) getActivity(), R.id.parentLayout, fragment,
                                    FragmentUtil.FRAGMENT_ANIMATION, false);

                            fragment.setOnDropdownitemSelection((from, countryModel) -> {
                                mSelectedCountryModel = countryModel;
                                countryET.setText(countryModel.getName());
                                if (countryModel.getName().equalsIgnoreCase("India"))
                                    enableStateField(true);
                                else
                                    enableStateField(false);
                                FragmentUtil.clearSingleBackStack((AppCompatActivity) getActivity());
//                                loadState(countryModel.getCode());
                            });

                        }, throwable ->
                                progressBar.setVisibility(View.GONE)
                ));
    }

    private void loadState(String countryCode) {
        if (!NetUtils.isConnected(getActivity())) {
            Alerts.showToastAtCenter(getContext(), getString(R.string.please_check_ur_connectivity));
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
                        FragmentUtil.clearSingleBackStack((AppCompatActivity) getActivity());
                    });
                    progressBar.setVisibility(View.GONE);

                }, throwable -> {
                    progressBar.setVisibility(View.GONE);
                }));
    }

    private void updateProfile() {
        if (!NetUtils.isConnected(getActivity())) {
            Alerts.showToastAtCenter(getContext(), getString(R.string.please_check_ur_connectivity));
            return;
        }
        String FullName = nameET.getText().toString();
        String DOB = dobLayoutET.getText().toString();
        String Gender = genderLayoutET.getText().toString().toLowerCase();
        String Profile_Country = countryET.getText().toString();
        String Profile_State = stateET.getText().toString();
        mDisposable.add(ApiManager.updateProfile(getActivity(), mUserProfile, BuildConfig.SITEID, FullName, DOB, Gender, Profile_Country, Profile_State)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(keyValueModel->{
                    if(keyValueModel.getState().equalsIgnoreCase(NetConstants.SUCCESS)) {
                        Alerts.showToast(getActivity(), "Profile updated successfully.");
                        //FragmentUtil.clearSingleBackStack((AppCompatActivity) getActivity());

                        //CleverTap
                        if(TextUtils.isEmpty(FullName)&& TextUtils.isEmpty(DOB)&&
                                TextUtils.isEmpty(Gender)&&TextUtils.isEmpty(Profile_Country)&&
                                TextUtils.isEmpty(Profile_State)) {
                            HashMap<String, Object> map = new HashMap<>();
                            map.put(THPConstants.CT_KEY_User_info, "No");
                            CleverTapUtil.cleverTapEvent(getActivity(), THPConstants.CT_EVENT_PROFILE, map);

                            //personal info
                            HashMap<String, Object> map2 = new HashMap<>();
                            map2.put("No", "No");
                            CleverTapUtil.cleverTapEvent(getActivity(), THPConstants.CT_EVENT_PERSONAL_INFO, map2);
                        }else {
                            //CleverTap
                            HashMap<String,Object> map = new HashMap<>();
                            map.put(THPConstants.CT_KEY_User_info,"Yes");
                            CleverTapUtil.cleverTapEvent(getActivity(),THPConstants.CT_EVENT_PROFILE,map);

                            //personal info
                            HashMap<String, Object> map2 = new HashMap<>();
                            map2.put("Yes", "Yes");
                            CleverTapUtil.cleverTapEvent(getActivity(), THPConstants.CT_EVENT_PERSONAL_INFO, map2);
                        }
                    } else {
                        Alerts.showAlertDialogOKBtn(getActivity(), "Sorry!", keyValueModel.getName());
                    }
                }, throwable -> {
                    disableAllView(true);
                    progressBarWhite.setVisibility(View.GONE);
                    updateBtn_Txt.setText("Update Profile");
                    if (throwable instanceof ConnectException
                            || throwable instanceof SocketTimeoutException || throwable instanceof TimeoutException) {
                        Alerts.showErrorDailog(getChildFragmentManager(), getResources().getString(R.string.kindly), getResources().getString(R.string.please_check_ur_connectivity));
                    }

                }, ()->{
                    disableAllView(true);
                    progressBarWhite.setVisibility(View.GONE);
                    updateBtn_Txt.setText("Update Profile");
                }));
    }

    private void disableAllView(boolean isEnable) {
        updateBtn_Txt.setEnabled(isEnable);
        nameET.setEnabled(isEnable);
        dobLayoutET.setEnabled(isEnable);
        genderLayoutET.setEnabled(isEnable);
        countryET.setEnabled(isEnable);
        stateET.setEnabled(isEnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        THPFirebaseAnalytics.setFirbaseAnalyticsScreenRecord(getActivity(), "Personal Info Screen", PersonalInfoFragment.class.getSimpleName());
    }


}
