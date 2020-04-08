package com.ns.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.ns.alerts.Alerts;
import com.ns.thpremium.R;
import com.ns.utils.THPFirebaseAnalytics;

/**
 * Created by ashwani on 16/10/16.
 */

public class THP_YouTubeFullScreenActivity extends YouTubeBaseActivity
        implements YouTubePlayer.OnFullscreenListener, YouTubePlayer.OnInitializedListener {

    private static final int RECOVERY_DIALOG_REQUEST = 1;

    private YouTubePlayerView playerView;
    private String mVideoId;

    private int mVideoViewLayoutWidth = 0;
    private int mVideoViewLayoutHeight = 0;


    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thp_activity_youttubefullscreen);

        playerView = findViewById(R.id.youTubePlayer);
        playerView.setVisibility(View.VISIBLE);
        mVideoId = getIntent().getStringExtra("videoId");
        playerView.initialize(getResources().getString(R.string.youtube_api_key), this);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
//        player.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
        player.setOnFullscreenListener(this);
        if (!wasRestored) {
            player.cueVideo(mVideoId);
        }
        // player.setFullscreen(true);
        // player.setShowFullscreenButton(false);
    }


    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
            Alerts.showToast(this, "There was an error initializing the YouTubePlayer " + errorReason.toString());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            playerView.initialize("AIzaSyBENdg9kEoEsb8WrOWIUk7cS0HPgU0BztQ", this);
        }
    }




    private Activity activity;


    public void setFullscreen(boolean fullscreen, int screenOrientation) {
        if (activity == null) {
            activity = this;
        }

        if (fullscreen) {
            if (mVideoViewLayoutWidth == 0 && mVideoViewLayoutHeight == 0) {
                ViewGroup.LayoutParams params = playerView.getLayoutParams();
                mVideoViewLayoutWidth = params.width;
                mVideoViewLayoutHeight = params.height;
            }
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            activity.setRequestedOrientation(screenOrientation);
        } else {
            ViewGroup.LayoutParams params = playerView.getLayoutParams();
            params.width = mVideoViewLayoutWidth;
            params.height = mVideoViewLayoutHeight;
            playerView.setLayoutParams(params);

            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            activity.setRequestedOrientation(screenOrientation);
        }
        onScaleChange(fullscreen);
    }

    public void onScaleChange(boolean isFullscreen) {
        if (activity == null) {
            activity = this;
        }

        if (isFullscreen) {
            ViewGroup.LayoutParams layoutParams = playerView.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            playerView.setLayoutParams(layoutParams);
        } else {
            ViewGroup.LayoutParams layoutParams = playerView.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            playerView.setLayoutParams(layoutParams);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        THPFirebaseAnalytics.setFirbaseAnalyticsScreenRecord(this, "THP_YouTubeFullScreenActivity Screen", THP_YouTubeFullScreenActivity.class.getSimpleName());
    }

    @Override
    public void onFullscreen(boolean b) {

    }
}
