package com.ns.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.netoperation.config.model.ColorOptionBean;
import com.netoperation.default_db.TableConfiguration;
import com.netoperation.model.ArticleBean;
import com.netoperation.net.ApiManager;
import com.netoperation.net.DefaultTHApiManager;
import com.netoperation.util.NetConstants;
import com.netoperation.util.DefaultPref;
import com.ns.activity.BaseAcitivityTHP;
import com.ns.alerts.Alerts;
import com.ns.callbacks.ToolbarClickListener;
import com.ns.model.ToolbarCallModel;
import com.ns.thpremium.R;
import com.ns.utils.IntentUtil;
import com.ns.utils.NetUtils;
import com.ns.utils.ResUtil;
import com.ns.utils.THPConstants;
import com.ns.view.img.LogoImgView;
import com.ns.view.img.TopbarIconView;


/**
 * Created by ashwanisingh on 21/09/18.
 */

public class Topbar extends Toolbar {

    private ToolbarClickListener mToolbarClickListener;
    private TextView mTitleTextView;
    private TopbarIconView mBackImageView;
    private LogoImgView mLogoImageView;
    private TopbarIconView mSearchImageView;
    private TopbarIconView mCreateBookMarkImageView;
    private TopbarIconView mRemoveBookMarkedImageView;
    private TopbarIconView mTextSizeImageView;
    private TopbarIconView mTTSPlayImageView;
    private TopbarIconView mTTSPauseImageView;
    private TopbarIconView premiumLogoBtn;
    private TopbarIconView mCommentBtn;

    private ProgressBar mProgressTTS;
    private ProgressBar mProgressFavourite;
    private ProgressBar mProgressBookmark;
    private ProgressBar mProgressLike;

    private TopbarIconView favStarTHPIC;
    private TopbarIconView shareTHPIC;
    private TopbarIconView toggleLikeDisLikeTHPIC;

    private FrameLayout overflowParent;
    private FrameLayout likeParent;
    private FrameLayout bookmarkParent;
    private FrameLayout favouriteParent;
    private FrameLayout ttsParent;
    private RelativeLayout commentParent;

    private boolean isUserThemeDay;

    public void SECTION_LISTING_TOPBAR_CROWN(OnClickListener onClickListener) {
        likeParent.setVisibility(GONE);
        bookmarkParent.setVisibility(GONE);
        favouriteParent.setVisibility(GONE);
        ttsParent.setVisibility(GONE);
        favStarTHPIC.setVisibility(GONE);
        shareTHPIC.setVisibility(GONE);
        toggleLikeDisLikeTHPIC.setVisibility(GONE);
        mTitleTextView.setVisibility(GONE);
        mTextSizeImageView.setVisibility(GONE);
        commentParent.setVisibility(GONE);

        mBackImageView.setVisibility(VISIBLE);
        mLogoImageView.setVisibility(VISIBLE);
        overflowParent.setVisibility(VISIBLE);
        mSearchImageView.setVisibility(VISIBLE);
        premiumLogoBtn.setVisibility(VISIBLE);
        mBackImageView.setOnClickListener(onClickListener);
        // 15 = app:iconType="slider"
        mBackImageView.updateIcon(15);
    }

    public void SECTION_LISTING_TOPBAR(OnClickListener onClickListener) {
        likeParent.setVisibility(GONE);
        bookmarkParent.setVisibility(GONE);
        favouriteParent.setVisibility(GONE);
        ttsParent.setVisibility(GONE);
        favStarTHPIC.setVisibility(GONE);
        shareTHPIC.setVisibility(GONE);
        toggleLikeDisLikeTHPIC.setVisibility(GONE);
        mTitleTextView.setVisibility(GONE);
        mTextSizeImageView.setVisibility(GONE);
        commentParent.setVisibility(GONE);
        premiumLogoBtn.setVisibility(GONE);

        mBackImageView.setVisibility(VISIBLE);
        mLogoImageView.setVisibility(VISIBLE);
        overflowParent.setVisibility(VISIBLE);
        mSearchImageView.setVisibility(VISIBLE);
        mBackImageView.setOnClickListener(onClickListener);
        // 15 = app:iconType="slider"
        mBackImageView.updateIcon(15);
    }

