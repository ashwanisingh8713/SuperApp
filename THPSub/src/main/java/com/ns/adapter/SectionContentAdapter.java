package com.ns.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.netoperation.model.SectionAdapterItem;
import com.ns.activity.BaseRecyclerViewAdapter;
import com.ns.thpremium.R;
import com.ns.viewholder.BookmarkViewHolder;
import com.ns.viewholder.LoadMoreViewHolder;

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
        return new LoadMoreViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_loadmore, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        SectionAdapterItem item = adapterItems.get(position);
        if(holder instanceof BannerViewHolder) {
            BannerViewHolder bannerViewHolder = (BannerViewHolder) holder;

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
        adapterItems.addAll(items);
    }

    public void insertItem(SectionAdapterItem item, int index) {
        adapterItems.add(index, item);
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

    public void clearAllItems() {
        if(adapterItems != null) {
            adapterItems.clear();
            adapterItems = new ArrayList<>();
        }
    }

}
