package com.ns.tts;

import android.content.Context;
import android.content.Intent;

/**
 * Created by ashwanisingh on 13/10/17.
 */

public class TTSUtil {


    public static void openTTSSettingScreen(Context context) {
        Intent intent = new Intent();
        intent.setAction("com.android.settings.TTS_SETTINGS");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void openTTSAppSettingScreen(Context context) {
        Intent intent = new Intent(context, TTSSettingActivity.class);
        context.startActivity(intent);
    }

    public static void openTTSAppLanguageSettingScreen(Context context) {
        Intent intent = new Intent(context, TTSLanguageSettingActivity.class);
        context.startActivity(intent);
    }





}
