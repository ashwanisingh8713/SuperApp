package com.ns.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.main.DFPConsent;
import com.netoperation.db.THPDB;
import com.netoperation.default_db.DaoWidget;
import com.netoperation.default_db.TableWidget;
import com.netoperation.net.DefaultTHApiManager;
import com.netoperation.net.RequestCallback;
import com.netoperation.util.DefaultPref;
import com.ns.alerts.Alerts;
import com.ns.thd_fragment.CitiesInterestFragment;
import com.ns.thd_fragment.CustomizeNewsFeedFragment;
import com.ns.thpremium.BuildConfig;
import com.ns.thpremium.R;
import com.ns.utils.IntentUtil;
import com.ns.utils.THPFirebaseAnalytics;
import com.ns.view.CustomProgressBar;
import com.ns.view.CustomViewPager;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.schedulers.Schedulers;


public class CustomizeHomeScreenActivity extends BaseAcitivityTHP {

    private CustomViewPager mCustomizeHomeScreenViewPager;
    private Button mSkipButton;
    private Button mNextButton;
    private CustomProgressBar mProgressBar;
    private CustomizePagerAdapter mCustomizePagerAdapter;
    private int NUMBER_OF_SCREENS = 1;

    private boolean isHomeArticleOptionScreenShown;

    private final String btn_previous = "PREVIOUS";
    private final String btn_save = "SAVE";
    private final String btn_done = "DONE";
    private final String btn_skip = "SKIP";
    private final String btn_next = "NEXT";

    private DFPConsent dfpConsent;

    private boolean isOptionsChanged = false;

    public void setIsOptionsChanged(boolean isOptionsChanged) {
        this.isOptionsChanged = isOptionsChanged;
    }

    public boolean isOptionsChanged() {
        return isOptionsChanged;
    }

    @Override
    public int layoutRes() {
        return R.layout.activity_customize_home_screen;
    }

    public void skipOrPreviousButtonFunctionality() {
        if (mCustomizeHomeScreenViewPager != null) {

            if (isHomeArticleOptionScreenShown || mSkipButton.getText().toString().equalsIgnoreCase(btn_previous)) {
                mCustomizeHomeScreenViewPager.setCurrentItem(0, true);
                return;
            }

            boolean isUserSelectedDfpConsent = DefaultPref.getInstance(CustomizeHomeScreenActivity.this).isUserSelectedDfpConsent();
            boolean isDfpConsentExecuted = DefaultPref.getInstance(CustomizeHomeScreenActivity.this).isDfpConsentExecuted();
            boolean isUserFromEurope = DefaultPref.getInstance(CustomizeHomeScreenActivity.this).isUserFromEurope();

            if (!isUserFromEurope && isDfpConsentExecuted) {
                getHomeDataFromServer();
            } else if ((isUserFromEurope && isDfpConsentExecuted) && !isUserSelectedDfpConsent) {
                DFP_GDPR_CONSENT(true);
                Alerts.showToast(CustomizeHomeScreenActivity.this, "Please complete user consent.");
            } else if ((isUserFromEurope && isDfpConsentExecuted) && isUserSelectedDfpConsent) {
                getHomeDataFromServer();
            }
        }
    }

