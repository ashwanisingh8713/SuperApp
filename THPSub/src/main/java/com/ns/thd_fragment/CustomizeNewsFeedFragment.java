package com.ns.thd_fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.netoperation.db.THPDB;
import com.netoperation.default_db.DaoPersonaliseDefault;
import com.netoperation.default_db.DaoSection;
import com.netoperation.default_db.TablePersonaliseDefault;
import com.netoperation.default_db.TableSection;
import com.netoperation.model.SectionBean;
import com.netoperation.util.NetConstants;
import com.netoperation.util.DefaultPref;
import com.ns.activity.CustomizeHomeScreenActivity;
import com.ns.clevertap.CleverTapUtil;
import com.ns.thpremium.BuildConfig;
import com.ns.thpremium.R;
import com.ns.utils.OverflowPopUp;
import com.ns.utils.ResUtil;
import com.ns.utils.THPConstants;
import com.ns.utils.THPFirebaseAnalytics;
import com.ns.view.flowlayout.FlowLayout;
import com.ns.view.flowlayout.TagAdapter;
import com.ns.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by zhy on 15/9/10.
 */
public class CustomizeNewsFeedFragment extends Fragment {
    private TagFlowLayout mFlowLayout;
    private List<TablePersonaliseDefault> mSectionList = new ArrayList<>();
    private CustomizeHomeScreenActivity mMainActivity;

    private ArrayList<String> mAlreadySelectionSecIds = new ArrayList<>();


