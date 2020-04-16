package com.ns.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.ns.thd_frag.CitiesInterestFragment;
import com.ns.thd_frag.CustomizeNewsFeedFragment;
import com.ns.thpremium.R;
import com.ns.view.CustomViewPager;


public class CustomizeHomeScreenActivity extends AppCompatActivity {

    private CustomViewPager mCustomizeHomeScreenViewPager;
    private Button mSkipButton;
    private Button mNextButton;
    private CustomizePagerAdapter mCustomizePagerAdapter;
    private final int NUMBER_OF_SCREENS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customize_home_screen);

        Toolbar mToolbar = findViewById(R.id.toolbar_customize_back_button);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.custom_home_screen));
        mCustomizeHomeScreenViewPager = findViewById(R.id.viewpager_customimze_homeScreen);
        mCustomizeHomeScreenViewPager.setPagingEnabled(false);
        mCustomizePagerAdapter = new CustomizePagerAdapter(getSupportFragmentManager());
        mCustomizeHomeScreenViewPager.setAdapter(mCustomizePagerAdapter);
        mNextButton = findViewById(R.id.button_feed_save);
        mSkipButton = findViewById(R.id.button_feed_skip);
        mCustomizeHomeScreenViewPager.beginFakeDrag();
        mNextButton.setTypeface(Typeface.createFromAsset(getAssets(), getResources().getString(R.string.THP_FiraSans_Regular)));
        mSkipButton.setTypeface(Typeface.createFromAsset(getAssets(), getResources().getString(R.string.THP_FiraSans_Regular)));
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = mCustomizeHomeScreenViewPager.getCurrentItem();
                switch (position) {
                    case 0:
                        Fragment fragment = getCurrentFragmet();
                        CustomizeNewsFeedFragment mCustomizeNewsFeedFragment = null;
                        if (fragment instanceof CustomizeNewsFeedFragment) {
                            mCustomizeNewsFeedFragment = (CustomizeNewsFeedFragment) fragment;
                            mCustomizeNewsFeedFragment.nextButtonClicked();
                        }
                        break;
                    case 1:
                        Fragment fragment1 = getCurrentFragmet();
                        CitiesInterestFragment citiesInterestFragment = null;
                        if (fragment1 instanceof CitiesInterestFragment) {
                            citiesInterestFragment = (CitiesInterestFragment) fragment1;
                        }
                        if (citiesInterestFragment != null) {
                            citiesInterestFragment.saveButtonClicked();
                        }
                        break;
                }
            }
        });

        mSkipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCustomizeHomeScreenViewPager != null) {
                    mCustomizeHomeScreenViewPager.setCurrentItem(0, true);
                }
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
                    getSupportActionBar().setTitle(getString(R.string.custom_local_screen));
                } else {
                    getSupportActionBar().setTitle(getString(R.string.custom_home_screen));
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                /*GoogleAnalyticsTracker.setGoogleAnalyticsEvent(
                        CustomizeHomeScreenActivity.this,
                        getString(R.string.ga_action),
                        "Customize news feed: Back button clicked",
                        getString(R.string.custom_home_screen));
                FlurryAgent.logEvent("Customize news feed: Back button clicked");*/
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setNextButtonText(String nextButtonText) {
        if (mNextButton != null) {
            mNextButton.setText(nextButtonText);
        }
    }

    public void setSkipButtonText(String skipButtonText) {
        if (mSkipButton != null) {
            mSkipButton.setText(skipButtonText);
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

    public void setVisiblityOfPriviousButton(int visiblity) {
        mSkipButton.setVisibility(visiblity);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //AppFirebaseAnalytics.setFirbaseAnalyticsScreenRecord(CustomizeHomeScreenActivity.this, "CustomizeHomeScreenActivity Screen", CustomizeHomeScreenActivity.class.getSimpleName());
    }
}