    public void SUB_SECTION_LISTING_TOPBAR_CROWN(String title, OnClickListener onClickListener) {
        likeParent.setVisibility(GONE);
        bookmarkParent.setVisibility(GONE);
        favouriteParent.setVisibility(GONE);
        ttsParent.setVisibility(GONE);
        favStarTHPIC.setVisibility(GONE);
        shareTHPIC.setVisibility(GONE);
        toggleLikeDisLikeTHPIC.setVisibility(GONE);
        mLogoImageView.setVisibility(GONE);
        mTextSizeImageView.setVisibility(GONE);
        commentParent.setVisibility(GONE);
        mBackImageView.setVisibility(GONE);

        mSearchImageView.setVisibility(VISIBLE);
        overflowParent.setVisibility(VISIBLE);
        mTitleTextView.setVisibility(VISIBLE);
        premiumLogoBtn.setVisibility(VISIBLE);
        mBackImageView.setVisibility(VISIBLE);

        mTitleTextView.setText(title);
        // 14 = app:iconType="back"
        mBackImageView.updateIcon(14);
        mBackImageView.setOnClickListener(onClickListener);
    }

    public void SUB_SECTION_LISTING_TOPBAR(String title, OnClickListener onClickListener) {
        likeParent.setVisibility(GONE);
        bookmarkParent.setVisibility(GONE);
        favouriteParent.setVisibility(GONE);
        ttsParent.setVisibility(GONE);
        favStarTHPIC.setVisibility(GONE);
        shareTHPIC.setVisibility(GONE);
        toggleLikeDisLikeTHPIC.setVisibility(GONE);
        mLogoImageView.setVisibility(GONE);
        mTextSizeImageView.setVisibility(GONE);
        commentParent.setVisibility(GONE);
        mBackImageView.setVisibility(GONE);
        premiumLogoBtn.setVisibility(GONE);

        mSearchImageView.setVisibility(VISIBLE);
        overflowParent.setVisibility(VISIBLE);
        mTitleTextView.setVisibility(VISIBLE);
        mBackImageView.setVisibility(VISIBLE);

        mTitleTextView.setText(title);
        // 14 = app:iconType="back"
        mBackImageView.updateIcon(14);
        mBackImageView.setOnClickListener(onClickListener);
    }

    public void showNotificationAndBookmarkeIcons(boolean isShowOverFlow) {
        likeParent.setVisibility(GONE);
        bookmarkParent.setVisibility(GONE);
        favouriteParent.setVisibility(GONE);
        ttsParent.setVisibility(GONE);
        favStarTHPIC.setVisibility(GONE);
        shareTHPIC.setVisibility(GONE);
        toggleLikeDisLikeTHPIC.setVisibility(GONE);
        mSearchImageView.setVisibility(GONE);
        mLogoImageView.setVisibility(GONE);
        mTextSizeImageView.setVisibility(GONE);
        commentParent.setVisibility(GONE);
        premiumLogoBtn.setVisibility(GONE);

        mTitleTextView.setVisibility(VISIBLE);
        mBackImageView.setVisibility(VISIBLE);

        if (isShowOverFlow)
        overflowParent.setVisibility(VISIBLE);
        else
        overflowParent.setVisibility(GONE);
    }

    public void showBackTitleIcons(String title, OnClickListener onClickListener) {
        overflowParent.setVisibility(GONE);
        likeParent.setVisibility(GONE);
        bookmarkParent.setVisibility(GONE);
        favouriteParent.setVisibility(GONE);
        ttsParent.setVisibility(GONE);
        favStarTHPIC.setVisibility(GONE);
        shareTHPIC.setVisibility(GONE);
        toggleLikeDisLikeTHPIC.setVisibility(GONE);
        mSearchImageView.setVisibility(GONE);
        mLogoImageView.setVisibility(GONE);
        mTextSizeImageView.setVisibility(GONE);
        commentParent.setVisibility(GONE);
        premiumLogoBtn.setVisibility(GONE);

        mTitleTextView.setVisibility(VISIBLE);
        mBackImageView.setVisibility(VISIBLE);
        mBackImageView.setVisibility(VISIBLE);
        mBackImageView.setOnClickListener(onClickListener);

        mTitleTextView.setText(title);

    }

