package com.ns.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.ns.model.ImageGallaryUrl;
import com.ns.thpremium.R;
import com.ns.utils.PicassoUtil;
import com.ns.utils.IntentUtil;
import com.ns.utils.ResUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ashwanisingh on 14/12/17.
 */

public class GalleryVerticleAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private ArrayList<ImageGallaryUrl> mImageUrlList;
    private List<String> mImageListToFolder;
    private String mTitle;
    private boolean mCheckPermission;

    private OnCheckPermisssion mOnCheckPermission;
    public interface OnCheckPermisssion {
        void onItemCheckPermission();
    }
    public void setOnChecPermission(OnCheckPermisssion onItemCheck) {
        mOnCheckPermission = onItemCheck;
    }


    public GalleryVerticleAdapter(Context mContext, ArrayList<ImageGallaryUrl> mImageUrlList, String title, List<String> imageListToFolder) {
        this.mContext = mContext;
        this.mImageUrlList = mImageUrlList;
        this.mImageListToFolder = imageListToFolder;
        this.mTitle = title;

        if(mTitle != null && mTitle.length() > 12) {
            mTitle = mTitle.substring(0, 11);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vertical_gallery_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ImageGallaryUrl item = mImageUrlList.get(position);
        final ViewHolder viewHolder = (ViewHolder) holder;

        final String imageUrl = item.getImageUrl();
        if (imageUrl != null && !TextUtils.isEmpty(imageUrl)) {
            PicassoUtil.loadImage(mContext, viewHolder.imgView, imageUrl);
        } else {
//            viewHolder.imgView.setImageResource(R.mipmap.ph_topnews_th);
        }

        //viewHolder.tv_photoCount.setVisibility(View.GONE);

        /*if(getItemCount() == 1) {
            viewHolder.tv_photoCount.setVisibility(View.GONE);
        } else {
            viewHolder.tv_photoCount.setText("Photo " + (position+1) + "/" + getItemCount());
            viewHolder.tv_photoCount.setVisibility(View.VISIBLE);
        }*/

        viewHolder.tv_photoCount.setText("Photo " + (position+1) + "/" + getItemCount());
        viewHolder.tv_photoCount.setVisibility(View.VISIBLE);

        String capton = item.getCaption();
        if (capton != null && !ResUtil.isEmpty(capton)) {
            viewHolder.tv_caption.setVisibility(View.VISIBLE);
            capton = replaceSpecialChar(capton);
            //viewHolder.tv_caption.setText(Html.fromHtml(capton));
            viewHolder.tv_caption.setText(ResUtil.htmlText(capton));
        } else {
            viewHolder.tv_caption.setVisibility(View.GONE);
        }


        // Share Button click listener
        String finalCapton = capton;
        viewHolder.shareimgView.setOnClickListener(v -> {
            Intent intent = IntentUtil.shareArticleImageIntent(mTitle, imageUrl, finalCapton);
            mContext.startActivity(intent);
        });

        // Check Image is download and store folder
        if (mImageListToFolder.size() != 0) {
            int imgPos = mImageListToFolder.indexOf(mTitle + "_" + (position + 1) + ".jpg");
            if (imgPos != -1) {
                viewHolder.downloadimgShowView.setVisibility(View.VISIBLE);
                viewHolder.downloadimgView.setVisibility(View.GONE);
            }
        }

        // Download Button click listener
        viewHolder.downloadimgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnCheckPermission != null) {
                    mOnCheckPermission.onItemCheckPermission();
                }
            }
        });


        viewHolder.imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtil.openHorizontalGalleryActivity(mContext, mImageUrlList, null, position);
            }
        });
    }

    private String replaceSpecialChar(String comment) {
        comment = comment.replaceAll("&amp;quot;", "\"");
        return comment;
    }

    @Override
    public int getItemCount() {
        return mImageUrlList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_caption;
        TextView tv_photoCount;
        ImageView imgView;
        ImageView shareimgView;
        ImageView downloadimgView;
        ImageView downloadimgShowView;
        public ViewHolder(View view) {
            super(view);

            tv_caption = view.findViewById(R.id.caption);
            tv_photoCount = view.findViewById(R.id.photoCount);
            imgView = view.findViewById(R.id.detail_image);
            shareimgView = view.findViewById(R.id.shareIV);
            downloadimgView = view.findViewById(R.id.downloadIV);
            downloadimgShowView = view.findViewById(R.id.downloadShowIV);
        }
    }

    public void setStoragePermission(boolean checkPermission) {
        mCheckPermission = checkPermission;
        notifyDataSetChanged();
    }
}
