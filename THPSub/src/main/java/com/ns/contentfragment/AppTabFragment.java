package com.ns.contentfragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GestureDetectorCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.main.DFPAds;
import com.netoperation.config.model.TabsBean;
import com.netoperation.default_db.TableConfiguration;
import com.netoperation.model.AdData;
import com.netoperation.model.TxnDataBean;
import com.netoperation.net.ApiManager;
import com.netoperation.net.DefaultTHApiManager;
import com.netoperation.util.NetConstants;
import com.netoperation.util.PremiumPref;
import com.netoperation.util.DefaultPref;
import com.ns.activity.BaseAcitivityTHP;
import com.ns.adapter.AppBottomTabAdapter;
import com.ns.callbacks.OnSubscribeBtnClick;
import com.ns.callbacks.TabClickListener;
import com.ns.clevertap.AppNotification;
import com.ns.loginfragment.BaseFragmentTHP;
import com.ns.thpremium.BuildConfig;
import com.ns.thpremium.R;
import com.ns.utils.IntentUtil;
import com.ns.utils.THPConstants;
import com.ns.utils.THPFirebaseAnalytics;
import com.ns.utils.TabUtils;
import com.ns.view.ViewPagerScroller;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class AppTabFragment extends BaseFragmentTHP implements OnSubscribeBtnClick, TabClickListener.OnTabClickListener {

    private ConstraintLayout subscribeLayout;
    private TabUtils mTabUtils;

    public static AppTabFragment getInstance() {
        AppTabFragment fragment = new AppTabFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }


    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private AppBottomTabAdapter mAppBottomTabAdapter;



    /**
     * Holds String value of User Name, to know whether user has logged in or not
     */
    protected String mUserLoggedName;

    private boolean mIsUserThemeDay;

    private String[] tabNames = {"Home", THPConstants.TAB_1, THPConstants.TAB_2, THPConstants.TAB_3, "Profile"};
    private int[] tabUnSelectedIcons = {
            com.ns.thpremium.R.drawable.ic_thp_tab_home_unselected,
            com.ns.thpremium.R.drawable.ic_tab_briefcase_unselected,
            com.ns.thpremium.R.drawable.ic_tab_dashboard_unselected,
            com.ns.thpremium.R.drawable.ic_tab_suggested_unselected,
            com.ns.thpremium.R.drawable.ic_thp_tab_profile_unselected
    };
    private int[] tabSelectedIcons = {
            com.ns.thpremium.R.drawable.ic_thp_tab_home_selected,
            com.ns.thpremium.R.drawable.ic_tab_briefcase_selected,
            com.ns.thpremium.R.drawable.ic_tab_dashboard_selected,
            com.ns.thpremium.R.drawable.ic_tab_suggested_selected,
            com.ns.thpremium.R.drawable.ic_thp_tab_profile_selected
    };

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_apptab;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
        mIsUserThemeDay = DefaultPref.getInstance(getActivity()).isUserThemeDay();
    }

    private Map<String, Integer> mPsTabIndexMap = new HashMap<>();

    public void showPageSource(String pageSource) {
        if(mPsTabIndexMap.containsKey(pageSource)) {
            int pageSourceIndex = mPsTabIndexMap.get(pageSource);
            mViewPager.setCurrentItem(pageSourceIndex);
        }
    }

    public void changeTab(int tabIndex) {
        mViewPager.setCurrentItem(tabIndex);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        subscribeLayout = view.findViewById(R.id.subscribeLayout);
        mTabLayout = view.findViewById(R.id.appTabsTabLayout);
        mViewPager = view.findViewById(R.id.appTabsViewPager);

        //Developer Options Button, display only if it is Debug build
        if (BuildConfig.DEBUG) {
            Button buttonDeveloperOptions = view.findViewById(R.id.buttonDeveloperOptions);
            buttonDeveloperOptions.setVisibility(View.VISIBLE);
            buttonDeveloperOptions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppNotification.customTestNotification(view.getContext(), true, false, false);
                }
            });
        }


        // This is smooth scroll of ViewPager
        smoothPagerScroll();
        mViewPager.setOffscreenPageLimit(4);
        // App Tab Configuration
        mDisposable.add(DefaultTHApiManager.appConfigurationTabs(getActivity(), mIsUserThemeDay)
                .subscribe(tabsBeans -> {
                    String[] tabNames = new String[tabsBeans.size()];
                    String[] tabUnSelectedIcon = new String[tabsBeans.size()];
                    String[] tabSelectedIcon = new String[tabsBeans.size()];

                    for (int i = 0; i < tabsBeans.size(); i++) {
                        TabsBean tab = tabsBeans.get(i);
                        tabNames[i] = tab.getTitle();
                        tabUnSelectedIcon[i] = tab.getIconUrl().getLocalFilePath();
                        tabSelectedIcon[i] = tab.getIconUrl().getLocalFileSelectedPath();
                        mPsTabIndexMap.put(tab.getPageSource(), i);
                    }

                    mTabUtils = new TabUtils(tabNames, tabSelectedIcons, tabUnSelectedIcons, mIsUserThemeDay, tabsBeans);

                    mAppBottomTabAdapter = new AppBottomTabAdapter(getChildFragmentManager(), tabsBeans);

                    mViewPager.setAdapter(mAppBottomTabAdapter);

                    mTabLayout.setupWithViewPager(mViewPager, true);

                    // Iterate over all tabs and set the custom view
                    for (int i = 0; i < mTabLayout.getTabCount(); i++) {
                        TabLayout.Tab tab = mTabLayout.getTabAt(i);
                        tab.setCustomView(mTabUtils.getTabView(i, getActivity()));
                    }

                    // To select default tab
                    mTabUtils.SetOnSelectView(getActivity(), mTabLayout, 0);

                    // Tabs custom click handling
                    tabClickHandling(tabsBeans);

                }, throwable -> {
                    Log.i("", "");
                }));


        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                mTabUtils.SetOnSelectView(getActivity(), mTabLayout, pos);
                mViewPager.setCurrentItem(pos);

                THPFirebaseAnalytics.setFirbaseAnalyticsScreenRecord(getActivity(), tab.getText() + " Screen", AppTabFragment.class.getSimpleName());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                mTabUtils.SetUnSelectView(getActivity(), mTabLayout, pos);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        // Subscribe Button Click Listener
        view.findViewById(R.id.subscribeBtn_Txt).setOnClickListener(v -> {
            THPConstants.FLOW_TAB_CLICK = THPConstants.TAP_BANNER_SUBSCRIPTION;
            if (BaseAcitivityTHP.sIsOnline) {
                IntentUtil.openSubscriptionActivity(getActivity(), THPConstants.FROM_SUBSCRIPTION_EXPLORE);
            } else {
                noConnectionSnackBar(getView());
            }
        });
        subscribeLayout.setOnClickListener(v -> {
            if (!BaseAcitivityTHP.sIsOnline) {
                noConnectionSnackBar(getView());
                return;
            }
            THPConstants.FLOW_TAB_CLICK = THPConstants.TAP_BANNER_SUBSCRIPTION;
            IntentUtil.openSubscriptionActivity(getActivity(), THPConstants.FROM_SUBSCRIPTION_EXPLORE);
        });

        view.findViewById(R.id.subsCloseImg).setOnClickListener(v -> {
            PremiumPref.getInstance(getActivity()).setIsSubscribeClose(true);
            subscribeLayoutVisibility(View.GONE);
        });

    }

    @Override
    public void onTabClick(int tabIndex, TabsBean tabsBean) {
        boolean isUserLoggedIn = PremiumPref.getInstance(getActivity()).isUserLoggedIn();
        boolean isUserAdsFree = PremiumPref.getInstance(getActivity()).isUserAdsFree();
        boolean isHasSubscription = PremiumPref.getInstance(getActivity()).isHasSubscription();

        THPConstants.FLOW_TAB_CLICK = tabsBean.getPageSource();

        if(tabsBean.getPageSource().equals(NetConstants.PS_GROUP_DEFAULT_SECTIONS) && tabsBean.getGroup().equals(NetConstants.G_DEFAULT_SECTIONS)) {
            mViewPager.setCurrentItem(tabIndex);
            return;
        }

        if(tabsBean.getPageSource().equals(NetConstants.PS_GROUP_DEFAULT_SECTIONS) && tabsBean.getGroup().equals(NetConstants.G_PREMIUM_SECTIONS)) {
            if(isUserLoggedIn) {
                mViewPager.setCurrentItem(tabIndex);
            } else {
                IntentUtil.openMemberActivity(getActivity(), "");
            }
            return;
        }

        if(tabsBean.getPageSource().equals(NetConstants.PS_Briefing) && tabsBean.getGroup().equals(NetConstants.G_DEFAULT_SECTIONS)) {
            mViewPager.setCurrentItem(tabIndex);
            return;
        }

        if(tabsBean.getPageSource().equals(NetConstants.PS_Briefing) && tabsBean.getGroup().equals(NetConstants.G_PREMIUM_SECTIONS)) {
            if(isUserLoggedIn) {
                if(!isUserAdsFree && !isHasSubscription) {
                    if (BaseAcitivityTHP.sIsOnline) {
                        IntentUtil.openSubscriptionActivity(getActivity(), THPConstants.FROM_SUBSCRIPTION_EXPLORE);
                    } else {
                        noConnectionSnackBar(getView());
                    }
                } else {
                    mViewPager.setCurrentItem(tabIndex);
                }
            } else {
                IntentUtil.openMemberActivity(getActivity(), "");
            }
            return;
        }

        if(tabsBean.getPageSource().equals(NetConstants.PS_My_Stories)) {
            if(isUserLoggedIn) {
                if(!isUserAdsFree && !isHasSubscription) {
                    if (BaseAcitivityTHP.sIsOnline) {
                        IntentUtil.openSubscriptionActivity(getActivity(), THPConstants.FROM_SUBSCRIPTION_EXPLORE);
                    } else {
                        noConnectionSnackBar(getView());
                    }
                } else {
                    mViewPager.setCurrentItem(tabIndex);
                }
            } else {
                IntentUtil.openMemberActivity(getActivity(), "");
            }
            return;
        }

        if(tabsBean.getPageSource().equals(NetConstants.PS_Suggested)) {
            if(isUserLoggedIn) {
                if(!isUserAdsFree && !isHasSubscription) {
                    if (BaseAcitivityTHP.sIsOnline) {
                        IntentUtil.openSubscriptionActivity(getActivity(), THPConstants.FROM_SUBSCRIPTION_EXPLORE);
                    } else {
                        noConnectionSnackBar(getView());
                    }
                } else {
                    mViewPager.setCurrentItem(tabIndex);
                }
            } else {
                IntentUtil.openMemberActivity(getActivity(), "");
            }
            return;
        }

        // URL
        if(tabsBean.getPageSource().equals(NetConstants.PS_Url) && tabsBean.getGroup().equals(NetConstants.G_DEFAULT_SECTIONS)) {
            mViewPager.setCurrentItem(tabIndex);
            return;
        }

        if(tabsBean.getPageSource().equals(NetConstants.PS_Url) && tabsBean.getGroup().equals(NetConstants.G_PREMIUM_SECTIONS)) {
            if(isUserLoggedIn) {
                if(!isUserAdsFree && !isHasSubscription) {
                    if (BaseAcitivityTHP.sIsOnline) {
                        IntentUtil.openSubscriptionActivity(getActivity(), THPConstants.FROM_SUBSCRIPTION_EXPLORE);
                    } else {
                        noConnectionSnackBar(getView());
                    }
                } else {
                    mViewPager.setCurrentItem(tabIndex);
                }
            } else {
                IntentUtil.openMemberActivity(getActivity(), "");
            }
            return;
        }


        // ADD_ON_SECTION
        if(tabsBean.getPageSource().equals(NetConstants.PS_ADD_ON_SECTION) && tabsBean.getGroup().equals(NetConstants.G_DEFAULT_SECTIONS)) {
            mViewPager.setCurrentItem(tabIndex);
            return;
        }

        if(tabsBean.getPageSource().equals(NetConstants.PS_ADD_ON_SECTION) && tabsBean.getGroup().equals(NetConstants.G_PREMIUM_SECTIONS)) {
            if(isUserLoggedIn) {
                if(!isUserAdsFree && !isHasSubscription) {
                    if (BaseAcitivityTHP.sIsOnline) {
                        IntentUtil.openSubscriptionActivity(getActivity(), THPConstants.FROM_SUBSCRIPTION_EXPLORE);
                    } else {
                        noConnectionSnackBar(getView());
                    }
                } else {
                    mViewPager.setCurrentItem(tabIndex);
                }
            } else {
                IntentUtil.openMemberActivity(getActivity(), "");
            }
            return;
        }


        // SENSEX
        if(tabsBean.getPageSource().equals(NetConstants.PS_SENSEX) && tabsBean.getGroup().equals(NetConstants.G_DEFAULT_SECTIONS)) {
            mViewPager.setCurrentItem(tabIndex);
            return;
        }

        if(tabsBean.getPageSource().equals(NetConstants.PS_SENSEX) && tabsBean.getGroup().equals(NetConstants.G_PREMIUM_SECTIONS)) {
            if(isUserLoggedIn) {
                if(!isUserAdsFree && !isHasSubscription) {
                    if (BaseAcitivityTHP.sIsOnline) {
                        IntentUtil.openSubscriptionActivity(getActivity(), THPConstants.FROM_SUBSCRIPTION_EXPLORE);
                    } else {
                        noConnectionSnackBar(getView());
                    }
                } else {
                    mViewPager.setCurrentItem(tabIndex);
                }
            } else {
                IntentUtil.openMemberActivity(getActivity(), "");
            }
            return;
        }

        if(tabsBean.getPageSource().equals(NetConstants.PS_Profile)) {
            if(isUserLoggedIn) {
                IntentUtil.openUserProfileActivity(getActivity(), THPConstants.FROM_USER_PROFILE);
            } else {
                IntentUtil.openMemberActivity(getActivity(), "");
            }
            return;
        }



    }


    /**
     * Tabs Custom click
     */
    private void tabClickHandling(List<TabsBean> tabsBeans) {
        LinearLayout tabStrip = ((LinearLayout)mTabLayout.getChildAt(0));
        for(int i = 0; i < tabStrip.getChildCount(); i++) {
            // Gesture Tab On-Click
            final TabClickListener tabClickListener = new TabClickListener(i, tabsBeans.get(i), this);
            GestureDetectorCompat gestureDetectorCompat = new GestureDetectorCompat(getContext(), tabClickListener);
            tabStrip.getChildAt(i).setHapticFeedbackEnabled(false);
            tabStrip.getChildAt(i).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return gestureDetectorCompat.onTouchEvent(event);
                }
            });
            // Disable Long click
            tabStrip.getChildAt(i).setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return true;
                }
            });
        }
    }

    /**
     * Loads User Profile Data
     */
    private void loadUserProfile() {
        mDisposable.add(ApiManager.getUserProfile(getActivity())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userProfile -> {
                    if (userProfile != null && !TextUtils.isEmpty(userProfile.getFullName())) {
                        mUserLoggedName = userProfile.getFullName().toUpperCase();
                    } else if (userProfile != null && !TextUtils.isEmpty(userProfile.getEmailId())) {
                        mUserLoggedName = userProfile.getEmailId().toUpperCase();
                    } else if (userProfile != null && !TextUtils.isEmpty(userProfile.getContact())) {
                        mUserLoggedName = userProfile.getContact().toUpperCase();
                    }

                    boolean hasFreePlan = userProfile.isHasFreePlan();
                    boolean hasSubscriptionPlan = userProfile.isHasSubscribedPlan();

                    if (hasSubscriptionPlan) {
                        subscribeLayoutVisibility(View.GONE);
                    } else if (PremiumPref.getInstance(getActivity()).isSubscribeClose()) {
                        subscribeLayoutVisibility(View.GONE);
                    } else {
                        subscribeLayoutVisibility(View.VISIBLE);
                    }
                }));
    }

    private void subscribeLayoutVisibility(int visibility) {
        subscribeLayout.setVisibility(visibility);
    }

    /**
     * This is ViewPager Page Scroll Animation
     */
    private void smoothPagerScroll() {
        try {
            Field mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            mScroller.set(mViewPager, new ViewPagerScroller(getActivity(),
                    new LinearInterpolator(), 250));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSubscribeBtnClick(TxnDataBean bean) {

    }

    @Override
    public void onResume() {
        super.onResume();
        // Shows user name
        loadUserProfile();

        EventBus.getDefault().register(this);
    }


    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("", "");
    }


    public void setCurrentTab(int tabIndex) {
        mViewPager.setCurrentItem(tabIndex);
    }



    private void updateUserStatus() {
        ApiManager.getUserProfile(getActivity())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userProfile -> {
                    boolean hasSubscriptionPlan = userProfile.isHasSubscribedPlan();

                    PremiumPref.getInstance(getActivity()).isUserLoggedIn();
                    //PremiumPref.getInstance(getActivity())

                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void handleEvent(AdData adData) {
        if(adData != null && adData.getSecId().equalsIgnoreCase(NetConstants.RECO_HOME_TAB)) {
            bottomBannerAds(true);
        }
        else {
            bottomBannerAds(false);
        }
    }

    private void bottomBannerAds(boolean isHomePage) {
        if(PremiumPref.getInstance(getActivity()).isUserAdsFree()) {
            getView().findViewById(R.id.banner_Ad_layout).setVisibility(View.GONE);

            boolean isHasSubscription = PremiumPref.getInstance(getActivity()).isHasSubscription();
            if(isHasSubscription) {
                subscribeLayoutVisibility(View.GONE);
            }
            return;
        }
        else {
            subscribeLayoutVisibility(View.VISIBLE);
        }
        TableConfiguration tableConfiguration = BaseAcitivityTHP.getTableConfiguration();
        if(tableConfiguration == null) return;
        DFPAds DFPAds = new DFPAds();
        DFPAds.createBannerAdRequest(isHomePage, tableConfiguration.getAds().getBottomAdHomeId(), tableConfiguration.getAds().getBottomAdOtherId());
        DFPAds.setOnAppAdLoadListener(new com.main.DFPAds.OnDFPAdLoadListener() {
            @Override
            public void onDFPAdLoadSuccess(AdData adData) {
                LinearLayout banner_Ad_layout = getView().findViewById(R.id.banner_Ad_layout);
                if(banner_Ad_layout != null) {
                    banner_Ad_layout.setVisibility(View.VISIBLE);
                    int childCount = banner_Ad_layout.getChildCount();
                    if(banner_Ad_layout.getChildCount() > 0) {
                        for(int i=0; i<childCount; i++) {
                            banner_Ad_layout.removeViewAt(i);
                        }
                    }
                    banner_Ad_layout.addView(adData.getAdView());
                }
            }

            @Override
            public void onDFPAdLoadFailure(AdData adData) {
                Log.i("", "");

            }
        });
    }

}
