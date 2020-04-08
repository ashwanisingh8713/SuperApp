package com.ns.tts;

public interface TTSCallbacks {

    boolean onTTSInitialized();
    boolean isPlaying();
    void onTTSError(int errorCode);
    void onTTSPlayStarted(int loopCount);

}
