package com.example.jagoda.popularmovies.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.example.jagoda.popularmovies.model.Review;

import java.util.List;

import static com.example.jagoda.popularmovies.view.OverviewFragment.KEY_OVERVIEW;
import static com.example.jagoda.popularmovies.view.OverviewFragment.KEY_RELEASE_DATE;
import static com.example.jagoda.popularmovies.view.ReviewsFragment.KEY_MOVIE_ID;


public class DetailPagerAdapter extends FragmentPagerAdapter {

    public static final int TAB_NUM = 2;

    private String overview;
    private String releaseDate;
    private int movieId;

    public DetailPagerAdapter(FragmentManager manager, String overview, String releaseDate, int movieId) {
        super(manager);
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
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return TAB_NUM;
    }
}
