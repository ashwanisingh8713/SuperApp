package com.ns.utils;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;


public class PicassoUtil {

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

    public static void loadImageFromCache(Context context, final ImageView imageView, String filePath) {
        File file = new File(filePath);
        Picasso picasso = Picasso.with(context);
        picasso.load(file)
                .into(imageView);

        /*Picasso.Builder builder = new Picasso.Builder(context);
        builder.listener(new Picasso.Listener()
        {
            @Override
            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception)
            {
                exception.printStackTrace();
            }
        });
        builder.build().load(filePath).into(imageView);*/
    }


}
