package com.ns.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import com.clevertap.android.sdk.CleverTapAPI;
import com.netoperation.config.model.UrlBean;
import com.netoperation.db.THPDB;
import com.netoperation.default_db.DaoSection;
import com.netoperation.default_db.TableOptional;
import com.netoperation.default_db.TableSection;
import com.netoperation.model.SectionBean;
import com.netoperation.net.ApiManager;
import com.netoperation.net.DefaultTHApiManager;
import com.netoperation.util.DefaultPref;
import com.netoperation.util.NetConstants;
import com.netoperation.util.PremiumPref;
import com.ns.adapter.NavigationExpandableListViewAdapter;
import com.ns.alerts.Alerts;
import com.ns.alerts.HomePermissionInfoDialog;
import com.ns.callbacks.BackPressCallback;
import com.ns.callbacks.BackPressImpl;
import com.ns.callbacks.OnDialogBtnClickListener;
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
import com.ns.utils.OverflowPopUp;
import com.ns.utils.PicassoUtil;
import com.ns.utils.ResUtil;
import com.ns.utils.SharingArticleUtil;
import com.ns.utils.THPConstants;
import com.ns.utils.THPFirebaseAnalytics;
import com.ns.view.layout.NSLinearLayout;
import com.ns.view.text.CustomTextView;
import com.taboola.android.api.TaboolaApi;
import com.taboola.android.js.TaboolaJs;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.ns.alerts.HomePermissionInfoDialog.REQUEST_PERMISSIONS_REQUEST_CODE;
import static com.paytm.pgsdk.easypay.manager.PaytmAssist.getContext;

public class AppTabActivity extends BaseAcitivityTHP implements OnExpandableListViewItemClickListener {

    private String mFrom;

    private AppTabFragment mAppTabFragment;

    private NavigationExpandableListViewAdapter mNavigationExpandableListViewAdapter;
    private ExpandableListView mNavigationExpandableListView;
    private DrawerLayout mDrawerLayout;
    //Counts
    private int mUnreadNotificationArticleCount, mUnreadBookmarkArticleCount;
    private Disposable notificationCountsObserver, bookmarksCountObserver;
    //Dialog for Location Permission
    private HomePermissionInfoDialog dialogPermission;


    @Override
    public int layoutRes() {
        return R.layout.activity_apptab;
    }

