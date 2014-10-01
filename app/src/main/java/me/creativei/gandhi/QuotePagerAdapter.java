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
    public Fragment getItem(int i) {
        Quote quote = quoteDataSource.getQuote(i, favorite);
        int count = quoteDataSource.count(favorite);
        return QuoteFragment.newInstance(quote, count);
    }

    @Override
    public int getCount() {
        return quoteDataSource.count(favorite);
    }
}
