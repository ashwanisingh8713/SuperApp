package com.ns.view;

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

public class TopbarIconView extends AppCompatImageView {

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

        if(attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NSIcon);
            iconType = typedArray.getInt(R.styleable.NSIcon_iconType, 0);

        }

        updateIcon(iconType);

    }

    private void updateIcon(int iconType) {
        final boolean isUserThemeDay = DefaultPref.getInstance(getContext()).isUserThemeDay();
        TableConfiguration tableConfiguration = BaseAcitivityTHP.getTableConfiguration();
        if(tableConfiguration != null && THPConstants.IS_USE_SEVER_THEME) {
            if(isUserThemeDay) {
                loadIconsFromServer(iconType, tableConfiguration.getOtherIconsDownloadUrls().getLight().getTopbar(), FileUtils.destinationFolder(getContext(), FileUtils.TOPBAR_ICONs_LIGHT).getPath());
            } else {
                loadIconsFromServer(iconType, tableConfiguration.getOtherIconsDownloadUrls().getDark().getTopbar(), FileUtils.destinationFolder(getContext(), FileUtils.TOPBAR_ICONs_DARK).getPath());
            }
        }
        else {
            loadIconsFromApp(iconType, isUserThemeDay);
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

        //17 = app:iconType="refresh"
        //TODO Set Icon for Refresh

        if(iconUrl == null) {
            iconUrl = "";
        }

        PicassoUtil.loadImageFromCache(getContext(), this, FileUtils.getFilePathFromUrl(destinationFolderPath, iconUrl));

    }

    private void loadIconsFromApp (int mIconType, boolean isUserThemeDay) {
        // 0 = app:iconType="ttsPlay"
        if (mIconType == 0) {
            if (isUserThemeDay) {
                setImageResource(R.drawable.ic_audio);
            } else {
                setImageResource(R.drawable.ic_audio_w);
            }
        }
        // 1 = app:iconType="share"
        else if (mIconType == 1) {
            if (isUserThemeDay) {
                setImageResource(R.drawable.ic_share_article);
            } else {
                setImageResource(R.drawable.ic_share_article_w);
            }
        }
        // 2 = app:iconType="favourite"
        else if (mIconType == 2) {
            if (isUserThemeDay) {
                setImageResource(R.drawable.ic_like_selected);
            } else {
                setImageResource(R.drawable.ic_like_selected);
            }
        }
        // 3 = app:iconType="bookmarked"
        else if (mIconType == 3) {
            if (isUserThemeDay) {
                setImageResource(R.drawable.ic_bookmark_selected);
            } else {
                setImageResource(R.drawable.ic_bookmark_selected);
            }
        }
        // 4 = app:iconType="unbookmark"
        else if (mIconType == 4) {
            if (isUserThemeDay) {
                setImageResource(R.drawable.ic_bookmark_unselected);
            } else {
                setImageResource(R.drawable.ic_bookmark_unselected);
            }
        }
        // 5 = app:iconType="fontSize"
        else if (mIconType == 5) {
            if (isUserThemeDay) {
                setImageResource(R.drawable.ic_textsize);
            } else {
                setImageResource(R.drawable.ic_textsize_w);
            }
        }
        // 6 = app:iconType="like"
        else if (mIconType == 6) {
            if (isUserThemeDay) {
                setImageResource(R.drawable.ic_switch_off_copy);
            } else {
                setImageResource(R.drawable.ic_switch_off_copy);
            }
        }
        // 7 = app:iconType="crown"
        else if (mIconType == 7) {
            if (isUserThemeDay) {
                setImageResource(R.drawable.ic_premium_logo);
            } else {
                setImageResource(R.drawable.ic_premium_logo);
            }
        }
        // 8 = app:iconType="overflow"
        else if (mIconType == 8) {
            if (isUserThemeDay) {
                setImageResource(R.drawable.ic_more);
            } else {
                setImageResource(R.drawable.ic_more_w);
            }
        }
        // 9 = app:iconType="search"
        else if (mIconType == 9) {
            if (isUserThemeDay) {
                setImageResource(R.drawable.ic_search);
            } else {
                setImageResource(R.drawable.ic_search_w);
            }
        }
        // 10 = app:iconType="dislike"
        else if (mIconType == 10) {
            if (isUserThemeDay) {
                setImageResource(R.drawable.ic_switch_on_copy);
            } else {
                setImageResource(R.drawable.ic_switch_on_copy);
            }
        }
        // 11 = app:iconType="unfavourite"
        else if (mIconType == 11) {
            if (isUserThemeDay) {
                setImageResource(R.drawable.ic_like_unselected);
            } else {
                setImageResource(R.drawable.ic_like_unselected);
            }
        }
        // 12 = app:iconType="ttsStop"
        else if (mIconType == 12) {
            if (isUserThemeDay) {
                setImageResource(R.drawable.tts_stop);
            } else {
                setImageResource(R.drawable.tts_stop);
            }
        }
        // 13 = app:iconType="comment"
        else if (mIconType == 13) {
            if (isUserThemeDay) {
                setImageResource(R.drawable.ic_comment);
            } else {
                setImageResource(R.drawable.ic_comment_w);
            }
        }
        // 14 = app:iconType="back"
        else if (mIconType == 14) {
            if (isUserThemeDay) {
                setImageResource(R.drawable.ic_arrow_back_light);
            } else {
                setImageResource(R.drawable.ic_arrow_back_dark);
            }
        }
        // 15 = app:iconType="slider"
        else if (mIconType == 15) {
            if (isUserThemeDay) {
                setImageResource(R.drawable.ic_navigation);
            } else {
                setImageResource(R.drawable.ic_navigation_dark);
            }
        }
        // 16 = app:iconType="cross"
        else if (mIconType == 15) {
            if (isUserThemeDay) {
                setImageResource(R.drawable.ic_close_search);
            } else {
                setImageResource(R.drawable.ic_close_search);
            }
        }
    }

    public void setIcon(int iconType) {
        updateIcon(iconType);
    }


}
