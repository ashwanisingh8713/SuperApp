package com.ns.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

import com.netoperation.config.model.OtherIconUrls;
import com.netoperation.config.model.OtherIconsDownloadUrls;
import com.netoperation.default_db.TableConfiguration;
import com.netoperation.util.DefaultPref;
import com.ns.activity.BaseAcitivityTHP;
import com.ns.thpremium.R;

public class TopbarIconView extends AppCompatImageView {

    private int mIconType = -1;

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
        boolean isUserThemeDay = DefaultPref.getInstance(context).isUserThemeDay();

        if(attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NSIcon);
            if (typedArray.hasValue(R.styleable.NSIcon_iconType)) {
                mIconType = typedArray.getInt(R.styleable.NSIcon_iconType, 0);
            } else {
                mIconType = -1;
            }
        }



        ///////////////////////////////////////////////////////////////////


        TableConfiguration tableConfiguration = BaseAcitivityTHP.getTableConfiguration();
        if(tableConfiguration != null) {
            OtherIconsDownloadUrls otherIconsDownloadUrls = tableConfiguration.getOtherIconsDownloadUrls();
            if (isUserThemeDay) {
                OtherIconUrls otherIconUrls = otherIconsDownloadUrls.getLight();
                otherIconUrls.getListing();
            }
            else {

            }
        }




        ///////////////////////////////////////////////////////////////////

        // 0 = app:iconType="ttsPlay"
        if(mIconType == 0) {
            if (isUserThemeDay) {
                setImageResource(R.drawable.ic_audio);
            } else {
                setImageResource(R.drawable.ic_audio_w);
            }
        }
        // 1 = app:iconType="share"
        else if(mIconType == 1) {
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
        // 5 = app:iconType="fontSize"
        else if(mIconType == 5) {
            if (isUserThemeDay) {
                setImageResource(R.drawable.ic_textsize);
            } else {
                setImageResource(R.drawable.ic_textsize_w);
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
        // 7 = app:iconType="crown"
        else if(mIconType == 7) {
            if (isUserThemeDay) {
                setImageResource(R.drawable.ic_premium_logo);
            } else {
                setImageResource(R.drawable.ic_premium_logo);
            }
        }
        // 8 = app:iconType="overflow"
        else if(mIconType == 8) {
            if (isUserThemeDay) {
                setImageResource(R.drawable.ic_more);
            } else {
                setImageResource(R.drawable.ic_more_w);
            }
        }
        // 9 = app:iconType="search"
        else if(mIconType == 9) {
            if (isUserThemeDay) {
                setImageResource(R.drawable.ic_search);
            } else {
                setImageResource(R.drawable.ic_search_w);
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
        // 12 = app:iconType="ttsStop"
        if(mIconType == 12) {
            if (isUserThemeDay) {
                setImageResource(R.drawable.tts_stop);
            } else {
                setImageResource(R.drawable.tts_stop);
            }
        }
        // 13 = app:iconType="comment"
        if(mIconType == 13) {
            if (isUserThemeDay) {
                setImageResource(R.drawable.ic_comment);
            } else {
                setImageResource(R.drawable.ic_comment_w);
            }
        }

    }


}
