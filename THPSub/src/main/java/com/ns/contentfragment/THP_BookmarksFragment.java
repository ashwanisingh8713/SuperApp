package com.ns.contentfragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.netoperation.model.ArticleBean;
import com.netoperation.model.UserProfile;
import com.netoperation.net.ApiManager;
import com.netoperation.util.DefaultPref;
import com.netoperation.util.NetConstants;
import com.netoperation.util.PremiumPref;
import com.ns.activity.BaseAcitivityTHP;
import com.ns.activity.BaseRecyclerViewAdapter;
import com.ns.activity.BookmarkActivity;
import com.ns.adapter.PremiumListingContentAdapter;
import com.ns.alerts.Alerts;
import com.ns.callbacks.FragmentTools;
import com.ns.callbacks.THP_AppEmptyPageListener;
import com.ns.loginfragment.BaseFragmentTHP;
import com.ns.model.AppTabContentModel;
import com.ns.model.ToolbarCallModel;
import com.ns.thpremium.BuildConfig;
import com.ns.thpremium.R;
import com.ns.utils.CommonUtil;
import com.ns.utils.ResUtil;
import com.ns.utils.THPFirebaseAnalytics;
import com.ns.view.NSEditText;
import com.ns.view.RecyclerViewPullToRefresh;
import com.ns.view.layout.NSLinearLayout;
import com.ns.view.text.CustomTextView;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class THP_BookmarksFragment extends BaseFragmentTHP implements RecyclerViewPullToRefresh.TryAgainBtnClickListener, THP_AppEmptyPageListener, FragmentTools, TextWatcher {

    private RecyclerViewPullToRefresh mPullToRefreshLayout;
    private LinearLayout emptyLayout;
    private PremiumListingContentAdapter mRecyclerAdapter;
    private String mUserId;
    private String mGoupType;
    private UserProfile mUserProfile;
    private NSLinearLayout mSearchParentLayout;
    private NSEditText mSearchBox;
    private AppCompatImageView mClearText;

    public static THP_BookmarksFragment getInstance(String groupType) {
        THP_BookmarksFragment fragment = new THP_BookmarksFragment();
        Bundle bundle = new Bundle();
        bundle.putString("groupType", groupType);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof BookmarkActivity) {
            mActivity = (BookmarkActivity) context;
        }
    }

    @Override
    public int getLayoutRes() {
        return R.layout.thp_fragment_bookmark;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mGoupType = getArguments().getString("groupType");
        }

        mUserId = PremiumPref.getInstance(getContext()).getUserId();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPullToRefreshLayout = view.findViewById(R.id.recyclerView);
        emptyLayout = view.findViewById(R.id.emptyLayout);
        mSearchParentLayout = view.findViewById(R.id.searchLayout);
        mSearchBox = view.findViewById(R.id.searchEditText);
        mClearText = view.findViewById(R.id.close_button);
//Update Search Box View
        if (mGoupType != null && mGoupType.equals(NetConstants.BOOKMARK_IN_ONE)){
            mSearchParentLayout.setVisibility(View.VISIBLE);
        }
        mRecyclerAdapter = new PremiumListingContentAdapter(new ArrayList<>(), mGoupType, mUserId, mPullToRefreshLayout.getRecyclerView());
        mRecyclerAdapter.setAppEmptyPageListener(this);
        mPullToRefreshLayout.setDataAdapter(mRecyclerAdapter);

        mPullToRefreshLayout.setTryAgainBtnClickListener(this);

        mPullToRefreshLayout.showProgressBar();

        // Pull To Refresh Listener
        registerPullToRefresh();

        loadData();

        mDisposable.add(ApiManager.getUserProfile(getActivity())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userProfile -> {
                    mUserProfile = userProfile;
                    loadData();
                }));
//      Add Text Watcher
        mSearchBox.addTextChangedListener(this);
//        Add ImeOptions Handler
        mSearchBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    CommonUtil.hideKeyboard(mSearchBox);
                    return true;
                }
                return false;
            }
        });