    private void saveODoneButtonFunctionality() {
        boolean isUserSelectedDfpConsent = DefaultPref.getInstance(CustomizeHomeScreenActivity.this).isUserSelectedDfpConsent();
        boolean isDfpConsentExecuted = DefaultPref.getInstance(CustomizeHomeScreenActivity.this).isDfpConsentExecuted();
        boolean isUserFromEurope = DefaultPref.getInstance(CustomizeHomeScreenActivity.this).isUserFromEurope();

        if (!isUserFromEurope && isDfpConsentExecuted) {
            Fragment fragment1 = getCurrentFragmet();
            CitiesInterestFragment citiesInterestFragment = null;
            if (fragment1 instanceof CitiesInterestFragment) {
                citiesInterestFragment = (CitiesInterestFragment) fragment1;
            }
            if (citiesInterestFragment != null) {
                citiesInterestFragment.saveButtonClicked();
            }
        } else if ((isUserFromEurope && isDfpConsentExecuted) && !isUserSelectedDfpConsent) {
            DFP_GDPR_CONSENT(isHomeArticleOptionScreenShown);
            Alerts.showToast(CustomizeHomeScreenActivity.this, "Please complete user consent.");
        } else if ((isUserFromEurope && isDfpConsentExecuted) && isUserSelectedDfpConsent) {
            Fragment fragment1 = getCurrentFragmet();
            CitiesInterestFragment citiesInterestFragment = null;
            if (fragment1 instanceof CitiesInterestFragment) {
                citiesInterestFragment = (CitiesInterestFragment) fragment1;
            }
            if (citiesInterestFragment != null) {
                citiesInterestFragment.saveButtonClicked();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (BuildConfig.IS_BL) {
            NUMBER_OF_SCREENS = 1;
        } else {
            NUMBER_OF_SCREENS = 2;
        }

        isHomeArticleOptionScreenShown = DefaultPref.getInstance(this).isHomeArticleOptionScreenShown();

        if (isHomeArticleOptionScreenShown) {
            getDetailToolbar().showHomePeronsoliseIcons(getString(R.string.custom_home_screen), backBtn -> {
                finish();
            });

        } else {
            DFP_GDPR_CONSENT(isHomeArticleOptionScreenShown);
            getDetailToolbar().setVisibility(View.GONE);

            // Get Widget Data from server, because in SplashActivity we are not loading, If we need to show OnBoarding option screen.
            final THPDB thpdb = THPDB.getInstance(CustomizeHomeScreenActivity.this);
            // Get Widget Ids from server
            DaoWidget daoWidget = thpdb.daoWidget();
            daoWidget.getWidgetsSingle()
                    .subscribeOn(Schedulers.io())
                    .map(widgets -> {
                        final Map<String, String> sections = new HashMap<>();
                        for (TableWidget widget : widgets) {
                            sections.put(widget.getSecId(), widget.getType());
                        }
                        // Get Widget Data From server
                        DefaultTHApiManager.widgetContent(CustomizeHomeScreenActivity.this, sections);
                        return "";
                    })
                    .subscribe(onSuccess -> {
                        //Metered Paywall Configs API calls.
                        DefaultTHApiManager.mpConfigurationAPI(CustomizeHomeScreenActivity.this, BuildConfig.MP_CYCLE_CONFIGURATION_API_URL);
                    }, throwable -> {

                    });
        }

        mProgressBar = findViewById(R.id.progress_bar);
        mCustomizeHomeScreenViewPager = findViewById(R.id.viewpager_customimze_homeScreen);
        mCustomizeHomeScreenViewPager.setPagingEnabled(false);
        mCustomizePagerAdapter = new CustomizePagerAdapter(getSupportFragmentManager());
        mCustomizeHomeScreenViewPager.setAdapter(mCustomizePagerAdapter);
        mNextButton = findViewById(R.id.button_feed_save);
        mSkipButton = findViewById(R.id.button_feed_skip);
        mCustomizeHomeScreenViewPager.beginFakeDrag();
        mNextButton.setTypeface(Typeface.createFromAsset(getAssets(), getResources().getString(R.string.THP_FiraSans_Regular)));
        mSkipButton.setTypeface(Typeface.createFromAsset(getAssets(), getResources().getString(R.string.THP_FiraSans_Regular)));

        // Next, Save or Done Button
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = mCustomizeHomeScreenViewPager.getCurrentItem();
                switch (position) {
                    case 0: //
                        // If two pages then, "Next" Title Button Click
                        // If single pages then, "Done" Title Button Click
                        Fragment fragment = getCurrentFragmet();
                        CustomizeNewsFeedFragment mCustomizeNewsFeedFragment = null;
                        if (fragment instanceof CustomizeNewsFeedFragment) {
                            mCustomizeNewsFeedFragment = (CustomizeNewsFeedFragment) fragment;
                            mCustomizeNewsFeedFragment.nextButtonClicked();
                        }
                        break;
                    case 1:
                        // // "Save or Done" Title Button Click
                        saveODoneButtonFunctionality();
                        break;
                }
            }
        });

