package com.ns.utils;

import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import com.google.android.exo.DefaultLoadControl;
import com.google.android.exo.DefaultRenderersFactory;
import com.google.android.exo.ExoPlaybackException;
import com.google.android.exo.ExoPlayer;
import com.google.android.exo.ExoPlayerFactory;
import com.google.android.exo.LoadControl;
import com.google.android.exo.PlaybackParameters;
import com.google.android.exo.Player;
import com.google.android.exo.RenderersFactory;
import com.google.android.exo.SimpleExoPlayer;
import com.google.android.exo.Timeline;
import com.google.android.exo.extractor.DefaultExtractorsFactory;
import com.google.android.exo.extractor.ExtractorsFactory;
import com.google.android.exo.source.ExtractorMediaSource;
import com.google.android.exo.source.MediaSource;
import com.google.android.exo.source.TrackGroupArray;
import com.google.android.exo.trackselection.AdaptiveTrackSelection;
import com.google.android.exo.trackselection.DefaultTrackSelector;
import com.google.android.exo.trackselection.TrackSelection;
import com.google.android.exo.trackselection.TrackSelectionArray;
import com.google.android.exo.trackselection.TrackSelector;
import com.google.android.exo.upstream.BandwidthMeter;
import com.google.android.exo.upstream.DataSource;
import com.google.android.exo.upstream.DefaultBandwidthMeter;
import com.google.android.exo.upstream.DefaultDataSourceFactory;
import com.google.android.exo.util.Util;
import com.main.SuperApp;


public class AppAudioManager implements ExoPlayer.EventListener {

    private Handler mainHandler;
    private RenderersFactory renderersFactory;
    private BandwidthMeter bandwidthMeter;
    private LoadControl loadControl;
    private DataSource.Factory dataSourceFactory;
    private ExtractorsFactory extractorsFactory;
    private MediaSource mediaSource;
    private TrackSelection.Factory trackSelectionFactory;
    private SimpleExoPlayer player;
    private final String streamUrl = "http://bbcwssc.ic.llnwd.net/stream/bbcwssc_mp1_ws-einws"; //bbc world service url
    private TrackSelector trackSelector;

    private static AppAudioManager sAppAudioManager;

    public static AppAudioManager getInstance() {
        if(sAppAudioManager == null) {
            sAppAudioManager = new AppAudioManager();
        }
        return sAppAudioManager;
    }


    private AppAudioManager() {
        renderersFactory = new DefaultRenderersFactory(SuperApp.getAppContext());
        bandwidthMeter = new DefaultBandwidthMeter();
        trackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        trackSelector = new DefaultTrackSelector(trackSelectionFactory);
        loadControl = new DefaultLoadControl();

        player = ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector, loadControl);
        player.addListener(this);

        dataSourceFactory = new DefaultDataSourceFactory(SuperApp.getAppContext(), "ExoplayerDemo");
        extractorsFactory = new DefaultExtractorsFactory();
        mainHandler = new Handler();

    }


    private void releaseExoPlayer() {
        if(player != null) {
            player.release();
        }
    }

    public void releaseMedia() {
        mediaSource.releaseSource();
    }

    public void changeMediaFile(String medialFile) {
        if(isPlaying()) {
            pausePlayer();
            releaseMedia();
        }
        mediaSource = new ExtractorMediaSource(Uri.parse(medialFile),
                dataSourceFactory,
                extractorsFactory,
                mainHandler,
                null);

        player.prepare(mediaSource);
        startPlayer();
    }

    public boolean isPlaying() {
        if(player != null) {
            return player.getPlayWhenReady();
        }
        return false;
    }


    public void pausePlayer(){
        player.setPlayWhenReady(false);
    }
    public void startPlayer(){
        player.setPlayWhenReady(true);
    }

    public void audioToggle() {
        if(player != null && mediaSource != null) {
            player.setPlayWhenReady(!player.getPlayWhenReady());
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {
        Log.i("", "");
    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
        Log.i("", "");
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
        Log.i("", "");
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        Log.i("", "");
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {
        Log.i("", "");
    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
        Log.i("", "");
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        Log.i("", "");
    }

    @Override
    public void onPositionDiscontinuity(int reason) {
        Log.i("", "");
    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
        Log.i("", "");
    }

    @Override
    public void onSeekProcessed() {
        Log.i("", "");
    }
}
