package com.kiwitomatostudio.anisearch.manager;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

public class AppSettingsManager {
    private static final String KEY_VIDEO_ENABLED = "video_enabled";
    private static final String KEY_MUTE_ENABLED = "mute_enabled";

    private static AppSettingsManager m_instance;
    private final SharedPreferences m_preferences;

    private AppSettingsManager(Context context) {
        m_preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
    }

    public static synchronized AppSettingsManager getInstance(Context context) {
        if (m_instance == null) {
            m_instance = new AppSettingsManager(context);
        }
        return m_instance;
    }


    // 是否啟用影片取代縮圖
    public void setVideoEnabled(boolean isEnabled) {
        SharedPreferences.Editor editor = m_preferences.edit();
        editor.putBoolean(KEY_VIDEO_ENABLED, isEnabled);
        editor.apply();
    }

    public boolean getVideoEnabled() {
        return m_preferences.getBoolean(KEY_VIDEO_ENABLED, false);
    }

    public void setMuteEnabled(boolean isEnabled) {
        SharedPreferences.Editor editor = m_preferences.edit();
        editor.putBoolean(KEY_MUTE_ENABLED, isEnabled);
        editor.apply();
    }

    public boolean getMuteEnabled() {
        return m_preferences.getBoolean(KEY_MUTE_ENABLED, false);
    }
}


