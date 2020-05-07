package com.ns.loginfragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.netoperation.model.TxnDataBean;
import com.netoperation.net.ApiManager;
import com.netoperation.util.PremiumPref;
import com.ns.activity.BaseAcitivityTHP;
import com.ns.adapter.SubscriptionPlanAdapter;
import com.ns.alerts.Alerts;
import com.ns.clevertap.CleverTapUtil;
import com.ns.model.SubscriptionPlanModel;
import com.ns.thpremium.BuildConfig;
import com.ns.thpremium.R;
import com.ns.utils.FragmentUtil;
import com.ns.utils.ResUtil;
import com.ns.utils.THPConstants;
import com.ns.utils.THPFirebaseAnalytics;
import com.ns.view.text.CustomTextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SubscriptionStep_3_Fragment extends BaseFragmentTHP {

    private String mFrom;

    private RecyclerView recyclerView;

    private LinearLayout emptyLayout;
    private ImageView emptyIcon;
    private CustomTextView emptyTitleTxt;
    private CustomTextView emptySubTitleTxt;
    private CustomTextView emptyBtnTxt;

    public static SubscriptionStep_3_Fragment getInstance(String from) {
        SubscriptionStep_3_Fragment fragment = new SubscriptionStep_3_Fragment();
        Bundle bundle = new Bundle();
        bundle.putString("from", from);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_subscription_step_3;
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

        recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(llm);

        // Initialing Empty Layout
        initEmptyLayoutAtLoading();


        // Back button click listener
        view.findViewById(R.id.backBtn).setOnClickListener(v-> {
                    if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0)
                        FragmentUtil.clearSingleBackStack((AppCompatActivity) getActivity());
                    else
                        getActivity().onBackPressed();
                    }
        );

        view.findViewById(R.id.exploreSubscriptionPlans_Txt).setOnClickListener(view1 -> {
            if(BaseAcitivityTHP.sIsOnline) {
                RecoPlansWebViewFragment fragment = RecoPlansWebViewFragment.getInstance("");
                FragmentUtil.addFragmentAnim((AppCompatActivity) getActivity(), R.id.parentLayout, fragment, FragmentUtil.FRAGMENT_NO_ANIMATION, false);
                //CleverTap
                HashMap<String, Object> map = new HashMap<>();
                map.put(THPConstants.CT_KEY_Explore_subscription_plan, "Yes");
                CleverTapUtil.cleverTapEvent(getActivity(), THPConstants.CT_EVENT_PROFILE, map);
            } else {
                noConnectionSnackBar(getView());
            }
        });

        showProgressDialog("Fetching purchased plans...");
        //loadData();
    }


    /**
     * Initializing all sub-views of Empty Layout
     */
    private void initEmptyLayoutAtLoading() {
        emptyLayout = getView().findViewById(R.id.emptyLayout);

        emptyIcon = getView().findViewById(R.id.emptyIcon);
        emptyTitleTxt = getView().findViewById(R.id.emptyTitleTxt);
        emptySubTitleTxt = getView().findViewById(R.id.emptySubTitleTxt);
        emptyBtnTxt = getView().findViewById(R.id.emptyBtnTxt);

        emptyBtnTxt.setVisibility(View.INVISIBLE);
        emptyBtnTxt.setEnabled(false);
        emptyTitleTxt.setVisibility(View.INVISIBLE);
        emptySubTitleTxt.setVisibility(View.INVISIBLE);

        emptyIcon.setImageResource(R.drawable.ic_empty_watermark);


    }

    @Override
    public void onResume() {
        super.onResume();

        if(mFrom != null && mFrom.equalsIgnoreCase(THPConstants.FROM_USER_ACCOUNT_CREATED)) {
            Observable.just("justDealy")
                    .subscribeOn(Schedulers.newThread())
                    .delay(5, TimeUnit.SECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(vaule->{
                        loadData();
                    });
        } else {
            loadData();
        }

        THPFirebaseAnalytics.setFirbaseAnalyticsScreenRecord(getActivity(), "My Subscription Plans", SubscriptionStep_3_Fragment.class.getSimpleName());

    }

    private ArrayList<SubscriptionPlanModel> subscriptionPlanModels;

    private void loadData() {
        subscriptionPlanModels = new ArrayList<>();

        mDisposable.add(ApiManager.getUserProfile(getActivity())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userProfile -> {
                            if (userProfile != null) {
                                mUserId = userProfile.getUserId();
                                if (userProfile.getUserPlanList() != null && userProfile.getUserPlanList().size() > 0) {
                                    // Here we are adding Subscribed Plan Title and Subtitle
                                    SubscriptionPlanModel txnTitleModel = new SubscriptionPlanModel(SubscriptionPlanAdapter.VT_PURCHASED_TITLE);
                                    txnTitleModel.setTitle("Subscription Plans");
                                    txnTitleModel.setSubTitle("Your Active Plan");
                                    subscriptionPlanModels.add(txnTitleModel);

                                    ArrayList<TxnDataBean> planBeans = userProfile.getUserPlanList();

                                    ArrayList<TxnDataBean> activePlan = new ArrayList<>();

                                    // As per requirement only one active plan should be shown.
                                    for(TxnDataBean bean : planBeans) {
                                        if(bean.getIsActive() == 1) {
                                            activePlan.add(bean);
                                            break;
                                        }
                                    }

                                    // Here we are adding horizontal recycler list data
                                    SubscriptionPlanModel model = new SubscriptionPlanModel(SubscriptionPlanAdapter.VT_HORIZONTAL_RECYCLER);
                                    model.setUserPlanDataBean(activePlan);
                                    subscriptionPlanModels.add(model);

                                    //CleverTap
                                    HashMap<String, Object> map = new HashMap<>();
                                    map.put(THPConstants.CT_KEY_Subscription_details, "Yes");
                                    CleverTapUtil.cleverTapEvent(getActivity(), THPConstants.CT_EVENT_PROFILE, map);

                                } else {
                                    fetchLatestUserInfo();
                                    return;
                                    /*SubscriptionPlanModel txnTitleModel = new SubscriptionPlanModel(SubscriptionPlanAdapter.VT_PURCHASED_TITLE);
                                    txnTitleModel.setTitle("Subscription Plans");
                                    txnTitleModel.setSubTitle("Currently you don't have any active plan.");
                                    subscriptionPlanModels.add(txnTitleModel);*/
                                }

                            ApiManager.getUserPlanInfo(userProfile.getUserId(), BuildConfig.SITEID)
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(txnDataBeans -> {
                                        hideProgressDialog();
                                        if (txnDataBeans != null) {
                                            Collections.sort(txnDataBeans, (txnDataBean, t1) -> t1.getIsActive() - txnDataBean.getIsActive());
                                        }

                                        // Here we are adding Purchased Plan Title and Subtitle
                                        if(txnDataBeans != null && txnDataBeans.size() > 0) {
                                            SubscriptionPlanModel txnTitleModel = new SubscriptionPlanModel(SubscriptionPlanAdapter.VT_PURCHASED_TITLE);
                                            txnTitleModel.setTitle("Purchased Plans");
                                            txnTitleModel.setSubTitle("Your list of purchased plan");
                                            subscriptionPlanModels.add(txnTitleModel);
                                        }
                                        else {
                                            SubscriptionPlanModel txnTitleModel = new SubscriptionPlanModel(SubscriptionPlanAdapter.VT_PURCHASED_TITLE);
                                            txnTitleModel.setTitle("Purchased Plans");
                                            txnTitleModel.setSubTitle("You have not purchased any plans yet");
                                            subscriptionPlanModels.add(txnTitleModel);
                                        }

                                        for(TxnDataBean bean : txnDataBeans) {
                                            // Here we are adding Purchased Plan Item view data
                                            SubscriptionPlanModel model1 = new SubscriptionPlanModel(SubscriptionPlanAdapter.VT_PURCHASED_ITEM);
                                            model1.setTxnDataBean(bean);
                                            subscriptionPlanModels.add(model1);
                                        }

                                        // This is MutliView Recycler Adapter
                                        SubscriptionPlanAdapter adapter = new SubscriptionPlanAdapter(subscriptionPlanModels);
                                        recyclerView.setAdapter(adapter);
                                    }, throwable -> {
                                        if(getActivity() != null && getView() != null) {
                                            hideProgressDialog();
                                            Alerts.showSnackbar(getActivity(), getResources().getString(R.string.something_went_wrong));
                                        }
                                    }, () -> {
                                        if(getActivity() != null && getView() != null) {
                                            hideProgressDialog();
                                        }
                                    });
                        }
    }
                        , throwable -> {

                        }));
    }


    // Fetch latest userinfo from server
    private void fetchLatestUserInfo() {
        String loginId = PremiumPref.getInstance(getActivity()).getLoginTypeId();
        String loginPasswd = PremiumPref.getInstance(getActivity()).getLoginPasswd();
        ApiManager.getUserInfoObject(getActivity(), BuildConfig.SITEID,
                ResUtil.getDeviceId(getActivity()), mUserId, loginId, loginPasswd)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userProfile -> {
                    if (userProfile != null) {
                        if (userProfile.getUserPlanList() != null && userProfile.getUserPlanList().size() > 0) {
                            // Here we are adding Subscribed Plan Title and Subtitle
                            SubscriptionPlanModel txnTitleModel = new SubscriptionPlanModel(SubscriptionPlanAdapter.VT_PURCHASED_TITLE);
                            txnTitleModel.setTitle("Subscription Plans");
                            txnTitleModel.setSubTitle("Your Active Plan");
                            subscriptionPlanModels.add(txnTitleModel);

                            ArrayList<TxnDataBean> planBeans = userProfile.getUserPlanList();
                            // Here we are adding horizontal recycler list data
                            SubscriptionPlanModel model = new SubscriptionPlanModel(SubscriptionPlanAdapter.VT_HORIZONTAL_RECYCLER);
                            model.setUserPlanDataBean(planBeans);
                            subscriptionPlanModels.add(model);
                            hideEmptyLayout();
                        } else {
                            SubscriptionPlanModel txnTitleModel = new SubscriptionPlanModel(SubscriptionPlanAdapter.VT_PURCHASED_TITLE);
                            txnTitleModel.setTitle("Subscription Plans");
                            txnTitleModel.setSubTitle("Currently you don't have any active plan"); //Change also in SubscriptionPlanAdapter
                            subscriptionPlanModels.add(txnTitleModel);
                            hideEmptyLayout();

                            //CleverTap
                            HashMap<String, Object> map = new HashMap<>();
                            map.put(THPConstants.CT_KEY_Subscription_details, "No");
                            CleverTapUtil.cleverTapEvent(getActivity(), THPConstants.CT_EVENT_PROFILE, map);
                        }

                        ApiManager.getUserPlanInfo(userProfile.getUserId(), BuildConfig.SITEID)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(txnDataBeans -> {
                                    hideEmptyLayout();
                                    hideProgressDialog();
                                    if (txnDataBeans != null) {
                                        Collections.sort(txnDataBeans, (txnDataBean, t1) -> t1.getIsActive() - txnDataBean.getIsActive());
                                    }

                                    // Here we are adding Purchased Plan Title and Subtitle
                                    if (txnDataBeans != null && txnDataBeans.size() > 0) {
                                        SubscriptionPlanModel txnTitleModel = new SubscriptionPlanModel(SubscriptionPlanAdapter.VT_PURCHASED_TITLE);
                                        txnTitleModel.setTitle("Purchased Plans");
                                        txnTitleModel.setSubTitle("Your list of purchased plan");
                                        subscriptionPlanModels.add(txnTitleModel);
                                    } else {
                                        SubscriptionPlanModel txnTitleModel = new SubscriptionPlanModel(SubscriptionPlanAdapter.VT_PURCHASED_TITLE);
                                        txnTitleModel.setTitle("Purchased Plans");
                                        txnTitleModel.setSubTitle("You have not purchased any plans yet");
                                        subscriptionPlanModels.add(txnTitleModel);
                                    }

                                    for (TxnDataBean bean : txnDataBeans) {
                                        // Here we are adding Purchased Plan Item view data
                                        SubscriptionPlanModel model1 = new SubscriptionPlanModel(SubscriptionPlanAdapter.VT_PURCHASED_ITEM);
                                        model1.setTxnDataBean(bean);
                                        subscriptionPlanModels.add(model1);
                                    }

                                    // This is MutliView Recycler Adapter
                                    SubscriptionPlanAdapter adapter = new SubscriptionPlanAdapter(subscriptionPlanModels);
                                    recyclerView.setAdapter(adapter);
                                }, throwable -> {
                                    if(getActivity() != null && getView() != null) {
                                        hideProgressDialog();
                                        showEmptyLayout();
                                    }
                                }, () -> {
                                    if(getActivity() != null && getView() != null) {
                                        hideProgressDialog();
                                    }
                                });
                    }
                }, thr -> {
                    if(getActivity() != null && getView() != null) {
                        showEmptyLayout();
                        hideProgressDialog();
                    }
                }, ()->{
                    if(getActivity() != null && getView() != null) {
                        hideProgressDialog();
                    }
                } );
    }

    private void hideEmptyLayout() {
        emptyLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }


    private void showEmptyLayout() {
        emptyLayout.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        emptyIcon = getView().findViewById(R.id.emptyIcon);
        emptyTitleTxt = getView().findViewById(R.id.emptyTitleTxt);
        emptySubTitleTxt = getView().findViewById(R.id.emptySubTitleTxt);
        emptyBtnTxt = getView().findViewById(R.id.emptyBtnTxt);


        emptyIcon.setImageResource(R.drawable.ic_empty_something_wrong);
        emptyTitleTxt.setVisibility(View.VISIBLE);
        emptyTitleTxt.setText("Oops...");
        emptySubTitleTxt.setText("Something went wrong");
        emptySubTitleTxt.setVisibility(View.VISIBLE);
        emptyBtnTxt.setVisibility(View.VISIBLE);
        emptyBtnTxt.setText("Refresh");
        emptyBtnTxt.setEnabled(true);
        emptyBtnTxt.setOnClickListener(v -> {
            if (!BaseAcitivityTHP.sIsOnline) {
                noConnectionSnackBar(getView());
                return;
            }
            loadData();
        });
    }



}
