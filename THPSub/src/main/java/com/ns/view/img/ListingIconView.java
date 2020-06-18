package com.ns.view.img;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;

import androidx.appcompat.widget.AppCompatImageView;

import com.netoperation.config.download.FileUtils;
import com.netoperation.config.model.ListingIconUrl;
import com.netoperation.config.model.OtherIconUrls;
import com.netoperation.config.model.OtherIconsDownloadUrls;
import com.netoperation.default_db.TableConfiguration;
import com.netoperation.util.DefaultPref;
import com.ns.activity.BaseAcitivityTHP;
import com.ns.thpremium.R;
import com.ns.utils.PicassoUtil;
import com.ns.utils.THPConstants;

import java.io.File;

public class ListingIconView extends BaseImgView {

    //private int mIconType = -1;

    public ListingIconView(Context context) {
        super(context);
        init(context, null);
    }

    public ListingIconView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ListingIconView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        int iconType = -1;
        if(attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NSIcon);
            iconType = typedArray.getInt(R.styleable.NSIcon_iconType, 0);
            typedArray.recycle();
        }

        updateIcon(iconType);
    }

    @Override
    public void updateIcon(int iconType) {
        final boolean isUserThemeDay = DefaultPref.getInstance(getContext()).isUserThemeDay();
        TableConfiguration tableConfiguration = BaseAcitivityTHP.getTableConfiguration();
        if(tableConfiguration != null) {
            if (isUserThemeDay) {
                loadIconsFromServer(iconType, tableConfiguration.getOtherIconsDownloadUrls().getLight().getListing(), FileUtils.destinationFolder(getContext(), FileUtils.LISTING_ICONs_LIGHT).getPath());
            } else {
                loadIconsFromServer(iconType, tableConfiguration.getOtherIconsDownloadUrls().getDark().getListing(), FileUtils.destinationFolder(getContext(), FileUtils.LISTING_ICONs_DARK).getPath());
            }
        }
    }

    private void loadIconsFromServer(int iconType, ListingIconUrl listingIconUrl, String destinationFolderPath) {

        String iconUrl = "";

        // 1 = app:iconType="share"
        if(iconType == 1) {
            iconUrl = listingIconUrl.getShare();
        }
        // 2 = app:iconType="favourite"
        else if(iconType == 2) {
            iconUrl = listingIconUrl.getFavorite();
        }
        // 3 = app:iconType="bookmarked"
        else if(iconType == 3) {
            iconUrl = listingIconUrl.getBookmark();
        }
        // 4 = app:iconType="unbookmark"
        else if(iconType == 4) {
            iconUrl = listingIconUrl.getUnbookmark();
        }
        // 6 = app:iconType="like"
        else if(iconType == 6) {
            iconUrl = listingIconUrl.getLike();
        }
        // 10 = app:iconType="dislike"
        else if(iconType == 10) {
            iconUrl = listingIconUrl.getDislike();
        }
        // 11 = app:iconType="unfavourite"
        else if(iconType == 11) {
            iconUrl = listingIconUrl.getUnfavorite();
        }

        if(iconUrl == null) {
            iconUrl = "";
        }

        PicassoUtil.loadImageFromCache(getContext(), this, FileUtils.getFilePathFromUrl(destinationFolderPath, iconUrl));
    }



}
