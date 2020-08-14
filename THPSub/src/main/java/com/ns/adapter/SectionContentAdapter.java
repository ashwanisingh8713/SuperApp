package com.ns.adapter;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.netoperation.config.model.TabsBean;
import com.netoperation.config.model.WidgetIndex;
import com.netoperation.default_db.TableConfiguration;
import com.netoperation.model.AdData;
import com.netoperation.model.ArticleBean;
import com.netoperation.model.SectionAdapterItem;
import com.netoperation.model.StaticPageUrlBean;
import com.netoperation.net.DefaultTHApiManager;
import com.netoperation.net.RequestCallback;
import com.netoperation.util.AppDateUtil;
import com.netoperation.util.DefaultPref;
import com.netoperation.util.NetConstants;
import com.ns.activity.BaseAcitivityTHP;
import com.ns.activity.BaseRecyclerViewAdapter;
import com.ns.callbacks.WidgetItemClickListener;
import com.ns.contentfragment.TabIndicesFragment;
import com.ns.model.BSEData;
import com.ns.model.NSEData;
import com.ns.model.SensexData;
import com.ns.model.SensexStatus;
import com.ns.thpremium.BuildConfig;
import com.ns.thpremium.R;
import com.ns.utils.CommonUtil;
import com.ns.utils.ContentUtil;
import com.ns.utils.FragmentUtil;
import com.ns.utils.PicassoUtil;
import com.ns.utils.IntentUtil;
import com.ns.utils.ResUtil;
import com.ns.utils.RowIds;
import com.ns.utils.SharingArticleUtil;
import com.ns.utils.THPConstants;
import com.ns.utils.THPFirebaseAnalytics;
import com.ns.utils.WebViewClientForWebPage;
import com.ns.viewholder.ArticlesViewHolder;
import com.ns.viewholder.BannerViewHolder;
import com.ns.viewholder.HomePageFooterViewViewHolder;
import com.ns.viewholder.ExploreViewHolder;
import com.ns.viewholder.InlineAdViewHolder;
import com.ns.viewholder.LoadMoreViewHolder;
import com.ns.viewholder.SearchRecyclerHolder;
import com.ns.viewholder.SensexViewHolder;
import com.ns.viewholder.StaticItemWebViewHolder;
import com.ns.viewholder.SubSectionGridViewHolder;
import com.ns.viewholder.SubSectionListViewHolder;
import com.ns.viewholder.TaboolaNativeAdViewHolder;
import com.ns.viewholder.WidgetGridViewHolder;
import com.ns.viewholder.WidgetListViewHolder;
import com.ns.viewholder.WidgetPagerViewHolder;
import com.taboola.android.api.TBImageView;
import com.taboola.android.api.TBRecommendationItem;
import com.taboola.android.api.TBTextView;
import com.taboola.android.api.TaboolaApi;

