package com.ns.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.netoperation.db.THPDB;
import com.netoperation.default_db.DaoSection;
import com.netoperation.default_db.TableSection;
import com.netoperation.model.SectionBean;
import com.netoperation.net.ApiManager;
import com.netoperation.net.DefaultTHApiManager;
import com.netoperation.retrofit.ServiceFactory;
import com.netoperation.util.THPPreferences;
import com.netoperation.util.UserPref;
import com.ns.adapter.NavigationExpandableListViewAdapter;
import com.ns.alerts.Alerts;
import com.ns.callbacks.BackPressCallback;
import com.ns.callbacks.BackPressImpl;
import com.ns.callbacks.OnExpandableListViewItemClickListener;
import com.ns.callbacks.ToolbarChangeRequired;
import com.ns.clevertap.CleverTapUtil;
import com.ns.contentfragment.AppTabFragment;
import com.ns.loginfragment.AccountCreatedFragment;
import com.ns.model.ToolbarCallModel;
import com.ns.thpremium.BuildConfig;
import com.ns.thpremium.R;
import com.ns.utils.FragmentUtil;
import com.ns.utils.IntentUtil;
import com.ns.utils.NetUtils;
import com.ns.utils.ResUtil;
import com.ns.utils.SharingArticleUtil;
import com.ns.utils.THPConstants;
import com.ns.utils.THPFirebaseAnalytics;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AppTabActivity extends BaseAcitivityTHP implements OnExpandableListViewItemClickListener {

    private String mFrom;

    private AppTabFragment mAppTabFragment;

    private NavigationExpandableListViewAdapter mNavigationExpandableListViewAdapter;
    private ExpandableListView mNavigationExpandableListView;
    private DrawerLayout mDrawerLayout;


    @Override
    public int layoutRes() {
        return R.layout.activity_apptab;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Section Navigation
        showSectionToolbar();

        // Fetch latest userinfo from server
        fetchLatestUserInfo();

        if(getIntent() != null && getIntent().getExtras()!= null) {
            mFrom = getIntent().getStringExtra("from");
        }

        if(BuildConfig.IS_PRODUCTION) {
            ServiceFactory.BASE_URL = BuildConfig.PRODUCTION_BASE_URL;
        } else {
            ServiceFactory.BASE_URL = BuildConfig.STATGGING_BASE_URL;
        }

        mDisposable.add(ApiManager.getUserProfile(this)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userProfile -> {
                    String taIn = THPConstants.FLOW_TAB_CLICK;
                    int tabIndex = getIntent().getIntExtra("tabIndex", 0);
                    mAppTabFragment = AppTabFragment.getInstance(mFrom, userProfile.getUserId(), tabIndex);

                    FragmentUtil.replaceFragmentAnim(this, R.id.parentLayout, mAppTabFragment, FragmentUtil.FRAGMENT_NO_ANIMATION, true);

                    // THis below condition will be executed when user creates normal Sign-UP
                    if(mFrom != null && !TextUtils.isEmpty(mFrom) && mFrom.equalsIgnoreCase(THPConstants.FROM_USER_SignUp)) {
                        AccountCreatedFragment accountCreated = AccountCreatedFragment.getInstance("");
                        FragmentUtil.addFragmentAnim(this, R.id.parentLayout, accountCreated, FragmentUtil.FRAGMENT_NO_ANIMATION, false);
                    }
                }));


        // Drawer Setup
        mNavigationExpandableListView = findViewById(R.id.expandableListView);

        mDrawerLayout = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Show Expandable List Content from Database
        DaoSection section = THPDB.getInstance(this).daoSection();
//        mDisposable.add(section.getSectionsOfBurger(true)
        mDisposable.add(section.getSectionsObs()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(sectionList -> {
                    return sectionList;
                })
                .subscribe(sectionList -> {
                    mNavigationExpandableListViewAdapter = new NavigationExpandableListViewAdapter(this, sectionList, this);
                    mNavigationExpandableListView.setAdapter(mNavigationExpandableListViewAdapter);
                }));

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if(intent != null) {
            mFrom = intent.getStringExtra("from");
            if(mAppTabFragment != null && mFrom != null) {
                mAppTabFragment.updateFromValue(mFrom);
                mAppTabFragment.updateTabIndex();
            }
            Log.i("", "");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null) {
            boolean isKillToAppTabActivity = data.getBooleanExtra("isKillToAppTabActivity", false);
            if(isKillToAppTabActivity) {
                finish();
            }
        }
    }

    String mUserId = "";

    /**
     * Fetch latest userinfo from server
     */
    private void fetchLatestUserInfo() {
        mDisposable.add(ApiManager.getUserProfile(this)
                .observeOn(AndroidSchedulers.mainThread())
                .delay(3, TimeUnit.SECONDS)
                .subscribe(userProfile -> {
                    mUserId = userProfile.getUserId();
                        // Fetch latest userinfo from server
                        mDisposable.add(ApiManager.getUserInfo(this, BuildConfig.SITEID,
                                ResUtil.getDeviceId(this), mUserId,
                                THPPreferences.getInstance(this).getLoginId(),
                                THPPreferences.getInstance(this).getLoginPasswd())
                                .subscribe(val->{
                                    Log.i("", "");
                                }, thr->{
                                    Log.i("", "");
                                }));
                }));
    }

    @Override
    protected void onResume() {
        super.onResume();
        THPFirebaseAnalytics.setFirbaseAnalyticsScreenRecord(this, "AppTabActivity Screen", AppTabActivity.class.getSimpleName());
        Log.i("TabFragment", "onResume() In Activity EventBus Registered");
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("TabFragment", "onPause() In Activity EventBus UnRegistered");
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("TabFragment", "onStop() In Activity EventBus UnRegistered");
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onExpandButtonClick(int groupPostion, boolean isExpanded) {
        Alerts.showToast(this, ""+groupPostion);

        if (isExpanded) {
            mNavigationExpandableListView.collapseGroup(groupPostion);
        } else {
            mNavigationExpandableListView.expandGroup(groupPostion);
        }
    }

    @Override
    public void onGroupClick(int groupPostion, TableSection tableSection, boolean isExpanded) {
        // Sending Event in TopTabsFragment.java => handleEvent()
        EventBus.getDefault().post(tableSection);
        mDrawerLayout.closeDrawers();

        // CleverTap Hamburger Event Tracking
        CleverTapUtil.cleverTapEventHamberger(this, tableSection.getSecName(), null);

    }

    @Override
    public void onChildClick(int groupPostion, int childPosition, TableSection groupSection, SectionBean childSection) {

        // Setting Group Section name, It will be used to show in Toolbar title
        childSection.setParentSecName(groupSection.getSecName());
        childSection.setParentSecId(groupSection.getSecId());
        // Sending Event in TopTabsFragment.java => handleEvent()
        EventBus.getDefault().post(childSection);
        // Closing Left Drawer
        mDrawerLayout.closeDrawers();

        // CleverTap Hamburger Event Tracking
        CleverTapUtil.cleverTapEventHamberger(this, groupSection.getSecName(), childSection.getSecName());
    }

    /**
     * Lock drawer
     */
    private void lockDrawer() {
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    /**
     * Un-Lock Drawer
     */
    private void unLockDrawer() {
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    /**
     * Show Section Screen Toolbar icons and sets navigation btn click
     */
    private void showPremiumToolbar() {
        getDetailToolbar().showPremiumDetailIcons(navigationBtnClick->{
            mAppTabFragment.setCurrentTab(0);
        });
    }

    /**
     * Show Section Screen Toolbar icons and sets navigation btn click
     */
    private void showSectionToolbar() {
        getDetailToolbar().showSectionIcons(navigationBtnClick->{
            mDrawerLayout.openDrawer(GravityCompat.START);
        });
    }

    /**
     * Show Sub-Section Screen Toolbar icons and sets Back btn click and send event for pop-out fragment
     */
    private void showSubSectionToolbar(String title) {
        getDetailToolbar().showSubSectionIcons(title, backBtnClick->{
            EventBus.getDefault().post(new BackPressImpl());
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void handleEvent(BackPressCallback backPressCallback) {
        Log.i("handleEvent", "Received BackPressCallback :: From TabIndex = "+backPressCallback.getTabIndex());
        if(backPressCallback.isPopBack()) {
            return;
        } else if(backPressCallback.getTabIndex() != 0){
            mAppTabFragment.setCurrentTab(0);
        } else {
            finish();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void handleEvent(ToolbarChangeRequired toolbarChangeRequired) {
        Log.i("handleEvent", "Received ToolbarChangeRequired :: From TabIndex = "+toolbarChangeRequired.getTabIndex());
        if(toolbarChangeRequired.isEnableLeftSlider()) {
            unLockDrawer();
        } else {
            lockDrawer();
        }

        if(toolbarChangeRequired.getTypeOfToolbar().equals(ToolbarChangeRequired.SECTION)) {
            showSectionToolbar();
        }
        else if(toolbarChangeRequired.getTypeOfToolbar().equals(ToolbarChangeRequired.SUB_SECTION)) {
            showSubSectionToolbar(toolbarChangeRequired.getTitle());
        }
        else if(toolbarChangeRequired.getTypeOfToolbar().equals(ToolbarChangeRequired.PREMIUM)) {
            showPremiumToolbar();
        }
    }

    @Override
    public void onBackPressed() {
        //drawer is open
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawers();
            return;
        }

        EventBus.getDefault().post(new BackPressImpl());
    }

    @Override
    public void onOverflowClickListener(ToolbarCallModel toolbarCallModel) {
        LinearLayout viewGroup = findViewById(R.id.layout_custom);

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.layout_menu_overflow, viewGroup);


        final PopupWindow changeSortPopUp = new PopupWindow(this);
        //Inflating the Popup using xml file
        changeSortPopUp.setContentView(layout);
        changeSortPopUp.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        changeSortPopUp.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        changeSortPopUp.setFocusable(true);
        changeSortPopUp.setBackgroundDrawable(getResources().getDrawable(R.drawable.shadow_143418));

        TextView mReadLaterCountTextView = layout.findViewById(R.id.textview_menu_readlater_count);
        TextView mNotificationsCountTextView = layout.findViewById(R.id.textview_menu_notifications_count);

        /*mReadLaterCountTextView.setText(mUnreadBookmarkArticleCount + "");
        mNotificationsCountTextView.setText(mUnreadNotificationArticleCount + "");

        if (mUnreadBookmarkArticleCount == 0) {
            mReadLaterCountTextView.setVisibility(View.GONE);
        }

        if (mUnreadNotificationArticleCount == 0) {
            mNotificationsCountTextView.setVisibility(View.GONE);
        }*/

        final boolean isUserFromEurope = UserPref.getInstance(this).isUserFromEurope();

        LinearLayout mReadLayout = layout.findViewById(R.id.layout_readlater);
        mReadLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                FlurryAgent.logEvent(getString(R.string.ga_bookmark_screen_button_clicked));
//                GoogleAnalyticsTracker.setGoogleAnalyticsEvent(MainActivity.this, getString(R.string.ga_action), getString(R.string.ga_bookmark_screen_button_clicked), "Home Fragment");


//                    Intent intent = new Intent(AppTabActivity.this, THP_BookmarkActivity.class);
                    Intent intent = new Intent(AppTabActivity.this, BookmarkMergedActivity.class);
                    intent.putExtra("userId", mUserId);
                    startActivity(intent);
                changeSortPopUp.dismiss();
            }
        });
        LinearLayout mNotificationsLayout = layout.findViewById(R.id.layout_notifications);
        mNotificationsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*GoogleAnalyticsTracker.setGoogleAnalyticsEvent(MainActivity.this, getString(R.string.ga_action), "Notification: Action Button Clicked ", "Main Activity");
                FlurryAgent.logEvent("Notification: Action Button clicked");
                NotificationFragment notificationFragment = new NotificationFragment();
                pushFragmentToBackStack(notificationFragment);
                SharedPreferenceHelper.putBoolean(getApplicationContext(), Constants.NEW_NOTIFICATION, false);*/
                changeSortPopUp.dismiss();
            }
        });

        TextView mHomeScreen = layout.findViewById(R.id.textView_menu_customize_home_screen);
        mHomeScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*GoogleAnalyticsTracker.setGoogleAnalyticsEvent(
                        MainActivity.this,
                        getString(R.string.ga_action),
                        "Customise: Customise Button Clicked ",
                        getString(R.string.custom_home_screen));
                FlurryAgent.logEvent("Customise: Customise Button Clicked ");*/
                startActivity(new Intent(AppTabActivity.this, CustomizeHomeScreenActivity.class));
                changeSortPopUp.dismiss();
            }
        });

        TextView personaliseSubscription = layout.findViewById(R.id.textView_menu_personalise_subscription);
        if(isUserLoggedIn() && !isUserFromEurope && NetUtils.isConnected(this)) {
            personaliseSubscription.setVisibility(View.VISIBLE);
            personaliseSubscription.setOnClickListener(v -> {
                if(!NetUtils.isConnected(this)) {
                    noConnectionSnackBar(v);
                    return;
                }
                /*GoogleAnalyticsTracker.setGoogleAnalyticsEvent(
                        MainActivity.this,
                        getString(R.string.ga_action),
                        "Customise Subscription: Customise Subscription Button Clicked ",
                        getString(R.string.custom_home_screen));
                FlurryAgent.logEvent("Customise Subscription: Customise Subscription Button Clicked ");*/

                startActivity(new Intent(AppTabActivity.this, THPPersonaliseActivity.class));
                changeSortPopUp.dismiss();
            });
        } else {
            personaliseSubscription.setVisibility(View.GONE);
        }

        TextView mSettigsScreen = layout.findViewById(R.id.textView_menu_settings);
        mSettigsScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*SettingsFragment settingsFragment = new SettingsFragment();
                pushFragmentToBackStack(settingsFragment);*/
                changeSortPopUp.dismiss();
            }
        });

        TextView mShareAppScreen = layout.findViewById(R.id.textView_menu_share_app);
        mShareAppScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeSortPopUp.dismiss();
                String mShareTitle = "Download The Hindu official app.";
                String mShareUrl = "https://play.google.com/store/apps/details?id=com.mobstac.thehindu";
                SharingArticleUtil.shareArticle(AppTabActivity.this, mShareTitle, mShareUrl,"Home");

                //CleverTap
                CleverTapUtil.cleverTapEvent(AppTabActivity.this,THPConstants.CT_EVENT_SHARE_THIS_APP,null);

            }
        });
        int width = getDetailToolbar().getWidth();
        int height = getDetailToolbar().getHeight();
        // Show Pop-up Window
        changeSortPopUp.showAsDropDown(getDetailToolbar(), width, -(height/3));
    }

    @Override
    public void onSearchClickListener(ToolbarCallModel toolbarCallModel) {
        IntentUtil.openSearchActivity(this);
    }
}
