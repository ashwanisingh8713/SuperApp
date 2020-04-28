package com.ns.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exo.ui.PlayerView;
import com.ns.thpremium.R;

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