//        ClearText onClickListener
        mClearText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSearchBox.setText("");
                mSearchBox.clearFocus();
                CommonUtil.hideKeyboard(mSearchBox);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set Toolbar Item Click Listener
        if (mActivity != null) {
            mActivity.setOnFragmentTools(this);
        }
        if (mRecyclerAdapter != null && mRecyclerAdapter.getItemCount() > 0) {
            loadData(false);
        }

        THPFirebaseAnalytics.setFirbaseAnalyticsScreenRecord(getActivity(), "Bookmark Screen", THP_BookmarksFragment.class.getSimpleName());

    }

    /**
     * Adding Pull To Refresh Listener
     */
    private void registerPullToRefresh() {
        mPullToRefreshLayout.getSwipeRefreshLayout().setOnRefreshListener(() -> {
            if (!BaseAcitivityTHP.sIsOnline) {
                Alerts.showSnackbar(getActivity(), getResources().getString(R.string.please_check_ur_connectivity));
                mPullToRefreshLayout.setRefreshing(false);
                return;
            }
            mPullToRefreshLayout.setRefreshing(true);

            loadData();
        });
    }

    @Override
    public void tryAgainBtnClick() {
        mPullToRefreshLayout.showProgressBar();
        loadData();
    }

    private void loadData() {
        loadData(BaseAcitivityTHP.sIsOnline);
    }

    /**
     * Load bookmarks from respective Group
     *
     * @param isOnline
     */
    private void loadData(boolean isOnline) {
        Observable<List<ArticleBean>> observable = null;
        if (isOnline && mGoupType != null && mGoupType.equals(NetConstants.G_PREMIUM_SECTIONS)) {
            observable = ApiManager.getRecommendationFromServer(getActivity(), mUserProfile.getAuthorization(), mUserId,
                    NetConstants.API_bookmarks, "" + 1000, BuildConfig.SITEID);
        } else if (mGoupType != null && mGoupType.equals(NetConstants.G_BOOKMARK_PREMIUM)) {
            observable = ApiManager.getBookmarkGroupType(getActivity(), NetConstants.G_BOOKMARK_PREMIUM);
        } else if (mGoupType != null && mGoupType.equals(NetConstants.G_BOOKMARK_DEFAULT)) {
            observable = ApiManager.getBookmarkGroupType(getActivity(), NetConstants.G_BOOKMARK_DEFAULT);
        } else { //(mGoupType!=null && mGoupType.equals(NetConstants.BOOKMARK_IN_ONE))
            if (ResUtil.isEmpty(mSearchBox.getText().toString())) {
                observable = ApiManager.getBookmarkGroupType(getActivity(), NetConstants.BOOKMARK_IN_ONE);
            } else {
                observable = ApiManager.getBookmarkGroupTypeWithQuery(getActivity(), mSearchBox.getText().toString());
            }
        }
        mDisposable.add(
                observable
                        .map(value -> {
                            List<AppTabContentModel> content = new ArrayList<>();
                            for (ArticleBean bean : value) {
                                int viewType = BaseRecyclerViewAdapter.VT_THD_DEFAULT_ROW;
                                if (bean.getGroupType() == null) {
                                    continue;
                                }
                                else if (bean.getGroupType().equals(NetConstants.G_BOOKMARK_DEFAULT)) {
                                    viewType = BaseRecyclerViewAdapter.VT_THD_DEFAULT_ROW;
                                }
                                else if (bean.getGroupType().equals(NetConstants.G_BOOKMARK_PREMIUM)) {
                                    viewType = BaseRecyclerViewAdapter.VT_BOOKMARK_PREMIUM;
                                }
                                AppTabContentModel model = new AppTabContentModel(viewType);
                                model.setBean(bean);
                                content.add(model);
                            }
                            return content;
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(value -> {
                            if (value.size() > 0) {
                                mRecyclerAdapter.clearData();
                                mRecyclerAdapter.addData(value);
                                mPullToRefreshLayout.setVisibility(View.VISIBLE);
                                emptyLayout.setVisibility(View.GONE);
                            } else {
                                if (mRecyclerAdapter != null && mRecyclerAdapter.getItemCount() > 0) {
                                    mRecyclerAdapter.clearData();
                                }
                                showEmptyLayout();
                            }
                            mPullToRefreshLayout.hideProgressBar();
                            mPullToRefreshLayout.setRefreshing(false);
                        }, throwable -> {
                            if (throwable instanceof ConnectException
                                    || throwable instanceof SocketTimeoutException || throwable instanceof TimeoutException
                                    || throwable instanceof NullPointerException) {
                                loadData(false);
                            } else {
                                showEmptyLayout();
                            }

                            mPullToRefreshLayout.hideProgressBar();
                            mPullToRefreshLayout.setRefreshing(false);

                        }, () -> {


                        }));

    }

    private void showEmptyLayout() {
        if (mRecyclerAdapter == null || mRecyclerAdapter.getItemCount() == 0) {
            if (mGoupType != null && mGoupType.equals(NetConstants.G_BOOKMARK_DEFAULT) || mGoupType.equals(NetConstants.BOOKMARK_IN_ONE)) {
                TextView emptyTxtMsg = emptyLayout.findViewById(R.id.emptyTitleTxt);
                emptyTxtMsg.setText("You have not added any bookmarks");
            }
            emptyLayout.setVisibility(View.VISIBLE);
            mPullToRefreshLayout.setVisibility(View.GONE);
        } else {
            emptyLayout.setVisibility(View.GONE);
            mPullToRefreshLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void checkPageEmpty() {
        //showEmptyLayout();
        //Reload contents
        loadData();
    }


    @Override
    public void onBackClickListener() {

    }

    @Override
    public void onSearchClickListener(ToolbarCallModel toolbarCallModel) {

    }

    @Override
    public void onShareClickListener(ToolbarCallModel toolbarCallModel) {

    }

    @Override
    public void onCreateBookmarkClickListener(ToolbarCallModel toolbarCallModel) {

    }

    @Override
    public void onRemoveBookmarkClickListener(ToolbarCallModel toolbarCallModel) {

    }

    @Override
    public void onFontSizeClickListener(ToolbarCallModel toolbarCallModel) {

    }

    @Override
    public void onCommentClickListener(ToolbarCallModel toolbarCallModel) {

    }

    @Override
    public void onTTSPlayClickListener(ToolbarCallModel toolbarCallModel) {

    }

    @Override
    public void onTTSStopClickListener(ToolbarCallModel toolbarCallModel) {

    }

    @Override
    public void onFavClickListener(ToolbarCallModel toolbarCallModel) {

    }

    @Override
    public void onLikeClickListener(ToolbarCallModel toolbarCallModel) {

    }

    @Override
    public void onOverflowClickListener(ToolbarCallModel toolbarCallModel) {
        if (mActivity == null) {
            return;
        }
        LayoutInflater layoutInflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.layout_submenu_overflow, null, true);

        final PopupWindow changeSortPopUp = new PopupWindow(getActivity());
        //Inflating the Popup using xml file
        changeSortPopUp.setContentView(layout);
        changeSortPopUp.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        changeSortPopUp.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        changeSortPopUp.setFocusable(true);
        changeSortPopUp.setBackgroundDrawable(getResources().getDrawable(R.drawable.shadow_143418));

        int width = mActivity.getDetailToolbar().getWidth();
        int height = mActivity.getDetailToolbar().getHeight();
        // Show Pop-up Window
        changeSortPopUp.showAsDropDown(mActivity.getDetailToolbar(), width, -(height / 3));

        //Click listener
        CustomTextView clearAll = layout.findViewById(R.id.textView_ClearAll);
        clearAll.setText(R.string.remove_all);
        clearAll.setOnClickListener(view -> {
            changeSortPopUp.dismiss();
            if (mRecyclerAdapter == null || mRecyclerAdapter.getItemCount() == 0) {
                return;
            }
            createWarningDialog();
        });
    }

    private void createWarningDialog() {
        AlertDialog.Builder builder = null;
        boolean mIsDayTheme = DefaultPref.getInstance(getActivity()).isUserThemeDay();
        if (mIsDayTheme) {
            builder = new AlertDialog.Builder(getActivity());
        } else {
            builder = new AlertDialog.Builder(getActivity(), android.app.AlertDialog.THEME_DEVICE_DEFAULT_DARK);
        }
        builder.setMessage("Do you want to remove all articles from Read Later ? ");
        builder.setPositiveButton("Yes", (dialog, id) -> {
            dialog.dismiss();
            //Clear Bookmarks
            mDisposable.add(ApiManager.deleteAllBookmarks(getActivity())
                    .subscribe(isAllDeleted -> {
                        Log.i("", "");
                        mRecyclerAdapter.clearData();
                        showEmptyLayout();
                    }, throwable -> {
                        Log.i("", "");
                    }));
        });
        builder.setNegativeButton("No", (dialog, id) -> dialog.cancel());
        Dialog mWarningDialog = builder.create();
        mWarningDialog.show();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (ResUtil.isEmpty(charSequence)) {
            mClearText.setVisibility(View.GONE);
        } else {
            mClearText.setVisibility(View.VISIBLE);
        }
        //Load data for query string
        loadData();
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
