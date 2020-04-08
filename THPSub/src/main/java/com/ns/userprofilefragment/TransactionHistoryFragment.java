package com.ns.userprofilefragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.netoperation.net.ApiManager;
import com.ns.adapter.TransactionHistoryAdapter;
import com.ns.alerts.Alerts;
import com.ns.clevertap.CleverTapUtil;
import com.ns.loginfragment.BaseFragmentTHP;
import com.ns.thpremium.BuildConfig;
import com.ns.thpremium.R;
import com.ns.utils.FragmentUtil;
import com.ns.utils.THPConstants;
import com.ns.utils.THPFirebaseAnalytics;
import com.ns.view.RecyclerViewPullToRefresh;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class TransactionHistoryFragment extends BaseFragmentTHP {

    private String mFrom;

    private RecyclerViewPullToRefresh mRecyclerViewPullToRefresh;
    private TransactionHistoryAdapter adapter;


    @Override
    public int getLayoutRes() {
        return R.layout.fragment_transaction_history;
    }

    public static TransactionHistoryFragment getInstance(String from) {
        TransactionHistoryFragment fragment = new TransactionHistoryFragment();
        Bundle bundle = new Bundle();
        bundle.putString("from", from);
        fragment.setArguments(bundle);
        return fragment;
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

        mRecyclerViewPullToRefresh = view.findViewById(R.id.recyclerViewPullToRefresh);
        mRecyclerViewPullToRefresh.enablePullToRefresh(false);
        mRecyclerViewPullToRefresh.setTryAgainBtnClickListener(this::loadData);

        // Back button click listener
        view.findViewById(R.id.backBtn).setOnClickListener(v->{
            FragmentUtil.clearSingleBackStack((AppCompatActivity)getActivity());
        });

        loadData();

    }




    private void loadData() {
        mRecyclerViewPullToRefresh.showProgressBar();
        mDisposable.add(ApiManager.getUserProfile(getActivity())
                .subscribe(userProfile ->
                    ApiManager.getTxnHistory(userProfile.getUserId(), BuildConfig.SITEID)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(txnDataBeans -> {
                                mRecyclerViewPullToRefresh.hideProgressBar();
                                adapter = new TransactionHistoryAdapter(txnDataBeans, "HISTORY");
                                mRecyclerViewPullToRefresh.setDataAdapter(adapter);
                            }, throwable -> {
                                mRecyclerViewPullToRefresh.hideProgressBar();
                                mRecyclerViewPullToRefresh.showTryAgainBtn(getString(R.string.connectivity_try_again));
                                Alerts.showErrorDailog(getChildFragmentManager(), getResources().getString(R.string.kindly), getResources().getString(R.string.please_check_ur_connectivity));
                            }, () -> {
                                mRecyclerViewPullToRefresh.hideProgressBar();
                                if(adapter == null || adapter.getItemCount()==0) {
                                    mRecyclerViewPullToRefresh.showTryAgainBtn("You have not purchased any subscription plan.");
                                    //CleverTap
                                    HashMap<String,Object> map = new HashMap<>();
                                    map.put(THPConstants.CT_KEY_Transaction_history,"No");
                                    CleverTapUtil.cleverTapEvent(getActivity(),THPConstants.CT_EVENT_PROFILE,map);
                                }else{
                                    //CleverTap
                                    HashMap<String,Object> map = new HashMap<>();
                                    map.put(THPConstants.CT_KEY_Transaction_history,"Yes");
                                    CleverTapUtil.cleverTapEvent(getActivity(),THPConstants.CT_EVENT_PROFILE,map);

                                }
                            })
                , throwable -> {
                            mRecyclerViewPullToRefresh.hideProgressBar();
                }));
    }

    @Override
    public void onResume() {
        super.onResume();
        THPFirebaseAnalytics.setFirbaseAnalyticsScreenRecord(getActivity(), "Transaction History Screen", TransactionHistoryFragment.class.getSimpleName());
    }


}