import org.greenrobot.eventbus.EventBus;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class SectionContentAdapter extends BaseRecyclerViewAdapter {

    private ArrayList<SectionAdapterItem> adapterItems;
    private String mPageSource;
    private boolean mIsSubSection;
    private String mSectionId;
    private String mSectionType;
    private boolean isFetchingDataFromServer;
    private SparseIntArray positionList = new SparseIntArray();


    public SectionContentAdapter(String pageSource, ArrayList<SectionAdapterItem> adapterItems, boolean isSubsection, String sectionId, String sectionType) {
        this.mPageSource = pageSource;
        this.adapterItems = adapterItems;
        this.mIsSubSection = isSubsection;
        this.mSectionId = sectionId;
        this.mSectionType = sectionType;
    }

    public boolean isFetchingDataFromServer() {
        return isFetchingDataFromServer;
    }

    public void setFetchingDataFromServer(boolean fetchingDataFromServer) {
        isFetchingDataFromServer = fetchingDataFromServer;
    }

    @Override
    public int getItemViewType(int position) {
        return adapterItems.get(position).getViewType();
    }


    @Override
    public int getItemCount() {
        return adapterItems.size();
    }

    /*@Override
    public void onViewAttachedToWindow(final RecyclerView.ViewHolder holder) {
        if (holder instanceof StaticItemWebViewHolder) {
            holder.setIsRecyclable(false);
        } else if (holder instanceof WidgetsViewHolder) {
            holder.setIsRecyclable(false);
        }
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewDetachedFromWindow(final RecyclerView.ViewHolder holder) {
        if (holder instanceof StaticItemWebViewHolder){
            holder.setIsRecyclable(true);
        } else if (holder instanceof WidgetsViewHolder){
            holder.setIsRecyclable(true);
        }
        super.onViewDetachedFromWindow(holder);
    }*/


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == VT_LOADMORE) {
            return new LoadMoreViewHolder(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_loadmore, viewGroup, false));
        } else if (viewType == VT_THD_BANNER) {
            if (BuildConfig.IS_BL) {
                return new BannerViewHolder(LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.bl_home_banner, viewGroup, false));
            } else {
                return new BannerViewHolder(LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.th_home_banner, viewGroup, false));
            }
        } else if (viewType == VT_THD_DEFAULT_ROW) {
            if (BuildConfig.IS_BL) {
                return new ArticlesViewHolder(LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.bl_cardview_article_list, viewGroup, false));
            } else {
                return new ArticlesViewHolder(LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.th_cardview_article_list, viewGroup, false));
            }
        } else if (viewType == VT_THD_SEARCH_ROW) {
            return new SearchRecyclerHolder(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.search_recycler_item, viewGroup, false));
        } else if (viewType == VT_WEB_WIDGET) {
            return new StaticItemWebViewHolder(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_webview, viewGroup, false));
        } else if (viewType == VT_THD_HORIZONTAL_LIST) {
            return new ExploreViewHolder(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.cardview_home_explore, viewGroup, false));
        } else if (viewType == VT_THD_300X250_ADS) {
            if (BuildConfig.IS_BL) {
                return new InlineAdViewHolder(LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.bl_inline_ads_container, viewGroup, false));
            } else {
                return new InlineAdViewHolder(LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.inline_ads_container, viewGroup, false));
            }
        } else if (viewType == VT_TABOOLA_LISTING_ADS) {
            if (BuildConfig.IS_BL) {
                return new TaboolaNativeAdViewHolder(LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.bl_cardview_taboola_native_ad_item, viewGroup, false));
            } else {
                return new TaboolaNativeAdViewHolder(LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.cardview_taboola_native_ad_item, viewGroup, false));
            }
        } else if (viewType == VT_BL_SENSEX) {
            return new SensexViewHolder(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.bl_cardview_sensex, viewGroup, false));
        } else if (viewType == WIDGET_LAYOUT_H_LIST) {
            return new WidgetListViewHolder(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.widget_h_list_layout, viewGroup, false));
        } else if (viewType == WIDGET_LAYOUT_GRID) {
            return new WidgetGridViewHolder(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.widget_grid_layout, viewGroup, false));
        } else if (viewType == WIDGET_LAYOUT_PAGER) {
            return new WidgetPagerViewHolder(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.widget_pager_layout, viewGroup, false));
        }
        else if (viewType == SUBSECTION_LAYOUT_H_LIST) {
            return new SubSectionListViewHolder(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.subsection_h_list_layout, viewGroup, false));
        } else if (viewType == SUBSECTION_LAYOUT_GRID) {
            return new SubSectionGridViewHolder(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.subsection_grid_layout, viewGroup, false));
        } else if(viewType == VT_HOME_PAGE_FOOTER_VIEW) {
            return new HomePageFooterViewViewHolder(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.home_footer_view, viewGroup, false));
        }

        return new LoadMoreViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_loadmore, viewGroup, false));
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        SectionAdapterItem item = adapterItems.get(position);
        if (holder instanceof BannerViewHolder) {
            fillBannerData((BannerViewHolder) holder, position);
        } else if (holder instanceof ArticlesViewHolder) {
            fillArticleData(holder, position);
        } else if (holder instanceof SearchRecyclerHolder) {
            fillSearchedArticleData(holder, position);
        } else if (holder instanceof StaticItemWebViewHolder) {
            fillStaticWebview(holder, item, position);
        } else if (holder instanceof InlineAdViewHolder) {
            fillInlineAdView(holder, item, position);
        } else if (holder instanceof TaboolaNativeAdViewHolder) {
            fillTaboolaAds(holder, item, position);
        } else if (holder instanceof ExploreViewHolder) {
            fillExploreData(holder, item, position);
        } else if (holder instanceof SensexViewHolder) {
            bl_fillSensexData((SensexViewHolder) holder, position);
        } else if (holder instanceof WidgetListViewHolder) {
            su_WidgetList(holder, item, position);
        } else if (holder instanceof WidgetGridViewHolder) {
            su_WidgetGrid(holder, item, position);
        } else if(holder instanceof WidgetPagerViewHolder) {
            su_WidgetPager(holder, item, position);
        } else if (holder instanceof SubSectionListViewHolder) {
            subSectionHList(holder, item, position);
        } else if (holder instanceof SubSectionGridViewHolder) {
            subSectionGrid(holder, item, position);
        } else if(holder instanceof HomePageFooterViewViewHolder) {
            HomePageFooterViewViewHolder footerViewViewHolder = (HomePageFooterViewViewHolder) holder;
            footerViewViewHolder.itemView.setOnClickListener(v->{
                /*GoogleAnalyticsTracker.setGoogleAnalyticsEvent(
                        MainActivity.this,
                        getString(R.string.ga_action),
                        "Customise Subscription: Customise Subscription Button Clicked ",
                        getString(R.string.custom_home_screen));
                FlurryAgent.logEvent("Customise Subscription: Customise Subscription Button Clicked ");*/

                //Firebase event
                THPFirebaseAnalytics.setFirbaseAnalyticsEvent(holder.itemView.getContext(), "Action", "Customise Subscription: Customise Subscription Button Clicked ", "Personalise Home Screen");


                IntentUtil.openHomeArticleOptionActivity((AppCompatActivity) footerViewViewHolder.itemView.getContext());
            });
        }
    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        if (holder instanceof TaboolaNativeAdViewHolder) {
            ((TaboolaNativeAdViewHolder) holder).mAdContainer.removeAllViews();
            ((TaboolaNativeAdViewHolder) holder).thumbNailContainer.removeAllViews();
        } else if (holder instanceof WidgetListViewHolder) {
            WidgetListViewHolder widgetsViewHolder = (WidgetListViewHolder) holder;
            final int position = widgetsViewHolder.getAdapterPosition();
            int firstVisiblePosition = ((LinearLayoutManager)(widgetsViewHolder.groupRecyclerView.getLayoutManager())).findFirstVisibleItemPosition();
            positionList.put(position, firstVisiblePosition);
        }
    }

    private void subSectionGrid(final RecyclerView.ViewHolder viewHolder, SectionAdapterItem item, int verticleItemPosition) {
        SubSectionGridViewHolder holder = (SubSectionGridViewHolder) viewHolder;

        ExploreAdapter adapter = item.getExploreAdapter();
        if (adapter == null || adapter.getItemCount() == 0) {
            return;
        }

        RecyclerView.LayoutParams itemParams = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
        ConstraintLayout.LayoutParams recyclerParams = (ConstraintLayout.LayoutParams) holder.groupRecyclerView.getLayoutParams();
        List<Integer> layoutPadding = adapter.getWidgetIndex().getGroupMargin();

        if(layoutPadding != null && layoutPadding.size() == 4) {
            itemParams.setMargins((int)ResUtil.pxFromDp(holder.itemView.getContext(), layoutPadding.get(0)),
                    (int)ResUtil.pxFromDp(holder.itemView.getContext(), layoutPadding.get(1)),
                    (int)ResUtil.pxFromDp(holder.itemView.getContext(), layoutPadding.get(2)),
                    (int)ResUtil.pxFromDp(holder.itemView.getContext(), layoutPadding.get(3)));
        }

        // Setting Radius
        holder.cardView.setRadius((int)ResUtil.pxFromDp(viewHolder.itemView.getContext(), adapter.getWidgetIndex().getGroupRadius()));
        // Setting Elevation
        holder.cardView.setElevation((int)ResUtil.pxFromDp(viewHolder.itemView.getContext(), adapter.getWidgetIndex().getGroupElevation()));

        holder.groupRecyclerView.setLayoutManager(new GridLayoutManager(viewHolder.itemView.getContext(), 2));
        holder.groupRecyclerView.setAdapter(adapter);

        WidgetIndex widgetIndex = adapter.getWidgetIndex();

        boolean iconRequired = widgetIndex.isActionIconRequired();
        boolean headerRequired = widgetIndex.isGroupHeaderRequired();
        String actionTitle = widgetIndex.getActionTitle();

        holder.groupHeaderTxt.setText(actionTitle);

        if (iconRequired && headerRequired) {
            holder.groupHeaderIcon.setVisibility(View.VISIBLE);
        } else {
            holder.groupHeaderIcon.setVisibility(View.GONE);
        }

        if(adapter.getWidgetIndex().isGroupOuterLineRequired()) {
            holder.innerParent.setBackground(ResUtil.getBackgroundDrawable(holder.itemView.getResources(), R.drawable.cartoon_border));
        } else {
            holder.innerParent.setBackground(null);
        }


    }

    private void subSectionHList(final RecyclerView.ViewHolder viewHolder, SectionAdapterItem item, int verticleItemPosition) {
        SubSectionListViewHolder holder = (SubSectionListViewHolder) viewHolder;

        ExploreAdapter adapter = item.getExploreAdapter();
        if (adapter == null || adapter.getItemCount() == 0) {
            return;
        }

        RecyclerView.LayoutParams itemParams = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
        ConstraintLayout.LayoutParams recyclerParams = (ConstraintLayout.LayoutParams) holder.groupRecyclerView.getLayoutParams();
        List<Integer> layoutPadding = adapter.getWidgetIndex().getGroupMargin();

        if(layoutPadding != null && layoutPadding.size() == 4) {
            itemParams.setMargins((int)ResUtil.pxFromDp(holder.itemView.getContext(), layoutPadding.get(0)),
                    (int)ResUtil.pxFromDp(holder.itemView.getContext(), layoutPadding.get(1)),
                    (int)ResUtil.pxFromDp(holder.itemView.getContext(), layoutPadding.get(2)),
                    (int)ResUtil.pxFromDp(holder.itemView.getContext(), layoutPadding.get(3)));
            recyclerParams.setMargins(0, 0, (int)ResUtil.pxFromDp(holder.itemView.getContext(), layoutPadding.get(3)), 0);
        }

        // Setting Radius
        holder.cardView.setRadius((int)ResUtil.pxFromDp(viewHolder.itemView.getContext(), adapter.getWidgetIndex().getGroupRadius()));
        // Setting Elevation
        holder.cardView.setElevation((int)ResUtil.pxFromDp(viewHolder.itemView.getContext(), adapter.getWidgetIndex().getGroupElevation()));

        WidgetIndex widgetIndex = adapter.getWidgetIndex();

        boolean iconRequired = widgetIndex.isActionIconRequired();
        boolean headerRequired = widgetIndex.isGroupHeaderRequired();
        String actionTitle = widgetIndex.getActionTitle();

        holder.groupHeaderTxt.setText(actionTitle);

        if (iconRequired && headerRequired) {
            holder.groupHeaderIcon.setVisibility(View.VISIBLE);
        } else {
            holder.groupHeaderIcon.setVisibility(View.GONE);
        }
        holder.groupRecyclerView.setAdapter(adapter);

        if(adapter.getWidgetIndex().isGroupOuterLineRequired()) {
            holder.innerParent.setBackground(ResUtil.getBackgroundDrawable(holder.itemView.getResources(), R.drawable.cartoon_border));
        } else {
            holder.innerParent.setBackground(null);
        }

        // Retrieve and set the saved position
        int lastSeenFirstPosition = positionList.get(verticleItemPosition, 0);
        if (lastSeenFirstPosition >= 0) {
            ((LinearLayoutManager)holder.groupRecyclerView.getLayoutManager()).scrollToPositionWithOffset(lastSeenFirstPosition, 0);
        }
    }

    private void su_WidgetList(final RecyclerView.ViewHolder viewHolder, SectionAdapterItem item, int verticleItemPosition) {
        WidgetListViewHolder holder = (WidgetListViewHolder) viewHolder;
        SuWidgetRecyclerAdapter adapter = item.getSuWidgetRecyclerAdapter();

        if (adapter == null || adapter.getItemCount() == 0) {
            return;
        }

        RecyclerView.LayoutParams itemParams = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
        ConstraintLayout.LayoutParams recyclerParams = (ConstraintLayout.LayoutParams) holder.groupRecyclerView.getLayoutParams();
        List<Integer> layoutPadding = adapter.getWidgetIndex().getGroupMargin();

        if(layoutPadding != null && layoutPadding.size() == 4) {
            itemParams.setMargins((int)ResUtil.pxFromDp(holder.itemView.getContext(), layoutPadding.get(0)),
                    (int)ResUtil.pxFromDp(holder.itemView.getContext(), layoutPadding.get(1)),
                    (int)ResUtil.pxFromDp(holder.itemView.getContext(), layoutPadding.get(2)),
                    (int)ResUtil.pxFromDp(holder.itemView.getContext(), layoutPadding.get(3)));
            recyclerParams.setMargins(0, 0, (int)ResUtil.pxFromDp(holder.itemView.getContext(), layoutPadding.get(3)), 0);
        }

        widgetLayoutCommonAdjustment(adapter, holder.groupHeaderIcon, holder.groupHeaderTxt, holder.groupRecyclerView,
                holder.groupActionLeft, holder.groupActionCenter, holder.groupActionRight);

        // Setting Radius
        holder.cardView.setRadius((int)ResUtil.pxFromDp(viewHolder.itemView.getContext(), adapter.getWidgetIndex().getGroupRadius()));
        // Setting Elevation
        holder.cardView.setElevation((int)ResUtil.pxFromDp(viewHolder.itemView.getContext(), adapter.getWidgetIndex().getGroupElevation()));


        holder.groupRecyclerView.setAdapter(adapter);

        adapter.setWidgetItemClickListener(new WidgetItemClickListener() {
            @Override
            public void onWidgetItemClickListener(int innerItemPosition, String secId) {
                int firstVisiblePosition = ((LinearLayoutManager)holder.groupRecyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                positionList.put(verticleItemPosition, firstVisiblePosition);
            }
        });

        if(adapter.getWidgetIndex().isGroupOuterLineRequired()) {
            holder.innerParent.setBackground(ResUtil.getBackgroundDrawable(holder.itemView.getResources(), R.drawable.cartoon_border));
        } else {
            holder.innerParent.setBackground(null);
        }

        // Retrieve and set the saved position
        int lastSeenFirstPosition = positionList.get(verticleItemPosition, 0);
        if (lastSeenFirstPosition >= 0) {
            ((LinearLayoutManager)holder.groupRecyclerView.getLayoutManager()).scrollToPositionWithOffset(lastSeenFirstPosition, 0);
        }

    }

    private void su_WidgetGrid(final RecyclerView.ViewHolder viewHolder, SectionAdapterItem item, int position) {
        WidgetGridViewHolder holder = (WidgetGridViewHolder) viewHolder;
        SuWidgetRecyclerAdapter adapter = item.getSuWidgetRecyclerAdapter();

        if (adapter == null || adapter.getItemCount() == 0) {
            return;
        }

        RecyclerView.LayoutParams itemParams = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
        //ConstraintLayout.LayoutParams recyclerParams = (ConstraintLayout.LayoutParams) widgetHolder.groupRecyclerView.getLayoutParams();
        List<Integer> layoutPadding = adapter.getWidgetIndex().getGroupMargin();

        if(layoutPadding != null && layoutPadding.size() == 4) {
            itemParams.setMargins((int)ResUtil.pxFromDp(holder.itemView.getContext(), layoutPadding.get(0)),
                    (int)ResUtil.pxFromDp(holder.itemView.getContext(), layoutPadding.get(1)),
                    (int)ResUtil.pxFromDp(holder.itemView.getContext(), layoutPadding.get(2)),
                    (int)ResUtil.pxFromDp(holder.itemView.getContext(), layoutPadding.get(3)));
            //recyclerParams.setMargins(0, 0, (int)ResUtil.pxFromDp(widgetHolder.itemView.getContext(), layoutPadding.get(3)), 0);
        }

        // Setting Radius
        holder.cardView.setRadius((int)ResUtil.pxFromDp(viewHolder.itemView.getContext(), adapter.getWidgetIndex().getGroupRadius()));
        // Setting Elevation
        holder.cardView.setElevation((int)ResUtil.pxFromDp(viewHolder.itemView.getContext(), adapter.getWidgetIndex().getGroupElevation()));

        holder.groupRecyclerView.setLayoutManager(new GridLayoutManager(viewHolder.itemView.getContext(), 2));
        holder.groupRecyclerView.setAdapter(adapter);

        if(adapter.getWidgetIndex().isGroupOuterLineRequired()) {
            holder.innerParent.setBackground(ResUtil.getBackgroundDrawable(holder.itemView.getResources(), R.drawable.cartoon_border));
        } else {
            holder.innerParent.setBackground(null);
        }

        widgetLayoutCommonAdjustment(adapter, holder.groupHeaderIcon, holder.groupHeaderTxt, holder.groupRecyclerView,
                holder.groupActionLeft, holder.groupActionCenter, holder.groupActionRight);

    }

    private void su_WidgetPager(final RecyclerView.ViewHolder viewHolder, SectionAdapterItem item, int position) {
        WidgetPagerViewHolder holder = (WidgetPagerViewHolder) viewHolder;
        SuWidgetRecyclerAdapter adapter = item.getSuWidgetRecyclerAdapter();

        if (adapter == null || adapter.getItemCount() == 0) {
            return;
        }

        RecyclerView.LayoutParams itemParams = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
        //ConstraintLayout.LayoutParams recyclerParams = (ConstraintLayout.LayoutParams) widgetHolder.groupRecyclerView.getLayoutParams();
        List<Integer> layoutPadding = adapter.getWidgetIndex().getGroupMargin();

        if(layoutPadding != null && layoutPadding.size() == 4) {
            itemParams.setMargins((int)ResUtil.pxFromDp(holder.itemView.getContext(), layoutPadding.get(0)),
                    (int)ResUtil.pxFromDp(holder.itemView.getContext(), layoutPadding.get(1)),
                    (int)ResUtil.pxFromDp(holder.itemView.getContext(), layoutPadding.get(2)),
                    (int)ResUtil.pxFromDp(holder.itemView.getContext(), layoutPadding.get(3)));
            //recyclerParams.setMargins(0, 0, (int)ResUtil.pxFromDp(widgetHolder.itemView.getContext(), layoutPadding.get(3)), 0);
        }

        // Setting Radius
        holder.cardView.setRadius((int)ResUtil.pxFromDp(viewHolder.itemView.getContext(), adapter.getWidgetIndex().getGroupRadius()));
        // Setting Elevation
        holder.cardView.setElevation((int)ResUtil.pxFromDp(viewHolder.itemView.getContext(), adapter.getWidgetIndex().getGroupElevation()));

        //holder.groupRecyclerView.setLayoutManager(new GridLayoutManager(viewHolder.itemView.getContext(), 2));
        holder.groupViewPager.setAdapter(adapter);

        /*widgetLayoutCommonAdjustment(adapter, holder.groupHeaderIcon, holder.groupHeaderTxt, holder.groupRecyclerView,
                holder.groupActionLeft, holder.groupActionCenter, holder.groupActionRight);*/

    }


    private void widgetLayoutCommonAdjustment(SuWidgetRecyclerAdapter adapter, ImageView groupHeaderIcon, TextView groupHeaderTxt, RecyclerView groupRecyclerView,
                                              TextView groupActionLeft, TextView groupActionCenter, TextView groupActionRight) {
        WidgetIndex widgetIndex = adapter.getWidgetIndex();

        String actionGravity = widgetIndex.getActionGravity();
        boolean iconRequired = widgetIndex.isActionIconRequired();
        boolean headerRequired = widgetIndex.isGroupHeaderRequired();
        String actionTitle = widgetIndex.getActionTitle();

        actionTitle += " "+adapter.getSectionName();

        if (iconRequired && headerRequired) {
            groupHeaderIcon.setVisibility(View.VISIBLE);
        } else {
            groupHeaderIcon.setVisibility(View.GONE);
        }

        if (headerRequired) {
            groupHeaderTxt.setText(adapter.getSectionName());
            groupHeaderTxt.setVisibility(View.VISIBLE);
            if(BaseAcitivityTHP.sIsDayTheme) {
                groupHeaderTxt.setTextColor(Color.parseColor(widgetIndex.getTitle().getLight()));
            } else {
                groupHeaderTxt.setTextColor(Color.parseColor(widgetIndex.getTitle().getDark()));
            }
        } else {
            groupHeaderTxt.setVisibility(View.GONE);
        }


        if (actionGravity.equalsIgnoreCase("LeftBottom")) {
            groupActionLeft.setVisibility(View.VISIBLE);
            groupActionLeft.setText(actionTitle);
            if(BaseAcitivityTHP.sIsDayTheme) {
                groupActionLeft.setTextColor(Color.parseColor(widgetIndex.getAction().getLight()));
            } else {
                groupActionLeft.setTextColor(Color.parseColor(widgetIndex.getAction().getDark()));
            }

            if(iconRequired) {
                groupActionLeft.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.back_more_from, 0);
            }

            groupActionCenter.setVisibility(View.GONE);
            groupActionRight.setVisibility(View.GONE);
        } else if (actionGravity.equalsIgnoreCase("CenterBottom")) {
            groupActionLeft.setVisibility(View.GONE);
            groupActionRight.setVisibility(View.GONE);
            groupActionCenter.setVisibility(View.VISIBLE);
            groupActionCenter.setText(actionTitle);
            if(BaseAcitivityTHP.sIsDayTheme) {
                groupActionCenter.setTextColor(Color.parseColor(widgetIndex.getAction().getLight()));
            } else {
                groupActionCenter.setTextColor(Color.parseColor(widgetIndex.getAction().getDark()));
            }

            if(iconRequired) {
                groupActionCenter.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.back_more_from, 0);
            }

        } else if (actionGravity.equalsIgnoreCase("RightBottom")) {
            groupActionRight.setVisibility(View.VISIBLE);
            groupActionRight.setText(actionTitle);
            if(BaseAcitivityTHP.sIsDayTheme) {
                groupActionRight.setTextColor(Color.parseColor(widgetIndex.getAction().getLight()));
            } else {
                groupActionRight.setTextColor(Color.parseColor(widgetIndex.getAction().getDark()));
            }
            if(iconRequired) {
                groupActionRight.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.back_more_from, 0);
            }
            groupActionLeft.setVisibility(View.GONE);
            groupActionCenter.setVisibility(View.GONE);
        } else {
            groupActionLeft.setVisibility(View.GONE);
            groupActionCenter.setVisibility(View.GONE);
            groupActionRight.setVisibility(View.GONE);
        }

        groupActionLeft.setOnClickListener(viewAllClick(widgetIndex.getSecId()));
        groupActionCenter.setOnClickListener(viewAllClick(widgetIndex.getSecId()));
        groupActionRight.setOnClickListener(viewAllClick(widgetIndex.getSecId()));

    }

    private View.OnClickListener viewAllClick(String sectionId) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentUtil.redirectionOnSectionAndSubSection(v.getContext(), sectionId);
            }
        };
    }

    private void fillExploreData(final RecyclerView.ViewHolder holder, SectionAdapterItem item, int position) {
        ExploreViewHolder exploreHolder = (ExploreViewHolder) holder;
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(exploreHolder.itemView.getContext(), LinearLayoutManager.HORIZONTAL, false);
        exploreHolder.mExploreRecyclerView.setLayoutManager(mLayoutManager);
        exploreHolder.mExploreRecyclerView.setHasFixedSize(true);
        exploreHolder.mExploreRecyclerView.setAdapter(item.getExploreAdapter());
        if (THPConstants.IS_SHOW_INDEX) {
            exploreHolder.exploreTitle.setText(indexText(position, holder.itemView.getContext().getString(R.string.info_home_explore)));
        }
    }

    private void fillTaboolaAds(final RecyclerView.ViewHolder holder, SectionAdapterItem item, int position) {
        TBRecommendationItem tbRecommendationItem = item.getAdData().getTaboolaNativeAdItem();
        TaboolaNativeAdViewHolder taboolaNativeAdViewHolder = (TaboolaNativeAdViewHolder) holder;
        taboolaNativeAdViewHolder.mAttributionView.setOnClickListener(view -> TaboolaApi.getInstance().handleAttributionClick(holder.itemView.getContext()));
        if (tbRecommendationItem == null) {
            return;
        }
        TBImageView thumbnailView = tbRecommendationItem.getThumbnailView(taboolaNativeAdViewHolder.itemView.getContext());
        thumbnailView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        ViewGroup parent = (ViewGroup) thumbnailView.getParent();
        if (parent != null) {
            parent.removeView(thumbnailView);
        }

        TBTextView tbTextView = tbRecommendationItem.getTitleView(taboolaNativeAdViewHolder.itemView.getContext());

        ViewGroup parent2 = (ViewGroup) tbTextView.getParent();
        if (parent2 != null) {
            parent2.removeView(tbTextView);
        }

        boolean isUserThemeDay = DefaultPref.getInstance(holder.itemView.getContext()).isUserThemeDay();
        if (isUserThemeDay) {
            tbTextView.setTextColor(ResUtil.getColor(holder.itemView.getContext().getResources(), R.color.color_111111_light));
        } else {
            tbTextView.setTextColor(ResUtil.getColor(holder.itemView.getContext().getResources(), R.color.color_ededed_dark));
        }
        tbTextView.setMaxLines(2);
        tbTextView.setEllipsize(TextUtils.TruncateAt.END);
        tbTextView.setTypeface(Typeface.createFromAsset(holder.itemView.getContext().getAssets(), holder.itemView.getContext().getResources().getString(R.string.THP_TundraOffc)));
        tbTextView.setTextSize(17);

        taboolaNativeAdViewHolder.thumbNailContainer.addView(thumbnailView);
        taboolaNativeAdViewHolder.mAdContainer.addView(tbTextView);

        if (tbRecommendationItem.getBrandingView(taboolaNativeAdViewHolder.itemView.getContext()) != null) {
            TBTextView tbBrandTextView = tbRecommendationItem.getBrandingView(taboolaNativeAdViewHolder.itemView.getContext());
            tbBrandTextView.setTypeface(Typeface.createFromAsset(holder.itemView.getContext().getAssets(),
                    holder.itemView.getContext().getResources().getString(R.string.THP_FiraSans_Regular)));
            ViewGroup parent3 = (ViewGroup) tbBrandTextView.getParent();
            if (parent3 != null) {
                parent3.removeView(tbBrandTextView);
            }
            taboolaNativeAdViewHolder.mAdContainer.addView(tbBrandTextView);
        }

        if (THPConstants.IS_SHOW_INDEX) {
            String title = tbTextView.getText().toString();
            tbTextView.setText(indexText(position, title));
        }

        TaboolaApi.getInstance().setOnClickListener((placementName, itemId, url, isOrganic) -> {
            Log.i("", "");

            if (isOrganic) {
                int articleId = CommonUtil.getArticleIdFromArticleUrl(url);
                IntentUtil.openDetailAfterSearchInActivity(holder.itemView.getContext(), "" + articleId, url, NetConstants.G_DEFAULT_SECTIONS);

              /*  FlurryAgent.logEvent(holder.itemView.getContext().getString(R.string.ga_article_taboola_home_organic_clicked));
                GoogleAnalyticsTracker.setGoogleAnalyticsEvent(holder.itemView.getContext(), "Taboola Item Click",
                        holder.itemView.getContext().getString(R.string.ga_article_taboola_home_organic_clicked),
                        holder.itemView.getContext().getString(R.string.ga_home));*/
                //Firebase event
                THPFirebaseAnalytics.setFirbaseAnalyticsEvent(holder.itemView.getContext(), "Taboola Item Click",
                        holder.itemView.getContext().getString(R.string.ga_article_taboola_home_organic_clicked),
                        holder.itemView.getContext().getString(R.string.ga_home));
                return false;
            } else {
                /*FlurryAgent.logEvent(holder.itemView.getContext().getString(R.string.ga_article_taboola_home_nonorganic_clicked));
                GoogleAnalyticsTracker.setGoogleAnalyticsEvent(holder.itemView.getContext(), "Taboola Item Click",
                        holder.itemView.getContext().getString(R.string.ga_article_taboola_home_nonorganic_clicked),
                        holder.itemView.getContext().getString(R.string.ga_home));*/

                //Firebase event
                THPFirebaseAnalytics.setFirbaseAnalyticsEvent(holder.itemView.getContext(), "Taboola Item Click",
                        holder.itemView.getContext().getString(R.string.ga_article_taboola_home_organic_clicked),
                        holder.itemView.getContext().getString(R.string.ga_home));
            }

            return true;
        });
    }

    private void fillInlineAdView(final RecyclerView.ViewHolder holder, SectionAdapterItem item, int position) {
        AdData adData = item.getAdData();
        InlineAdViewHolder inlineAdViewHolder = (InlineAdViewHolder) holder;
        if (THPConstants.IS_SHOW_INDEX) {
            inlineAdViewHolder.indexTxt.setText(indexText(position, ""));
        }
        inlineAdViewHolder.frameLayout.removeAllViews();
        final PublisherAdView adView = adData.getAdView();
        //inlineAdViewHolder.frameLayout.setBackground(null);

        if (adView != null && adData.isReloadOnScroll()) {
            // Create an ad request.
            PublisherAdRequest.Builder publisherAdRequestBuilder = new PublisherAdRequest.Builder();
            // Start loading the ad.
            adView.loadAd(publisherAdRequestBuilder.build());
        }

        inlineAdViewHolder.frameLayout.setBackground(null);
        if (adView != null) {
            adView.removeView(inlineAdViewHolder.frameLayout);
        }
        if (adView != null && adView.getParent() != null) {
            adView.removeView(inlineAdViewHolder.frameLayout);
            ((ViewGroup) adView.getParent()).removeView(adView);
        }
        if (adView != null) {
            inlineAdViewHolder.frameLayout.addView(adView);
        }

    }

    private void fillStaticWebview(final RecyclerView.ViewHolder holder, SectionAdapterItem item, int position) {
        StaticItemWebViewHolder staticItemHolder = (StaticItemWebViewHolder) holder;
        StaticPageUrlBean pageUrlBean = item.getStaticPageUrlBean();
        if (pageUrlBean == null || !BaseAcitivityTHP.sIsOnline) {
            return;
        }
        staticItemHolder.webView.loadUrl(pageUrlBean.getUrl());
        if (!pageUrlBean.getSectionId().equals("0")) {
            staticItemHolder.mDummyView.setVisibility(View.VISIBLE);
            // DummyView Click Listener
            // TODO, redirect on particular section or sub-section
            // staticItemHolder.mDummyView.setOnClickListener(new StaticItemDummyViewClick(url, mContext, null, false, -1));
        } else {
            staticItemHolder.mDummyView.setVisibility(View.GONE);
            // Enabling Weblink click on Lead Text
            new WebViewClientForWebPage().linkClick(staticItemHolder.webView, staticItemHolder.itemView.getContext(), null);
        }
    }

    private void fillSearchedArticleData(final RecyclerView.ViewHolder holder, final int position) {
        SearchRecyclerHolder searchHolder = (SearchRecyclerHolder) holder;
        final ArticleBean bean = adapterItems.get(position).getArticleBean();
        searchHolder.title.setText(bean.getTi());
        if (THPConstants.IS_SHOW_INDEX) {
            searchHolder.title.setText(indexText(position, bean.getTi()));
        }
        searchHolder.sname.setText(bean.getSname());
        String publishDate = bean.getPd();
        String formatedDate = AppDateUtil.getDurationFormattedDate(AppDateUtil.changeStringToMillis(publishDate), Locale.ENGLISH);
        searchHolder.publishDate.setText(formatedDate);
        searchHolder.mParentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    /*GoogleAnalyticsTracker.setGoogleAnalyticsEvent(v.getContext(), "Searched ", "Searched: Article Clicked", "Search Fragment");
                    FlurryAgent.logEvent("Searched: " + "Article Clicked");*/
                //Firebase event
                THPFirebaseAnalytics.setFirbaseAnalyticsEvent(v.getContext(), "Searched ", "Searched: Article Clicked", "Search Fragment");
                IntentUtil.openSingleDetailActivity(v.getContext(), NetConstants.RECO_TEMP_NOT_EXIST, bean, bean.getArticleLink());
            }
        });

    }

    private void fillArticleData(final RecyclerView.ViewHolder articlesViewHolder, final int position) {
        ArticlesViewHolder holder = (ArticlesViewHolder) articlesViewHolder;
        final ArticleBean bean = adapterItems.get(position).getArticleBean();
        if (bean != null) {

            if (BuildConfig.IS_BL) {
                holder.author_textView.setText(bean.getAu());
            }

            // Dims Read article given view
            dimReadArticle(holder.itemView.getContext(), bean.getArticleId(), holder.mArticlesLayout);
            // Checks article's type
            articleTypeImage(bean.getArticleType(), bean, holder.mMultimediaButton);

            // Checks whether article is bookmarked or not, If yes then it updates UI
            isExistInBookmark(holder.itemView.getContext(), bean, holder.mBookmarkButton);

            String imageUrl = bean.getIm_thumbnail_v2();

            if (imageUrl == null || TextUtils.isEmpty(imageUrl)) {
                imageUrl = bean.getIm_thumbnail();
            }

            if (imageUrl != null && !TextUtils.isEmpty(imageUrl)) {
                holder.mImageParentLayout.setVisibility(View.VISIBLE);
                holder.mArticleImageView.setVisibility(View.VISIBLE);
                imageUrl = ContentUtil.getThumbUrl(imageUrl);
                PicassoUtil.loadImageWithFilePH(holder.itemView.getContext(), holder.mArticleImageView, imageUrl);

            } else {
                holder.mArticleImageView.setVisibility(View.GONE);
                holder.mImageParentLayout.setVisibility(View.GONE);
            }

            holder.mArticleTextView.setText(bean.getTi());

            if (THPConstants.IS_SHOW_INDEX) {
                holder.mArticleTextView.setText(indexText(position, bean.getTi()));
            }

            String publishTime = bean.getGmt();
            String timeDiff = AppDateUtil.getDurationFormattedDate(AppDateUtil.changeStringToMillisGMT(publishTime), Locale.ENGLISH);

            holder.mArticleTimeTextView.setText(timeDiff);
            holder.mArticleSectionName.setText(bean.getSname());

            holder.mBookmarkButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isFetchingDataFromServer()) {
                        return;
                    }
                    //GoogleAnalyticsTracker.setGoogleAnalyticsEvent(v.getContext(), "Home", "Home: Bookmark button Clicked", "Home Fragment");

                    //Firebase Events
                    THPFirebaseAnalytics.setFirbaseAnalyticsEvent(v.getContext(), "Home", "Home: Bookmark button Clicked", "Home Fragment");

                    //FlurryAgent.logEvent("Home: " + "Bookmark button Clicked");

                    local_bookmarkOperation(v.getContext(), bean, holder.mBookmarkButton, position);

                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isFetchingDataFromServer()) {
                        return;
                    }
                    //GoogleAnalyticsTracker.setGoogleAnalyticsEvent(view.getContext(), "Home", "Home: Article Clicked", "Home Fragment");

                    //Firebase Events
                    THPFirebaseAnalytics.setFirbaseAnalyticsEvent(view.getContext(), "Home", "Home: Article Clicked", "Home Fragment");

                    //FlurryAgent.logEvent("Home: " + "Article Clicked");
                    IntentUtil.openDetailActivity(view.getContext(), mPageSource, bean.getArticleId(), mSectionId, mSectionType, bean.getSectionName(), mIsSubSection);
                }
            });

            holder.mMultimediaButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isFetchingDataFromServer()) {
                        return;
                    }
                    //GoogleAnalyticsTracker.setGoogleAnalyticsEvent(view.getContext(), "Home", "Home: Article Clicked", "Home Fragment");

                    //Firebase Events
                    THPFirebaseAnalytics.setFirbaseAnalyticsEvent(view.getContext(), "Home", "Home: Article Clicked", "Home Fragment");

                    //FlurryAgent.logEvent("Home: " + "Article Clicked");
                    IntentUtil.openDetailActivity(view.getContext(), mPageSource, bean.getArticleId(), mSectionId, mSectionType, bean.getSectionName(), mIsSubSection);
                }
            });

            holder.mShareArticleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isFetchingDataFromServer()) {
                        return;
                    }
                    SharingArticleUtil.shareArticle(v.getContext(), bean);
                }
            });

            //Enabling sliding for view pager
            //new UniversalTouchListener(mArticleViewHolder.mArticlesLayout, true);
        }
    }


    /**
     * Fill Banner
     *
     * @param holder
     * @param position
     */
    private void fillBannerData(final BannerViewHolder holder, final int position) {
        final ArticleBean bean = adapterItems.get(position).getArticleBean();

        // Dims Read article given view
        dimReadArticle(holder.itemView.getContext(), bean.getArticleId(), holder.mBannerTextView);

        // Checks article's type
        articleTypeImage(bean.getArticleType(), bean, holder.mMultimediaButton);

        // Checks whether article is bookmarked or not, If yes then it updates UI
        isExistInBookmark(holder.itemView.getContext(), bean, holder.mBookmarkButton);

        String imageUrl = null;
        if (bean.getHi().equals("1")) {
            imageUrl = bean.getMe().get(0).getIm_v2();
        }
        if (imageUrl != null && !TextUtils.isEmpty(imageUrl)) {
            imageUrl = ContentUtil.getBannerUrl(imageUrl);
//            imageUrl = ContentUtil.getWidgetUrl(imageUrl);
            PicassoUtil.loadImageWithFilePH(holder.itemView.getContext(), holder.mBannerImageView, imageUrl);
        }

        articleTypeImage(bean.getArticleType(), bean, holder.mMultimediaButton);

        holder.mBannerTextView.setText(bean.getTi());

        if (THPConstants.IS_SHOW_INDEX) {
            holder.mBannerTextView.setText(indexText(position, bean.getTi()));
        }

        String publishTime = bean.getGmt();
        String timeDiff = AppDateUtil.getDurationFormattedDate(AppDateUtil.changeStringToMillisGMT(publishTime), Locale.ENGLISH);
        holder.mArticleUpdateTime.setText(timeDiff);
        holder.mArticleSectionName.setText(bean.getSname());

        holder.mBannerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFetchingDataFromServer()) {
                    return;
                }
                    /*GoogleAnalyticsTracker.setGoogleAnalyticsEvent(v.getContext(), "Banner", "Banner: Article Clicked", "Home Fragment");
                    FlurryAgent.logEvent("Banner: " + "Article Clicked");*/

                //Firebase Events
                THPFirebaseAnalytics.setFirbaseAnalyticsEvent(v.getContext(), "Banner", "Banner: Article Clicked", "Home Fragment");


                IntentUtil.openDetailActivity(holder.itemView.getContext(), mPageSource,
                        bean.getArticleId(), mSectionId, mSectionType, bean.getSectionName(), mIsSubSection);

            }
        });

        holder.mMultimediaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFetchingDataFromServer()) {
                    return;
                }
                    /*GoogleAnalyticsTracker.setGoogleAnalyticsEvent(v.getContext(), "Banner", "Banner: Article Clicked", "Home Fragment");
                    FlurryAgent.logEvent("Banner: " + "Article Clicked");*/

                //Firebase Events
                THPFirebaseAnalytics.setFirbaseAnalyticsEvent(v.getContext(), "Banner", "Banner: Article Clicked", "Home Fragment");


            }
        });
        holder.mBookmarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFetchingDataFromServer()) {
                    return;
                }
                    /*GoogleAnalyticsTracker.setGoogleAnalyticsEvent(v.getContext(), "Home", "Home: Bookmark button Clicked", "Home Fragment");
                    FlurryAgent.logEvent("Home: " + "Bookmark button Clicked");*/

                //Firebase Events
                THPFirebaseAnalytics.setFirbaseAnalyticsEvent(v.getContext(), "Banner", "Home: Bookmark button Clicked", "Home Fragment");


                local_bookmarkOperation(v.getContext(), bean, holder.mBookmarkButton, position);
            }
        });

        holder.mShareArticleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFetchingDataFromServer()) {
                    return;
                }
                SharingArticleUtil.shareArticle(v.getContext(), bean);
            }
        });

        //Enabling sliding for view pager
        //new UniversalTouchListener(mBannerViewHolder.mBannerLayout, true);

    }


    public void deleteItem(SectionAdapterItem item) {
        adapterItems.remove(item);
    }

    public void addSingleItem(SectionAdapterItem item) {
        if (adapterItems == null) {
            adapterItems = new ArrayList<>();
        }
        adapterItems.add(item);
        notifyItemChanged(adapterItems.size() - 1);
    }

    public void addMultiItems(ArrayList<SectionAdapterItem> items) {
        if (adapterItems == null) {
            adapterItems = new ArrayList<>();
        }
        int fromIndex = adapterItems.size();
        adapterItems.addAll(items);
        notifyItemRangeChanged(fromIndex, items.size());
    }

    public int insertItem(SectionAdapterItem item, int index) {
        int updateIndex = 0;
        if (index >= adapterItems.size()) {
            adapterItems.add(item);
            notifyItemChanged(adapterItems.size());
            updateIndex = adapterItems.size() - 1;
        } else if (index < adapterItems.size()) {
            adapterItems.add(index, item);
            updateIndex = index;
        }

        return updateIndex;
    }

    public boolean insertItemAfterArrangingIndex(SectionAdapterItem item, int index) {
        boolean isInserted = false;
        if (index >= adapterItems.size()) {
            isInserted = false;
        } else if (index < adapterItems.size()) {
            adapterItems.add(index, item);
            isInserted = true;
        }

        return isInserted;
    }

    public int indexOf(SectionAdapterItem item) {
        return adapterItems.indexOf(item);
    }

    public SectionAdapterItem getItem(int index) {
        return adapterItems.get(index);
    }

    public List<SectionAdapterItem> getAllItem() {
        return adapterItems;
    }


    public void deleteAllItems() {
        if (adapterItems != null) {
            adapterItems.clear();
            adapterItems = new ArrayList<>();
        }
    }

    private void bl_getSensexWidgetData() {
        DefaultTHApiManager.bl_sensexWidget(new RequestCallback() {
            @Override
            public void onNext(Object o) {
                final String itemRowId = RowIds.rowId_sensexWidget();
                SectionAdapterItem item = new SectionAdapterItem(BaseRecyclerViewAdapter.VT_BL_SENSEX, itemRowId);
                int index = indexOf(item);
                if (index != -1) {
                    item = getItem(index);
                    if (o instanceof NSEData) {
                        NSEData nseData = (NSEData) o;
                        nseData.setStatus(SensexStatus.SUCCESS);
                        item.getSensexData().setNSEData(nseData);
                    } else if (o instanceof BSEData) {
                        BSEData bseData = (BSEData) o;
                        bseData.setStatus(SensexStatus.SUCCESS);
                        item.getSensexData().setBSEData(bseData);
                    }
                    notifyItemChanged(index);
                }
            }

            @Override
            public void onError(Throwable t, String str) {
                final String itemRowId = RowIds.rowId_sensexWidget();
                SectionAdapterItem item = new SectionAdapterItem(BaseRecyclerViewAdapter.VT_BL_SENSEX, itemRowId);
                int index = indexOf(item);
                if (index != -1) {
                    item = getItem(index);
                    SensexData sensexData = item.getSensexData();
                    sensexData.getmBSEData().setStatus(SensexStatus.ERROR);
                    sensexData.getNSEData().setStatus(SensexStatus.ERROR);
                    notifyItemChanged(index);
                }
            }

            @Override
            public void onComplete(String str) {

            }
        });
    }

    private void bl_fillSensexData(SensexViewHolder sensexViewHolder, int position) {

        final SectionAdapterItem dataBean = adapterItems.get(position);

        int bseStatus = -1;
        int nseStatus = -1;

        final BSEData bseData = dataBean.getSensexData().getmBSEData();
        final NSEData nseData = dataBean.getSensexData().getNSEData();
        if (bseData != null) {
            bseStatus = bseData.getStatus();
        } else {
            bl_getSensexWidgetData();
        }
        if (nseData != null) {
            nseStatus = nseData.getStatus();
        } else {
            bl_getSensexWidgetData();
        }


        boolean isDayTheme = DefaultPref.getInstance(sensexViewHolder.itemView.getContext()).isUserThemeDay();

        if (isDayTheme) {
            sensexViewHolder.mBseParentLayout.setBackground(ResUtil.getBackgroundDrawable(sensexViewHolder.itemView.getResources(), R.drawable.bl_light_drawable_indices_border));
            sensexViewHolder.mNseParentLayout.setBackground(ResUtil.getBackgroundDrawable(sensexViewHolder.itemView.getResources(), R.drawable.bl_light_drawable_indices_border));
        } else {
            sensexViewHolder.mBseParentLayout.setBackground(ResUtil.getBackgroundDrawable(sensexViewHolder.itemView.getResources(), R.drawable.bl_dark_drawable_indices_border));
            sensexViewHolder.mNseParentLayout.setBackground(ResUtil.getBackgroundDrawable(sensexViewHolder.itemView.getResources(), R.drawable.bl_dark_drawable_indices_border));
        }


        // For BSE
        switch (bseStatus) {
            case SensexStatus.NONE:
            case SensexStatus.INITIALISING:
                sensexViewHolder.bseValParent.findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
                sensexViewHolder.bseValParent.findViewById(R.id.refresh).setVisibility(View.GONE);
                sensexViewHolder.bseValParent.findViewById(R.id.topVal).setVisibility(View.GONE);
                sensexViewHolder.bseValParent.findViewById(R.id.bottomVal).setVisibility(View.GONE);
                sensexViewHolder.bseValParent.findViewById(R.id.refresh).setOnClickListener(null);
                break;
            case SensexStatus.SUCCESS:
                sensexViewHolder.bseValParent.findViewById(R.id.progressBar).setVisibility(View.GONE);
                sensexViewHolder.bseValParent.findViewById(R.id.refresh).setVisibility(View.GONE);
                TextView bseTopVal = sensexViewHolder.bseValParent.findViewById(R.id.topVal);
                TextView bseBottomVal = sensexViewHolder.bseValParent.findViewById(R.id.bottomVal);

                bseTopVal.setVisibility(View.VISIBLE);
                bseBottomVal.setVisibility(View.VISIBLE);

                double latestPrice = bseData.getCp();
                double stockChange = bseData.getPer();
                double change = bseData.getCh();
                String latestUpdatedData = NumberFormat.getNumberInstance(Locale.US).format(latestPrice);
                bseTopVal.setText("" + latestUpdatedData);
                float changeIndicator;
                changeIndicator = (float) stockChange;
                if (changeIndicator < 0) {
                    bseBottomVal.setText("" + change + "(" + stockChange + "%" + ")");
                    bseBottomVal.setTextColor(sensexViewHolder.itemView.getResources().getColor(R.color.red));
                    bseBottomVal.setCompoundDrawablesWithIntrinsicBounds(R.drawable.stockchange_loss, 0, 0, 0);
                } else if (changeIndicator > 0) {
                    bseBottomVal.setText("+" + change + "(" + stockChange + "%" + ")");
                    bseBottomVal.setTextColor(sensexViewHolder.itemView.getResources().getColor(R.color.green));
                    bseBottomVal.setCompoundDrawablesWithIntrinsicBounds(R.drawable.stockchange_profit, 0, 0, 0);//.setImageResource(R.drawable.stockchange_profit);

                } else {
                    bseBottomVal.setText("N.A.");
                }


                sensexViewHolder.mBseParentLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TableConfiguration tableConfiguration = BaseAcitivityTHP.getTableConfiguration();
                        List<TabsBean> tabs = tableConfiguration.getTabs();
                        int count = 0;
                        for (TabsBean tabsBean1 : tabs) {
                            tabsBean1.setIndex(count);
                            if (tabsBean1.getPageSource().equalsIgnoreCase(NetConstants.PS_SENSEX)) {
                                TabIndicesFragment.sIndicesSelectedTabPosition = 0;
                                // Sending Event in AppTabFragment.java => handleEvent(TabsBean tabsBean)
                                EventBus.getDefault().post(tabsBean1);
                                break;
                            }
                            count++;
                        }
                    }
                });

                break;
            case SensexStatus.ERROR:
                if (bseData == null) {
                    sensexViewHolder.bseValParent.findViewById(R.id.progressBar).setVisibility(View.GONE);
                    sensexViewHolder.bseValParent.findViewById(R.id.refresh).setVisibility(View.VISIBLE);
                    sensexViewHolder.bseValParent.findViewById(R.id.refresh).setOnClickListener(v -> {
                        bl_getSensexWidgetData();
                    });

                    bseData.setStatus(SensexStatus.INITIALISING);
                }
                break;

        }

        // For NSE
        switch (nseStatus) {
            case SensexStatus.NONE:
            case SensexStatus.INITIALISING:
                sensexViewHolder.nseValParent.findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
                sensexViewHolder.nseValParent.findViewById(R.id.refresh).setVisibility(View.GONE);
                sensexViewHolder.nseValParent.findViewById(R.id.topVal).setVisibility(View.GONE);
                sensexViewHolder.nseValParent.findViewById(R.id.bottomVal).setVisibility(View.GONE);
                sensexViewHolder.nseValParent.findViewById(R.id.refresh).setOnClickListener(null);
                break;
            case SensexStatus.SUCCESS:
                sensexViewHolder.nseValParent.findViewById(R.id.refresh).setVisibility(View.GONE);
                sensexViewHolder.nseValParent.findViewById(R.id.progressBar).setVisibility(View.GONE);

                TextView nseTopVal = sensexViewHolder.nseValParent.findViewById(R.id.topVal);
                TextView nseBottomVal = sensexViewHolder.nseValParent.findViewById(R.id.bottomVal);

                nseTopVal.setVisibility(View.VISIBLE);
                nseBottomVal.setVisibility(View.VISIBLE);

                double latestPrice = nseData.getCp();
                double stockChange = nseData.getPer();
                double change = nseData.getCh();
                String latestUpdatedData = NumberFormat.getNumberInstance(Locale.US).format(latestPrice);
                nseTopVal.setText("" + latestUpdatedData);
                float changeIndicator;
                changeIndicator = (float) stockChange;
                if (changeIndicator < 0) {
                    nseBottomVal.setText("" + change + "(" + stockChange + "%" + ")");
                    nseBottomVal.setTextColor(sensexViewHolder.itemView.getResources().getColor(R.color.red));
                    nseBottomVal.setCompoundDrawablesWithIntrinsicBounds(R.drawable.stockchange_loss, 0, 0, 0);

                } else if (changeIndicator > 0) {
                    nseBottomVal.setText("+" + change + "(" + stockChange + "%" + ")");
                    nseBottomVal.setTextColor(sensexViewHolder.itemView.getResources().getColor(R.color.green));
                    nseBottomVal.setCompoundDrawablesWithIntrinsicBounds(R.drawable.stockchange_profit, 0, 0, 0);
                } else {
                    nseBottomVal.setText("N.A.");
                }

                sensexViewHolder.mNseParentLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TableConfiguration tableConfiguration = BaseAcitivityTHP.getTableConfiguration();
                        List<TabsBean> tabs = tableConfiguration.getTabs();
                        int count = 0;
                        for (TabsBean tabsBean1 : tabs) {
                            tabsBean1.setIndex(count);
                            if (tabsBean1.getPageSource().equalsIgnoreCase(NetConstants.PS_SENSEX)) {
                                TabIndicesFragment.sIndicesSelectedTabPosition = 1;
                                // Sending Event in AppTabFragment.java => handleEvent(TabsBean tabsBean)
                                EventBus.getDefault().post(tabsBean1);
                                break;
                            }
                            count++;
                        }
                    }
                });

                break;
            case SensexStatus.ERROR:
                if (nseData == null) {
                    sensexViewHolder.nseValParent.findViewById(R.id.refresh).setVisibility(View.VISIBLE);
                    sensexViewHolder.nseValParent.findViewById(R.id.progressBar).setVisibility(View.GONE);
                    sensexViewHolder.nseValParent.findViewById(R.id.refresh)
                            .setOnClickListener(v -> {
                                bl_getSensexWidgetData();
                            });
                }
                break;

        }
    }


    private String indexText(int index, String actualText) {
        return index + " :: " + actualText;
    }

}
