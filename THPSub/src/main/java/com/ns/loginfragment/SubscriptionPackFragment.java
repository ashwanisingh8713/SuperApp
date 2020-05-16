package com.ns.loginfragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.netoperation.model.TxnDataBean;
import com.netoperation.model.UserProfile;
import com.netoperation.net.ApiManager;
import com.ns.activity.THPUserProfileActivity;
import com.ns.adapter.SubscriptionPackAdapter;
import com.ns.callbacks.OnPlanInfoLoad;
import com.ns.callbacks.OnSubscribeBtnClick;
import com.ns.callbacks.OnSubscribeEvent;
import com.ns.thpremium.BuildConfig;
import com.ns.thpremium.R;
import com.ns.utils.THPFirebaseAnalytics;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;


public class SubscriptionPackFragment extends BaseFragmentTHP  implements OnSubscribeBtnClick {

    private RecyclerView mRecyclerView;

    private SubscriptionPackAdapter mAdapter;

    private String mFrom;
    private UserProfile mUserProfile;

    private THPUserProfileActivity mTHPUserProfileActivity;
    private OnSubscribeBtnClick mOnSubscribeBtnClick;
    private OnPlanInfoLoad mPlanInfoLoad;

    public void setOnSubscribeBtnClick(OnSubscribeBtnClick subscribeBtnClick) {
        mOnSubscribeBtnClick = subscribeBtnClick;
    }

    public void setOnPlanInfoLoad(OnPlanInfoLoad onPlanInfoLoad) {
        mPlanInfoLoad = onPlanInfoLoad;
    }

    public static SubscriptionPackFragment getInstance(String from) {
        Bundle bundle = new Bundle();
        bundle.putString("from", from);
        SubscriptionPackFragment fragment = new SubscriptionPackFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_subscription_pack;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof THPUserProfileActivity) {
            mTHPUserProfileActivity = (THPUserProfileActivity) context;
        }
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        if(context instanceof THPUserProfileActivity) {
            mTHPUserProfileActivity = (THPUserProfileActivity) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null) {
            mFrom = getArguments().getString("from");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = view.findViewById(R.id.recyclerView);

        // Setting Listener subscription Button Click
        setOnSubscribeBtnClick(mTHPUserProfileActivity);

        // Setting Lisener to listen, when Plan is loaded
        setOnPlanInfoLoad(mTHPUserProfileActivity);

        // Listens Subscription Payment Success and Failure
        mTHPUserProfileActivity.setOnSubscribeEvent(new OnSubscribeEvent() {
            @Override
            public void onSubscribeEvent(boolean isSuccess) {

            }
        });

        LinearLayoutManager llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(llm);


        mDisposable.add(ApiManager.getUserProfile(getActivity())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userProfile -> {
                    mUserProfile = userProfile;
                    mUserId = userProfile.getUserId();
                    /*if(mFrom != null && mFrom.equalsIgnoreCase(THPConstants.FROM_PROFILE_VIEWALL)) {
                        loadUserPlanData();
                    }
                    else if(mFrom != null && mFrom.equalsIgnoreCase(THPConstants.FROM_SUBSCRIPTION_EXPLORE)) {
                        recommendedPlan();
                    }*/
                    //Always show recommended plans
                    recommendedPlan();
                }));



    }

    /**
     * Load User Plan Info
     */
    private void loadUserPlanData() {
        mDisposable.add(ApiManager.getUserPlanInfo(mUserProfile.getAuthorization(), mUserId, BuildConfig.SITEID)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(planInfoList->{
                    if(planInfoList.size() != 0) {
                        mAdapter = new SubscriptionPackAdapter(mFrom, planInfoList, this);
                        mRecyclerView.setAdapter(mAdapter);
                    }
                    else {
                        recommendedPlan();
                    }
                }, throwable -> {
                    recommendedPlan();
                }, ()->{

                }));
    }

    private void recommendedPlan() {
        mDisposable.add(ApiManager.getRecommendedPlan(mUserId, BuildConfig.SITEID)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(planInfoList -> {
                            mAdapter = new SubscriptionPackAdapter(mFrom, planInfoList, this);
                            if(planInfoList == null || planInfoList.size()==0) {
                                planInfoList = new ArrayList<>();
                                TxnDataBean bean = new TxnDataBean();
                                bean.setPlanName("Currently we don't have any recommended subscription plans for you.");
                                planInfoList.add(bean);
                                mAdapter.setEmpty(true, planInfoList);
                            }
                            mRecyclerView.setAdapter(mAdapter);
                            if(mPlanInfoLoad != null) {
                                mPlanInfoLoad.onPlansLoaded(planInfoList);
                            }

                        },
                        throwable -> {
                            Log.i("", "");
                            if(getActivity() != null && getView() != null) {
                                ArrayList planInfoList = new ArrayList<>();
                                mAdapter = new SubscriptionPackAdapter(mFrom, planInfoList, this);
                                if(planInfoList == null || planInfoList.size()==0) {
                                    TxnDataBean bean = new TxnDataBean();
                                    bean.setPlanName(getString(R.string.please_check_ur_connectivity));
                                    planInfoList.add(bean);
                                    mAdapter.setEmpty(true, planInfoList);
                                }
                                mRecyclerView.setAdapter(mAdapter);
                                if(mPlanInfoLoad != null) {
                                    mPlanInfoLoad.onPlansLoaded(planInfoList);
                                }
                            }
                        }));

    }


    @Override
    public void onSubscribeBtnClick(TxnDataBean bean) {
        if(mOnSubscribeBtnClick != null) {
            mOnSubscribeBtnClick.onSubscribeBtnClick(bean);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        THPFirebaseAnalytics.setFirbaseAnalyticsScreenRecord(getActivity(), "Subscription Pack Screen", SubscriptionPackFragment.class.getSimpleName());
    }




}