    public void showHomePeronsoliseIcons(String title, OnClickListener onClickListener) {
        overflowParent.setVisibility(GONE);
        likeParent.setVisibility(GONE);
        bookmarkParent.setVisibility(GONE);
        favouriteParent.setVisibility(GONE);
        ttsParent.setVisibility(GONE);
        favStarTHPIC.setVisibility(GONE);
        shareTHPIC.setVisibility(GONE);
        toggleLikeDisLikeTHPIC.setVisibility(GONE);
        mSearchImageView.setVisibility(GONE);
        mLogoImageView.setVisibility(GONE);
        mTextSizeImageView.setVisibility(GONE);
        commentParent.setVisibility(GONE);
        premiumLogoBtn.setVisibility(GONE);

        mTitleTextView.setVisibility(VISIBLE);
        mBackImageView.setVisibility(VISIBLE);

        boolean isHomeArticleOptionScreenShown = DefaultPref.getInstance(getContext()).isHomeArticleOptionScreenShown();

        if(!isHomeArticleOptionScreenShown) {
            mBackImageView.setVisibility(GONE);
        } else {
            mBackImageView.setOnClickListener(onClickListener);
        }

        mTitleTextView.setText(title);

    }

    public void BREIFING_DETAIL_TOPBAR_CROWN() {
        overflowParent.setVisibility(GONE);
        likeParent.setVisibility(GONE);
        bookmarkParent.setVisibility(GONE);
        favouriteParent.setVisibility(GONE);
        favStarTHPIC.setVisibility(GONE);
        toggleLikeDisLikeTHPIC.setVisibility(GONE);
        mSearchImageView.setVisibility(GONE);
        mLogoImageView.setVisibility(GONE);
        mTextSizeImageView.setVisibility(GONE);
        commentParent.setVisibility(GONE);
        mTitleTextView.setVisibility(GONE);

        premiumLogoBtn.setVisibility(VISIBLE);
        ttsParent.setVisibility(VISIBLE);
        shareTHPIC.setVisibility(VISIBLE);
        mBackImageView.setVisibility(VISIBLE);
    }

    public void BREIFING_DETAIL_TOPBAR() {
        overflowParent.setVisibility(GONE);
        likeParent.setVisibility(GONE);
        bookmarkParent.setVisibility(GONE);
        favouriteParent.setVisibility(GONE);
        favStarTHPIC.setVisibility(GONE);
        toggleLikeDisLikeTHPIC.setVisibility(GONE);
        mSearchImageView.setVisibility(GONE);
        mLogoImageView.setVisibility(GONE);
        mTextSizeImageView.setVisibility(GONE);
        commentParent.setVisibility(GONE);
        mTitleTextView.setVisibility(GONE);
        premiumLogoBtn.setVisibility(GONE);

        ttsParent.setVisibility(VISIBLE);
        shareTHPIC.setVisibility(VISIBLE);
        mBackImageView.setVisibility(VISIBLE);
    }



    public void showGalleryIcons(OnClickListener onClickListener) {
        overflowParent.setVisibility(GONE);
        likeParent.setVisibility(GONE);
        bookmarkParent.setVisibility(GONE);
        favouriteParent.setVisibility(GONE);
        ttsParent.setVisibility(GONE);
        favStarTHPIC.setVisibility(GONE);
        shareTHPIC.setVisibility(GONE);
        toggleLikeDisLikeTHPIC.setVisibility(GONE);
        mSearchImageView.setVisibility(GONE);
        mLogoImageView.setVisibility(GONE);
        mTextSizeImageView.setVisibility(GONE);
        commentParent.setVisibility(GONE);
        premiumLogoBtn.setVisibility(GONE);

        mTitleTextView.setVisibility(VISIBLE);
        mBackImageView.setVisibility(VISIBLE);

        mTitleTextView.setText("");
        mBackImageView.setOnClickListener(onClickListener);
    }

    public void PREMIUM_LISTING_TOPBAR_CROWN(OnClickListener onClickListener) {
        likeParent.setVisibility(GONE);
        bookmarkParent.setVisibility(GONE);
        favouriteParent.setVisibility(GONE);
        ttsParent.setVisibility(GONE);
        favStarTHPIC.setVisibility(GONE);
        shareTHPIC.setVisibility(GONE);
        toggleLikeDisLikeTHPIC.setVisibility(GONE);
        mTitleTextView.setVisibility(GONE);
        mTextSizeImageView.setVisibility(GONE);
        overflowParent.setVisibility(GONE);
        mSearchImageView.setVisibility(GONE);
        commentParent.setVisibility(GONE);

        mBackImageView.setVisibility(VISIBLE);
        mLogoImageView.setVisibility(VISIBLE);
        premiumLogoBtn.setVisibility(VISIBLE);
        mBackImageView.setOnClickListener(onClickListener);
        // 14 = app:iconType="back"
        mBackImageView.updateIcon(14);
    }

