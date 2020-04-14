package com.ns.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.netoperation.model.SectionAdapterItem;
import com.netoperation.model.StaticPageUrlBean;
import com.ns.activity.BaseRecyclerViewAdapter;
import com.ns.thpremium.R;
import com.ns.utils.WebViewLinkClick;
import com.ns.viewholder.LoadMoreViewHolder;
import com.ns.viewholder.StaticItemWebViewHolder;

import java.util.ArrayList;


public class SectionContentAdapter extends BaseRecyclerViewAdapter {

    private ArrayList<SectionAdapterItem> adapterItems;
    private String mFrom;

    public SectionContentAdapter(String from, ArrayList<SectionAdapterItem> adapterItems) {
        this.mFrom = from;
        this.adapterItems = adapterItems;
    }

    @Override
    public int getItemViewType(int position) {
        return adapterItems.get(position).getViewType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if(viewType == VT_LOADMORE) {
            return new LoadMoreViewHolder(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_loadmore, viewGroup, false));
        }
        else if(viewType == VT_THD_BANNER) {
            return new BannerViewHolder(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.cardview_home_banner, viewGroup, false));
        }
        else if(viewType == VT_THD_DEFAULT_ROW) {
            return new ArticlesViewHolder(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.cardview_article_list, viewGroup, false));
        }
        else if(viewType == VT_THD_WIDGET_DEFAULT) {
            return new WidgetsViewHolder(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.cardview_home_widgets, viewGroup, false));
        }
        else if(viewType == VT_WEB_WIDGET) {
            return new StaticItemWebViewHolder(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_webview, viewGroup, false));
        }
        else if(viewType == VT_THD_SUB_SECTION) {
            return new StaticItemWebViewHolder(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.cardview_home_explore, viewGroup, false));
        }
        return new LoadMoreViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_loadmore, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        SectionAdapterItem item = adapterItems.get(position);
        if(holder instanceof BannerViewHolder) {
            BannerViewHolder bannerViewHolder = (BannerViewHolder) holder;
            bannerViewHolder.mArticleSectionName.setText("pos : "+position+"--"+item.getItemRowId());
        }
        else if(holder instanceof WidgetsViewHolder) {
            fillWidgetData((WidgetsViewHolder)holder, position);
        }
        else if(holder instanceof ArticlesViewHolder) {
            ArticlesViewHolder articlesViewHolder = (ArticlesViewHolder) holder;
            articlesViewHolder.mArticleSectionName.setText("pos : "+position+"--"+item.getItemRowId());
        }
        else if(holder instanceof StaticItemWebViewHolder) {
            StaticItemWebViewHolder staticItemHolder = (StaticItemWebViewHolder) holder;
            StaticPageUrlBean pageUrlBean = item.getStaticPageUrlBean();
            staticItemHolder.webView.loadUrl(pageUrlBean.getUrl());
            if(!pageUrlBean.getSectionId().equals("0")) {
                staticItemHolder.mDummyView.setVisibility(View.VISIBLE);
                // DummyView Click Listener
                // TODO, redirect on particular section or sub-section
//                staticItemHolder.mDummyView.setOnClickListener(new StaticItemDummyViewClick(url, mContext, null, false, -1));
            } else {
                staticItemHolder.mDummyView.setVisibility(View.GONE);
                // Enabling Weblink click on Lead Text
                new WebViewLinkClick().linkClick(staticItemHolder.webView, staticItemHolder.itemView.getContext());
            }
        }

    }

    @Override
    public int getItemCount() {
        return adapterItems.size();
    }

    /**
     * Banner View Holder
     */
    public class BannerViewHolder extends RecyclerView.ViewHolder {

        public ImageView mBannerImageView;
        public TextView mBannerTextView;
        public LinearLayout mBannerLayout;
        public TextView mArticleUpdateTime;
        public TextView mArticleSectionName;
        public Button mBookmarkButton;
        public Button mShareArticleButton;
        public ImageButton mMultimediaButton;

        public BannerViewHolder(View itemView) {
            super(itemView);
            mBookmarkButton = itemView.findViewById(R.id.button_bookmark);
            mBannerLayout = itemView.findViewById(R.id.layout_banner);
            mBannerImageView = itemView.findViewById(R.id.imageview_banner);
            mBannerTextView = itemView.findViewById(R.id.textview_banner);
            mArticleSectionName = itemView.findViewById(R.id.section_name);
            mArticleUpdateTime = itemView.findViewById(R.id.textview_time);
            mShareArticleButton = itemView.findViewById(R.id.button_article_share);
            mMultimediaButton = itemView.findViewById(R.id.multimedia_button);
            mArticleSectionName.setVisibility(View.VISIBLE);
        }
    }


    /**
     * Normal Row Item View Holder
     */
    public class ArticlesViewHolder extends RecyclerView.ViewHolder {

        public ImageView mArticleImageView;
        public TextView mArticleTextView;
        public TextView mArticleTimeTextView;
        public TextView mArticleSectionName;
        public LinearLayout mArticlesLayout;
        public Button mBookmarkButton;
        public Button mShareArticleButton;
        public ImageButton mMultimediaButton;
        public FrameLayout mImageParentLayout;

        public ArticlesViewHolder(View itemView) {
            super(itemView);
            mMultimediaButton = itemView.findViewById(R.id.multimedia_button);
            mImageParentLayout = itemView.findViewById(R.id.imageParentLayout);
            mArticlesLayout = itemView.findViewById(R.id.layout_articles_root);
            mArticleImageView = itemView.findViewById(R.id.imageview_article_list_image);
            mArticleTextView = itemView.findViewById(R.id.textview_article_list_header);
            mShareArticleButton = itemView.findViewById(R.id.button_article_share);
            mArticleTimeTextView = itemView.findViewById(R.id.textview_time);
            mBookmarkButton = itemView.findViewById(R.id.button_bookmark);
            mArticleSectionName = itemView.findViewById(R.id.section_name);
            mArticleSectionName.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Default Widget View Holder
     */
    public class WidgetsViewHolder extends RecyclerView.ViewHolder {

        public RecyclerView mWidgetsRecyclerView;
        public TextView mWidgetTitleTextView, mWidgetFooterTextView;

        public WidgetsViewHolder(View itemView) {
            super(itemView);

            mWidgetsRecyclerView = itemView.findViewById(R.id.recyclerview_widgets);
            mWidgetTitleTextView = itemView.findViewById(R.id.textview_widget_title);
            mWidgetFooterTextView = itemView.findViewById(R.id.textview_widget_viewAll);

        }
    }

    public void deleteItem(SectionAdapterItem item) {
        adapterItems.remove(item);
    }

    public void addSingleItem(SectionAdapterItem item) {
        if(adapterItems == null) {
            adapterItems = new ArrayList<>();
        }
        adapterItems.add(item);
        notifyItemChanged(adapterItems.size()-1);
    }

    public void addMultiItems(ArrayList<SectionAdapterItem> items) {
        if(adapterItems == null) {
            adapterItems = new ArrayList<>();
        }
        int fromIndex = adapterItems.size();
        adapterItems.addAll(items);
        notifyItemRangeChanged(fromIndex, items.size());
    }

    public void insertItem(SectionAdapterItem item, int index) {
        if(index >= adapterItems.size()) {
            adapterItems.add(item);
        } else if(index < adapterItems.size()){
            adapterItems.add(index, item);
        }
    }

    public int indexOf(SectionAdapterItem item) {
        return adapterItems.indexOf(item);
    }

    public SectionAdapterItem getItem(int index) {
        return adapterItems.get(index);
    }

    public void updateItem(SectionAdapterItem item) {
        int index = adapterItems.indexOf(item);
        if(index != -1) {
            final SectionAdapterItem oldItem = adapterItems.get(index);
            oldItem.setADID_300X250(item.getADID_300X250());
            oldItem.setArticleBean(item.getArticleBean());
            oldItem.setItemRowId(item.getItemRowId());
        }

    }

    public void deleteAllItems() {
        if(adapterItems != null) {
            adapterItems.clear();
            adapterItems = new ArrayList<>();
        }
    }


    ////////////////////////////////////
    private void fillWidgetData(WidgetsViewHolder mWidgetsViewHolder, final int position) {

        final SectionAdapterItem dataBean = adapterItems.get(position);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mWidgetsViewHolder.mWidgetsRecyclerView.getContext(), LinearLayoutManager.HORIZONTAL, false);

        /*mWidgetsViewHolder.mWidgetsRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_MOVE:
                        if (SlidingSectionFragment.mViewPager != null) {
                            SlidingSectionFragment.mViewPager.setPagingEnabled(false);
                        }
                        break;
                    case MotionEvent.ACTION_DOWN:
                        if (SlidingSectionFragment.mViewPager != null) {
                            SlidingSectionFragment.mViewPager.setPagingEnabled(false);
                        }
                        break;
                    case MotionEvent.ACTION_OUTSIDE:
                        if (SlidingSectionFragment.mViewPager != null) {
                            SlidingSectionFragment.mViewPager.setPagingEnabled(true);
                        }
                        break;

                }
                return false;
            }
        });*/

        mWidgetsViewHolder.mWidgetsRecyclerView.setAdapter(dataBean.getWidgetAdapter());
        mWidgetsViewHolder.mWidgetsRecyclerView.setLayoutManager(mLayoutManager);
//        mWidgetsViewHolder.mWidgetsRecyclerView.setHasFixedSize(true);
        mWidgetsViewHolder.mWidgetTitleTextView.setText(dataBean.getWidgetAdapter().getSectionName());
        //for top-picks we are desableing the visiblity the view all textview
        if (dataBean.getWidgetAdapter().getSectionId()== 88) {
            mWidgetsViewHolder.mWidgetFooterTextView.setVisibility(View.GONE);
        } else {
            mWidgetsViewHolder.mWidgetFooterTextView.setVisibility(View.VISIBLE);
            mWidgetsViewHolder.mWidgetFooterTextView.setText("View All " + dataBean.getWidgetAdapter().getSectionName());
        }

        /*mWidgetsViewHolder.mWidgetFooterTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int parentId = dataBean.getWidgetParentSecId();
                int position;
                boolean isSubsection;
                String parentName;
                if (parentId == 0) {
                    position = DatabaseJanitor.getSectionPosition(dataBean.getWidgetSectionId());
                    parentName = dataBean.getWidgetName();
                    isSubsection = false;
                } else {
                    position = DatabaseJanitor.getSubsectionPostion(parentId, dataBean.getWidgetSectionId());
                    parentName = DatabaseJanitor.getParentSectionName(parentId);
                    isSubsection = true;
                }
                if (position >= 0) {
                    SlidingSectionFragment fragment = SlidingSectionFragment.newInstance(SlidingSectionFragment.FROM_VIEW_ALL, position, isSubsection, parentId, parentName);
                    pushFragmentToBackStack(fragment);

                } else if (position == -1) {
                    SlidingArticleFragment fragment = SlidingArticleFragment.newInstance(0, String.valueOf(dataBean.getWidgetSectionId()),
                            false);
                    pushFragmentToBackStack(fragment);
                }
            }
        });*/

    }

}
