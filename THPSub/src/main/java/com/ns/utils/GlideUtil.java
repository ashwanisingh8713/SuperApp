package com.ns.utils;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;


public class GlideUtil {


    public static void loadImage(Context context, final ImageView imageView, String url, int placeholderResId) {
        Picasso picasso = Picasso.with(context);
        picasso.load(url)
                .placeholder(placeholderResId)
                .error(placeholderResId)
                .into(imageView);
    }

    public static void loadImage(Context context, final ImageView imageView, String url) {
        Picasso picasso = Picasso.with(context);
        picasso.load(url)
                .into(imageView);
    }


}
