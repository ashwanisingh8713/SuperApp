package com.ns.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ExpandableListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.drawerlayout.widget.DrawerLayout;

import com.netoperation.db.THPDB;
import com.netoperation.default_db.DaoSection;
import com.netoperation.default_db.TableSection;
import com.netoperation.model.SectionBean;
import com.netoperation.net.ApiManager;
import com.netoperation.retrofit.ServiceFactory;
import com.netoperation.util.THPPreferences;
import com.ns.adapter.NavigationExpandableListViewAdapter;
import com.ns.alerts.Alerts;
import com.ns.callbacks.OnExpandableListViewItemClickListener;
import com.ns.clevertap.CleverTapUtil;
import com.ns.contentfragment.AppTabFragment;
import com.ns.loginfragment.AccountCreatedFragment;
import com.ns.thpremium.BuildConfig;
import com.ns.thpremium.R;
import com.ns.utils.FragmentUtil;
import com.ns.utils.ResUtil;
import com.ns.utils.THPConstants;
import com.ns.utils.THPFirebaseAnalytics;

import org.greenrobot.eventbus.EventBus;

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

                    FragmentUtil.pushFragmentAnim(this, R.id.parentLayout, mAppTabFragment, FragmentUtil.FRAGMENT_NO_ANIMATION, true);

                    // THis below condition will be executed when user creates normal Sign-UP
                    if(mFrom != null && !TextUtils.isEmpty(mFrom) && mFrom.equalsIgnoreCase(THPConstants.FROM_USER_SignUp)) {
                        AccountCreatedFragment accountCreated = AccountCreatedFragment.getInstance("");
                        FragmentUtil.addFragmentAnim(this, R.id.parentLayout, accountCreated, FragmentUtil.FRAGMENT_NO_ANIMATION, false);
                    }
                }));


        // Drawer Setup
        mNavigationExpandableListView = findViewById(R.id.expandableListView);

        mDrawerLayout = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, getToolbar(), R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Show Expandable List Content from Database
        DaoSection section = THPDB.getInstance(this).daoSection();
        mDisposable.add(section.getSectionsOfBurger(true)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(sectionList -> {
                    return sectionList;
                })
                .subscribe(sectionList -> {
                    mNavigationExpandableListViewAdapter = new NavigationExpandableListViewAdapter(this, sectionList, this);
                    mNavigationExpandableListView.setAdapter(mNavigationExpandableListViewAdapter);
                }));

//        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

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
    public void onChildClick(int groupPostion, int childPosition, TableSection tableSection, SectionBean childSection) {
        // Sending Event in TopTabsFragment.java => handleEvent()
        EventBus.getDefault().post(childSection);
        mDrawerLayout.closeDrawers();

        // CleverTap Hamburger Event Tracking
        CleverTapUtil.cleverTapEventHamberger(this, tableSection.getSecName(), childSection.getSecName());
    }
}
