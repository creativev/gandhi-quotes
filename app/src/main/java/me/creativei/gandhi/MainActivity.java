package me.creativei.gandhi;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.analytics.Tracker;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, QuoteDataSource {

    private BackgroundMusic backgroundMusic;
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private QuoteDBDataSource dataSource;

    private CharSequence mTitle;
    private Utils utils;
    private Tracker tracker;

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
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_pager, PagerFragment.newInstance(position))
                .commit();
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

            backgroundMusic.onCreateOptionsMenu(menu);

            restoreActionBar();
            return true;
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_settings:
                return true;
            case R.id.menu_music:
                backgroundMusic.onOptionsItemSelected(item);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Quote getQuote(int offset, boolean favorite) {
        return dataSource.getQuote(offset, favorite);
    }

    @Override
    public void setFavorite(int id, boolean favorite) {
        dataSource.setFavorite(id, favorite);
    }

    @Override
    public int count(boolean favorite) {
        return dataSource.count(favorite);
    }

    public Typeface getQuoteFont() {
        return utils.getQuoteFont();
    }
}
