package me.creativei.gandhi;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class QuotePagerAdapter extends FragmentStatePagerAdapter {
    private final QuoteDataSource quoteDataSource;
    private final boolean favorite;

    public QuotePagerAdapter(FragmentManager fm, QuoteDataSource quoteDataSource, boolean favorite) {
        super(fm);
        this.quoteDataSource = quoteDataSource;
        this.favorite = favorite;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Fragment getItem(int i) {
        Quote quote = quoteDataSource.getQuote(i, favorite);
        int count = quoteDataSource.count(favorite);
        return QuoteFragment.newInstance(quote, i, count);
    }

    @Override
    public int getCount() {
        // To display blank page
        int count = quoteDataSource.count(favorite);
        return Math.max(1, count);
    }
}
