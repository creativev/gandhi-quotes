package me.creativei.gandhi;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.analytics.Tracker;

import org.apache.http.protocol.HTTP;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, QuoteDataSource {

    private BackgroundMusic backgroundMusic;
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private QuoteDBDataSource dataSource;

    private CharSequence mTitle;
    private Utils utils;
    private Tracker tracker;
    private Menu menu;
    private Quote currentQuote;
    private DataSetChangedListener dataSetChangedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        QuotesDbHelper quotesDb = new QuotesDbHelper(this);
        dataSource = new QuoteDBDataSource(quotesDb);
        dataSource.open();


        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        utils = new Utils(this);
        backgroundMusic = utils.createMediaPlayer();
        utils.initAds();
        tracker = utils.initAnalytics();
    }

    @Override
    protected void onStart() {
        super.onStart();
        dataSource.open();
    }

    @Override
    protected void onResume() {
        super.onResume();
        dataSource.open();
        backgroundMusic.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        dataSource.close();
        backgroundMusic.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        backgroundMusic.onStop();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        switch (position) {
            case 0:
            case 1:
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_pager, PagerFragment.newInstance(position))
                        .commit();
                break;
            case 2:
            case 3:
            case 4:
            case 5:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                String[] packageName = new String[]{"me.creativei.quotes", "me.creativei.dussera", "me.creativei.everyday", "me.creativei.diwali"};
                intent.setData(Uri.parse("market://details?id=" + packageName[position - 2]));
                startActivity(intent);
                break;
            case 6:
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://search?q=pub:creativei.me"));
                startActivity(intent);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            getMenuInflater().inflate(R.menu.main, menu);
            this.menu = menu;
            backgroundMusic.onCreateOptionsMenu(menu);
            utils.syncScreenWakeMenu(menu.findItem(R.id.menu_screen_awake), utils.getPreferenceValue(Utils.PREF_KEEP_SCREEN_ON, false));
            syncMenuItems();
            restoreActionBar();
            return true;
        }
        this.menu = null;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_screen_awake:
                utils.screenWakeMenuClicked(item);
                return true;
            case R.id.menu_favorite:
                setAsFavorite(!item.isChecked());
                return true;
            case R.id.menu_share:
                shareCurrentQuote();
                return true;
            case R.id.menu_music:
                backgroundMusic.onOptionsItemSelected(item);
                return true;
            case R.id.menu_about:
                utils.aboutDialog();
                return true;
            case R.id.menu_help:
                utils.showHelpOverlay();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Quote getQuote(int offset, boolean favorite) {
        return dataSource.getQuote(offset, favorite);
    }

    @Override
    public int count(boolean favorite) {
        return dataSource.count(favorite);
    }

    public Typeface getQuoteFont() {
        return utils.getQuoteFont();
    }

    public void setAsFavorite(boolean state) {
        dataSource.setFavorite(currentQuote._id, state);
        int resId = state ? R.string.quote_favorited : R.string.quote_unfavorited;
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
        dataSetChangedListener.notifyDataSetChanged();
    }

    public void shareCurrentQuote() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType(HTTP.PLAIN_TEXT_TYPE);
        intent.putExtra(Intent.EXTRA_TEXT, currentQuote.content + " - Mahatma Gandhi");
        startActivity(Intent.createChooser(intent, "Share"));
    }

    public void onQuoteSelected(int position, boolean favorite) {
        currentQuote = dataSource.getQuote(position, favorite);
        syncMenuItems();
    }

    private void syncMenuItems() {
        if (menu == null) return;
        MenuItem favorite = menu.findItem(R.id.menu_favorite);
        MenuItem share = menu.findItem(R.id.menu_share);

        if (currentQuote == null) {
            favorite.setVisible(false);
            share.setVisible(false);
        } else {
            favorite.setVisible(true);
            favorite.setChecked(currentQuote.favorite);
            favorite.setIcon(currentQuote.favorite ? android.R.drawable.btn_star_big_on : android.R.drawable.btn_star_big_off);
            share.setVisible(true);
        }
    }

    public void setDataSetChangedListener(DataSetChangedListener dataSetChangedListener) {
        this.dataSetChangedListener = dataSetChangedListener;
    }

}
