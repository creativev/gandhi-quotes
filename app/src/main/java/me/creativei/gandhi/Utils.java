package me.creativei.gandhi;

import android.app.Activity;
import android.content.SharedPreferences;
import android.media.MediaPlayer;

import static android.content.Context.MODE_PRIVATE;

public class Utils {
    public static String LOG_TAG = "GANDHIQUOTES";
    public static String PREF_MUSIC = "PREF_MUSIC";
    private final Activity activity;

    public Utils(Activity activity) {
        this.activity = activity;
    }

    public boolean getPreferenceValue(String key, boolean defaultValue) {
        SharedPreferences preferences = activity.getPreferences(MODE_PRIVATE);
        return preferences.getBoolean(key, defaultValue);
    }

    public BackgroundMusic createMediaPlayer() {
        return new BackgroundMusic(activity, R.raw.raghupati);
    }

    public void savePreference(String key, boolean value) {
        SharedPreferences preferences = activity.getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }
}
