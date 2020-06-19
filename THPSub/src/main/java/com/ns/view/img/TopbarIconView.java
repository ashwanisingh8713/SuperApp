package com.ns.view.img;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

import com.netoperation.config.download.FileUtils;
import com.netoperation.config.model.TopbarIconUrl;
import com.netoperation.default_db.TableConfiguration;
import com.netoperation.util.DefaultPref;
import com.ns.activity.BaseAcitivityTHP;
import com.ns.thpremium.R;
import com.ns.utils.PicassoUtil;
import com.ns.utils.THPConstants;

public class TopbarIconView extends BaseImgView {

    public TopbarIconView(Context context) {
        super(context);
        init(context, null);
    }

    public TopbarIconView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TopbarIconView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        int iconType = 0;

        if (attrs != null) {
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
                loadIconsFromServer(iconType, tableConfiguration.getOtherIconsDownloadUrls().getLight().getTopbar(), FileUtils.destinationFolder(getContext(), FileUtils.TOPBAR_ICONs_LIGHT).getPath());
            } else {
                loadIconsFromServer(iconType, tableConfiguration.getOtherIconsDownloadUrls().getDark().getTopbar(), FileUtils.destinationFolder(getContext(), FileUtils.TOPBAR_ICONs_DARK).getPath());
            }
        }
    }

    private void loadIconsFromServer(int mIconType, TopbarIconUrl topbarIconUrl, String destinationFolderPath) {

        String iconUrl = "";
        // 0 = app:iconType="ttsPlay"
        if (mIconType == 0) {
            iconUrl = topbarIconUrl.getTtsPlay();
        }
        // 1 = app:iconType="share"
        else if (mIconType == 1) {
            iconUrl = topbarIconUrl.getShare();
        }
        // 2 = app:iconType="favourite"
        else if (mIconType == 2) {
            iconUrl = topbarIconUrl.getFavorite();
        }
        // 3 = app:iconType="bookmarked"
        else if (mIconType == 3) {
            iconUrl = topbarIconUrl.getBookmark();
        }
        // 4 = app:iconType="unbookmark"
        else if (mIconType == 4) {
            iconUrl = topbarIconUrl.getUnbookmark();
        }
        // 5 = app:iconType="fontSize"
        else if (mIconType == 5) {
            iconUrl = topbarIconUrl.getTextSize();
        }
        // 6 = app:iconType="like"
        else if (mIconType == 6) {
            iconUrl = topbarIconUrl.getLike();
        }
        // 7 = app:iconType="crown"
        else if (mIconType == 7) {
            iconUrl = topbarIconUrl.getCrown();
        }
        // 8 = app:iconType="overflow"
        else if (mIconType == 8) {
            iconUrl = topbarIconUrl.getOverflow_verticle_dot();
        }
        // 9 = app:iconType="search"
        else if (mIconType == 9) {
            iconUrl = topbarIconUrl.getSearch();
        }
        // 10 = app:iconType="dislike"
        else if (mIconType == 10) {
            iconUrl = topbarIconUrl.getDislike();
        }
        // 11 = app:iconType="unfavourite"
        else if (mIconType == 11) {
            iconUrl = topbarIconUrl.getUnfavorite();
        }
        // 12 = app:iconType="ttsStop"
        else if (mIconType == 12) {
            iconUrl = topbarIconUrl.getTtsPause();
        }
        // 13 = app:iconType="comment"
        else if (mIconType == 13) {
            iconUrl = topbarIconUrl.getComment();
        }
        // 14 = app:iconType="back"
        else if (mIconType == 14) {
            iconUrl = topbarIconUrl.getBack();
        }
        // 15 = app:iconType="slider"
        else if (mIconType == 15) {
            iconUrl = topbarIconUrl.getLeft_slider_three_line();
        }
        // 16 = app:iconType="cross"
        else if (mIconType == 16) {
            iconUrl = topbarIconUrl.getCross();
        }
        // 17 = app:iconType="refresh"
        else if (mIconType == 17) {
            iconUrl = topbarIconUrl.getRefresh();
        }

        //17 = app:iconType="refresh"
        //TODO Set Icon for Refresh

        if (iconUrl == null) {
            iconUrl = "";
        }

        PicassoUtil.loadImageFromCache(getContext(), this, FileUtils.getFilePathFromUrl(destinationFolderPath, iconUrl));

    }


}
