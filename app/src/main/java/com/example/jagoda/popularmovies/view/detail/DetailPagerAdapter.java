package com.example.jagoda.popularmovies.view.detail;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.jagoda.popularmovies.R;

import static com.example.jagoda.popularmovies.view.detail.OverviewFragment.KEY_OVERVIEW;
import static com.example.jagoda.popularmovies.view.detail.OverviewFragment.KEY_RELEASE_DATE;
import static com.example.jagoda.popularmovies.view.detail.ReviewsFragment.KEY_MOVIE_ID;


public class DetailPagerAdapter extends FragmentPagerAdapter {

    public static final int TAB_NUM = 3;

    private String overview;
    private String releaseDate;
    private int movieId;
    private Context context;

    public DetailPagerAdapter(Context context, FragmentManager manager, String overview, String releaseDate, int movieId) {
        super(manager);
        this.context = context;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.movieId = movieId;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                Fragment overviewFragment = new OverviewFragment();
                Bundle overviewBundle = new Bundle();
                overviewBundle.putString(KEY_OVERVIEW, overview);
                overviewBundle.putString(KEY_RELEASE_DATE, releaseDate);
                overviewFragment.setArguments(overviewBundle);
                return overviewFragment;
            case 1:
                ReviewsFragment reviewsFragment = new ReviewsFragment();
                Bundle reviewsBundle = new Bundle();
                reviewsBundle.putInt(KEY_MOVIE_ID, movieId);
                reviewsFragment.setArguments(reviewsBundle);
                return reviewsFragment;
            case 2:
                TrailersFragment trailersFragment = new TrailersFragment();
                Bundle trailersBundle = new Bundle();
                trailersBundle.putInt(TrailersFragment.KEY_MOVIE_ID, movieId);
                trailersFragment.setArguments(trailersBundle);
                return trailersFragment;
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return context.getString(R.string.tab_overview_label);
            case 1:
                return context.getString(R.string.tab_reviews_label);
            case 2:
                return context.getString(R.string.tab_trailers_label);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return TAB_NUM;
    }
}