    public void PREMIUM_LISTING_TOPBAR(OnClickListener onClickListener) {
        likeParent.setVisibility(GONE);
        bookmarkParent.setVisibility(GONE);
        favouriteParent.setVisibility(GONE);
        ttsParent.setVisibility(GONE);
        favStarTHPIC.setVisibility(GONE);
        shareTHPIC.setVisibility(GONE);
        toggleLikeDisLikeTHPIC.setVisibility(GONE);
        mTitleTextView.setVisibility(GONE);
        mTextSizeImageView.setVisibility(GONE);
        overflowParent.setVisibility(GONE);
        mSearchImageView.setVisibility(GONE);
        commentParent.setVisibility(GONE);
        premiumLogoBtn.setVisibility(GONE);

        mBackImageView.setVisibility(VISIBLE);
        mLogoImageView.setVisibility(VISIBLE);
        mBackImageView.setOnClickListener(onClickListener);
        // 14 = app:iconType="back"
        mBackImageView.updateIcon(14);
    }

    public void PREMIUM_DETAIL_TOPBAR() {
        overflowParent.setVisibility(GONE);
        mSearchImageView.setVisibility(GONE);
        mLogoImageView.setVisibility(GONE);
        mTextSizeImageView.setVisibility(GONE);
        commentParent.setVisibility(GONE);
        mTitleTextView.setVisibility(GONE);
        premiumLogoBtn.setVisibility(GONE);

        likeParent.setVisibility(VISIBLE);
        bookmarkParent.setVisibility(VISIBLE);
        favouriteParent.setVisibility(VISIBLE);
        favStarTHPIC.setVisibility(VISIBLE);
        toggleLikeDisLikeTHPIC.setVisibility(VISIBLE);
        ttsParent.setVisibility(VISIBLE);
        shareTHPIC.setVisibility(VISIBLE);
        mBackImageView.setVisibility(VISIBLE);
    }

    public void PREMIUM_DETAIL_TOPBAR_CROWN() {
        overflowParent.setVisibility(GONE);
        mSearchImageView.setVisibility(GONE);
        mLogoImageView.setVisibility(GONE);
        mTextSizeImageView.setVisibility(GONE);
        commentParent.setVisibility(GONE);
        mTitleTextView.setVisibility(GONE);

        premiumLogoBtn.setVisibility(VISIBLE);
        likeParent.setVisibility(VISIBLE);
        bookmarkParent.setVisibility(VISIBLE);
        favouriteParent.setVisibility(VISIBLE);
        favStarTHPIC.setVisibility(VISIBLE);
        toggleLikeDisLikeTHPIC.setVisibility(VISIBLE);
        ttsParent.setVisibility(VISIBLE);
        shareTHPIC.setVisibility(VISIBLE);
        mBackImageView.setVisibility(VISIBLE);
    }

    public void PREMIUM_BOOKMARK_DETAIL_TOPBAR_CROWN() {
        overflowParent.setVisibility(GONE);
        mSearchImageView.setVisibility(GONE);
        mLogoImageView.setVisibility(GONE);
        mTextSizeImageView.setVisibility(GONE);
        mTitleTextView.setVisibility(GONE);
        favStarTHPIC.setVisibility(GONE);
        toggleLikeDisLikeTHPIC.setVisibility(GONE);
        favouriteParent.setVisibility(GONE);
        likeParent.setVisibility(GONE);
        commentParent.setVisibility(GONE);
        bookmarkParent.setVisibility(GONE);

        premiumLogoBtn.setVisibility(VISIBLE);
        ttsParent.setVisibility(VISIBLE);
        shareTHPIC.setVisibility(VISIBLE);
        mTextSizeImageView.setVisibility(VISIBLE);
        mBackImageView.setVisibility(VISIBLE);
    }

    public void PREMIUM_BOOKMARK_DETAIL_TOPBAR() {
        overflowParent.setVisibility(GONE);
        mSearchImageView.setVisibility(GONE);
        mLogoImageView.setVisibility(GONE);
        mTextSizeImageView.setVisibility(GONE);
        mTitleTextView.setVisibility(GONE);
        favStarTHPIC.setVisibility(GONE);
        toggleLikeDisLikeTHPIC.setVisibility(GONE);
        favouriteParent.setVisibility(GONE);
        likeParent.setVisibility(GONE);
        commentParent.setVisibility(GONE);
        bookmarkParent.setVisibility(GONE);
        premiumLogoBtn.setVisibility(GONE);

        ttsParent.setVisibility(VISIBLE);
        shareTHPIC.setVisibility(VISIBLE);
        mTextSizeImageView.setVisibility(VISIBLE);
        mBackImageView.setVisibility(VISIBLE);
    }

