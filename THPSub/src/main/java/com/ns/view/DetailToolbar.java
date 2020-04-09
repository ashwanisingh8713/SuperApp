package com.ns.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.netoperation.model.ArticleBean;
import com.netoperation.net.ApiManager;
import com.netoperation.util.NetConstants;
import com.ns.alerts.Alerts;
import com.ns.callbacks.ToolbarClickListener;
import com.ns.model.ToolbarCallModel;
import com.ns.thpremium.R;
import com.ns.utils.IntentUtil;
import com.ns.utils.NetUtils;
import com.ns.utils.THPConstants;


/**
 * Created by ashwanisingh on 21/09/18.
 */

public class DetailToolbar extends Toolbar {

    private ToolbarClickListener mToolbarClickListener;
    private TextView mTitleTextView;
    private ImageButton mBackImageView;
    private ImageView mCreateBookMarkImageView;
    private ImageView mRemoveBookMarkedImageView;
    private ImageView mTextSizeImageView;
    private ImageView mTTSPlayImageView;
    private ImageView mTTSPauseImageView;
    private ImageView premiumLogoBtn;

    private ProgressBar mTtsProgress;
    private ProgressBar favTHPProgressBar;
    private ProgressBar bookmarkProgressBar;
    private ProgressBar likeTHPProgressBar;

    private ImageView favStarTHPIC;
    private ImageView shareTHPIC;
    private ImageView toggleLikeDisLikeTHPIC;

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

    public void hideBookmark_Fav_Like() {
        bookmarkProgressBar.setVisibility(GONE);
        favTHPProgressBar.setVisibility(GONE);
        likeTHPProgressBar.setVisibility(GONE);

        mCreateBookMarkImageView.setVisibility(GONE);
        mRemoveBookMarkedImageView.setVisibility(GONE);

        favStarTHPIC.setVisibility(GONE);
        toggleLikeDisLikeTHPIC.setVisibility(GONE);
    }

    public void hide_Fav_Like() {
        favTHPProgressBar.setVisibility(GONE);
        likeTHPProgressBar.setVisibility(GONE);

        favStarTHPIC.setVisibility(GONE);
        toggleLikeDisLikeTHPIC.setVisibility(GONE);
    }

    private void showHideBookmarkImg(boolean isBookmarked) {
        bookmarkProgressBar.setVisibility(GONE);
        if(isBookmarked) {
            mCreateBookMarkImageView.setVisibility(GONE);
            mRemoveBookMarkedImageView.setVisibility(VISIBLE);
        } else {
            mRemoveBookMarkedImageView.setVisibility(GONE);
            mCreateBookMarkImageView.setVisibility(VISIBLE);
        }
    }

    private void showBookmarkProgTHP(boolean shouldVisible, boolean isBookmarked) {
        if(shouldVisible) {
            bookmarkProgressBar.setVisibility(VISIBLE);
            mCreateBookMarkImageView.setVisibility(GONE);
            mRemoveBookMarkedImageView.setVisibility(GONE);
        }
        else {
            if(isBookmarked) {
                mCreateBookMarkImageView.setVisibility(GONE);
                mRemoveBookMarkedImageView.setVisibility(VISIBLE);
            } else {
                mRemoveBookMarkedImageView.setVisibility(GONE);
                mCreateBookMarkImageView.setVisibility(VISIBLE);
            }
            bookmarkProgressBar.setVisibility(GONE);
        }
    }

    private void hideFavProgTHP(boolean shouldVisible) {
        if(shouldVisible) {
            favTHPProgressBar.setVisibility(GONE);
            favStarTHPIC.setVisibility(VISIBLE);
        } else {
            favTHPProgressBar.setVisibility(VISIBLE);
            favStarTHPIC.setVisibility(INVISIBLE);
        }
    }

    private void hideLikeProgTHP(boolean shouldVisible) {
        if(shouldVisible) {
            likeTHPProgressBar.setVisibility(GONE);
            toggleLikeDisLikeTHPIC.setVisibility(VISIBLE);
        } else {
            likeTHPProgressBar.setVisibility(VISIBLE);
            toggleLikeDisLikeTHPIC.setVisibility(INVISIBLE);
        }
    }

