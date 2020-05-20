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

import com.netoperation.config.model.UrlBean;
import com.netoperation.db.THPDB;
import com.netoperation.default_db.DaoSection;
import com.netoperation.default_db.TableSection;
import com.netoperation.model.SectionBean;
import com.netoperation.net.ApiManager;
import com.netoperation.util.NetConstants;
import com.netoperation.util.PremiumPref;
import com.netoperation.util.DefaultPref;
import com.ns.adapter.NavigationExpandableListViewAdapter;
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
import com.taboola.android.api.TaboolaApi;
import com.taboola.android.js.TaboolaJs;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
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

        initTaboola();

        THPConstants.FLOW_TAB_CLICK = null;

        // Section Navigation
        showSectionToolbar();

        // Fetch latest userinfo from server
        fetchLatestUserInfo();

        if(getIntent() != null && getIntent().getExtras()!= null) {
            mFrom = getIntent().getStringExtra("from");
        }


        mAppTabFragment = AppTabFragment.getInstance();

        FragmentUtil.replaceFragmentAnim(this, R.id.parentLayout, mAppTabFragment, FragmentUtil.FRAGMENT_NO_ANIMATION, true);

        mDisposable.add(ApiManager.getUserProfile(this)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userProfile -> {

                }));


        // Drawer Setup
        mNavigationExpandableListView = findViewById(R.id.expandableListView);

        mDrawerLayout = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Show Expandable List Content from Database
        DaoSection section = THPDB.getInstance(this).daoSection();
        mDisposable.add(section.getSectionsOfBurger()
                .subscribeOn(Schedulers.io())
                .map(sectionList -> {
                    return sectionList;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(sectionList -> {
                    mNavigationExpandableListViewAdapter = new NavigationExpandableListViewAdapter(this, sectionList, this);
                    mNavigationExpandableListView.setAdapter(mNavigationExpandableListViewAdapter);

                    getDrawerStaticItem();

                }));

    }


    private void getDrawerStaticItem() {
        mDisposable.add(THPDB.getInstance(this).daoConfiguration()
                .getConfigurationSingle()
                .subscribeOn(Schedulers.io())
                .map(tableConfiguration -> {

                    List<TableSection> staticItemList = new ArrayList<>();
                    List<UrlBean> configStaticItems = tableConfiguration.getStaticItem();
                    for(UrlBean bean: configStaticItems) {
                        TableSection staticItem = new TableSection();
                        staticItem.setSecName(bean.getTitle());
                        staticItem.setType("staticUrlPage");
                        staticItem.setSecId("staticUrlPage");
                        if(mIsDayTheme) {
                            staticItem.setWebLink(bean.getUrlLight());
                        }
                        else {
                            staticItem.setWebLink(bean.getUrlLight());
                        }
                        staticItemList.add(staticItem);
                    }
                    return staticItemList;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(staticItem->{
                    mNavigationExpandableListViewAdapter.addStaticItemGroup(staticItem);
                }, throwable -> {

                }));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if(intent != null) {
            String from = intent.getStringExtra("from");

            if(from!=null && from.equalsIgnoreCase("Logout")) {
                if(mAppTabFragment != null) {
                    mAppTabFragment.changeTab(0);
                }
            }
            else if(from != null) {
                if(mAppTabFragment != null) {
                    mAppTabFragment.showPageSource(from);
                }
            }


            // THis below condition will be executed when user creates normal Sign-UP
            if(from != null && !TextUtils.isEmpty(from) && from.equalsIgnoreCase(THPConstants.FROM_USER_SignUp)) {
                AccountCreatedFragment accountCreated = AccountCreatedFragment.getInstance("");
                FragmentUtil.addFragmentAnim(this, R.id.parentLayout, accountCreated, FragmentUtil.FRAGMENT_NO_ANIMATION, false);
            }
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
                        mDisposable.add(ApiManager.getUserInfo(this, userProfile.getAuthorization(), BuildConfig.SITEID,
                                ResUtil.getDeviceId(this), mUserId,
                                PremiumPref.getInstance(this).getLoginTypeId(),
                                PremiumPref.getInstance(this).getLoginPasswd())
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
        Log.i("TabFragment", "onResume() In AppTabActivity EventBus Registered");
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("TabFragment", "onPause() In AppTabActivity EventBus UnRegistered");
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onExpandButtonClick(int groupPostion, boolean isExpanded) {
        if (isExpanded) {
            mNavigationExpandableListView.collapseGroup(groupPostion);
        } else {
            mNavigationExpandableListView.expandGroup(groupPostion);
        }
    }

    @Override
    public void onGroupClick(int groupPostion, TableSection tableSection, boolean isExpanded) {
        if(tableSection.getType().equalsIgnoreCase("staticUrlPage")) {
            IntentUtil.openWebActivity(this, tableSection.getSecName(), tableSection.getWebLink());
        }
        // News Digest
        else if(tableSection.getType().equalsIgnoreCase("static")
                && tableSection.getSecId().equalsIgnoreCase("998")) {
            if(BuildConfig.IS_BL) {
                IntentUtil.openWebActivity(this, "News Digest", DefaultPref.getInstance(this).getNewsDigestUrl());
            }
            else {
                FragmentUtil.redirectionOnSectionAndSubSection(this, tableSection.getSecId());
            }
        }
        else if(tableSection.getType().equalsIgnoreCase("static")) {
            IntentUtil.openUrlInBrowser(this, tableSection.getSection().getWebLink());
        }
        else {
            FragmentUtil.redirectionOnSectionAndSubSection(this, tableSection.getSecId());
        }
        mDrawerLayout.closeDrawers();
        // CleverTap Hamburger Event Tracking
        CleverTapUtil.cleverTapEventHamberger(this, tableSection.getSecName(), null);

    }

    @Override
    public void onChildClick(int groupPostion, int childPosition, TableSection groupSection, SectionBean childSection) {
        FragmentUtil.redirectionOnSectionAndSubSection(this, childSection.getSecId());
        // Setting Group Section name, It will be used to show in Toolbar title
        /*childSection.setParentSecName(groupSection.getSecName());
        childSection.setParentSecId(groupSection.getSecId());
        // Sending Event in TopTabsFragment.java => handleEvent()
        EventBus.getDefault().post(childSection);*/
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
     * Show Premium Section Screen Toolbar icons and sets navigation btn click
     */
    private void showPremiumToolbar() {
        if(PremiumPref.getInstance(this).isHasSubscription()) {
            getDetailToolbar().PREMIUM_LISTING_TOPBAR(navigationBtnClick -> {
                mAppTabFragment.setCurrentTab(0);
            });
        }
        else {
            getDetailToolbar().PREMIUM_LISTING_TOPBAR_CROWN(navigationBtnClick -> {
                mAppTabFragment.setCurrentTab(0);
            });
        }
    }

    /**
     * Show Section Screen Toolbar icons and sets navigation btn click
     */
    private void showSectionToolbar() {
        if(PremiumPref.getInstance(this).isHasSubscription()) {
            getDetailToolbar().SECTION_LISTING_TOPBAR(navigationBtnClick -> {
                mDrawerLayout.openDrawer(GravityCompat.START);
            });
        } else {
            getDetailToolbar().SECTION_LISTING_TOPBAR_CROWN(navigationBtnClick -> {
                mDrawerLayout.openDrawer(GravityCompat.START);
            });
        }
    }

    /**
     * Show Sub-Section Screen Toolbar icons and sets Back btn click and send event for pop-out fragment
     */
    private void showSubSectionToolbar(String title) {
        if(PremiumPref.getInstance(this).isHasSubscription()) {
            getDetailToolbar().SUB_SECTION_LISTING_TOPBAR(title, backBtnClick -> {
                EventBus.getDefault().post(new BackPressImpl());
            });
        }
        else {
            getDetailToolbar().SUB_SECTION_LISTING_TOPBAR_CROWN(title, backBtnClick -> {
                EventBus.getDefault().post(new BackPressImpl());
            });
        }
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

        if(toolbarChangeRequired.getTypeOfToolbar().equals(ToolbarChangeRequired.SECTION_LISTING_TOPBAR)) {
            showSectionToolbar();
        }
        else if(toolbarChangeRequired.getTypeOfToolbar().equals(ToolbarChangeRequired.SUB_SECTION_LISTING_TOPBAR)) {
            showSubSectionToolbar(toolbarChangeRequired.getTitle());
        }
        else if(toolbarChangeRequired.getTypeOfToolbar().equals(ToolbarChangeRequired.OTHER_LISTING_TOPBAR)) {
            showSubSectionToolbar(toolbarChangeRequired.getTitle());
        }
        else if(toolbarChangeRequired.getTypeOfToolbar().equals(ToolbarChangeRequired.PREMIUM_LISTING_TOPBAR)) {
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

        final boolean isUserFromEurope = DefaultPref.getInstance(this).isUserFromEurope();

        LinearLayout mReadLayout = layout.findViewById(R.id.layout_readlater);
        mReadLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                FlurryAgent.logEvent(getString(R.string.ga_bookmark_screen_button_clicked));
//                GoogleAnalyticsTracker.setGoogleAnalyticsEvent(MainActivity.this, getString(R.string.ga_action), getString(R.string.ga_bookmark_screen_button_clicked), "Home Fragment");
                /*if(PremiumPref.getInstance(AppTabActivity.this).isUserLoggedIn()) {
                    IntentUtil.openBookmarkActivity(AppTabActivity.this, NetConstants.BOOKMARK_IN_TAB);
                } else {
                    IntentUtil.openBookmarkActivity(AppTabActivity.this, NetConstants.BOOKMARK_IN_ONE);
                }*/
                IntentUtil.openBookmarkActivity(AppTabActivity.this, NetConstants.BOOKMARK_IN_ONE);
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

                IntentUtil.openNotificationArticleActivity(AppTabActivity.this);

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
                changeSortPopUp.dismiss();
                IntentUtil.openHomeArticleOptionActivity(AppTabActivity.this);
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

                changeSortPopUp.dismiss();
                startActivity(new Intent(AppTabActivity.this, THPPersonaliseActivity.class));
            });
        } else {
            personaliseSubscription.setVisibility(View.GONE);
        }

        TextView mSettigsScreen = layout.findViewById(R.id.textView_menu_settings);
        mSettigsScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeSortPopUp.dismiss();
                IntentUtil.openSettingActivity(AppTabActivity.this);
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

    private void initTaboola() {
        // Required when using TaboolaJS integration
        TaboolaJs.getInstance().init(this);

        // Required when using TaboolaApi (Native Android) integration
        TaboolaApi.getInstance().init(this, getResources().getString(R.string.taboola_publisher_id),
                getResources().getString(R.string.taboola_publisher_apikey));
    }
}
