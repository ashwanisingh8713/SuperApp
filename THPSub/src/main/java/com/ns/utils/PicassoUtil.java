package com.ns.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import com.ns.thpremium.R;
import com.ns.view.roundedimageview.RoundedImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Target;

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
    }

    public static void loadImageWithFilePH(Context context, final RoundedImageView imageView, String bannerUrl) {
        if(imageView.getBannerFilePath() != null) {
            File file = new File(imageView.getBannerFilePath());
            picassoCombo(
                    Picasso.with(context)
                            .load(file)
                    ,
                    Picasso.with(context)
                            .load(bannerUrl)
                    ,
                    imageView
            );
        }
        else {
            Picasso picasso = Picasso.with(context);
            picasso.load(bannerUrl)
                    .into(imageView);
        }

        /*picassoCombo(
                Picasso.with(context)
                        .load(thumbUrl)
                        .placeholder(R.drawable.ic_image_placeholder),
                Picasso.with(context)
                        .load(bannerUrl)
                        .error(R.drawable.ic_image_broken),
                imageView
        );*/
    }

    public static void picassoCombo(final RequestCreator thumbnail,
                                    final RequestCreator large,
                                    final ImageView imageView) {
        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                imageView.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                //imageView.setImageDrawable(errorDrawable);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                thumbnail.into(imageView);
            }
        };

        imageView.setTag(target); // To prevent target from being garbage collected
        large.into(target);
    }
}
