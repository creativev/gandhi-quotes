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
    private MainActivity activity;

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
        ViewPager view = (ViewPager) inflater.inflate(R.layout.fragment_pager, container, false);
        view.setAdapter(new QuotePagerAdapter(activity.getSupportFragmentManager(), activity, getArguments().getBoolean(FAVORITE_QUOTES)));
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = ((MainActivity) activity);
    }
}
