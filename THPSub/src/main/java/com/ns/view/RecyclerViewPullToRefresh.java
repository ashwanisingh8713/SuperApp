package com.ns.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ns.activity.BaseRecyclerViewAdapter;
import com.ns.thpremium.R;
import com.ns.utils.WrapContentLinearLayoutManager;
import com.ns.view.text.CustomTextView;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;


public class RecyclerViewPullToRefresh extends FrameLayout  {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private CustomTextView mTryAgainBtn;
    private CustomProgressBar mProgressBar;
    private SmoothProgressBar smoothProgressBar;

    private LinearLayout networkIndicationLayout;

    private boolean isLoading;
    private boolean isLastPage;

    private TryAgainBtnClickListener mTryAgainBtnClickListener;
    private WrapContentLinearLayoutManager llm;

    public void setTryAgainBtnClickListener(TryAgainBtnClickListener tryAgainBtnClickListener) {
        mTryAgainBtnClickListener = tryAgainBtnClickListener;
    }

    public RecyclerViewPullToRefresh(@NonNull Context context) {
        super(context);
        init(context);
    }

    public RecyclerViewPullToRefresh(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RecyclerViewPullToRefresh(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_recycler_pull_refresh, this, true);
        mSwipeRefreshLayout = findViewById(R.id.swipeRefresh);
        mRecyclerView = findViewById(R.id.recyclerViewPull);
        mTryAgainBtn = findViewById(R.id.tryAgainButton);
        mProgressBar = findViewById(R.id.progressBar);
        smoothProgressBar = findViewById(R.id.smoothProgressBar);
        networkIndicationLayout = findViewById(R.id.networkIndicationLayout);

        enablePullToRefresh(true);

        // Removes blinks
        ((SimpleItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

        // Setting Recycler Layout Manager
//        llm = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        llm = new WrapContentLinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(llm);

        // Try Again Click Listener
        mTryAgainBtn.setOnClickListener(v-> {
            if (mTryAgainBtnClickListener != null) {
                mTryAgainBtnClickListener.tryAgainBtnClick();
            }
        });

    }

    public void setCustomLayoutManager(RecyclerView.LayoutManager layoutManager) {
        mRecyclerView.setLayoutManager(layoutManager);
    }


    public void showProgressBar() {
        mProgressBar.setVisibility(VISIBLE);
        smoothProgressBar.setVisibility(GONE);
        mTryAgainBtn.setVisibility(GONE);
    }

    public void showSmoothProgressBar() {
        smoothProgressBar.setVisibility(VISIBLE);
        mProgressBar.setVisibility(GONE);
        mTryAgainBtn.setVisibility(GONE);
    }

    public void showTryAgainBtn() {
        mProgressBar.setVisibility(GONE);
        smoothProgressBar.setVisibility(GONE);
        mTryAgainBtn.setVisibility(VISIBLE);
        mTryAgainBtn.setText(R.string.try_again);
    }

    public void showTryAgainBtn(String tryAgainMsg) {
        mProgressBar.setVisibility(GONE);
        smoothProgressBar.setVisibility(GONE);
        mTryAgainBtn.setVisibility(VISIBLE);
        mTryAgainBtn.setText(tryAgainMsg);
    }

    public void hideProgressBar() {
        mProgressBar.setVisibility(GONE);
        mTryAgainBtn.setVisibility(GONE);
        smoothProgressBar.postDelayed(()->{
            smoothProgressBar.setVisibility(GONE);
        }, 1000);
    }


    public void enablePullToRefresh(boolean isEnable) {
        mSwipeRefreshLayout.setEnabled(isEnable);
    }

    public void setRefreshing(boolean isRefreshing) {
        mSwipeRefreshLayout.setRefreshing(isRefreshing);
    }

    public void setScrollEnabled(boolean isScrollEnabled) {
        llm.setScrollEnabled(isScrollEnabled);
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public interface TryAgainBtnClickListener {
        void tryAgainBtnClick();
    }

    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return mSwipeRefreshLayout;
    }

    public LinearLayoutManager getLinearLayoutManager() {
        return llm;
    }


    public boolean isRefreshing() {
        return mSwipeRefreshLayout.isRefreshing();
    }


    public void setDataAdapter(BaseRecyclerViewAdapter adapter) {
            if(mRecyclerView != null && adapter != null) {
                mRecyclerView.setAdapter(adapter);
        }
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public boolean isLastPage() {
        return isLastPage;
    }

    public void setLastPage(boolean lastPage) {
        isLastPage = lastPage;
    }

    public void showNetworkFailMessage() {
        networkIndicationLayout.setVisibility(VISIBLE);
    }

    public void hideNetworkFailMessage() {
        networkIndicationLayout.setVisibility(GONE);
    }


}