    public void isFavOrLike(Context context, ArticleBean articleBean, String articleId) {
        ApiManager.isExistFavNdLike(context, articleId)
                .subscribe(likeVal-> {
                    hideLikeProgTHP(true);
                    hideFavProgTHP(true);
                    int like = (int)likeVal;
                    if(articleBean != null) {
                        articleBean.setIsFavourite(like);
                    }
                    favStarTHPIC.setVisibility(View.VISIBLE);
                    toggleLikeDisLikeTHPIC.setVisibility(View.VISIBLE);
                    favStarTHPIC.setEnabled(true);
                    toggleLikeDisLikeTHPIC.setEnabled(true);
                    if(like == NetConstants.LIKE_NEUTRAL) {
                        favStarTHPIC.setImageResource(R.drawable.ic_like_unselected);
                        toggleLikeDisLikeTHPIC.setImageResource(R.drawable.ic_switch_off_copy);
                    }
                    else if(like == NetConstants.LIKE_YES) {
                        favStarTHPIC.setImageResource(R.drawable.ic_like_selected);
                        toggleLikeDisLikeTHPIC.setImageResource(R.drawable.ic_switch_off_copy);
                    }
                    else if(like == NetConstants.LIKE_NO) {
                        favStarTHPIC.setImageResource(R.drawable.ic_like_unselected);
                        toggleLikeDisLikeTHPIC.setImageResource(R.drawable.ic_switch_on_copy);
                    }

                }, val->{
                    Log.i("", "");
                });
    }

    private void init(Context context, AttributeSet attrs) {
        mView = LayoutInflater.from(context).inflate(R.layout.toolbar_for_detail, this, true);

        if(mView == null) {
            return;
        }

        mTitleTextView =  findViewById(R.id.title);
        mBackImageView = findViewById(R.id.back);
        mCreateBookMarkImageView = findViewById(R.id.bookmarkIC);
        mRemoveBookMarkedImageView = findViewById(R.id.bookmarkedIC);
        mTextSizeImageView = findViewById(R.id.fontSizeIC);
        mTTSPlayImageView = findViewById(R.id.ttsPlayIC);
        mTTSPauseImageView = findViewById(R.id.ttsPauseIC);

        mTtsProgress = findViewById(R.id.ttsProgress);
        favTHPProgressBar = findViewById(R.id.favTHPProgressBar);
        bookmarkProgressBar = findViewById(R.id.bookmarkrogressBar);
        likeTHPProgressBar = findViewById(R.id.likeTHPProgressBar);

        favStarTHPIC = findViewById(R.id.favTHPIC);
        shareTHPIC = findViewById(R.id.shareTHPIC);
        toggleLikeDisLikeTHPIC = findViewById(R.id.likeTHPIC);

        premiumLogoBtn = findViewById(R.id.premiumLogoBtn);

        if(favStarTHPIC != null) {
            favStarTHPIC.setOnClickListener(v -> {
                if (mToolbarClickListener != null) {
                    hideFavProgTHP(false);
                    mToolbarClickListener.onFavClickListener(mToolbarCallModel);

                }
            });
        }

        if(toggleLikeDisLikeTHPIC != null) {
            toggleLikeDisLikeTHPIC.setOnClickListener(v -> {
                if (mToolbarClickListener != null) {
                    hideLikeProgTHP(false);
                    mToolbarClickListener.onLikeClickListener(mToolbarCallModel);
                }
            });
        }

        if(mCreateBookMarkImageView != null) {
            mCreateBookMarkImageView.setOnClickListener(v->{
                if (mToolbarClickListener != null) {
                    showBookmarkProgTHP(true, false);
                    mToolbarClickListener.onCreateBookmarkClickListener(mToolbarCallModel);
                }
            });
        }

        if(mRemoveBookMarkedImageView != null) {
            mRemoveBookMarkedImageView.setOnClickListener(v->{
                if (mToolbarClickListener != null) {
                    showBookmarkProgTHP(true, false);
                    mToolbarClickListener.onRemoveBookmarkClickListener(mToolbarCallModel);
                }
            });
        }

        if(mBackImageView != null) {
            mBackImageView.setOnClickListener(v -> {
                if (mToolbarClickListener != null) {
                    mToolbarClickListener.onBackClickListener();
                }
            });
        }

        if(shareTHPIC != null) {
            shareTHPIC.setOnClickListener(v -> {
                if (mToolbarClickListener != null) {
                    mToolbarClickListener.onShareClickListener(mToolbarCallModel);
                }
            });
        }



        if(mTextSizeImageView != null) {
            mTextSizeImageView.setOnClickListener(v->{
                if (mToolbarClickListener != null) {
                    mToolbarClickListener.onFontSizeClickListener(mToolbarCallModel);
                }
            });
        }

        if(mTTSPlayImageView != null) {
            mTTSPlayImageView.setOnClickListener(v->{

                mTtsProgress.setVisibility(VISIBLE);
                mTTSPlayImageView.setVisibility(GONE);

                if (mToolbarClickListener != null) {
                    mToolbarClickListener.onTTSPlayClickListener(mToolbarCallModel);
                }


//                mTTSPauseImageView.setVisibility(VISIBLE);
            });
        }

        if(mTTSPauseImageView != null) {
            mTTSPauseImageView.setOnClickListener(v->{
                mTtsProgress.setVisibility(VISIBLE);
                mTTSPauseImageView.setVisibility(GONE);

                if (mToolbarClickListener != null) {
                    mToolbarClickListener.onTTSStopClickListener(mToolbarCallModel);
                }



//                mTTSPlayImageView.setVisibility(VISIBLE);
            });
        }

        if(premiumLogoBtn != null) {
            premiumLogoBtn.setOnClickListener(v->{
                if(NetUtils.isConnected(getContext())) {
                    IntentUtil.openSubscriptionActivity(getContext(), THPConstants.FROM_SUBSCRIPTION_EXPLORE);
                } else {
                    Alerts.noConnectionSnackBar(v, (AppCompatActivity)getContext());
                }
            });
        }


        setToolbarTitle(mTitle);
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
        if(mTitleTextView != null) {
            mTitleTextView.setText(title);
            mTitleTextView.setVisibility(VISIBLE);
        }
    }




