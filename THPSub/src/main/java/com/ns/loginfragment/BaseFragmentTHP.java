package com.ns.loginfragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;
import com.google.android.material.snackbar.Snackbar;
import com.netoperation.net.ApiManager;
import com.netoperation.util.NetConstants;
import com.netoperation.util.UserPref;
import com.ns.activity.BaseAcitivityTHP;
import com.ns.activity.BaseRecyclerViewAdapter;
import com.ns.thpremium.R;
import com.ns.view.RecyclerViewPullToRefresh;
import com.ns.view.text.CustomTextView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public abstract class BaseFragmentTHP extends Fragment {

    public abstract int getLayoutRes();

    protected boolean mIsOnline;
    protected boolean mIsVisible;
    protected int mSize = 30;
    protected String mUserId;
    protected static String mUserEmail;
    protected static String mUserPhone;

    protected BaseAcitivityTHP mActivity;

    protected final CompositeDisposable mDisposable = new CompositeDisposable();

    protected boolean mIsDayTheme = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIsDayTheme = UserPref.getInstance(getActivity()).isUserThemeDay();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(getLayoutRes(), container, false);
        netCheck(rootView);
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

    private void netCheck(View rootview) {
        mDisposable.add(ReactiveNetwork
                .observeNetworkConnectivity(getActivity())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(connectivity -> {
                    if(connectivity.state() == NetworkInfo.State.CONNECTED) {
                        mIsOnline = true;
                    }
                    else {
                        mIsOnline = false;
                        if(mIsVisible && rootview!=null) {
                            //Alerts.noInternetSnackbar(rootview);
                            noConnectionSnackBar(getView());
                        }
                    }

                    Log.i("", "");
                }));
    }


    ProgressDialog progress;
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



    private BaseFragmentListener mBaseFragmentListener;

    public void setBaseFragmentListener(BaseFragmentListener baseFragmentListener) {
        mBaseFragmentListener = baseFragmentListener;
    }

    public interface BaseFragmentListener {
        void onEmptyRefreshBtnClick();
        void onOtherStuffWork();
    }


    public void showEmptyLayout(LinearLayout emptyLayout, boolean isNoContent, BaseRecyclerViewAdapter mRecyclerAdapter, RecyclerViewPullToRefresh mPullToRefreshLayout, boolean isBriefingPage, @NonNull String mFrom) {
        if(emptyLayout == null) {
            return;
        }
        if(mRecyclerAdapter == null || mRecyclerAdapter.getItemCount() == 0) {
            emptyLayout.setVisibility(View.VISIBLE);
            mPullToRefreshLayout.setVisibility(View.GONE);

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
                        if(mBaseFragmentListener != null) {
                            mBaseFragmentListener.onEmptyRefreshBtnClick();
                        }
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
                        if(mBaseFragmentListener != null) {
                            mBaseFragmentListener.onEmptyRefreshBtnClick();
                        }
                    });
                }
            }
            else if (mFrom.equalsIgnoreCase(NetConstants.RECO_Mystories)) {
                if(isNoContent) {
                    emptyIcon.setImageResource(R.drawable.ic_empty_watermark);
                    emptyTitleTxt.setVisibility(View.INVISIBLE);
                    emptySubTitleTxt.setVisibility(View.INVISIBLE);
                    emptyBtnTxt.setVisibility(View.INVISIBLE);
                    emptyBtnTxt.setEnabled(false);
                    if(mBaseFragmentListener != null) {
                        mBaseFragmentListener.onOtherStuffWork();
                    }

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
                        if(mBaseFragmentListener != null) {
                            mBaseFragmentListener.onEmptyRefreshBtnClick();
                        }
                    });
                }
            }
            else if (mFrom.equalsIgnoreCase(NetConstants.RECO_suggested)) {
                if(isNoContent) {
                    emptyIcon.setImageResource(R.drawable.ic_empty_suggestion);
                    emptyTitleTxt.setVisibility(View.INVISIBLE);
                    emptySubTitleTxt.setVisibility(View.VISIBLE);
                    emptySubTitleTxt.setText("No content in Suggestion. Please look \n back after sometime");
                    emptyBtnTxt.setVisibility(View.VISIBLE);
                    emptyBtnTxt.setEnabled(true);
                    emptyBtnTxt.setText("Refresh");
                    emptyBtnTxt.setOnClickListener(v->{
                        if(mBaseFragmentListener != null) {
                            mBaseFragmentListener.onEmptyRefreshBtnClick();
                        }
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
                        if(mBaseFragmentListener != null) {
                            mBaseFragmentListener.onEmptyRefreshBtnClick();
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
                        if(mBaseFragmentListener != null) {
                            mBaseFragmentListener.onEmptyRefreshBtnClick();
                        }
                    });
                }
                else {
                    if (!mIsOnline) {
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
                    emptyBtnTxt.setOnClickListener(v -> {
                        if (mBaseFragmentListener != null) {
                            mBaseFragmentListener.onEmptyRefreshBtnClick();
                        }
                    });
                }
            }
        } else {
            mPullToRefreshLayout.setVisibility(View.VISIBLE);
            emptyLayout.setVisibility(View.GONE);
        }
    }


}
