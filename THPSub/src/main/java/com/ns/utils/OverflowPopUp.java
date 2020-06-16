package com.ns.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.appcompat.app.AppCompatActivity;

import com.netoperation.default_db.TableOptional;
import com.netoperation.util.NetConstants;
import com.netoperation.util.PremiumPref;
import com.ns.activity.AppTabActivity;
import com.ns.activity.THPPersonaliseActivity;
import com.ns.alerts.Alerts;
import com.ns.clevertap.CleverTapUtil;
import com.ns.thpremium.R;
import com.ns.view.text.CustomTextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OverflowPopUp {

    private Context mContext;
    private List<TableOptional.OptionsBean> menuItems;
    private PopupWindow changeSortPopUp = null;

    public OverflowPopUp(Context mContext, List<TableOptional.OptionsBean> menuItems) {
        this.mContext = mContext;
        this.menuItems = menuItems;
    }

    public PopupWindow initPopUpView(int mUnreadBookmarkArticleCount, int mUnreadNotificationArticleCount) {
        List<Integer> titleLength = new ArrayList<>();
        for( TableOptional.OptionsBean bean :menuItems) {
            titleLength.add(bean.getTitle().length());
        }
        Collections.sort(titleLength);
        float overflowWidth = ResUtil.pxFromDp(mContext, titleLength.get(titleLength.size()-1)*9);;
        if(mUnreadBookmarkArticleCount > 0 || mUnreadNotificationArticleCount > 0) {
            overflowWidth = ResUtil.pxFromDp(mContext, titleLength.get(titleLength.size()-1)*10);
        }

        int itemHeight = (int) mContext.getResources().getDimension(R.dimen.overflow_item);

        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.layout_menu_overflow_no_item, null);

        LinearLayout viewGroup = layout.findViewById(R.id.layout_custom);
        LinearLayout.LayoutParams llparam = new LinearLayout.LayoutParams((int)overflowWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
        viewGroup.setLayoutParams(llparam);

        // Adding Views in Overflow
        for( TableOptional.OptionsBean bean :menuItems) {
            if (bean.getTitle().contains("My Stories") && !PremiumPref.getInstance(mContext).isUserLoggedIn()) {
                continue;
            }
            viewGroup.addView(getOverflowView(bean.getId(), itemHeight, bean.getTitle(), "#808080", mUnreadBookmarkArticleCount, mUnreadNotificationArticleCount));
        }

        changeSortPopUp = new PopupWindow(mContext);
        //Inflating the Popup using xml file
        changeSortPopUp.setContentView(layout);
        changeSortPopUp.setWidth((int)overflowWidth);
        changeSortPopUp.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        changeSortPopUp.setFocusable(true);
        changeSortPopUp.setBackgroundDrawable(ResUtil.getBackgroundDrawable(mContext.getResources(), R.drawable.shadow_143418));

        return changeSortPopUp;
    }

    private LinearLayout rowTitleCount(int id, int height, String title, String textColor, boolean needCounterView, int count) {
        LinearLayout ll_1 = new LinearLayout(mContext);
        ll_1.setOrientation(LinearLayout.HORIZONTAL);
        ll_1.setGravity(Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams llParam_1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height);
        ll_1.setLayoutParams(llParam_1);

        CustomTextView title_TV_1 = new CustomTextView(mContext);
        title_TV_1.setGravity(Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams textParam_1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.8f);
        title_TV_1.setLayoutParams(textParam_1);
        title_TV_1.setText(title);
        title_TV_1.applyCustomFont(mContext, mContext.getString(R.string.THP_FiraSans_Regular));
        title_TV_1.setTextColor(Color.parseColor(textColor));
        ll_1.addView(title_TV_1);

        if(needCounterView && count > 0) {
            int readCountMargin = (int) mContext.getResources().getDimension(R.dimen.overflow_item_readcount_margin);
            int readCountWidth = (int) mContext.getResources().getDimension(R.dimen.overflow_item_readcount_width);
            int readCountHeight = (int) mContext.getResources().getDimension(R.dimen.overflow_item_readcount_height);

            CustomTextView readCount_TV_1 = new CustomTextView(mContext);
            readCount_TV_1.setBackground(ResUtil.getBackgroundDrawable(mContext.getResources(), R.drawable.layout_border));
            readCount_TV_1.setGravity(Gravity.CENTER);
            //readCount_TV_1.setPadding(10, 1, 10, 1);
            LinearLayout.LayoutParams readCount_TVParam = new LinearLayout.LayoutParams(readCountWidth, readCountHeight);

            readCount_TVParam.setMargins(readCountMargin, readCountMargin, readCountMargin, readCountMargin);
            readCount_TV_1.setLayoutParams(readCount_TVParam);
            readCount_TV_1.setTextSize(12);
            readCount_TV_1.setText(count + "");
            readCount_TV_1.applyCustomFont(mContext, mContext.getString(R.string.THP_FiraSans_Regular));
            readCount_TV_1.setTextColor(Color.WHITE);
            ll_1.addView(readCount_TV_1);
        }

        return ll_1;
    }

    private View getOverflowView(int id, int height, String title, String textColor, int readCount, int notificationCount) {
        final int READ_LATER_ID = 1;
        final int NOTIFICATION_ID = 2;
        final int PESONALISE_HOME_SECEEN_ID = 3;
        final int PESONALISE_MY_STORIES_ID = 4;
        final int SETTINGS_ID = 5;
        final int SHARE_ID = 6;

        switch (id) {
            case READ_LATER_ID:
                LinearLayout ll_1 = rowTitleCount(id, height, title, textColor, true, readCount);
                ll_1.setOnClickListener(v->{
                    IntentUtil.openBookmarkActivity(mContext, NetConstants.BOOKMARK_IN_ONE);
                    if(changeSortPopUp != null) {
                        changeSortPopUp.dismiss();
                        changeSortPopUp = null;
                    }
                });
                return ll_1;
            case NOTIFICATION_ID:
                LinearLayout ll_2 = rowTitleCount(id, height, title, textColor, true, notificationCount);
                ll_2.setOnClickListener(v->{
                    IntentUtil.openNotificationArticleActivity(mContext);
                    if(changeSortPopUp != null) {
                        changeSortPopUp.dismiss();
                        changeSortPopUp = null;
                    }
                });
                return ll_2;

            case PESONALISE_HOME_SECEEN_ID:
                LinearLayout ll_3 = rowTitleCount(id, height, title, textColor, false, -1);
                ll_3.setOnClickListener(v->{
                /*GoogleAnalyticsTracker.setGoogleAnalyticsEvent(
                        MainActivity.this,
                        getString(R.string.ga_action),
                        "Customise Subscription: Customise Subscription Button Clicked ",
                        getString(R.string.custom_home_screen));
                FlurryAgent.logEvent("Customise Subscription: Customise Subscription Button Clicked ");*/
                    IntentUtil.openHomeArticleOptionActivity((AppCompatActivity) mContext);
                    if(changeSortPopUp != null) {
                        changeSortPopUp.dismiss();
                        changeSortPopUp = null;
                    }
                });
                return ll_3;

            case PESONALISE_MY_STORIES_ID:
                LinearLayout ll_4 = rowTitleCount(id, height, title, textColor, false, -1);
                ll_4.setOnClickListener(v->{
                    if(!NetUtils.isConnected(mContext)) {
                        Alerts.noConnectionSnackBar(v, (AppCompatActivity) mContext);
                        return;
                    }
                /*GoogleAnalyticsTracker.setGoogleAnalyticsEvent(
                        MainActivity.this,
                        getString(R.string.ga_action),
                        "Customise: Customise Button Clicked ",
                        getString(R.string.custom_home_screen));
                FlurryAgent.logEvent("Customise: Customise Button Clicked ");*/
                    mContext.startActivity(new Intent(mContext, THPPersonaliseActivity.class));
                    if(changeSortPopUp != null) {
                        changeSortPopUp.dismiss();
                        changeSortPopUp = null;
                    }
                });
                return ll_4;

            case SETTINGS_ID:
                LinearLayout ll_5 = rowTitleCount(id, height, title, textColor, false, -1);
                ll_5.setOnClickListener(v->{
                    IntentUtil.openSettingActivity((AppCompatActivity) mContext);
                    if(changeSortPopUp != null) {
                        changeSortPopUp.dismiss();
                        changeSortPopUp = null;
                    }
                });
                return ll_5;

            case SHARE_ID:
                LinearLayout ll_6 = rowTitleCount(id, height, title, textColor, false, -1);
                ll_6.setOnClickListener(v->{
                    String mShareTitle = "Download The Hindu official app.";
                    String mShareUrl = "https://play.google.com/store/apps/details?id=com.mobstac.thehindu";
                    SharingArticleUtil.shareArticle(mContext, mShareTitle, mShareUrl,"Home");

                    //CleverTap
                    CleverTapUtil.cleverTapEvent(mContext,THPConstants.CT_EVENT_SHARE_THIS_APP,null);
                    if(changeSortPopUp != null) {
                        changeSortPopUp.dismiss();
                        changeSortPopUp = null;
                    }
                });
                return ll_6;
            default:
                LinearLayout ll_7 = rowTitleCount(id, height, title, textColor, false, -1);
                ll_7.setOnClickListener(v->{
                    if(changeSortPopUp != null) {
                        changeSortPopUp.dismiss();

                    }
                });
                return ll_7;
        }
    }
}
