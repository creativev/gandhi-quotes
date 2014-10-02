package me.creativei.gandhi;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PagerFragment extends Fragment {
    private static final String FAVORITE_QUOTES = "FAVORITE_QUOTES";
    public static final String LAST_VISITED_PAGE = "LAST_VISITED_PAGE";
    private MainActivity activity;
    private ViewPager viewPager;
    private boolean themeIsFavorite;
    private QuotePagerAdapter pagerAdapter;

    public PagerFragment() {
    }

    public static PagerFragment newInstance(int sectionNumber) {
        switch (sectionNumber) {
            case 0:
            case 1:
                PagerFragment fragment = new PagerFragment();
                Bundle args = new Bundle();
                args.putBoolean(FAVORITE_QUOTES, sectionNumber == 1);
                fragment.setArguments(args);
                return fragment;
            default:
                throw new RuntimeException("Not implemented");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        themeIsFavorite = getArguments().getBoolean(FAVORITE_QUOTES);
        viewPager = (ViewPager) inflater.inflate(R.layout.fragment_pager, container, false);
        pagerAdapter = new QuotePagerAdapter(activity.getSupportFragmentManager(), activity, themeIsFavorite);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                if (positionOffset != 0) return;
                activity.onQuoteSelected(position, themeIsFavorite);

                if (!themeIsFavorite)
                    new Utils(activity).savePreference(LAST_VISITED_PAGE, position);
            }
        });
        if (!themeIsFavorite)
            viewPager.setCurrentItem(new Utils(activity).getPreferenceValue(LAST_VISITED_PAGE, 0), false);
        activity.setDataSetChangedListener(new DataSetChangedListener() {
            @Override
            public void notifyDataSetChanged() {
                pagerAdapter.notifyDataSetChanged();
            }
        });
        return viewPager;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = ((MainActivity) activity);
    }
}
