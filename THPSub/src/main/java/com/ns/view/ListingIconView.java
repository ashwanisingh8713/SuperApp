package com.ns.view;

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

public class ListingIconView extends AppCompatImageView {

    private int mIconType = -1;

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
        boolean isUserThemeDay = DefaultPref.getInstance(context).isUserThemeDay();

        if(attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NSIcon);
            if (typedArray.hasValue(R.styleable.NSIcon_iconType)) {
                mIconType = typedArray.getInt(R.styleable.NSIcon_iconType, 0);
            } else {
                mIconType = -1;
            }
        }

        TableConfiguration tableConfiguration = BaseAcitivityTHP.getTableConfiguration();
        if(tableConfiguration != null && THPConstants.IS_USE_SEVER_THEME) {
            if(isUserThemeDay) {
                loadIconsFromServer(tableConfiguration.getOtherIconsDownloadUrls().getLight().getListing(), FileUtils.destinationFolder(context, FileUtils.LISTING_ICONs_LIGHT).getPath());
            } else {
                loadIconsFromServer(tableConfiguration.getOtherIconsDownloadUrls().getDark().getListing(), FileUtils.destinationFolder(context, FileUtils.LISTING_ICONs_DARK).getPath());
            }
        }
        else {
            loadIconsFromApp(isUserThemeDay);
        }
    }

    private void loadIconsFromServer(ListingIconUrl listingIconUrl, String destinationFolderPath) {

        String iconUrl = "";

        // 1 = app:iconType="share"
        if(mIconType == 1) {
            iconUrl = listingIconUrl.getShare();
        }
        // 2 = app:iconType="favourite"
        else if(mIconType == 2) {
            iconUrl = listingIconUrl.getFavorite();
        }
        // 3 = app:iconType="bookmarked"
        else if(mIconType == 3) {
            iconUrl = listingIconUrl.getBookmark();
        }
        // 4 = app:iconType="unbookmark"
        else if(mIconType == 4) {
            iconUrl = listingIconUrl.getUnbookmark();
        }
        // 6 = app:iconType="like"
        else if(mIconType == 6) {
            iconUrl = listingIconUrl.getLike();
        }
        // 10 = app:iconType="dislike"
        else if(mIconType == 10) {
            iconUrl = listingIconUrl.getDislike();
        }
        // 11 = app:iconType="unfavourite"
        else if(mIconType == 11) {
            iconUrl = listingIconUrl.getUnfavorite();
        }

        if(iconUrl == null) {
            iconUrl = "";
        }

        PicassoUtil.loadImageFromCache(getContext(), this, FileUtils.getFilePathFromUrl(destinationFolderPath, iconUrl));
    }

    private void loadIconsFromApp(boolean isUserThemeDay) {
        // 1 = app:iconType="share"
        if(mIconType == 1) {
            if (isUserThemeDay) {
                setImageResource(R.drawable.ic_share_article);
            } else {
                setImageResource(R.drawable.ic_share_article_w);
            }
        }
        // 2 = app:iconType="favourite"
        else if(mIconType == 2) {
            if (isUserThemeDay) {
                setImageResource(R.drawable.ic_like_selected);
            } else {
                setImageResource(R.drawable.ic_like_selected);
            }
        }
        // 3 = app:iconType="bookmarked"
        else if(mIconType == 3) {
            if (isUserThemeDay) {
                setImageResource(R.drawable.ic_bookmark_selected);
            } else {
                setImageResource(R.drawable.ic_bookmark_selected);
            }
        }
        // 4 = app:iconType="unbookmark"
        else if(mIconType == 4) {
            if (isUserThemeDay) {
                setImageResource(R.drawable.ic_bookmark_unselected);
            } else {
                setImageResource(R.drawable.ic_bookmark_unselected);
            }
        }
        // 6 = app:iconType="like"
        else if(mIconType == 6) {
            if (isUserThemeDay) {
                setImageResource(R.drawable.ic_switch_off_copy);
            } else {
                setImageResource(R.drawable.ic_switch_off_copy);
            }
        }

        // 10 = app:iconType="dislike"
        else if(mIconType == 10) {
            if (isUserThemeDay) {
                setImageResource(R.drawable.ic_switch_on_copy);
            } else {
                setImageResource(R.drawable.ic_switch_on_copy);
            }
        }
        // 11 = app:iconType="unfavourite"
        else if(mIconType == 11) {
            if (isUserThemeDay) {
                setImageResource(R.drawable.ic_like_unselected);
            } else {
                setImageResource(R.drawable.ic_like_unselected);
            }
        }
    }


}
