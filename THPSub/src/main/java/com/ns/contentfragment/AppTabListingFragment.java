package com.ns.contentfragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.netoperation.model.PersonaliseDetails;
import com.netoperation.model.PersonaliseModel;
import com.netoperation.model.ArticleBean;
import com.netoperation.net.ApiManager;
import com.netoperation.util.AppDateUtil;
import com.netoperation.util.NetConstants;
import com.ns.activity.BaseRecyclerViewAdapter;
import com.ns.adapter.AppTabContentAdapter;
import com.ns.callbacks.BackPressCallback;
import com.ns.callbacks.BackPressImpl;
import com.ns.callbacks.OnEditionBtnClickListener;
import com.ns.callbacks.THP_AppEmptyPageListener;
import com.ns.clevertap.CleverTapUtil;
import com.ns.loginfragment.BaseFragmentTHP;
import com.ns.model.AppTabContentModel;
import com.ns.thpremium.BuildConfig;
import com.ns.thpremium.R;
import com.ns.utils.FragmentUtil;
import com.ns.utils.IntentUtil;
import com.ns.utils.ResUtil;
import com.ns.utils.THPConstants;
import com.ns.utils.THPFirebaseAnalytics;
import com.ns.view.text.CustomTextView;
import com.ns.view.RecyclerViewPullToRefresh;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class AppTabListingFragment extends BaseFragmentTHP implements RecyclerViewPullToRefresh.TryAgainBtnClickListener
        , OnEditionBtnClickListener, THP_AppEmptyPageListener {

    private RecyclerViewPullToRefresh mPullToRefreshLayout;
    private AppTabContentAdapter mRecyclerAdapter;
    private String mBreifingType = NetConstants.BREIFING_ALL;
    private AppTabContentModel mProfileNameModel;
    private String mFrom;

    private LinearLayout emptyLayout;
    private ImageView emptyIcon;
    private CustomTextView emptyTitleTxt;
    private CustomTextView emptySubTitleTxt;
    private CustomTextView emptyBtnTxt;

    private long mPageStartTime = 0l;
    private long mPageEndTime = 0l;

    private int mTabIndex;

    public static AppTabListingFragment getInstance(int tabIndex, String userId, String from) {
        AppTabListingFragment fragment = new AppTabListingFragment();
        Bundle bundle = new Bundle();
        bundle.putString("userId", userId);
        bundle.putString("from", from);
        bundle.putInt("tabIndex", tabIndex);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_trending;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            mUserId = getArguments().getString("userId");
            mFrom = getArguments().getString("from");
            mTabIndex = getArguments().getInt("tabIndex");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPullToRefreshLayout = view.findViewById(R.id.recyclerView);
        emptyLayout = view.findViewById(R.id.emptyLayout);

        mRecyclerAdapter = new AppTabContentAdapter(new ArrayList<>(), mFrom, mUserId, mPullToRefreshLayout.getRecyclerView());
        mRecyclerAdapter.setOnEditionBtnClickListener(this::OnEditionBtnClickListener);
        mRecyclerAdapter.setAppEmptyPageListener(this :: checkPageEmpty);
        mPullToRefreshLayout.setDataAdapter(mRecyclerAdapter);

        mPullToRefreshLayout.setTryAgainBtnClickListener(this);

        mPullToRefreshLayout.showSmoothProgressBar();


        // Pull To Refresh Listener
        registerPullToRefresh();

            if(mIsVisible) {
                mPageStartTime = System.currentTimeMillis();
            }

    }



    @Override
    public void onResume() {
        super.onResume();
        Log.i("TabFragment", "onResume() TabIndex = "+mTabIndex);
        Log.i("TabFragment", "onResume() TabIndex = " + mTabIndex + " EventBus Registered");
        EventBus.getDefault().register(this);

        // Shows user name
        mDisposable.add(ApiManager.getUserProfile(getActivity())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userProfile -> {
                    String title = "";

                    if (userProfile != null && TextUtils.isEmpty(userProfile.getFullNameForProfile())) {
                        title = "Hi User";
                    } else if (userProfile != null && !TextUtils.isEmpty(userProfile.getFullNameForProfile())) {
                        title = "Hi " + userProfile.getFullNameForProfile();
                    } else if(userProfile != null && !TextUtils.isEmpty(userProfile.getEmailId())) {
                        title = userProfile.getEmailId();
                    } else if(userProfile != null && !TextUtils.isEmpty(userProfile.getContact())) {
                        title = userProfile.getContact();
                    }

                    //Create Header Model
                    if(mProfileNameModel != null) {
                        final ArticleBean bean = mProfileNameModel.getBean();
                        final String tt = bean.getTitle();
                        if(tt != null && !tt.equals(title)) {
                            createUserNameHeaderModel(title);
                        }
                    } else {
                        createUserNameHeaderModel(title);
                    }

//                    loadData();

                    if(mRecyclerAdapter != null && mRecyclerAdapter.getItemCount() > 0) {
                        /*loadData(false);*/
                        mRecyclerAdapter.notifyDataSetChanged();
                        loadData();
                    } else {
                        loadData();
                    }
                }));
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if(isVisibleToUser) {
            mPageStartTime = System.currentTimeMillis();
        }

        if(!isVisibleToUser) {
            if(mPageStartTime >= 1000) {
                mPageEndTime = System.currentTimeMillis();
                sendEventCapture(mPageStartTime, mPageEndTime);
                mPageStartTime = -1l;
                mPageEndTime = -1l;
            }
        }

    }

    /**
     * Adding Pull To Refresh Listener
     */
    private void registerPullToRefresh() {
        mPullToRefreshLayout.getSwipeRefreshLayout().setOnRefreshListener(()->{
            if(!mIsOnline) {
                noConnectionSnackBar(getView());
                mPullToRefreshLayout.setRefreshing(false);
                return;
            }
            mPullToRefreshLayout.setRefreshing(true);
            loadData();
        });
    }


    @Override
    public void tryAgainBtnClick() {
        mPullToRefreshLayout.showSmoothProgressBar();
        loadData();
    }

    private void loadData() {
        if(!mPullToRefreshLayout.isRefreshing()) {
            mPullToRefreshLayout.showSmoothProgressBar();
        }
        hideEmptyLayout();
        loadData(mIsOnline);
    }


    private void loadData(boolean isOnline ) {
        Observable<List<ArticleBean>> observable = null;
        if(mFrom.equalsIgnoreCase(NetConstants.BREIFING_ALL)) {
            if (isOnline) {

                String BREIGINE_URL = "";
                if(BuildConfig.IS_PRODUCTION) {
                    BREIGINE_URL = BuildConfig.PRODUCTION_BREIFING_URL;
                } else {
                    BREIGINE_URL = BuildConfig.STATGGING_BREIFING_URL;
                }

                observable = ApiManager.getBreifingFromServer(getActivity(), BREIGINE_URL, mBreifingType);
            } else {
                observable = ApiManager.getBreifingFromDB(getActivity(), mBreifingType);
            }
        }
        else {
            if (isOnline) {
                observable = ApiManager.getRecommendationFromServer(getActivity(), mUserId,
                        mFrom, ""+mSize, BuildConfig.SITEID);
            } else {
                observable = ApiManager.getRecommendationFromDB(getActivity(), mFrom, null);
            }
        }

        mDisposable.add(
                observable
                        .map(value->{
                            List<AppTabContentModel> content = new ArrayList<>();
                            if(value.size() > 0) {
                                addHeaderModel(content);
                            }
                            int viewType = BaseRecyclerViewAdapter.VT_DASHBOARD;
                            if(isBriefingPage()) {
                                viewType = BaseRecyclerViewAdapter.VT_BRIEFCASE;
                            } else {
                                viewType = BaseRecyclerViewAdapter.VT_DASHBOARD;
                            }
                            for(ArticleBean bean : value) {
                                AppTabContentModel model = new AppTabContentModel(viewType);
                                model.setBean(bean);
                                content.add(model);
                            }

                            return content;
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(value -> {
                            if (isBriefingPage()) {
                                mRecyclerAdapter.setFrom(mBreifingType);
                            } else {
                                mRecyclerAdapter.setFrom(mFrom);
                            }
                            mRecyclerAdapter.setData(value);
                        }, throwable -> {
                            loadData(false);
                            mPullToRefreshLayout.hideProgressBar();
                            mPullToRefreshLayout.setRefreshing(false);

                            Log.i("AshwaniE", "Error :: "+isOnline);

                        }, () -> {
                            mPullToRefreshLayout.hideProgressBar();
                            mPullToRefreshLayout.setRefreshing(false);
                            // Showing Empty Msg.
                            showEmptyLayout(isOnline);

                        }));

    }


    private Map<String, String> timeMap = new HashMap<>();

    @Override
    public void OnEditionBtnClickListener() {

        EditionOptionFragment fragment = EditionOptionFragment.getInstance();
        FragmentUtil.addFragmentAnim((AppCompatActivity) getActivity(),
                R.id.parentLayout, fragment, FragmentUtil.FRAGMENT_NO_ANIMATION, false);

        fragment.setOnEditionOptionClickListener(value -> {
            if(mProfileNameModel != null) {
                mProfileNameModel.getBean().setSectionName(value);
            }
            // Clearing Edition option Fragment
            FragmentUtil.clearSingleBackStack((AppCompatActivity) getActivity());

            if(value.equalsIgnoreCase("All Editions")) {
                if (!mBreifingType.equalsIgnoreCase(NetConstants.BREIFING_ALL)) {
                    //Capture Event
                    mPageEndTime = System.currentTimeMillis();
                    briefingEventCapture(mPageStartTime, mPageEndTime);
                    mPageStartTime = System.currentTimeMillis();
                }
                mBreifingType = NetConstants.BREIFING_ALL;
                THPFirebaseAnalytics.setFirbaseAnalyticsEvent(getContext(), "Action", "Briefing : All Editions clicked", AppTabListingFragment.class.getSimpleName());
            } else if(value.equalsIgnoreCase("Morning Editions")) {
                if (!mBreifingType.equalsIgnoreCase(NetConstants.BREIFING_MORNING)) {
                    //Capture Event
                    mPageEndTime = System.currentTimeMillis();
                    briefingEventCapture(mPageStartTime, mPageEndTime);
                    mPageStartTime = System.currentTimeMillis();
                }
                mBreifingType = NetConstants.BREIFING_MORNING;
                THPFirebaseAnalytics.setFirbaseAnalyticsEvent(getContext(), "Action", "Briefing : Morning Editions clicked", AppTabListingFragment.class.getSimpleName());
            } else if(value.equalsIgnoreCase("Noon Editions")) {
                if (!mBreifingType.equalsIgnoreCase(NetConstants.BREIFING_NOON)) {
                    //Capture Event
                    mPageEndTime = System.currentTimeMillis();
                    briefingEventCapture(mPageStartTime, mPageEndTime);
                    mPageStartTime = System.currentTimeMillis();
                }
                mBreifingType = NetConstants.BREIFING_NOON;
                THPFirebaseAnalytics.setFirbaseAnalyticsEvent(getContext(), "Action", "Briefing : Noon Editions clicked", AppTabListingFragment.class.getSimpleName());
            } else if(value.equalsIgnoreCase("Evening Editions")) {
                if (!mBreifingType.equalsIgnoreCase(NetConstants.BREIFING_EVENING)) {
                    //Capture Event
                    mPageEndTime = System.currentTimeMillis();
                    briefingEventCapture(mPageStartTime, mPageEndTime);
                    mPageStartTime = System.currentTimeMillis();
                }
                mBreifingType = NetConstants.BREIFING_EVENING;
                THPFirebaseAnalytics.setFirbaseAnalyticsEvent(getContext(), "Action", "Briefing : Evening Editions clicked", AppTabListingFragment.class.getSimpleName());
            }

            loadData(false);

        });
    }

    private boolean isBriefingPage() {
        return mFrom.equalsIgnoreCase(NetConstants.BREIFING_ALL) || mFrom.equalsIgnoreCase(NetConstants.BREIFING_MORNING)
                || mFrom.equalsIgnoreCase(NetConstants.BREIFING_NOON) || mFrom.equalsIgnoreCase(NetConstants.BREIFING_EVENING);
    }


    private void createUserNameHeaderModel(String title) {
        ArticleBean profileArticleBean = new ArticleBean();
        if (isBriefingPage()) {
            profileArticleBean.setSectionName("All Editions");
        } else if (mFrom.equalsIgnoreCase(NetConstants.RECO_Mystories)) {
            profileArticleBean.setSectionName("Yours personalised stories");
        } else if (mFrom.equalsIgnoreCase(NetConstants.RECO_suggested)) {
            profileArticleBean.setSectionName("Your suggested stories");
//            title = "Your suggested stories";
            profileArticleBean.setSectionName("Your suggested stories");
        } else if (mFrom.equalsIgnoreCase(NetConstants.RECO_trending)) {
            profileArticleBean.setSectionName("Trending now");
            title = "Trending now";
        }
        if(BuildConfig.DEBUG) {
//            profileArticleBean.setTitle(title + " :: " + getString(R.string.type_device));
            profileArticleBean.setTitle(title);
        } else {
            profileArticleBean.setTitle(title);
        }
        mProfileNameModel = new AppTabContentModel(BaseRecyclerViewAdapter.VT_HEADER, "userHeader");
        mProfileNameModel.setBean(profileArticleBean);
    }


    private void addHeaderModel(List<AppTabContentModel> content) {
        if(mProfileNameModel != null) {
            int index = content.indexOf(mProfileNameModel);
            if(index != -1) {
                content.remove(index);
            }
            content.add(mProfileNameModel);
        }
    }





    private void hideEmptyLayout() {
        if(mRecyclerAdapter == null || mRecyclerAdapter.getItemCount() < 1) {
            showEmptyLayoutAtLoading();
        } else {
            mPullToRefreshLayout.setVisibility(View.VISIBLE);
            emptyLayout.setVisibility(View.GONE);
        }
    }

    private void showEmptyLayoutAtLoading() {
        emptyLayout.setVisibility(View.VISIBLE);
        mPullToRefreshLayout.setVisibility(View.VISIBLE);

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

    private void showEmptyLayout(boolean isNoContent) {
        if(mRecyclerAdapter == null || mRecyclerAdapter.getItemCount() == 0) {
            emptyLayout.setVisibility(View.VISIBLE);
            mPullToRefreshLayout.setVisibility(View.GONE);

            emptyIcon = getView().findViewById(R.id.emptyIcon);
            emptyTitleTxt = getView().findViewById(R.id.emptyTitleTxt);
            emptySubTitleTxt = getView().findViewById(R.id.emptySubTitleTxt);
            emptyBtnTxt = getView().findViewById(R.id.emptyBtnTxt);

            if (isBriefingPage()) {
                if(isNoContent) {
                    emptyIcon.setImageResource(R.drawable.ic_empty_breifing);
                    emptyTitleTxt.setVisibility(View.INVISIBLE);
                    emptySubTitleTxt.setVisibility(View.VISIBLE);
                    emptySubTitleTxt.setText("No content in Breifing. Please look \n back after sometime");
                    emptyBtnTxt.setVisibility(View.VISIBLE);
                    emptyBtnTxt.setEnabled(true);
                    emptyBtnTxt.setText("Refresh");
                    emptyBtnTxt.setOnClickListener(v->{
                        loadData();
                    });
                }
                else {
                    if(!mIsOnline) {
                        noConnectionSnackBar(getView());
                    }
                    emptyIcon.setImageResource(R.drawable.ic_empty_something_wrong);
                    emptyTitleTxt.setVisibility(View.VISIBLE);
                    emptyTitleTxt.setText("Oops...");
                    emptySubTitleTxt.setText("Something went wrong");
                    emptyBtnTxt.setVisibility(View.VISIBLE);
                    emptySubTitleTxt.setVisibility(View.VISIBLE);
                    emptyBtnTxt.setText("Refresh");
                    emptyBtnTxt.setEnabled(true);
                    emptyBtnTxt.setOnClickListener(v->{
                        loadData();
                    });
                }
            } else if (mFrom.equalsIgnoreCase(NetConstants.RECO_Mystories)) {
                if(isNoContent) {
                    emptyIcon.setImageResource(R.drawable.ic_empty_watermark);
                    emptyTitleTxt.setVisibility(View.INVISIBLE);
                    emptySubTitleTxt.setVisibility(View.INVISIBLE);
                    emptyBtnTxt.setVisibility(View.INVISIBLE);
                    emptyBtnTxt.setEnabled(false);
                    // To set Empty Btn txt, if user has set preferences or not
                    getUserSavedPersonalise(mUserId);

                }
                else {
                    if(!mIsOnline) {
                        noConnectionSnackBar(getView());
                    }
                    emptyIcon.setImageResource(R.drawable.ic_empty_something_wrong);
                    emptyTitleTxt.setText("Oops...");
                    emptySubTitleTxt.setText("Something went wrong");
                    emptySubTitleTxt.setVisibility(View.VISIBLE);
                    emptyBtnTxt.setVisibility(View.VISIBLE);
                    emptyBtnTxt.setText("Refresh");
                    emptyBtnTxt.setEnabled(true);
                    emptyBtnTxt.setOnClickListener(v->{
                        if(!mIsOnline) {
                            noConnectionSnackBar(getView());
                            return;
                        }
                        loadData();
                    });
                }
            } else if (mFrom.equalsIgnoreCase(NetConstants.RECO_suggested)) {
                if(isNoContent) {
                    emptyIcon.setImageResource(R.drawable.ic_empty_suggestion);
                    emptyTitleTxt.setVisibility(View.INVISIBLE);
                    emptySubTitleTxt.setVisibility(View.VISIBLE);
                    emptySubTitleTxt.setText("No content in Suggestion. Please look \n back after sometime");
                    emptyBtnTxt.setVisibility(View.VISIBLE);
                    emptyBtnTxt.setEnabled(true);
                    emptyBtnTxt.setText("Refresh");
                    emptyBtnTxt.setOnClickListener(v->{
                        loadData();
                    });
                }
                else {
                    if(!mIsOnline) {
                        noConnectionSnackBar(getView());
                    }
                    emptyIcon.setImageResource(R.drawable.ic_empty_something_wrong);
                    emptyTitleTxt.setVisibility(View.VISIBLE);
                    emptyTitleTxt.setText("Oops...");
                    emptySubTitleTxt.setText("Something went wrong");
                    emptySubTitleTxt.setVisibility(View.VISIBLE);
                    emptyBtnTxt.setVisibility(View.VISIBLE);
                    emptyBtnTxt.setText("Refresh");
                    emptyBtnTxt.setEnabled(true);
                    emptyBtnTxt.setOnClickListener(v->{
                        if(!mIsOnline) {
                            noConnectionSnackBar(getView());
                            return;
                        }
                        loadData();
                    });
                }
            }
        } else {
            mPullToRefreshLayout.setVisibility(View.VISIBLE);
            emptyLayout.setVisibility(View.GONE);
        }
    }




    private void getUserSavedPersonalise(String userId) {
        mDisposable.add(
                ApiManager.getUserSavedPersonalise(userId, BuildConfig.SITEID, ResUtil.getDeviceId(getActivity()))
                        .map(mPrefListModel ->{
                            boolean hasPreference = false;
                            if(mPrefListModel != null) {
                                PersonaliseDetails topicsFromUser = mPrefListModel.getTopics();
                                PersonaliseDetails citiesFromUser = mPrefListModel.getCities();
                                PersonaliseDetails authorsFromUser = mPrefListModel.getAuthors();

                                ArrayList<PersonaliseModel> topicSelected = topicsFromUser.getValues();
                                ArrayList<PersonaliseModel> citiesSelected = citiesFromUser.getValues();
                                ArrayList<PersonaliseModel> authorsSelected = authorsFromUser.getValues();

                                hasPreference = (topicSelected != null && topicSelected.size() > 0)
                                        || (citiesSelected != null && citiesSelected.size() > 0)
                                        || (authorsSelected != null && authorsSelected.size() > 0);

                            }
                            return hasPreference;
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(value->{
                            if(getView() != null && getActivity() != null && emptyBtnTxt != null) {
                                if(value) {
                                    emptySubTitleTxt.setText("No content in my stories for your \n set preferences");
                                    emptyBtnTxt.setText("Add more preferences");
                                    emptyIcon.setImageResource(R.drawable.ic_empty_mystories_more);
                                } else {
                                    emptySubTitleTxt.setText("You have not set your preferences");
                                    emptyBtnTxt.setText("Set Preferences");
//                                    emptyBtnTxt.setText("");
                                    emptyIcon.setImageResource(R.drawable.ic_empty_mystories);
                                }

                                emptyBtnTxt.setOnClickListener(v->{
                                    if(!mIsOnline) {
                                        noConnectionSnackBar(getView());
                                        return;
                                    }
                                    IntentUtil.openPersonaliseActivity(getActivity(), THPConstants.FROM_DASHBOARD_SET_PREFERENCE);
                                });

                                emptyBtnTxt.setEnabled(true);

                                emptySubTitleTxt.setVisibility(View.VISIBLE);
                                emptyBtnTxt.setVisibility(View.VISIBLE);
                            }
                            Log.i("", "");
                        }, throwable -> {
                            Log.i("", "");

                        })
        );

    }


    @Override
    public void checkPageEmpty() {

        if(mRecyclerAdapter != null && mRecyclerAdapter.getItemCount() == 1) {
            mRecyclerAdapter.deleteIndex(0);
            showEmptyLayout(true);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mPageEndTime == -1 || mPageStartTime == -1) {
            return;
        }
        if(mPageStartTime >= 1000 && mIsVisible) {
            mPageEndTime = System.currentTimeMillis();
            sendEventCapture(mPageStartTime, mPageEndTime);
            mPageStartTime = -1l;
            mPageEndTime = -1l;
        }
    }

    private void briefingEventCapture(long pageStartTime, long pageEndTime) {
        if(isBriefingPage()) {
            final String totalTime = AppDateUtil.millisToMinAndSec(pageEndTime - pageStartTime);
            final long timeInSeconds = AppDateUtil.millisToSecs(pageEndTime - pageStartTime);
            HashMap<String,Object> map = new HashMap<>();
            map.put(THPConstants.CT_KEY_TimeSpent,totalTime);
            map.put(THPConstants.CT_KEY_TimeSpentSeconds,timeInSeconds);
            map.put(THPConstants.CT_KEY_Ediitons, mBreifingType);
            CleverTapUtil.cleverTapEvent(getActivity(),THPConstants.CT_EVENT_BRIEFING,map);
        }

    }

    private void sendEventCapture(long pageStartTime, long pageEndTime) {
        String from = "";
        if(isBriefingPage()) {
            from = "Briefing";
            briefingEventCapture(pageStartTime, pageEndTime);
        } else if(mFrom != null && mFrom.equalsIgnoreCase(NetConstants.RECO_Mystories)) {
            from = "My Stories";
        } else {
            from = mFrom;
        }
        final String totalTime = AppDateUtil.millisToMinAndSec(pageEndTime - pageStartTime);
        final long timeInSeconds = AppDateUtil.millisToSecs(pageEndTime - pageStartTime);
        CleverTapUtil.cleverTapTHPTabTimeSpent(getActivity(), from,totalTime,timeInSeconds);
    }

    /*@Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }*/

    @Override
    public void onPause() {
        super.onPause();
        Log.i("TabFragment", "onPause() TabIndex = "+mTabIndex);
        EventBus.getDefault().unregister(this);
        Log.i("TabFragment", "onPause() TabIndex = "+mTabIndex+" EventBus UnRegistered");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i("TabFragment", "onDestroyView() TabIndex = "+mTabIndex);
        EventBus.getDefault().unregister(this);
        Log.i("TabFragment", "onDestroyView() TabIndex = "+mTabIndex+" EventBus UnRegistered");
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void handleEvent(BackPressImpl backPress) {
        Log.i("handleEvent", "Back Button Pressed :: TabIndex = "+mTabIndex);
        BackPressCallback backPressCallback = new BackPressImpl(this, mFrom, mTabIndex).onBackPressed();
        EventBus.getDefault().post(backPressCallback);
    }


}