    public void setToolbarTitle(int resId) {
        mTitleTextView.setText(resId);
    }

    @Override
    public void setNavigationIcon(@Nullable Drawable icon) {
        if(icon == null) {
            mBackImageView.setImageDrawable(icon);
            mBackImageView.setVisibility(GONE);
        } else {
            mBackImageView.setImageDrawable(icon);
            mBackImageView.setVisibility(VISIBLE);
        }
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
        if(isLanguageSupportTTS == 0) {
            if(mTTSPlayImageView != null) {
                mTTSPlayImageView.setVisibility(GONE);
                mTTSPauseImageView.setVisibility(GONE);
            }
        } else {
            if (mTTSPauseImageView != null) {
                mTTSPauseImageView.setVisibility(GONE);
                mTTSPlayImageView.setVisibility(VISIBLE);
                mTtsProgress.setVisibility(GONE);
            }
        }
    }

    public void showTTSPauseView(int isLanguageSupportTTS) {
        if(isLanguageSupportTTS == 0) {
            if(mTTSPlayImageView != null) {
                mTTSPlayImageView.setVisibility(GONE);
                mTTSPauseImageView.setVisibility(GONE);
            }
        } else {
            if (mTTSPauseImageView != null) {
                mTTSPauseImageView.setVisibility(VISIBLE);
                mTTSPlayImageView.setVisibility(GONE);
                mTtsProgress.setVisibility(GONE);
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
        if(premiumLogoBtn != null) {
            premiumLogoBtn.setVisibility(GONE);
        }
    }

    public void showCrownBtn() {
            if(premiumLogoBtn != null) {
                premiumLogoBtn.setVisibility(VISIBLE);
            }
        }
}
