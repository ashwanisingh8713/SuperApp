package com.ns.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.netoperation.model.PersonaliseDetails;
import com.netoperation.model.PersonaliseModel;
import com.netoperation.model.PrefListModel;
import com.netoperation.model.UserProfile;
import com.netoperation.net.ApiManager;
import com.netoperation.util.NetConstants;
import com.ns.adapter.PersonaliseAdapter;
import com.ns.alerts.Alerts;
import com.ns.callbacks.THPPersonaliseItemClickListener;
import com.ns.clevertap.CleverTapUtil;
import com.ns.model.PersonaliseItem;
import com.ns.personalisefragment.AuthorsFragment;
import com.ns.personalisefragment.CitiesFragment;
import com.ns.personalisefragment.TopicsFragment;
import com.ns.thpremium.BuildConfig;
import com.ns.thpremium.R;
import com.ns.utils.IntentUtil;
import com.ns.utils.NetUtils;
import com.ns.utils.ResUtil;
import com.ns.utils.THPConstants;
import com.ns.utils.THPFirebaseAnalytics;
import com.ns.view.text.CustomTextView;
import com.ns.view.ViewPagerScroller;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public class THPPersonaliseActivity extends BaseAcitivityTHP implements THPPersonaliseItemClickListener {
    private ViewPager viewPager;
    private TextView mErrorText, savePersonaliseBtn_Txt;
    private ProgressBar mProgressBar;
    private String refreshText = "Refresh";

    ImageButton backBtn, forwardArrow, backArrow;
    private PersonaliseAdapter personaliseAdapter;

    protected final CompositeDisposable mDisposable = new CompositeDisposable();
    ArrayList<PersonaliseItem> topics = new ArrayList<>();
    ArrayList<PersonaliseItem> cities = new ArrayList<>();
    ArrayList<PersonaliseItem> authors = new ArrayList<>();

    ArrayList<String> topicsAlreadySelected = new ArrayList<>();
    ArrayList<String> citiesAlreadySelected = new ArrayList<>();
    ArrayList<String> authorsAlreadySelected = new ArrayList<>();

    private CustomTextView tv_savepref, tv_items;

    private UserProfile mUserProfile;

    private PrefListModel mPrefListModel;

    private String mFrom;

    @Override
    public int layoutRes() {
        return R.layout.activity_thp_personalise;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        backBtn = findViewById(R.id.backBtn);
        tv_savepref = findViewById(R.id.tv_savepref);
        tv_items = findViewById(R.id.tv_items);
        forwardArrow = findViewById(R.id.forwardArrow);
        backArrow = findViewById(R.id.backArrow);
        viewPager = findViewById(R.id.personalise_viewPager);
        savePersonaliseBtn_Txt = findViewById(R.id.savePersonaliseBtn_Txt);

        viewPager.setOffscreenPageLimit(4);

        if(getIntent() != null) {
            mFrom = getIntent().getStringExtra("from");
        }


        // TODO, Show ProgressBar
        mProgressBar = findViewById(R.id.section_progress);
        mErrorText = findViewById(R.id.error_text);
        // This is smooth scroll of ViewPager
        smoothPagerScroll();

        savePersonaliseBtn_Txt.setOnClickListener(v->{
            if(NetUtils.isConnected(this)) {
                /*if ((topics != null && topics.size() > 0) || (cities != null && cities.size() > 0) ||
                        (authors != null && authors.size() > 0)) {
                    saveUserPersonalise();
                } else {
                    Alerts.showErrorDailog(getSupportFragmentManager(), getResources().getString(R.string.kindly), getResources().getString(R.string.select_preference));
                }*/

                if (savePersonaliseBtn_Txt.getText().toString().equalsIgnoreCase(refreshText)) {
                    //Hide button and set text
                    savePersonaliseBtn_Txt.setVisibility(View.GONE);
                    savePersonaliseBtn_Txt.setText("Save & Next");
                    mProgressBar.setVisibility(View.VISIBLE);
                    mErrorText.setVisibility(View.GONE);
                    // It load First UserProfile Data, next User Preference data
                    loadUserProfile();
                } else {
                    saveUserPersonalise();
                }

            } else {
                Alerts.noConnectionSnackBar(tv_savepref, this);
            }

        });

        backBtn.setOnClickListener(v->{
            finish();
        });

        getCurrentItemPosition();
        forwardArrow.setOnClickListener(v->{
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            getCurrentItemPosition();
        });

        backArrow.setOnClickListener(v->{
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
            getCurrentItemPosition();
        });

        // It load First UserProfile Data, next User Preference data
        loadUserProfile();

        registerPageChangeListener();
    }

    private void getCurrentItemPosition() {
        int selectedPosition=0;
        selectedPosition=viewPager.getCurrentItem();
        if(selectedPosition==0) {
            backArrow.setImageResource(R.drawable.ic_left_arrow_disable);
            forwardArrow.setImageResource(R.drawable.ic_right_arror_enable);
            forwardArrow.setEnabled(true);
            backArrow.setEnabled(false);
        }else if(selectedPosition==1) {
            backArrow.setImageResource(R.drawable.ic_left_arrow_enable);
            forwardArrow.setImageResource(R.drawable.ic_right_arror_enable);
            forwardArrow.setEnabled(true);
            backArrow.setEnabled(true);
        }else if(selectedPosition==2) {
            forwardArrow.setEnabled(false);
            backArrow.setEnabled(true);
            forwardArrow.setImageResource(R.drawable.ic_right_arrow_disable);
            backArrow.setImageResource(R.drawable.ic_left_arrow_enable);
        }
    }

    private void registerPageChangeListener() {
        savePersonaliseBtn_Txt.setVisibility(View.GONE);
        savePersonaliseBtn_Txt.setText("Save & Next");

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                getCurrentItemPosition();
                String fragName =personaliseAdapter.getPageTitle(i).toString();
                fragName = fragName.substring(0,1).toUpperCase() + fragName.substring(1).toLowerCase();
                switch (i){
                    case 0:
                        tv_items.setText(fragName);
                        savePersonaliseBtn_Txt.setText("Save & Next");
                        break;
                    case 1:
                        tv_items.setText(fragName);
                        savePersonaliseBtn_Txt.setText("Save & Next");
                        break;
                    case 2:
                        tv_items.setText(fragName);
                        savePersonaliseBtn_Txt.setText("Set My Dashboard");
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });


    }

    /**
     * Loads User Profile
     */
    private void loadUserProfile() {
        mDisposable.add(ApiManager.getUserProfile(this)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userProfile -> {
                   mUserProfile = userProfile;
                    getUserSavedPersonalise(mUserProfile.getUserId());
                }));
    }



    private void getUserSavedPersonalise(String userId) {
        mDisposable.add(
                ApiManager.getUserSavedPersonalise(userId, BuildConfig.SITEID, ResUtil.getDeviceId(this))
                        .map(prefListModel -> {
                    mPrefListModel = prefListModel;
                    getAllPersonalise();
                    return "";
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(value->{
                    Log.i("", "");
                }, throwable -> {
                    Log.i("", "");
                    Alerts.noConnectionSnackBar(tv_savepref, this);
                    savePersonaliseBtn_Txt.setVisibility(View.VISIBLE);
                    savePersonaliseBtn_Txt.setText(refreshText);
                    mProgressBar.setVisibility(View.GONE);
                    mErrorText.setVisibility(View.VISIBLE);
                    mErrorText.setText(R.string.please_check_ur_connectivity);
                })
        );

    }


    private void getAllPersonalise() {
        String url = "";
        if(BuildConfig.IS_PRODUCTION) {
            url = BuildConfig.PRODUCTION_PERSONALISE_URL;
        } else {
            url = BuildConfig.STATGGING_PERSONALISE_URL;
        }
             mDisposable.add(
                ApiManager.getAllPreferences(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        value -> {
                            List<Fragment> mPersonaliseFragments = new ArrayList<>();
                            PersonaliseDetails topicsFromServer = value.getTopics();
                            PersonaliseDetails citiesFromServer = value.getCities();
                            PersonaliseDetails authorsFromServer = value.getAuthors();

                            if(mPrefListModel != null) {
                                PersonaliseDetails topicsFromUser = mPrefListModel.getTopics();
                                PersonaliseDetails citiesFromUser = mPrefListModel.getCities();
                                PersonaliseDetails authorsFromUser = mPrefListModel.getAuthors();

                                ArrayList<PersonaliseModel> topicSelected = topicsFromUser.getValues();
                                if(topicSelected != null) {
                                    ArrayList<PersonaliseModel> topicDataServer = topicsFromServer.getValues();
                                    for(PersonaliseModel topicD : topicSelected) {
                                        int index = topicsFromServer.getValues().indexOf(topicD);
                                        if (index != -1) {
                                            String val = getPrefeItemTrimValue(topicD.getValue());
                                            topicDataServer.get(index).setSelected(true);
                                            topicsAlreadySelected.add(val);
                                        }
                                    }
                                }

                                ArrayList<PersonaliseModel> citiesSelected = citiesFromUser.getValues();
                                if(citiesSelected != null) {
                                    ArrayList<PersonaliseModel> citiesDataServer = citiesFromServer.getValues();
                                    for(PersonaliseModel cityD : citiesSelected) {
                                        int index = citiesFromServer.getValues().indexOf(cityD);
                                        if (index != -1) {
                                            citiesDataServer.get(index).setSelected(true);
                                            String val = getPrefeItemTrimValue(cityD.getValue());
                                            citiesAlreadySelected.add(val);
                                        }
                                    }
                                }

                                ArrayList<PersonaliseModel> authorsSelected = authorsFromUser.getValues();
                                if(authorsSelected != null) {
                                    ArrayList<PersonaliseModel> authorsDataServer = authorsFromServer.getValues();
                                    for(PersonaliseModel authorD : authorsSelected) {
                                        int index = authorsFromServer.getValues().indexOf(authorD);
                                        if (index != -1) {
                                            authorsDataServer.get(index).setSelected(true);
                                            String val = getPrefeItemTrimValue(authorD.getValue());
                                            authorsAlreadySelected.add(val);
                                        }
                                    }
                                }
                            }
                            mPersonaliseFragments.add(TopicsFragment.getInstance(topicsFromServer, topicsFromServer.getName()));
                            mPersonaliseFragments.add(CitiesFragment.getInstance(citiesFromServer, citiesFromServer.getName()));
                            mPersonaliseFragments.add(AuthorsFragment.getInstance(authorsFromServer, authorsFromServer.getName()));

                            String topic=value.getTopics().getName();
                            String city=value.getCities().getName();
                            String author=value.getAuthors().getName();

                            personaliseAdapter = new PersonaliseAdapter(getSupportFragmentManager(), mPersonaliseFragments, topic, city, author);
                            viewPager.setAdapter(personaliseAdapter);

                            topic = topic.substring(0,1).toUpperCase() + topic.substring(1).toLowerCase();

                            tv_items.setText(topic);

                            //Set the already selected values

                            for(String selectedValue : topicsAlreadySelected) {
                                PersonaliseItem item = new PersonaliseItem(selectedValue, selectedValue);
                                topics.add(item);
                            }

                            for(String selectedValue : citiesAlreadySelected) {
                                PersonaliseItem item = new PersonaliseItem(selectedValue, selectedValue);
                                cities.add(item);
                            }

                            for(String selectedValue : authorsAlreadySelected) {
                                PersonaliseItem item = new PersonaliseItem(selectedValue, selectedValue);
                                authors.add(item);
                            }

                            //View Save button
                            savePersonaliseBtn_Txt.setVisibility(View.VISIBLE);
                            /*topics.addAll(topicsAlreadySelected);
                            cities.addAll(citiesAlreadySelected);
                            authors.addAll(authorsAlreadySelected);*/

                        }, throwable -> {
                            /*if (throwable instanceof HttpException || throwable instanceof ConnectException
                                    || throwable instanceof SocketTimeoutException || throwable instanceof TimeoutException) {
                                Alerts.showErrorDailog(getSupportFragmentManager(), getResources().getString(R.string.kindly), getResources().getString(R.string.please_check_ur_connectivity));
                            }*/

                            savePersonaliseBtn_Txt.setVisibility(View.VISIBLE);
                            savePersonaliseBtn_Txt.setText(refreshText);
                            mProgressBar.setVisibility(View.GONE);
                            mErrorText.setVisibility(View.VISIBLE);
                            mErrorText.setText(R.string.please_check_ur_connectivity);
                            Log.i("Ashwani", "111"+throwable.getLocalizedMessage());

                        }, () -> {
                            mProgressBar.setVisibility(View.GONE);
                            //mErrorText.setVisibility(View.VISIBLE);
                        })
        );
    }



    private void smoothPagerScroll() {
        try {
            Field mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            mScroller.set(viewPager, new ViewPagerScroller(THPPersonaliseActivity.this,
                    new LinearInterpolator(), 250));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDisposable.dispose();
        mDisposable.clear();
    }

    @Override
    public void personaliseItemClick(PersonaliseModel model, String from) {
        if (from != null) {

            String value = model.getValue();
            String title = model.getTitle();
            PersonaliseItem item = new PersonaliseItem(title, value);

            if (from.equalsIgnoreCase("Topics")) {
                if (topics.contains(item)) {
                    topics.remove(item);
                } else {
                    topics.add(item);
                }
            } else if (from.equalsIgnoreCase("Cities")) {
                if (cities.contains(item)) {
                    cities.remove(item);
                } else {
                    cities.add(item);
                }
            } else if (from.equalsIgnoreCase("Authors")) {
                if (authors.contains(item)) {
                    authors.remove(item);
                } else {
                    authors.add(item);
                }
            }
        }

        Log.i("", ""+topics);
        Log.i("", ""+cities);
        Log.i("", ""+authors);
    }

    /**
     * Loads User Profile Data from local Database
     */
    private void saveUserPersonalise() {
        tv_savepref.setEnabled(false);
        final ProgressDialog progress = Alerts.showProgressDialog(THPPersonaliseActivity.this);
        mDisposable.add(ApiManager.getUserProfile(this)
                .map(userProfile -> {
                    if (userProfile == null) {
                        return "";
                    }

                    ArrayList<String> topicsL = new ArrayList<>();
                    ArrayList<String> citiesL = new ArrayList<>();
                    ArrayList<String> authorsL = new ArrayList<>();

                    for(PersonaliseItem item : topics) {
                        topicsL.add(item.getValue());
                    }

                    for(PersonaliseItem item : cities) {
                        citiesL.add(item.getValue());
                    }

                    for(PersonaliseItem item : authors) {
                        authorsL.add(item.getValue());
                    }


                    //CleverTap
                    if (viewPager.getCurrentItem() == 0) { //Topic
                        HashMap<String,Object> map = new HashMap<>();
                        map.put(THPConstants.CT_KEY_Topics, TextUtils.join(", ", topicsL));
                        CleverTapUtil.cleverTapEvent(this,THPConstants.CT_EVENT_SUBSCRIPTION_PERSONALIZE,map);
                    }else if (viewPager.getCurrentItem() == 1) { //Cities
                        HashMap<String,Object> map = new HashMap<>();
                        map.put(THPConstants.CT_KEY_Cities, TextUtils.join(", ", citiesL));
                        CleverTapUtil.cleverTapEvent(this,THPConstants.CT_EVENT_SUBSCRIPTION_PERSONALIZE,map);
                    }else if (viewPager.getCurrentItem() == 2) { //Authors
                        HashMap<String,Object> map = new HashMap<>();
                        map.put(THPConstants.CT_KEY_Authors, TextUtils.join(", ", authorsL));
                        CleverTapUtil.cleverTapEvent(this,THPConstants.CT_EVENT_SUBSCRIPTION_PERSONALIZE,map);
                    }

                    // Now we are sending Personalise data to server
                    ApiManager.setPersonalise(userProfile.getAuthorization(), userProfile.getUserId(), BuildConfig.SITEID, ResUtil.getDeviceId(THPPersonaliseActivity.this),
                            topicsL, citiesL, authorsL)
                            .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(keyValueModel->{
                        if(keyValueModel.getState() != null && keyValueModel.getState().equalsIgnoreCase(NetConstants.SUCCESS)) {
                            if (viewPager.getCurrentItem() == 0) {
                                Alerts.showToast(THPPersonaliseActivity.this, "Topics preference set successfully");
                                viewPager.setCurrentItem(1);
                            } else if (viewPager.getCurrentItem() == 1) {
                                Alerts.showToast(THPPersonaliseActivity.this, "Cities preference set successfully");
                                viewPager.setCurrentItem(2);
                            } else if (viewPager.getCurrentItem() == 2) {
                                Alerts.showToast(THPPersonaliseActivity.this, "Author preference set successfully");
                                if(mFrom != null && mFrom.equalsIgnoreCase(THPConstants.FROM_DASHBOARD_SET_PREFERENCE)) {
                                    // TODO, currently nothing to do.
                                }
                                else if(userProfile.isHasSubscribedPlan() || userProfile.isHasFreePlan()) {
                                    IntentUtil.openContentListingActivity(THPPersonaliseActivity.this, NetConstants.PS_My_Stories);
                                }
                                else if(!userProfile.isHasSubscribedPlan() && !userProfile.isHasFreePlan()) {
                                    IntentUtil.openSubscriptionActivity(THPPersonaliseActivity.this, THPConstants.FROM_SUBSCRIPTION_EXPLORE);
                                }
                                finish();
                            }
                        }
                        else {
                            Alerts.showAlertDialogOKBtn(THPPersonaliseActivity.this, "Sorry!", keyValueModel.getName());
                        }
                    }, throwable -> {
                        if(tv_savepref != null) {
                            Alerts.showSnackbar(this, getResources().getString(R.string.something_went_wrong));
                            tv_savepref.setEnabled(true);
                            progress.dismiss();
                        }
                    }, ()->{
                        tv_savepref.setEnabled(true);
                        progress.dismiss();
                    })
                    ;

                    return "";
                })
                .subscribe(v -> {
                        },
                        t ->
                                Log.i("", "" + t)
                ));
    }


    private String getPrefeItemTrimValue(String value) {
        return value.trim();
    }

    @Override
    protected void onResume() {
        super.onResume();
        THPFirebaseAnalytics.setFirbaseAnalyticsScreenRecord(this, "THPPersonaliseActivity Screen", THPPersonaliseActivity.class.getSimpleName());
    }
}
