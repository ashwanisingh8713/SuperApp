package com.ns.thd_frag;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.netoperation.db.THPDB;
import com.netoperation.default_db.DaoPersonaliseDefault;
import com.netoperation.default_db.DaoSection;
import com.netoperation.default_db.TablePersonaliseDefault;
import com.netoperation.default_db.TableSection;
import com.netoperation.model.SectionBean;
import com.netoperation.util.NetConstants;
import com.ns.activity.CustomizeHomeScreenActivity;
import com.ns.alerts.Alerts;
import com.ns.clevertap.CleverTapUtil;
import com.ns.model.SectionBasicData;
import com.ns.thpremium.R;
import com.ns.utils.ResUtil;
import com.ns.utils.THPConstants;
import com.ns.view.flowlayout.FlowLayout;
import com.ns.view.flowlayout.TagAdapter;
import com.ns.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * A simple {@link Fragment} subclass.
 */
public class CitiesInterestFragment extends Fragment {

    private TagFlowLayout mFlowLayout;
    private TagAdapter<TablePersonaliseDefault> mAdapter;
    private List<TablePersonaliseDefault> mSectionList = new ArrayList<>();
    private boolean isCity;
    private CustomizeHomeScreenActivity mMainActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mMainActivity = (CustomizeHomeScreenActivity) activity;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMainActivity = (CustomizeHomeScreenActivity) context;
    }

    public static CitiesInterestFragment newInstance(boolean isCity) {
        CitiesInterestFragment fragment = new CitiesInterestFragment();
        Bundle args = new Bundle();
        args.putBoolean(THPConstants.IS_CITY, isCity);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.isCity = getArguments().getBoolean(THPConstants.IS_CITY);
        }
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mRootView = inflater.inflate(R.layout.fragment_cities_interest, container, false);

        mFlowLayout = mRootView.findViewById(R.id.flowlayout_city_interest);

        Observable.just("")
                .subscribeOn(Schedulers.newThread())
                .map(val->{
                    THPDB db = THPDB.getInstance(getActivity());
                    DaoSection daoSection = db.daoSection();
                    DaoPersonaliseDefault daoPersonaliseDefault = db.daoPersonaliseDefault();
                    List<TableSection> allList = daoSection.getSections();
                    List<TablePersonaliseDefault> allPersonalised = daoPersonaliseDefault.getCategoryPersonalise(NetConstants.PERSONALISE_CATEGORY_CITY);

                    for(TableSection tableSection : allList) {
                        if(tableSection.getCustomScreen().equals("2")) {
                            TablePersonaliseDefault defaultSec = new TablePersonaliseDefault(NetConstants.PERSONALISE_CATEGORY_CITY, tableSection.getCustomScreenPri(),tableSection.getSecId(), null, tableSection.getSecName(), false, false);
                            if(allPersonalised.contains(defaultSec)) {
                                defaultSec.setUserPreffered(true);
                            }
                            mSectionList.add(defaultSec);
                        }
                        List<SectionBean> subSection = tableSection.getSubSections();
                        for(SectionBean bean : subSection) {
                            if(bean.getCustomScreen().equals("2")) {
                                TablePersonaliseDefault defaultSec = new TablePersonaliseDefault(NetConstants.PERSONALISE_CATEGORY_CITY, bean.getCustomScreenPri(), bean.getSecId(), tableSection.getSecId(), bean.getSecName(), true, false);
                                if(allPersonalised.contains(defaultSec)) {
                                    defaultSec.setUserPreffered(true);
                                }
                                mSectionList.add(defaultSec);
                            }
                        }
                    }
                    Log.i("", "");
                    return "";
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(val->{
                    final LayoutInflater mInflater = LayoutInflater.from(getActivity());

                    if (mSectionList != null && mSectionList.size() > 0) {
                        mFlowLayout.setAdapter(mAdapter = new TagAdapter<TablePersonaliseDefault>(mSectionList) {

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
        /*GoogleAnalyticsTracker.setGoogleAnalyticScreenName(getActivity(), "City Interest Screen");
        AppFirebaseAnalytics.setFirbaseAnalyticsScreenRecord(getActivity(), "City Interest Screen", CitiesInterestFragment.class.getSimpleName());

        FlurryAgent.logEvent("City interest screen");
        FlurryAgent.onPageView();*/
    }



    private boolean isFragmentVisibleToUser;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isFragmentVisibleToUser = isVisibleToUser;
        if (isVisibleToUser && mMainActivity != null) {
            mMainActivity.setNextButtonText("SAVE");
            mMainActivity.setVisiblityOfPriviousButton(View.VISIBLE);
            mMainActivity.setSkipButtonText("Previous");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (isFragmentVisibleToUser && mMainActivity != null) {
            mMainActivity.setNextButtonText("SAVE");
            mMainActivity.setVisiblityOfPriviousButton(View.VISIBLE);
            mMainActivity.setSkipButtonText("Previous");
        }
    }

    public void saveButtonClicked() {
        /*GoogleAnalyticsTracker.setGoogleAnalyticsEvent(
                getActivity(),
                getString(R.string.ga_action),
                "Cities of interest: Save button clicked",
                getString(R.string.title_city_interest));
        FlurryAgent.logEvent("Cities of interest: Save button clicked");*/

        ArrayList<String> selectedSectionsName = new ArrayList<>();

        for(TablePersonaliseDefault personaliseDefault : mSectionList) {
            if(personaliseDefault.isUserPreffered()) {
                selectedSectionsName.add(personaliseDefault.getDisplayName());
            }
        }

        Observable.just("")
                .subscribeOn(Schedulers.io())
                .map(v->{
                    THPDB thpdb = THPDB.getInstance(getActivity());
                    DaoPersonaliseDefault daoPersonaliseDefault = thpdb.daoPersonaliseDefault();
                    daoPersonaliseDefault.delete(NetConstants.PERSONALISE_CATEGORY_CITY);
                    for(TablePersonaliseDefault personaliseDefault : mSectionList) {
                        if(personaliseDefault.isUserPreffered()) {
                            daoPersonaliseDefault.insertDefaultPersonalise(personaliseDefault);
                        }
                    }
                    return "";
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(v->{
                    Alerts.showToastAtCenter(getActivity(), "Saved");
                    mMainActivity.finish();
                });

        //CleverTap
        HashMap<String,Object> map = new HashMap<>();
        map.put(THPConstants.CT_KEY_Cities,TextUtils.join(", ", selectedSectionsName));
        CleverTapUtil.cleverTapEvent(getActivity(), THPConstants.CT_EVENT_PERSONALIZE_HOME_SCREEN,map);


    }
}
