package me.creativei.gandhi;

import android.app.Activity;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import static me.creativei.gandhi.Utils.LOG_TAG;
import static me.creativei.gandhi.Utils.PREF_MUSIC;

public class BackgroundMusic {
    private final MediaPlayer mediaPlayer;
    private final Utils utils;

    public BackgroundMusic(Activity activity, int resource) {
        mediaPlayer = MediaPlayer.create(activity, resource);
        mediaPlayer.setLooping(true);
        utils = new Utils(activity);
    }

    public void onCreateOptionsMenu(Menu menu) {
        MenuItem musicMenu = menu.findItem(R.id.menu_music);
        musicMenu.setChecked(utils.getPreferenceValue(PREF_MUSIC, true));
    }

    public void onOptionsItemSelected(MenuItem item) {
        Log.d(LOG_TAG, "Music menu clicked");
        if (!item.isChecked()) {
            if (!mediaPlayer.isPlaying())
                mediaPlayer.start();
            utils.savePreference(PREF_MUSIC, true);
            item.setChecked(true);
        } else {
            if (mediaPlayer.isPlaying())
                mediaPlayer.pause();
            utils.savePreference(PREF_MUSIC, false);
            item.setChecked(false);
        }
    }

    public void onResume() {
        boolean playMusic = utils.getPreferenceValue(PREF_MUSIC, true);
        if (playMusic)
            mediaPlayer.start();
    }

    public void onPause() {
        if (mediaPlayer.isPlaying())
            mediaPlayer.pause();
    }

    public void onStop() {
        if (mediaPlayer.isPlaying())
            mediaPlayer.stop();
    }
}