    @Override
    protected void onStart() {
        super.onStart();
        THPConstants.sISMAIN_ACTIVITY_LAUNCHED = true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Get Menu Options API call
        if (sIsOnline) {
            getMenuOptionsFromApi();
        }

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


        // Guide Overlay, for Listing Page
        String guideOverlayListing = DefaultPref.getInstance(this).getGuideOverlayUrl(true);
        if(!ResUtil.isEmpty(guideOverlayListing)) {
            findViewById(R.id.overlayoutGuideImg).setVisibility(View.GONE);
            PicassoUtil.loadImage(AppTabActivity.this, findViewById(R.id.overlayoutGuideImg), guideOverlayListing);
            findViewById(R.id.overlayoutGuideImg).setOnClickListener(v -> {
                findViewById(R.id.overlayoutGuideImg).setVisibility(View.GONE);
                DefaultPref.getInstance(AppTabActivity.this).saveGuideOverlay("", DefaultPref.getInstance(this).getGuideOverlayUrl(false));
            });
        }
        else {
            findViewById(R.id.overlayoutGuideImg).setVisibility(View.GONE);
        }


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
                        if(sIsDayTheme) {
                            staticItem.setWebLink(bean.getUrlLight());
                        }
                        else {
                            staticItem.setWebLink(bean.getUrlDark());
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
        //Current location permission
        boolean alreadyCancelled = DefaultPref.getInstance(this).getPermissionDialogPreference();
        if(!alreadyCancelled) {
            if (dialogPermission == null) {
                dialogPermission = HomePermissionInfoDialog.getInstance("defaultInfoDialogForPermission");
                showPermissionDialog();
            }
        }
        THPFirebaseAnalytics.setFirbaseAnalyticsScreenRecord(this, "AppTabActivity Screen", AppTabActivity.class.getSimpleName());
        Log.i("TabFragment", "onResume() In AppTabActivity EventBus Registered");
        EventBus.getDefault().register(this);
        //Observe notifications count
        observeNotificationsCount();
        //Observe bookmarks count
        observeBookmarksCount();

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("TabFragment", "onPause() In AppTabActivity EventBus UnRegistered");
        EventBus.getDefault().unregister(this);
        clearObservers();
    }

    @Override
    public void onExpandButtonClick(int groupPostion, boolean isExpanded) {
        //Firebase event
        THPFirebaseAnalytics.setFirbaseAnalyticsEvent(AppTabActivity.this, "Navigation Screen", "Navigation drawer Expand Button Click", "Drawer View");

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
        // Firebase Hamburger Event Tracking
        THPFirebaseAnalytics.setFirbaseAnalyticsEvent(this,"Hamberger","Clicked",tableSection.getSecName());

        //crossword events for Firebase and Clevertap
        if(tableSection.getSecName().equalsIgnoreCase("Crossword+")){
            //clevertap & firebase events
            THPFirebaseAnalytics.firebase_crossword(this);
            CleverTapUtil.cleverTap_crossword(this);
            //  Log.e("Crossword","Crossword");
        }

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
        // Firebase Hamburger Event Tracking
        THPFirebaseAnalytics.setFirbaseAnalyticsEvent(this,"Hamberger","Clicked", groupSection.getSecName()+" --> "+childSection.getSecName());
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

    @Override
    public void onBackPressed() {
        //drawer is open
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawers();
            return;
        }
        // It sends event to current visible fragment, in handleEvent(BackPressImpl backPress)
        EventBus.getDefault().post(new BackPressImpl());
    }



    @Override
    public void onOverflowClickListener(ToolbarCallModel toolbarCallModel) {

        mDisposable.add(DefaultTHApiManager.getOptionsListDB(this)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(optionsBeans -> {
                            Log.d("AppTabActivity", "getMenuOptionsFromDB");
                            List <TableOptional.OptionsBean> menuItems = new ArrayList<>();
                            if (optionsBeans == null || optionsBeans.size() == 0) {
                                menuItems = getDefaultMenusOptions();
                            } else {
                                menuItems = optionsBeans;
                            }

                            OverflowPopUp overflowPopUp = new OverflowPopUp(this, menuItems);
                            PopupWindow changeSortPopUp = overflowPopUp.initPopUpView(mUnreadBookmarkArticleCount, mUnreadNotificationArticleCount);
                            int width = getDetailToolbar().getWidth();
                            int height = getDetailToolbar().getHeight();
                            // Show Pop-up Window
                            changeSortPopUp.showAsDropDown(getDetailToolbar(), width, -(height/3));
                        },
                        throwable -> {
                            Log.d("AppTabActivity", "getMenuOptionsFromDB : Failed");
                            OverflowPopUp overflowPopUp = new OverflowPopUp(this, getDefaultMenusOptions());
                            PopupWindow changeSortPopUp = overflowPopUp.initPopUpView(mUnreadBookmarkArticleCount, mUnreadNotificationArticleCount);
                            int width = getDetailToolbar().getWidth();
                            int height = getDetailToolbar().getHeight();
                            // Show Pop-up Window
                            changeSortPopUp.showAsDropDown(getDetailToolbar(), width, -(height/3));
                        }));



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

    /**
     * Observe notifications counts
     */
    private void observeNotificationsCount() {
        notificationCountsObserver = DefaultTHApiManager.getUnreadNotificationArticlesCount(this)
                .subscribe( counts -> {
                    mUnreadNotificationArticleCount = counts;
                    if (getDetailToolbar() != null) {
                        getDetailToolbar().updateOverFlowMenuActionButtonCounts((mUnreadNotificationArticleCount + mUnreadBookmarkArticleCount));
                    }
                });
        mDisposable.add(notificationCountsObserver);
    }
    /*
    * Observe Bookmarks counts*/
    private void observeBookmarksCount() {
        bookmarksCountObserver = ApiManager.getUnReadBookmarksCounts(this)
                .subscribe( counts -> {
                    mUnreadBookmarkArticleCount = counts;
                    if (getDetailToolbar() != null) {
                        getDetailToolbar().updateOverFlowMenuActionButtonCounts((mUnreadBookmarkArticleCount + mUnreadNotificationArticleCount));
                    }
                });
        mDisposable.add(bookmarksCountObserver);
    }

    /*
     * Clear observers for counts*/
    private void clearObservers() {
        mDisposable.remove(notificationCountsObserver);
        mDisposable.remove(bookmarksCountObserver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        THPConstants.sISMAIN_ACTIVITY_LAUNCHED = false;
        //DefaultTHApiManager.deleteTableOptions(this);
    }

    /**
     * Return the current state of the permissions needed.
     */
    private boolean checkPermissions(Context context) {
        int permissionState = ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    public void showPermissionDialog() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION);
        if(!checkPermissions(this) && shouldProvideRationale
                || !DefaultPref.getInstance(getContext()).isLocationEnabled()){
            dialogPermission.show(getSupportFragmentManager(), "di");
            dialogPermission.setCancelable(false);
            dialogPermission.setOnDialogOkClickListener(new OnDialogBtnClickListener() {
                @Override
                public void onDialogOkClickListener() {
                    requestPermissions();
                }

                @Override
                public void onDialogAppInfoClickListener() {

                }

                @Override
                public void onDialogCancelClickListener() {
                    DefaultPref.getInstance(getContext()).savePermissionDialogPreference(true);
                }
            });

        }
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION);

        if (shouldProvideRationale) {
            Alerts.showSnackbarInfinite(this, com.ns.thpremium.R.string.permission_rationale, android.R.string.ok,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(AppTabActivity.this,
                                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    });
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (permissions[0].equals(Manifest.permission.ACCESS_COARSE_LOCATION) && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Alerts.showSnackbarInfinite(this, com.ns.thpremium.R.string.permission_location, R.string.settings,
                        view -> {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                            intent.setData(uri);
                            startActivity(intent);
                        });
            } else if (permissions[0].equals(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                DefaultPref.getInstance(getContext()).setLocationEnabled(true);
                CleverTapAPI clevertap = CleverTapAPI.getDefaultInstance(getContext());
                if (clevertap != null) {
                    Location location = clevertap.getLocation();
                    clevertap.setLocation(location);
                }
            }
        }
    }

    //Setting Menu Options
    private void getMenuOptionsFromApi() {
        mDisposable.add(DefaultTHApiManager.getOptionsListApi(this)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(optionsBeans -> {
                    Log.d("AppTabActivity", "getMenuOptionsFromApi");
                },
                throwable -> {
                    Log.d("AppTabActivity", "getMenuOptionsFromApi : Failed");
                }));
    }



    private List <TableOptional.OptionsBean> getDefaultMenusOptions() {
        List <TableOptional.OptionsBean> menuItems = new ArrayList<>();
        menuItems.add(new TableOptional.OptionsBean("Read Later",1));
        menuItems.add(new TableOptional.OptionsBean("Notifications",2));
        menuItems.add(new TableOptional.OptionsBean("Personalise Home Screen",3));
        menuItems.add(new TableOptional.OptionsBean("Personalise My Stories", 4));
        menuItems.add(new TableOptional.OptionsBean("Settings",5));
        menuItems.add(new TableOptional.OptionsBean("Share this app",6));
        return menuItems;
    }

}
