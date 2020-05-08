package com.ns.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.netoperation.model.ArticleBean;
import com.netoperation.model.SectionAdapterItem;
import com.netoperation.net.DefaultTHApiManager;
import com.netoperation.util.NetConstants;
import com.ns.adapter.SectionContentAdapter;
import com.ns.thpremium.R;
import com.ns.view.RecyclerViewPullToRefresh;

import java.util.ArrayList;


public class NotificationArticleActivity extends BaseAcitivityTHP {


    private RecyclerViewPullToRefresh mPullToRefreshLayout;
    private SectionContentAdapter mRecyclerAdapter;

    @Override
    public int layoutRes() {
        return R.layout.activity_notification_article;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getDetailToolbar().setTitle("Notification");

        mPullToRefreshLayout = findViewById(R.id.recyclerViewPullToRefresh);

        mRecyclerAdapter = new SectionContentAdapter(NetConstants.GROUP_NOTIFICATION, new ArrayList<>(), false, "mSectionId", NetConstants.GROUP_NOTIFICATION);
        mPullToRefreshLayout.setDataAdapter(mRecyclerAdapter);
        mDisposable.add(DefaultTHApiManager.getNotificationArticles(this).map(articleBeanList->{
            for (ArticleBean bean : articleBeanList) {
                final String itemRowId = "defaultRow_" + bean.getSid() + "_" + bean.getAid();
                SectionAdapterItem item = new SectionAdapterItem(BaseRecyclerViewAdapter.VT_THD_DEFAULT_ROW, itemRowId);
                item.setArticleBean(bean);
                mRecyclerAdapter.addSingleItem(item);
            }
            return mRecyclerAdapter.getItemCount()>0;
        })
                .subscribe(isEmpty->{
                    Log.i("", "");
                }, throwable -> {
                    Log.i("", "");
                }));

    }
}
