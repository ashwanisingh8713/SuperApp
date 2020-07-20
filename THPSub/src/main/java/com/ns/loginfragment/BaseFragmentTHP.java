package com.ns.loginfragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.netoperation.db.THPDB;
import com.netoperation.net.ApiManager;
import com.netoperation.util.AppDateUtil;
import com.netoperation.util.NetConstants;
import com.netoperation.util.DefaultPref;
import com.ns.activity.BaseAcitivityTHP;
import com.ns.activity.BaseRecyclerViewAdapter;
import com.ns.thpremium.R;
import com.ns.view.RecyclerViewPullToRefresh;
import com.ns.view.text.CustomTextView;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public abstract class BaseFragmentTHP extends Fragment {

    public abstract int getLayoutRes();

    public static boolean sIsDayTheme = true;
    protected boolean mIsVisible;
    protected int mSize = 30;
    protected String mUserId;
    protected static String mUserEmail;
    protected static String mUserPhone;

    protected BaseAcitivityTHP mActivity;

    protected final CompositeDisposable mDisposable = new CompositeDisposable();



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sIsDayTheme = DefaultPref.getInstance(getActivity()).isUserThemeDay();
        meteredPaywallAllowedCount(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(getLayoutRes(), container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        mIsVisible = isVisibleToUser;
    }


    private ProgressDialog progress;
    protected void showProgressDialog(String message) {
        if (progress == null) {
            progress = new ProgressDialog(getContext());
        }
        progress.setTitle(getString(R.string.please_wait));
        progress.setMessage(message);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        progress.show();
    }

    protected void hideProgressDialog() {
        if (progress != null) {
            if (progress.isShowing()) {
                progress.dismiss();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mDisposable.clear();
    }

    protected void readUserData() {
        if(mUserEmail == null && mUserPhone == null) {
            ApiManager.getUserProfile(getActivity())
                    .subscribe(userProfile -> {
                        mUserPhone = userProfile.getContact();
                        mUserEmail = userProfile.getEmailId();
                    });
        }
    }

    public void noConnectionSnackBar(View view) {
        if(view == null) {
            return;
        }
        Snackbar mSnackbar = Snackbar.make(view, "", Snackbar.LENGTH_LONG);
        Snackbar.SnackbarLayout mSnackbarView = (Snackbar.SnackbarLayout) mSnackbar.getView();
        mSnackbarView.setBackgroundColor(Color.BLACK);
        TextView textView = mSnackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        TextView textView1 = mSnackbarView.findViewById(com.google.android.material.R.id.snackbar_action);
        textView.setVisibility(View.INVISIBLE);
        textView1.setVisibility(View.INVISIBLE);
        View snackView = getLayoutInflater().inflate(R.layout.thp_noconnection_snackbar, null);
        snackView.findViewById(R.id.action_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                startActivity(intent);
            }
        });
        mSnackbarView.addView(snackView);
        mSnackbar.show();
    }



    private EmptyViewClickListener mEmptyViewClickListener;

    public void setEmptyViewClickListener(EmptyViewClickListener emptyViewClickListener) {
        mEmptyViewClickListener = emptyViewClickListener;
    }

    public interface EmptyViewClickListener {
        void onEmptyRefreshBtnClick();
        void onOtherStuffWork();
    }

    public void showHideLoadingViewCrossFade(View visibleView, View hideView) {

        // Set the content view to 0% opacity but visible, so that it is visible
        // (but fully transparent) during the animation.
        visibleView.setAlpha(0f);
        visibleView.setVisibility(View.VISIBLE);

        // Animate the content view to 100% opacity, and clear any animation
        // listener set on the view.
        visibleView.animate()
                .alpha(1f)
                .setDuration(400)
                .setListener(null);

        // Animate the loading view to 0% opacity. After the animation ends,
        // set its visibility to GONE as an optimization step (it won't
        // participate in layout passes, etc.)
        hideView.animate()
                .alpha(0f)
                .setDuration(300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        hideView.setVisibility(View.GONE);
                    }
                });
    }

    public void hideEmptyLayout(LinearLayout emptyLayout) {
        if(emptyLayout != null) {
            emptyLayout.setVisibility(View.GONE);
        }
    }


    public void showEmptyLayout(LinearLayout emptyLayout, boolean isNoContent, BaseRecyclerViewAdapter mRecyclerAdapter,
                                RecyclerViewPullToRefresh mPullToRefreshLayout, boolean isBriefingPage, @NonNull String mFrom) {
        if(emptyLayout == null) {
            return;
        }
        if(mRecyclerAdapter == null || mRecyclerAdapter.getItemCount() == 0) {
            showHideLoadingViewCrossFade(emptyLayout, mPullToRefreshLayout);

            ImageView emptyIcon = emptyLayout.findViewById(R.id.emptyIcon);
            CustomTextView emptyTitleTxt = emptyLayout.findViewById(R.id.emptyTitleTxt);
            CustomTextView emptySubTitleTxt = emptyLayout.findViewById(R.id.emptySubTitleTxt);
            CustomTextView emptyBtnTxt = emptyLayout.findViewById(R.id.emptyBtnTxt);

            if (isBriefingPage) {
                if(isNoContent) {
                    emptyIcon.setImageResource(R.drawable.ic_empty_breifing);
                    emptyTitleTxt.setVisibility(View.INVISIBLE);
                    emptySubTitleTxt.setVisibility(View.VISIBLE);
                    emptySubTitleTxt.setText("No content in Breifing. Please look \n back after sometime");
                    emptyBtnTxt.setVisibility(View.VISIBLE);
                    emptyBtnTxt.setEnabled(true);
                    emptyBtnTxt.setText("Refresh");
                    emptyBtnTxt.setOnClickListener(v->{
                        if(mEmptyViewClickListener != null) {
                            mEmptyViewClickListener.onEmptyRefreshBtnClick();
                        }
                    });
                }
                else {
                    if(!BaseAcitivityTHP.sIsOnline) {
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
                        if(mEmptyViewClickListener != null) {
                            mEmptyViewClickListener.onEmptyRefreshBtnClick();
                        }
                    });
                }
            }
            else if (mFrom.equalsIgnoreCase(NetConstants.API_Mystories)) {
                if(isNoContent) {
                    emptyIcon.setImageResource(R.drawable.ic_empty_watermark);
                    emptyTitleTxt.setVisibility(View.INVISIBLE);
                    emptySubTitleTxt.setVisibility(View.INVISIBLE);
                    emptyBtnTxt.setVisibility(View.INVISIBLE);
                    emptyBtnTxt.setEnabled(false);
                    if(mEmptyViewClickListener != null) {
                        mEmptyViewClickListener.onOtherStuffWork();
                    }

                }
                else {
                    if (!BaseAcitivityTHP.sIsOnline) {
                        //noConnectionSnackBar(getView());
                        emptyTitleTxt.setText("Oh no!");
                        emptySubTitleTxt.setText("NO INTERNET - WHAT TO DO?");
                        emptySubTitleTxt.setTypeface(Typeface.DEFAULT_BOLD);
                    }
                    else {
                        emptyTitleTxt.setText("Oops...");
                        emptySubTitleTxt.setText("Something went wrong");
                    }
                    emptyIcon.setImageResource(R.drawable.ic_empty_something_wrong);
                    emptySubTitleTxt.setVisibility(View.VISIBLE);
                    emptyBtnTxt.setVisibility(View.VISIBLE);
                    emptyBtnTxt.setText("Refresh");
                    emptyBtnTxt.setEnabled(true);
                    emptyBtnTxt.setOnClickListener(v->{
                        if(!BaseAcitivityTHP.sIsOnline) {
                            noConnectionSnackBar(getView());
                            return;
                        }
                        if(mEmptyViewClickListener != null) {
                            mEmptyViewClickListener.onEmptyRefreshBtnClick();
                        }
                    });
                }
            }
            else if (mFrom.equalsIgnoreCase(NetConstants.API_suggested)) {
                if(isNoContent) {
                    emptyIcon.setImageResource(R.drawable.ic_empty_suggestion);
                    emptyTitleTxt.setVisibility(View.INVISIBLE);
                    emptySubTitleTxt.setVisibility(View.VISIBLE);
                    emptySubTitleTxt.setText("No content in Suggestion. Please look \n back after sometime");
                    emptyBtnTxt.setVisibility(View.VISIBLE);
                    emptyBtnTxt.setEnabled(true);
                    emptyBtnTxt.setText("Refresh");
                    emptyBtnTxt.setOnClickListener(v->{
                        if(mEmptyViewClickListener != null) {
                            mEmptyViewClickListener.onEmptyRefreshBtnClick();
                        }
                    });
                }
                else {
                    if (!BaseAcitivityTHP.sIsOnline) {
                        //noConnectionSnackBar(getView());
                        emptyTitleTxt.setText("Oh no!");
                        emptySubTitleTxt.setText("NO INTERNET - WHAT TO DO?");
                        emptySubTitleTxt.setTypeface(Typeface.DEFAULT_BOLD);
                    }
                    else {
                        emptyTitleTxt.setText("Oops...");
                        emptySubTitleTxt.setText("Something went wrong");
                    }
                    emptyIcon.setImageResource(R.drawable.ic_empty_something_wrong);
                    emptyTitleTxt.setVisibility(View.VISIBLE);
                    emptySubTitleTxt.setVisibility(View.VISIBLE);
                    emptyBtnTxt.setVisibility(View.VISIBLE);
                    emptyBtnTxt.setText("Refresh");
                    emptyBtnTxt.setEnabled(true);
                    emptyBtnTxt.setOnClickListener(v->{
                        if(!BaseAcitivityTHP.sIsOnline) {
                            noConnectionSnackBar(getView());
                            return;
                        }
                        if(mEmptyViewClickListener != null) {
                            mEmptyViewClickListener.onEmptyRefreshBtnClick();
                        }
                    });
                }
            }
            else {
                if(isNoContent) {
                    emptyIcon.setImageResource(R.drawable.ic_empty_breifing);
                    emptyTitleTxt.setVisibility(View.INVISIBLE);
                    emptySubTitleTxt.setVisibility(View.VISIBLE);
                    emptySubTitleTxt.setText("No content in Breifing. Please look \n back after sometime");
                    emptyBtnTxt.setVisibility(View.VISIBLE);
                    emptyBtnTxt.setEnabled(true);
                    emptyBtnTxt.setText("Refresh");
                    emptyBtnTxt.setOnClickListener(v->{
                        if(mEmptyViewClickListener != null) {
                            mEmptyViewClickListener.onEmptyRefreshBtnClick();
                        }
                    });
                }
                else {
                    if (!BaseAcitivityTHP.sIsOnline) {
                        //noConnectionSnackBar(getView());
                        emptyTitleTxt.setText("Oh no!");
                        emptySubTitleTxt.setText("NO INTERNET - WHAT TO DO?");
                        emptySubTitleTxt.setTypeface(Typeface.DEFAULT_BOLD);
                    }
                    else {
                        emptyTitleTxt.setText("Oops...");
                        emptySubTitleTxt.setText("Something went wrong");
                    }
                    emptyIcon.setImageResource(R.drawable.ic_empty_something_wrong);
                    emptyTitleTxt.setVisibility(View.VISIBLE);
                    emptyBtnTxt.setVisibility(View.VISIBLE);
                    emptySubTitleTxt.setVisibility(View.VISIBLE);
                    emptyBtnTxt.setText("Refresh");
                    emptyBtnTxt.setEnabled(true);
                    emptyBtnTxt.setOnClickListener(v -> {
                        if (mEmptyViewClickListener != null) {
                            mEmptyViewClickListener.onEmptyRefreshBtnClick();
                        }
                    });
                }
            }
        }
        else {
            showHideLoadingViewCrossFade(mPullToRefreshLayout, emptyLayout);
        }
    }

    private static int allowedCount = -1;
    private static String mpBannerMsg;
    private static String cycleName;
    private static String durationUnit;
    private static String mpNonSignInTitleBlocker, mpSignInButtonName;
    private static String mpExpiredUserTitleBlocker, mpSignUpButtonName;
    private static String mpNonSignInDescBlocker;
    private static String mpExpiredUserDescBlocker;
    private static String mpGetFullAccessButtonName;

    protected static void meteredPaywallAllowedCount(Context context) {
        if(cycleName == null) {
            Observable.just("")
                    .observeOn(Schedulers.io())
                    .map(va -> {
                        if(context != null) {
                            THPDB thpdb = THPDB.getInstance(context);
                            allowedCount = thpdb.daoMp().getAllowedArticleCounts();
                            mpBannerMsg = thpdb.daoMp().getMpBannerMsg();
                            cycleName = thpdb.daoMp().getCycleName();
                            durationUnit = AppDateUtil.calculateDurationAndUnit(thpdb.daoMp().getAllowedArticleTimesInSecs());
                            mpNonSignInTitleBlocker = thpdb.daoMp().getNonSignInMPTitleBlocker();
                            mpNonSignInDescBlocker = thpdb.daoMp().getNonSignInBlockerDescription();
                            mpExpiredUserTitleBlocker = thpdb.daoMp().getExpiredUserBlockerTitle();
                            mpExpiredUserDescBlocker = thpdb.daoMp().getExpiredUserBlockerDescription();
                            mpSignInButtonName = thpdb.daoMp().getSignInButtonName();
                            mpSignUpButtonName = thpdb.daoMp().getSignUpButtonName();
                            mpGetFullAccessButtonName = thpdb.daoMp().getFullAccessButtonName();
                        }
                        return "";
                    })
                    .subscribe();
        }
    }

    public static int getAllowedCount(Context context) {
        if(cycleName == null) {
            meteredPaywallAllowedCount(context);
        }
        return allowedCount;
    }

    public static String getMpBannerMsg() {
        return mpBannerMsg;
    }

    public static String getCycleName() {
        return cycleName;
    }

    public static String getDurationUnit() {
        return durationUnit;
    }

    public static String getMpNonSignInTitleBlocker() {
        return mpNonSignInTitleBlocker;
    }

    public static String getMpExpiredUserTitleBlocker() {
        return mpExpiredUserTitleBlocker;
    }

    public static String getMpNonSignInDescBlocker() {
        return mpNonSignInDescBlocker;
    }

    public static String getMpExpiredUserDescBlocker() {
        return mpExpiredUserDescBlocker;
    }

    public static String getMpSignInButtonName() {
        return mpSignInButtonName;
    }

    public static String getMpSignUpButtonName() {
        return mpSignUpButtonName;
    }

    public static String getMpGetFullAccessButtonName() {
        return mpGetFullAccessButtonName;
    }

}
