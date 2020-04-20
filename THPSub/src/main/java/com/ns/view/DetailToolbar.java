package com.ns.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.netoperation.model.ArticleBean;
import com.netoperation.net.ApiManager;
import com.netoperation.util.NetConstants;
import com.netoperation.util.UserPref;
import com.ns.alerts.Alerts;
import com.ns.callbacks.ToolbarClickListener;
import com.ns.model.ToolbarCallModel;
import com.ns.thpremium.R;
import com.ns.utils.IntentUtil;
import com.ns.utils.NetUtils;
import com.ns.utils.THPConstants;
import com.ns.view.btn.NSImageButton;


/**
 * Created by ashwanisingh on 21/09/18.
 */

public class DetailToolbar extends Toolbar {

    private ToolbarClickListener mToolbarClickListener;
    private TextView mTitleTextView;
    private NSImageButton mBackImageView;
    private LogoImgView mLogoImageView;
    private IconImgView mSearchImageView;
    private IconImgView mCreateBookMarkImageView;
    private IconImgView mRemoveBookMarkedImageView;
    private IconImgView mTextSizeImageView;
    private IconImgView mTTSPlayImageView;
    private IconImgView mTTSPauseImageView;
    private IconImgView premiumLogoBtn;
    private IconImgView mCommentBtn;

    private ProgressBar mProgressTTS;
    private ProgressBar mProgressFavourite;
    private ProgressBar mProgressBookmark;
    private ProgressBar mProgressLike;

    private IconImgView favStarTHPIC;
    private IconImgView shareTHPIC;
    private IconImgView toggleLikeDisLikeTHPIC;

    private FrameLayout overflowParent;
    private FrameLayout likeParent;
    private FrameLayout bookmarkParent;
    private FrameLayout favouriteParent;
    private FrameLayout ttsParent;


    public void showSectionIcons(OnClickListener onClickListener) {
        likeParent.setVisibility(GONE);
        bookmarkParent.setVisibility(GONE);
        favouriteParent.setVisibility(GONE);
        ttsParent.setVisibility(GONE);
        favStarTHPIC.setVisibility(GONE);
        shareTHPIC.setVisibility(GONE);
        toggleLikeDisLikeTHPIC.setVisibility(GONE);
        mTitleTextView.setVisibility(GONE);
        mTextSizeImageView.setVisibility(GONE);
        mCommentBtn.setVisibility(GONE);

        mBackImageView.setVisibility(VISIBLE);
        mLogoImageView.setVisibility(VISIBLE);
        overflowParent.setVisibility(VISIBLE);
        mSearchImageView.setVisibility(VISIBLE);
        premiumLogoBtn.setVisibility(VISIBLE);
        // In declare-styleable name="NSImageButton" enum Section = 6
        int sectionBtnType = 6;
        mBackImageView.setIcon(sectionBtnType);
        mBackImageView.setOnClickListener(onClickListener);
    }

    public void showSubSectionIcons(String title, OnClickListener onClickListener) {
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
        mCommentBtn.setVisibility(GONE);

        mTitleTextView.setVisibility(VISIBLE);
        premiumLogoBtn.setVisibility(VISIBLE);
        mBackImageView.setVisibility(VISIBLE);

        mTitleTextView.setText(title);

        // In declare-styleable name="NSImageButton" enum arrow_back = 2
        int arrow_back = 2;
        mBackImageView.setIcon(arrow_back);
        mBackImageView.setOnClickListener(onClickListener);
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
        mCommentBtn.setVisibility(GONE);
        premiumLogoBtn.setVisibility(GONE);

        mTitleTextView.setVisibility(VISIBLE);
        mBackImageView.setVisibility(VISIBLE);
        mBackImageView.setVisibility(VISIBLE);
        // In declare-styleable name="NSImageButton" enum arrow_back = 2
        int arrow_back = 2;
        mBackImageView.setIcon(arrow_back);
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
        mCommentBtn.setVisibility(GONE);
        premiumLogoBtn.setVisibility(GONE);

        mTitleTextView.setVisibility(VISIBLE);
        mBackImageView.setVisibility(VISIBLE);

        boolean isHomeArticleOptionScreenShown = UserPref.getInstance(getContext()).isHomeArticleOptionScreenShown();

        if(!isHomeArticleOptionScreenShown) {
            mBackImageView.setVisibility(GONE);
        } else {
            // In declare-styleable name="NSImageButton" enum arrow_back = 2
            int arrow_back = 2;
            mBackImageView.setIcon(arrow_back);
            mBackImageView.setOnClickListener(onClickListener);
        }

        mTitleTextView.setText(title);

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
        mCommentBtn.setVisibility(GONE);
        premiumLogoBtn.setVisibility(GONE);

        mTitleTextView.setVisibility(VISIBLE);
        mBackImageView.setVisibility(VISIBLE);

        mTitleTextView.setText("");
        // In declare-styleable name="NSImageButton" enum arrow_back = 2
        int arrow_back = 2;
        mBackImageView.setIcon(arrow_back);
        mBackImageView.setOnClickListener(onClickListener);
    }