    public void DEFAULT_DETAIL_TOPBAR_CROWN() {
        overflowParent.setVisibility(GONE);
        mSearchImageView.setVisibility(GONE);
        mLogoImageView.setVisibility(GONE);
        mTextSizeImageView.setVisibility(GONE);
        mTitleTextView.setVisibility(GONE);
        favStarTHPIC.setVisibility(GONE);
        toggleLikeDisLikeTHPIC.setVisibility(GONE);
        favouriteParent.setVisibility(GONE);
        likeParent.setVisibility(GONE);

        premiumLogoBtn.setVisibility(VISIBLE);
        commentParent.setVisibility(VISIBLE);
        ttsParent.setVisibility(VISIBLE);
        shareTHPIC.setVisibility(VISIBLE);
        bookmarkParent.setVisibility(VISIBLE);
        mTextSizeImageView.setVisibility(VISIBLE);
        mBackImageView.setVisibility(VISIBLE);

    }

    public void DEFAULT_DETAIL_TOPBAR() {
        overflowParent.setVisibility(GONE);
        mSearchImageView.setVisibility(GONE);
        mLogoImageView.setVisibility(GONE);
        mTextSizeImageView.setVisibility(GONE);
        mTitleTextView.setVisibility(GONE);
        favStarTHPIC.setVisibility(GONE);
        toggleLikeDisLikeTHPIC.setVisibility(GONE);
        favouriteParent.setVisibility(GONE);
        likeParent.setVisibility(GONE);
        premiumLogoBtn.setVisibility(GONE);

        commentParent.setVisibility(VISIBLE);
        ttsParent.setVisibility(VISIBLE);
        shareTHPIC.setVisibility(VISIBLE);
        bookmarkParent.setVisibility(VISIBLE);
        mTextSizeImageView.setVisibility(VISIBLE);
        mBackImageView.setVisibility(VISIBLE);

    }

    public void DEFAULT_RESTRICTED_DETAIL_TOPBAR_CROWN() {
        overflowParent.setVisibility(GONE);
        mSearchImageView.setVisibility(GONE);
        mLogoImageView.setVisibility(GONE);
        mTextSizeImageView.setVisibility(GONE);
        mTitleTextView.setVisibility(GONE);
        favStarTHPIC.setVisibility(GONE);
        toggleLikeDisLikeTHPIC.setVisibility(GONE);
        favouriteParent.setVisibility(GONE);
        likeParent.setVisibility(GONE);
        commentParent.setVisibility(GONE);
        ttsParent.setVisibility(GONE);
        bookmarkParent.setVisibility(GONE);
        mTextSizeImageView.setVisibility(GONE);

        mBackImageView.setVisibility(VISIBLE);
        premiumLogoBtn.setVisibility(VISIBLE);
        shareTHPIC.setVisibility(VISIBLE);
    }

    private ToolbarCallModel mToolbarCallModel;

    public Topbar(Context context) {
        super(context);
        init(context, null);
    }

