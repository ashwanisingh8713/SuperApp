package com.ns.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.annotation.Nullable;

import com.netoperation.model.ArticleBean;
import com.netoperation.model.SectionAdapterItem;
import com.netoperation.net.DefaultTHApiManager;
import com.netoperation.util.NetConstants;
import com.ns.adapter.SectionContentAdapter;
import com.ns.model.ToolbarCallModel;
import com.ns.thpremium.R;
import com.ns.view.RecyclerViewPullToRefresh;
import com.ns.view.text.ArticleTitleTextView;
import com.ns.view.text.CustomTextView;

import java.util.ArrayList;


public class NotificationArticleActivity extends BaseAcitivityTHP {


    private RecyclerViewPullToRefresh mPullToRefreshLayout;
    private SectionContentAdapter mRecyclerAdapter;
    private ArticleTitleTextView mNoNotificationView;

    @Override
    public int layoutRes() {
        return R.layout.activity_notification_article;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getDetailToolbar().setTitle("Notification");
        //Show ToolBar icons at Start
        getDetailToolbar().showNotificationAndBookmarkeIcons(false);

        mPullToRefreshLayout = findViewById(R.id.recyclerViewPullToRefresh);
        mPullToRefreshLayout.enablePullToRefresh(false);
        mNoNotificationView = findViewById(R.id.no_notification);

        mRecyclerAdapter = new SectionContentAdapter(NetConstants.GROUP_NOTIFICATION, new ArrayList<>(), false, "mSectionId", NetConstants.GROUP_NOTIFICATION);
        mPullToRefreshLayout.setDataAdapter(mRecyclerAdapter);
        mDisposable.add(DefaultTHApiManager.getNotificationArticles(this).map(articleBeanList->{
            mRecyclerAdapter.deleteAllItems();
            for (ArticleBean bean : articleBeanList) {
                final String itemRowId = "defaultRow_" + bean.getSid() + "_" + bean.getAid();
                SectionAdapterItem item = new SectionAdapterItem(BaseRecyclerViewAdapter.VT_THD_DEFAULT_ROW, itemRowId);
                item.setArticleBean(bean);
                mRecyclerAdapter.addSingleItem(item);
            }
            return articleBeanList.size() == 0;
        })
                .subscribe(isEmpty->{
                    Log.i("", "");
                    if (isEmpty) {
                        //Show ToolBar icons
                        getDetailToolbar().showNotificationAndBookmarkeIcons(false);
                        mPullToRefreshLayout.setVisibility(View.GONE);
                        mNoNotificationView.setVisibility(View.VISIBLE);
                    } else {
                        //Show ToolBar icons
                        getDetailToolbar().showNotificationAndBookmarkeIcons(true);
                        mPullToRefreshLayout.setVisibility(View.VISIBLE);
                        mNoNotificationView.setVisibility(View.GONE);
                    }
                }, throwable -> {
                    Log.i("", "");
                }));

    }

    @Override
    public void onOverflowClickListener(ToolbarCallModel toolbarCallModel){
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.layout_submenu_overflow, null, true);

        final PopupWindow changeSortPopUp = new PopupWindow(this);
        //Inflating the Popup using xml file
        changeSortPopUp.setContentView(layout);
        changeSortPopUp.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        changeSortPopUp.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        changeSortPopUp.setFocusable(true);
        changeSortPopUp.setBackgroundDrawable(getResources().getDrawable(R.drawable.shadow_143418));

        int width = getDetailToolbar().getWidth();
        int height = getDetailToolbar().getHeight();
        // Show Pop-up Window
        changeSortPopUp.showAsDropDown(getDetailToolbar(), width, -(height/3));

        //Click listener
        CustomTextView clearAll = layout.findViewById(R.id.textView_ClearAll);
        clearAll.setOnClickListener(view -> {
            changeSortPopUp.dismiss();
            createWarningDialog();
        });
    }

    private void createWarningDialog() {
        AlertDialog.Builder builder = null;
        if(mIsDayTheme) {
            builder = new AlertDialog.Builder(this);
        } else {
            builder = new AlertDialog.Builder(this, android.app.AlertDialog.THEME_DEVICE_DEFAULT_DARK);
        }
        builder.setMessage("Do you want to clear all articles from Notifications ? ");
        builder.setPositiveButton("Yes", (dialog, id) -> {
            dialog.dismiss();
            //Clear Notifications
            mDisposable.add(DefaultTHApiManager.deleteAllNotification(NotificationArticleActivity.this)
                    .subscribe(isAllDeleted->{
                        Log.i("", "");
                    }, throwable -> {
                        Log.i("", "");
                    }));
        });
        builder.setNegativeButton("No", (dialog, id) -> dialog.cancel());
        Dialog mWarningDialog = builder.create();
        mWarningDialog.show();
    }
}