    public static CustomizeNewsFeedFragment newInstance(boolean isCity) {
        CustomizeNewsFeedFragment fragment = new CustomizeNewsFeedFragment();
        Bundle args = new Bundle();
        args.putBoolean(THPConstants.IS_CITY, isCity);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mMainActivity = (CustomizeHomeScreenActivity) activity;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mMainActivity = (CustomizeHomeScreenActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View mRootView = inflater.inflate(R.layout.fragment_news_feed, container, false);
        mFlowLayout = mRootView.findViewById(R.id.id_flowlayout);

        if(DefaultPref.getInstance(getActivity()).isHomeArticleOptionScreenShown()) {
            mRootView.findViewById(R.id.selectTopic).setVisibility(View.GONE);
        }

        Observable.just("")
                .subscribeOn(Schedulers.newThread())
                .map(val->{
                    THPDB db = THPDB.getInstance(getActivity());
                    DaoSection daoSection = db.daoSection();
                    DaoPersonaliseDefault daoPersonaliseDefault = db.daoPersonaliseDefault();
                    List<TableSection> allList = daoSection.getSections();
                    List<TablePersonaliseDefault> allPersonalised = daoPersonaliseDefault.getCategoryPersonalise(NetConstants.PERSONALISE_CATEGORY_NEWS);

                    List<TablePersonaliseDefault> AllSectionListSelected = new ArrayList<>();

                    for(TableSection tableSection : allList) {
                        if(tableSection.getCustomScreen().equals("1")) {
                            TablePersonaliseDefault defaultSec = new TablePersonaliseDefault(NetConstants.PERSONALISE_CATEGORY_NEWS, tableSection.getCustomScreenPri(), tableSection.getSecId(), null, tableSection.getSecName(), false, false);
                            if(allPersonalised.contains(defaultSec)) {
                                defaultSec.setUserPreffered(true);
                                mAlreadySelectionSecIds.add(tableSection.getSecId());
                            }
                            AllSectionListSelected.add(defaultSec);

                        }
                        List<SectionBean> subSection = tableSection.getSubSections();
                        for(SectionBean bean : subSection) {
                            if(bean.getCustomScreen().equals("1")) {
                                TablePersonaliseDefault defaultSec = new TablePersonaliseDefault(NetConstants.PERSONALISE_CATEGORY_NEWS, bean.getCustomScreenPri(), bean.getSecId(), tableSection.getSecId(), bean.getSecName(), true, false);
                                if(allPersonalised.contains(defaultSec)) {
                                    defaultSec.setUserPreffered(true);
                                    mAlreadySelectionSecIds.add(bean.getSecId());
                                }
                                AllSectionListSelected.add(defaultSec);

                            }
                        }
                    }

                    List<TablePersonaliseDefault> SectionListSelected = new ArrayList<>();
                    List<TablePersonaliseDefault> SectionListNotSelected = new ArrayList<>();

                    for(TablePersonaliseDefault tablePersonalise : AllSectionListSelected) {
                        if(tablePersonalise.isUserPreffered()) {
                            SectionListSelected.add(tablePersonalise);
                        }
                    }

                    for(TablePersonaliseDefault tablePersonalise : AllSectionListSelected) {
                        if(!tablePersonalise.isUserPreffered()) {
                            SectionListNotSelected.add(tablePersonalise);
                        }
                    }

                    mSectionList.addAll(SectionListSelected);
                    mSectionList.addAll(SectionListNotSelected);

                    Log.i("", "");
                    return "";
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(val->{
                    final LayoutInflater mInflater = LayoutInflater.from(getActivity());
                    if (mSectionList != null && mSectionList.size() > 0) {
                        mFlowLayout.setAdapter(new TagAdapter<TablePersonaliseDefault>(mSectionList) {

                            @Override
                            public View getView(FlowLayout parent, int position, TablePersonaliseDefault section) {
                                View view = mInflater.inflate(R.layout.customize_news_feed_item, mFlowLayout, false);
                                TextView tv = view.findViewById(R.id.news_feed_section_name);
                                tv.setText(section.getDisplayName());
                                if(section.isUserPreffered()) {
                                    tv.setTextColor(getResources().getColor(android.R.color.white));
                                    view.setBackground(ResUtil.getBackgroundDrawable(getResources(), R.drawable.flowlayout_checked_item));
                                } else {
                                    tv.setTextColor(getResources().getColor(R.color.color_customize_text_normal));
                                    view.setBackground(ResUtil.getBackgroundDrawable(getResources(), R.drawable.flowlayout_normal_background));
                                }
                                return view;
                            }
                        });
                    }
                });


        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        mFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                TextView mSectionTextView = view.findViewById(R.id.news_feed_section_name);
                if (mSectionList.get(position).isUserPreffered()) {
                    mSectionTextView.setTextColor(getResources().getColor(android.R.color.white));
                    view.setBackground(ResUtil.getBackgroundDrawable(getResources(), R.drawable.flowlayout_checked_item));
                } else {
                    mSectionTextView.setTextColor(getResources().getColor(R.color.color_customize_text_normal));
                    view.setBackground(ResUtil.getBackgroundDrawable(getResources(), R.drawable.flowlayout_normal_background));
                }
                return true;
            }
        });


        mFlowLayout.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(int selectPosSet) {
                TablePersonaliseDefault personaliseDefault = mSectionList.get(selectPosSet);
                personaliseDefault.setUserPreffered(!personaliseDefault.isUserPreffered());
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        /*GoogleAnalyticsTracker.setGoogleAnalyticScreenName(getActivity(), "Customize News Feed Screen");
        AppFirebaseAnalytics.setFirbaseAnalyticsScreenRecord(getActivity(), "Customize News Feed Screen", CustomizeNewsFeedFragment.class.getSimpleName());
        FlurryAgent.logEvent("Customize News Feed Screen");
        FlurryAgent.onPageView();*/

        THPFirebaseAnalytics.setFirbaseAnalyticsScreenRecord(getActivity(), "Customize News Feed Screen", CustomizeNewsFeedFragment.class.getSimpleName());
    }


    private boolean isFragmentVisibleToUser;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isFragmentVisibleToUser = isVisibleToUser;
        if (isVisibleToUser && mMainActivity != null) {
            mMainActivity.firstFragmentBtn();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (isFragmentVisibleToUser && mMainActivity != null) {
            mMainActivity.firstFragmentBtn();
        }
    }

    public void nextButtonClicked() {

        ArrayList<String> selectedSectionsName = new ArrayList<>();

        for(TablePersonaliseDefault personaliseDefault : mSectionList) {
            if(personaliseDefault.isUserPreffered()) {
                selectedSectionsName.add(personaliseDefault.getDisplayName());
            }
        }

        if (selectedSectionsName.size() < 5) {
            Toast.makeText(mMainActivity, getString(R.string.select_atleast_five_section), Toast.LENGTH_LONG).show();
            return;
        }

        //CleverTap
        HashMap<String,Object> map = new HashMap<>();
        map.put(THPConstants.CT_KEY_Topics, TextUtils.join(", ", selectedSectionsName));
        CleverTapUtil.cleverTapEvent(getActivity(), THPConstants.CT_EVENT_PERSONALIZE_HOME_SCREEN,map);

        Observable.just("")
                .subscribeOn(Schedulers.io())
                .map(v->{
                    THPDB thpdb = THPDB.getInstance(getActivity());
                    DaoPersonaliseDefault daoPersonaliseDefault = thpdb.daoPersonaliseDefault();
                    daoPersonaliseDefault.delete(NetConstants.PERSONALISE_CATEGORY_NEWS);
                    for(TablePersonaliseDefault personaliseDefault : mSectionList) {
                        String secId = personaliseDefault.getPersonaliseSecId();
                        if(!mAlreadySelectionSecIds.contains(secId)) {
                            mMainActivity.setIsOptionsChanged(true);
                        }
                        if(personaliseDefault.isUserPreffered()) {
                            daoPersonaliseDefault.insertDefaultPersonalise(personaliseDefault);
                        }
                    }
                    return "";
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(v->{

                    if(BuildConfig.IS_BL) {
                        mMainActivity.skipOrPreviousButtonFunctionality();
                    }

                });


        /*GoogleAnalyticsTracker.setGoogleAnalyticsEvent(
                getActivity(),
                getString(R.string.ga_action),
                "Customize news feed: Save button clicked",
                getString(R.string.custom_home_screen));
        FlurryAgent.logEvent("Customize news feed: Save button clicked");*/

        //Firebase event
        THPFirebaseAnalytics.setFirbaseAnalyticsEvent(getActivity(), "Action", "Customize news feed: Save button clicked", CustomizeNewsFeedFragment.class.getSimpleName());

        if (mMainActivity != null && !BuildConfig.IS_BL) {
            mMainActivity.setViewPagerItem(1);
        }

    }
}
