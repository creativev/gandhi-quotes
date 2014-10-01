package me.creativei.gandhi;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Typeface;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import java.lang.reflect.Field;

import static android.content.Context.MODE_PRIVATE;

public class Utils {
    public static String LOG_TAG = "GANDHIQUOTES";
    public static String PREF_MUSIC = "PREF_MUSIC";
    private final Activity activity;
    private Typeface font;

    public Utils(Activity activity) {
        this.activity = activity;
        font = Typeface.createFromAsset(activity.getAssets(), "fonts/segoepr.ttf");
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

    public void initAds() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("7187854231AB5A5B59198755F05B42CA")
                .addTestDevice("D27BE559F36AC73AFA3ED3E64322B072")
                .build();
        AdView adView = (AdView) activity.findViewById(R.id.adView);
        adView.loadAd(adRequest);
    }

    public Tracker initAnalytics() {
        GoogleAnalytics analytics = GoogleAnalytics.getInstance(activity);
        return analytics.newTracker(R.xml.tracker);
    }

    public static int getResource(String resourceId, Class<?> idClass) {
        try {
            Field idField = idClass.getDeclaredField(resourceId);
            return idField.getInt(idField);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public Typeface getQuoteFont() {
        return font;
    }
}
