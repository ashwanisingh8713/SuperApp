package com.ns.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exo.ui.PlayerView;
import com.ns.thpremium.R;
import com.ns.utils.THPFirebaseAnalytics;

/**
 * Created by ashwani on 16/10/16.
 */

public class VideoPlayerActivity extends AppCompatActivity {

    private PlayerView playerView;
    private PlayerManager player;
    private String mVideoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jwplayer);

        mVideoUrl = getIntent().getStringExtra("videoId");

        playerView = findViewById(R.id.player_view);

        player = new PlayerManager(this, mVideoUrl);


    }


    @Override
    public void onResume() {
        super.onResume();
        player.init(this, playerView);
        //Firebase Analytics
        THPFirebaseAnalytics.setFirbaseAnalyticsScreenRecord(VideoPlayerActivity.this, "Video Player Screen", VideoPlayerActivity.class.getSimpleName());
    }

    @Override
    public void onPause() {
        super.onPause();
        player.reset();
    }

    @Override
    public void onDestroy() {
        player.release();
        super.onDestroy();
    }



}