        // Skip Btton
        mSkipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skipOrPreviousButtonFunctionality();

            }
        });
        /**
         * addOnPageChangeListener to save the selected section while user is swiping
         */
        mCustomizeHomeScreenViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //when the swipe id completed
                if (position == 1) {
                  /*  // Add Google analytics anf flurry
                    GoogleAnalyticsTracker.setGoogleAnalyticsEvent(
                            CustomizeHomeScreenActivity.this,
                            getString(R.string.ga_action),
                            "Customize news feed: Save button clicked",
                            getString(R.string.customize_news_feed_menu));
                    FlurryAgent.logEvent("Customize news feed: Save button clicked");
                    */

                    //Firebase Analytics event
                    THPFirebaseAnalytics.setFirbaseAnalyticsEvent(CustomizeHomeScreenActivity.this, "Action", "Customize news feed: Back button clicked", CustomizeHomeScreenActivity.class.getSimpleName());
                    getDetailToolbar().setTitle(getString(R.string.custom_local_screen));
                } else {
                    getDetailToolbar().setTitle(getString(R.string.custom_home_screen));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }


    /**
     * ViewPager Adapter to load the two fragments which is a part of onBoardingscreen
     * {@link CustomizeNewsFeedFragment} -- list of sections(widgets+topnews are removed)
     * {@link CitiesInterestFragment} -- list of cities and states     *
     */
    public class CustomizePagerAdapter extends FragmentStatePagerAdapter {
        private SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

        public CustomizePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return CustomizeNewsFeedFragment.newInstance(false);
                case 1:
                    return CitiesInterestFragment.newInstance(true);
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return NUMBER_OF_SCREENS;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            registeredFragments.put(position, fragment);
            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            registeredFragments.remove(position);
            super.destroyItem(container, position, object);
        }


        public Fragment getRegisteredFragment(int position) {
            return registeredFragments.get(position);
        }
    }


    private Fragment getCurrentFragmet() {
        int position = mCustomizeHomeScreenViewPager.getCurrentItem();
        if (mCustomizePagerAdapter != null) {
            return mCustomizePagerAdapter.getRegisteredFragment(position);
        } else {
            return null;
        }
    }

    public void setViewPagerItem(int position) {
        if (mCustomizeHomeScreenViewPager != null) {
            mCustomizeHomeScreenViewPager.setCurrentItem(position, true);
        }
    }

    private void setVisiblityOfPriviousButton(int visiblity) {
        mSkipButton.setVisibility(visiblity);
    }

    private void setNextButtonText(String nextButtonText) {
        if (mNextButton != null) {
            mNextButton.setText(nextButtonText);
        }
    }

    private void setSkipButtonText(String skipButtonText) {
        if (mSkipButton != null) {
            mSkipButton.setText(skipButtonText);
        }
    }

    public void secondFragmentBtn() {
        if (isHomeArticleOptionScreenShown) {
            setSkipButtonText(btn_previous);
            setNextButtonText(btn_save);
            setVisiblityOfPriviousButton(View.VISIBLE);
        } else {
            setSkipButtonText(btn_previous);
            setNextButtonText(btn_done);
            setVisiblityOfPriviousButton(View.VISIBLE);
        }
    }

    public void firstFragmentBtn() {

        if (BuildConfig.IS_BL) {
            setSkipButtonText(btn_skip);
            setNextButtonText(btn_done);
            setVisiblityOfPriviousButton(View.VISIBLE);
        } else {
            if (isHomeArticleOptionScreenShown) {
                setNextButtonText(btn_next);
                setVisiblityOfPriviousButton(View.GONE);
            } else {
                setSkipButtonText(btn_skip);
                setNextButtonText(btn_next);
                setVisiblityOfPriviousButton(View.VISIBLE);
            }
        }
    }

    private void enableAllBtns(boolean isEnable) {
        mNextButton.setEnabled(isEnable);
        mSkipButton.setEnabled(isEnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        THPFirebaseAnalytics.setFirbaseAnalyticsScreenRecord(CustomizeHomeScreenActivity.this, "CustomizeHomeScreenActivity Screen", CustomizeHomeScreenActivity.class.getSimpleName());
    }


    private void DFP_GDPR_CONSENT(boolean isHomeArticleOptionScreenShown) {
        if (!isHomeArticleOptionScreenShown) {
            if (dfpConsent != null) {
                dfpConsent.initUserConsentForm(CustomizeHomeScreenActivity.this);
                return;
            }
            dfpConsent = new DFPConsent();
            dfpConsent.GDPR_Testing(this);
            dfpConsent.init(this, true, new DFPConsent.ConsentSelectionListener() {
                @Override
                public void isUserInEurope(boolean isInEurope) {
                    if (isInEurope) {
                        dfpConsent.initUserConsentForm(CustomizeHomeScreenActivity.this);
                    }
                }

                @Override
                public void consentLoadingError(String errorDescription) {
                    Alerts.showToast(CustomizeHomeScreenActivity.this, "consentLoadingError :: " + errorDescription);
                }
            });
        }
    }


    public void getHomeDataFromServer() {
        enableAllBtns(false);
        mProgressBar.setVisibility(View.VISIBLE);
        final long startTime = System.currentTimeMillis();
        Log.i("NSPEED", "Home Article APIs are called ");
        DefaultTHApiManager.homeArticles(this, "SplashActivity", new RequestCallback() {
            @Override
            public void onNext(Object o) {
                DefaultPref.getInstance(CustomizeHomeScreenActivity.this).setHomeArticleOptionScreenShown(true);
                long totalExecutionTime = System.currentTimeMillis() - startTime;
                Log.i("NSPEED", "Home Article Loaded from server, Launched Main Tab Page :: " + totalExecutionTime);
            }

            @Override
            public void onError(Throwable t, String str) {
                long totalExecutionTime = System.currentTimeMillis() - startTime;
                Log.i("NSPEED", "ERROR2 Occur time :: " + totalExecutionTime);

                if (t.getMessage() != null && t.getMessage().contains("mapper function returned a null")) {
                    DefaultTHApiManager.sectionDirectFromServer(CustomizeHomeScreenActivity.this, new RequestCallback() {
                                @Override
                                public void onNext(Object o) {

                                }

                                @Override
                                public void onError(Throwable t, String str) {
                                    if (mProgressBar != null) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                mProgressBar.setVisibility(View.GONE);
                                                Alerts.noConnectionSnackBar(mProgressBar, CustomizeHomeScreenActivity.this);
                                                enableAllBtns(true);
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onComplete(String str) {
                                    getHomeDataFromServer();
                                }
                            }, System.currentTimeMillis());
                } else {
                    if (mProgressBar != null) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mProgressBar.setVisibility(View.GONE);
                                Alerts.noConnectionSnackBar(mProgressBar, CustomizeHomeScreenActivity.this);
                                enableAllBtns(true);
                            }
                        });
                    }
                }
            }

            @Override
            public void onComplete(String str) {
                // Opens Main Tab Screen
                IntentUtil.openMainTabPage(CustomizeHomeScreenActivity.this);


            }
        });
    }

    @Override
    public void onBackPressed() {
        if (isHomeArticleOptionScreenShown) {

        }
        super.onBackPressed();
    }


}