    public void showPremiumDetailIcons(OnClickListener onClickListener) {
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
        mCommentBtn.setVisibility(GONE);

        mBackImageView.setVisibility(VISIBLE);
        mLogoImageView.setVisibility(VISIBLE);
        premiumLogoBtn.setVisibility(VISIBLE);
        // In declare-styleable name="NSImageButton" enum Section = 6
        int arrow_no_line = 0;
        mBackImageView.setIcon(arrow_no_line);
        mBackImageView.setOnClickListener(onClickListener);
    }

    public void showBookmarkPremiumDetailIcons(boolean hasSubscriptionPlan) {
        overflowParent.setVisibility(GONE);
        mSearchImageView.setVisibility(GONE);
        mLogoImageView.setVisibility(GONE);
        mTextSizeImageView.setVisibility(GONE);
        mTitleTextView.setVisibility(GONE);
        favStarTHPIC.setVisibility(GONE);
        toggleLikeDisLikeTHPIC.setVisibility(GONE);
        favouriteParent.setVisibility(GONE);
        likeParent.setVisibility(GONE);
        mCommentBtn.setVisibility(GONE);
        bookmarkParent.setVisibility(GONE);

        if(hasSubscriptionPlan) {
            premiumLogoBtn.setVisibility(GONE);
        } else {
            premiumLogoBtn.setVisibility(VISIBLE);
        }

        ttsParent.setVisibility(VISIBLE);
        shareTHPIC.setVisibility(VISIBLE);
        mTextSizeImageView.setVisibility(VISIBLE);
        mBackImageView.setVisibility(VISIBLE);


        // In declare-styleable name="NSImageButton" enum arrow_back = 2
        int arrow_back = 2;
        mBackImageView.setIcon(arrow_back);
    }

    public void showNonPremiumDetailIcons(boolean hasSubscriptionPlan) {
        overflowParent.setVisibility(GONE);
        mSearchImageView.setVisibility(GONE);
        mLogoImageView.setVisibility(GONE);
        mTextSizeImageView.setVisibility(GONE);
        mTitleTextView.setVisibility(GONE);
        favStarTHPIC.setVisibility(GONE);
        toggleLikeDisLikeTHPIC.setVisibility(GONE);
        favouriteParent.setVisibility(GONE);
        likeParent.setVisibility(GONE);

        if(hasSubscriptionPlan) {
            premiumLogoBtn.setVisibility(GONE);
        } else {
            premiumLogoBtn.setVisibility(VISIBLE);
        }

        mCommentBtn.setVisibility(VISIBLE);
        ttsParent.setVisibility(VISIBLE);
        shareTHPIC.setVisibility(VISIBLE);
        bookmarkParent.setVisibility(VISIBLE);
        mTextSizeImageView.setVisibility(VISIBLE);
        mBackImageView.setVisibility(VISIBLE);


        // In declare-styleable name="NSImageButton" enum arrow_back = 2
        int arrow_back = 2;
        mBackImageView.setIcon(arrow_back);
    }

    public void showDetailIcons() {

    }

    private View mView;
    private String mTitle;

    private ToolbarCallModel mToolbarCallModel;

    public DetailToolbar(Context context) {
        super(context);
        init(context, null);
    }

    public DetailToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DetailToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        mView = LayoutInflater.from(context).inflate(R.layout.toolbar_for_detail, this, true);
        mTitleTextView = findViewById(R.id.action_titleText);
        mBackImageView = findViewById(R.id.action_back);
        mLogoImageView = findViewById(R.id.action_logo);
        mSearchImageView = findViewById(R.id.action_search);
        mCreateBookMarkImageView = findViewById(R.id.bookmarkIC);
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


        setToolbarTitle(mTitle);
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
        mProgressBookmark.setVisibility(GONE);
        if (isBookmarked) {
            mCreateBookMarkImageView.setVisibility(GONE);
            mRemoveBookMarkedImageView.setVisibility(VISIBLE);
        } else {
            mRemoveBookMarkedImageView.setVisibility(GONE);
            mCreateBookMarkImageView.setVisibility(VISIBLE);
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


    public void setIsBookmarked(boolean isBookmarked) {
        showHideBookmarkImg(isBookmarked);
    }


    public void setToolbarTextSize(int size) {
        mTitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, size);
    }

    @Override
    public void setTitle(CharSequence title) {

    }

    @Override
    public void setTitle(int resId) {

    }


    public void setToolbarTitle(CharSequence title) {
        if (mTitleTextView != null) {
            mTitleTextView.setText(title);
            mTitleTextView.setVisibility(VISIBLE);
        }
    }

    public void setToolbarTitle(int resId) {
        mTitleTextView.setText(resId);
    }

    public TextView getTitleView() {
        return mTitleTextView;
    }

    public ImageView getBackView() {
        return mBackImageView;
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