    public Topbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public Topbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.toolbar_for_detail, this, true);
        setContentInsetsAbsolute(0, 0);

        isUserThemeDay = DefaultPref.getInstance(context).isUserThemeDay();
        TableConfiguration tableConfiguration = BaseAcitivityTHP.getTableConfiguration();

        if(tableConfiguration != null && THPConstants.IS_USE_SEVER_THEME) {
            ColorOptionBean topbarTheme = tableConfiguration.getAppTheme().getTopBarBg();
            if(isUserThemeDay) {
                setBackgroundColor(Color.parseColor(topbarTheme.getLight()));
            } else {
                setBackgroundColor(Color.parseColor(topbarTheme.getDark()));
            }
        } else {
            if(isUserThemeDay) {
                setBackgroundColor(ResUtil.getColor(getResources(), R.color.topbar_light));
            }
            else {
                setBackgroundColor(ResUtil.getColor(getResources(), R.color.topbar_dark));
            }
        }

        mTitleTextView = findViewById(R.id.action_titleText);
        mBackImageView = findViewById(R.id.action_back);
        mLogoImageView = findViewById(R.id.action_logo);
        mSearchImageView = findViewById(R.id.action_search);
        mCreateBookMarkImageView = findViewById(R.id.unbookmarkIC);
        mRemoveBookMarkedImageView = findViewById(R.id.bookmarkedIC);
        mTextSizeImageView = findViewById(R.id.action_fontSizeIC);
        mTTSPlayImageView = findViewById(R.id.action_ttsPlay);
        mTTSPauseImageView = findViewById(R.id.action_ttsStop);
        mCommentBtn = findViewById(R.id.action_commentTHP);

        mProgressTTS = findViewById(R.id.action_ttsProgress);
        mProgressFavourite = findViewById(R.id.action_favTHPProgressBar);
        mProgressBookmark = findViewById(R.id.bookmarkrogressBar);
        mProgressLike = findViewById(R.id.action_likeTHPProgressBar);

        favStarTHPIC = findViewById(R.id.action_favTHP);
        shareTHPIC = findViewById(R.id.action_shareTHP);
        toggleLikeDisLikeTHPIC = findViewById(R.id.action_likeTHPIC);

        overflowParent = findViewById(R.id.overflowParent);
        likeParent = findViewById(R.id.likeParent);
        bookmarkParent = findViewById(R.id.bookmarkParent);
        favouriteParent = findViewById(R.id.favouriteParent);
        ttsParent = findViewById(R.id.ttsParent);
        commentParent = findViewById(R.id.commentParent);

        premiumLogoBtn = findViewById(R.id.action_premiumLogoBtn);

        if (favStarTHPIC != null) {
            favStarTHPIC.setOnClickListener(v -> {
                if (mToolbarClickListener != null) {
                    hideFavProgTHP(false);
                    mToolbarClickListener.onFavClickListener(mToolbarCallModel);

                }
            });
        }

        if (toggleLikeDisLikeTHPIC != null) {
            toggleLikeDisLikeTHPIC.setOnClickListener(v -> {
                if (mToolbarClickListener != null) {
                    hideLikeProgTHP(false);
                    mToolbarClickListener.onLikeClickListener(mToolbarCallModel);
                }
            });
        }

        if (mCreateBookMarkImageView != null) {
            mCreateBookMarkImageView.setOnClickListener(v -> {
                if (mToolbarClickListener != null) {
                    showBookmarkProgTHP(true, false);
                    mToolbarClickListener.onCreateBookmarkClickListener(mToolbarCallModel);
                }
            });
        }

        if (mRemoveBookMarkedImageView != null) {
            mRemoveBookMarkedImageView.setOnClickListener(v -> {
                if (mToolbarClickListener != null) {
                    showBookmarkProgTHP(true, false);
                    mToolbarClickListener.onRemoveBookmarkClickListener(mToolbarCallModel);
                }
            });
        }

        if (mBackImageView != null) {
            mBackImageView.setOnClickListener(v -> {
                if (mToolbarClickListener != null) {
                    mToolbarClickListener.onBackClickListener();
                }
            });
        }

        if (shareTHPIC != null) {
            shareTHPIC.setOnClickListener(v -> {
                if (mToolbarClickListener != null) {
                    mToolbarClickListener.onShareClickListener(mToolbarCallModel);
                }
            });
        }


        if (mTextSizeImageView != null) {
            mTextSizeImageView.setOnClickListener(v -> {
                if (mToolbarClickListener != null) {
                    mToolbarClickListener.onFontSizeClickListener(mToolbarCallModel);
                }
            });
        }

        mSearchImageView.setOnClickListener(v -> {
            if (mToolbarClickListener != null) {
                mToolbarClickListener.onSearchClickListener(mToolbarCallModel);
            }
        });

        overflowParent.setOnClickListener(v -> {
            if (mToolbarClickListener != null) {
                mToolbarClickListener.onOverflowClickListener(mToolbarCallModel);
            }
        });

        mCommentBtn.setOnClickListener(v -> {
            if (mToolbarClickListener != null) {
                mToolbarClickListener.onCommentClickListener(mToolbarCallModel);
            }
        });

        if (mTTSPlayImageView != null) {
            mTTSPlayImageView.setOnClickListener(v -> {

                mProgressTTS.setVisibility(VISIBLE);
                mTTSPlayImageView.setVisibility(GONE);

                if (mToolbarClickListener != null) {
                    mToolbarClickListener.onTTSPlayClickListener(mToolbarCallModel);
                }
            });
        }

        if (mTTSPauseImageView != null) {
            mTTSPauseImageView.setOnClickListener(v -> {
                mProgressTTS.setVisibility(VISIBLE);
                mTTSPauseImageView.setVisibility(GONE);

                if (mToolbarClickListener != null) {
                    mToolbarClickListener.onTTSStopClickListener(mToolbarCallModel);
                }
            });
        }

        if (premiumLogoBtn != null) {
            premiumLogoBtn.setOnClickListener(v -> {
                if (NetUtils.isConnected(getContext())) {
                    IntentUtil.openSubscriptionActivity(getContext(), THPConstants.FROM_SUBSCRIPTION_EXPLORE);
                } else {
                    Alerts.noConnectionSnackBar(v, (AppCompatActivity) getContext());
                }
            });
        }


    }


    public void hideBookmark_Fav_Like() {
        mProgressBookmark.setVisibility(GONE);
        mProgressFavourite.setVisibility(GONE);
        mProgressLike.setVisibility(GONE);

        mCreateBookMarkImageView.setVisibility(GONE);
        mRemoveBookMarkedImageView.setVisibility(GONE);

        favStarTHPIC.setVisibility(GONE);
        toggleLikeDisLikeTHPIC.setVisibility(GONE);
    }

    public void hide_Fav_Like() {
        mProgressFavourite.setVisibility(GONE);
        mProgressLike.setVisibility(GONE);

        favStarTHPIC.setVisibility(GONE);
        toggleLikeDisLikeTHPIC.setVisibility(GONE);
    }

    private void showHideBookmarkImg(boolean isBookmarked) {
        // 4 = app:iconType="unbookmark"
        // 3 = app:iconType="bookmarked"
        mProgressBookmark.setVisibility(GONE);
        if (isBookmarked) {
            mCreateBookMarkImageView.setVisibility(GONE);
            mRemoveBookMarkedImageView.setVisibility(VISIBLE);
            //mRemoveBookMarkedImageView.updateIcon(4);
        } else {
            mRemoveBookMarkedImageView.setVisibility(GONE);
            mCreateBookMarkImageView.setVisibility(VISIBLE);
            //mCreateBookMarkImageView.updateIcon(3);
        }
    }

    private void showBookmarkProgTHP(boolean shouldVisible, boolean isBookmarked) {
        if (shouldVisible) {
            mProgressBookmark.setVisibility(VISIBLE);
            mCreateBookMarkImageView.setVisibility(GONE);
            mRemoveBookMarkedImageView.setVisibility(GONE);
        } else {
            if (isBookmarked) {
                mCreateBookMarkImageView.setVisibility(GONE);
                mRemoveBookMarkedImageView.setVisibility(VISIBLE);
            } else {
                mRemoveBookMarkedImageView.setVisibility(GONE);
                mCreateBookMarkImageView.setVisibility(VISIBLE);
            }
            mProgressBookmark.setVisibility(GONE);
        }
    }

    private void hideFavProgTHP(boolean shouldVisible) {
        if (shouldVisible) {
            mProgressFavourite.setVisibility(GONE);
            favStarTHPIC.setVisibility(VISIBLE);
        } else {
            mProgressFavourite.setVisibility(VISIBLE);
            favStarTHPIC.setVisibility(INVISIBLE);
        }
    }

    private void hideLikeProgTHP(boolean shouldVisible) {
        if (shouldVisible) {
            mProgressLike.setVisibility(GONE);
            toggleLikeDisLikeTHPIC.setVisibility(VISIBLE);
        } else {
            mProgressLike.setVisibility(VISIBLE);
            toggleLikeDisLikeTHPIC.setVisibility(INVISIBLE);
        }
    }

    public void isFavOrLike(Context context, ArticleBean articleBean, String articleId) {
        ApiManager.isExistFavNdLike(context, articleId)
                .subscribe(likeVal -> {
                    hideLikeProgTHP(true);
                    hideFavProgTHP(true);
                    int like = (int) likeVal;
                    if (articleBean != null) {
                        articleBean.setIsFavourite(like);
                    }
                    favStarTHPIC.setVisibility(View.VISIBLE);
                    toggleLikeDisLikeTHPIC.setVisibility(View.VISIBLE);
                    favStarTHPIC.setEnabled(true);
                    toggleLikeDisLikeTHPIC.setEnabled(true);
                    if (like == NetConstants.LIKE_NEUTRAL) {
                        favStarTHPIC.setImageResource(R.drawable.ic_like_unselected);
                        toggleLikeDisLikeTHPIC.setImageResource(R.drawable.ic_switch_off_copy);
                    } else if (like == NetConstants.LIKE_YES) {
                        favStarTHPIC.setImageResource(R.drawable.ic_like_selected);
                        toggleLikeDisLikeTHPIC.setImageResource(R.drawable.ic_switch_off_copy);
                    } else if (like == NetConstants.LIKE_NO) {
                        favStarTHPIC.setImageResource(R.drawable.ic_like_unselected);
                        toggleLikeDisLikeTHPIC.setImageResource(R.drawable.ic_switch_on_copy);
                    }

                }, val -> {
                    Log.i("", "");
                });
    }

    public void showCommentCount(Context context, String articleId) {
        DefaultTHApiManager.isReadArticleId(context, articleId)
                .subscribe(tableRead -> {
                    Log.i("", "");
                    if (tableRead != null && tableRead.getArticleId().equals(articleId)) {
                        String count = tableRead.getCommentCount();
                        if(!ResUtil.isEmpty(count) && !count.equals("0")) {
                            TextView countTV = commentParent.findViewById(R.id.textview_comment_count);
                            countTV.setVisibility(VISIBLE);
                            countTV.setText(count);
                        }
                        else {
                            commentParent.findViewById(R.id.textview_comment_count).setVisibility(GONE);
                        }

                    }
                    else {
                        commentParent.findViewById(R.id.textview_comment_count).setVisibility(GONE);
                    }
                }, throwable -> {
                    Log.i("", "");
                    commentParent.findViewById(R.id.textview_comment_count).setVisibility(GONE);
                });
    }


    public void setIsBookmarked(boolean isBookmarked) {
        showHideBookmarkImg(isBookmarked);
    }


    public void setToolbarTextSize(int size) {
        mTitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, size);
    }

    @Override
    public void setTitle(CharSequence title) {
        setToolbarTitle(title);
    }

    @Override
    public void setTitle(int resId) {
        setToolbarTitle(resId);
    }


    public void setToolbarTitle(CharSequence title) {
        if (mTitleTextView != null) {
            mTitleTextView.setText(title);
            mTitleTextView.setVisibility(VISIBLE);
        }
    }

    public void setToolbarTitle(int resId) {
        if (mTitleTextView != null) {
            mTitleTextView.setText(resId);
            mTitleTextView.setVisibility(VISIBLE);
        }
    }

    public TextView getTitleView() {
        return mTitleTextView;
    }

    public ImageView getBackView() {
        return mBackImageView;
    }

    public TopbarIconView getPremiumLogoBtn() {
        return premiumLogoBtn;
    }

    public void hideBackBtn() {
        mBackImageView.setVisibility(GONE);
    }

    public void showBackBtn() {
        mBackImageView.setVisibility(VISIBLE);
    }


    public void showTTSPlayView(int isLanguageSupportTTS) {
        if (isLanguageSupportTTS == 0) {
            if (mTTSPlayImageView != null) {
                mTTSPlayImageView.setVisibility(GONE);
                mTTSPauseImageView.setVisibility(GONE);
            }
        } else {
            if (mTTSPauseImageView != null) {
                mTTSPauseImageView.setVisibility(GONE);
                mTTSPlayImageView.setVisibility(VISIBLE);
                mProgressTTS.setVisibility(GONE);
            }
        }
    }

    public void showTTSPauseView(int isLanguageSupportTTS) {
        if (isLanguageSupportTTS == 0) {
            if (mTTSPlayImageView != null) {
                mTTSPlayImageView.setVisibility(GONE);
                mTTSPauseImageView.setVisibility(GONE);
            }
        } else {
            if (mTTSPauseImageView != null) {
                mTTSPauseImageView.setVisibility(VISIBLE);
                mTTSPlayImageView.setVisibility(GONE);
                mProgressTTS.setVisibility(GONE);
            }
        }
    }

    public void setToolbarMenuListener(ToolbarClickListener toolbarClickListener) {
        mToolbarClickListener = toolbarClickListener;
    }

    public ToolbarCallModel getToolbarCallModel() {
        return mToolbarCallModel;
    }

    public void setToolbarCallModel(ToolbarCallModel toolbarCallModel) {
        this.mToolbarCallModel = toolbarCallModel;
    }

    public void hideCrownBtn() {
        if (premiumLogoBtn != null) {
            premiumLogoBtn.setVisibility(GONE);
        }
    }

    public void showCrownBtn() {
        if (premiumLogoBtn != null) {
            premiumLogoBtn.setVisibility(VISIBLE);
        }
    }

    @Override
    public CharSequence getTitle() {
        return mTitleTextView.getText();
    }
}
