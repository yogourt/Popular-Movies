package com.example.jagoda.popularmovies.view.detail;

import android.content.Intent;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.jagoda.popularmovies.R;
import com.example.jagoda.popularmovies.model.data.DatabaseSingleton;
import com.example.jagoda.popularmovies.presenter.detail.DetailPresenter;
import com.example.jagoda.popularmovies.view.main.PostersAdapter;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {


    @BindView(R.id.title_text_view)
    TextView titleTv;
    @BindView(R.id.thumbnail_image_view)
    ImageView thumbnailIv;
    @BindView(R.id.rating_bar)
    RatingBar ratingBar;
    @BindView(R.id.fav_button)
    ImageView favButton;
    @BindView(R.id.detail_view_pager)
    ViewPager detailViewPager;
    @BindView(R.id.detail_tab_layout)
    TabLayout detailTabs;
    @BindView(R.id.detail_coordinator_layout)
    CoordinatorLayout detailCoordinatorLayout;

    public static final String BASE_BACKDROP_IMAGE_URL = "http://image.tmdb.org/t/p/w342/";

    private Intent intentThatStartedActivity;

    private DetailPagerAdapter pagerAdapter;
    private DetailPresenter presenter;


    // movie Id in theMovieDb.org database
    private int id;
    private String title;


     // this value helps in changing fav button state as comparing drawables is impossible,
     // so present drawable's id is saved in this field.
    private int favImageId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        intentThatStartedActivity = getIntent();

        fillTopDetailLayout();

        String releaseDate = intentThatStartedActivity.getStringExtra(PostersAdapter.KEY_RELEASE_DATE);
        String overview = intentThatStartedActivity.getStringExtra(PostersAdapter.KEY_OVERVIEW);
        id = intentThatStartedActivity.getIntExtra(PostersAdapter.KEY_ID, -1);

        android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
        pagerAdapter = new DetailPagerAdapter(this, manager, overview, releaseDate, id);
        detailViewPager.setAdapter(pagerAdapter);
        detailTabs.setupWithViewPager(detailViewPager);


        DatabaseSingleton writerSingleton = DatabaseSingleton.getInstance(getApplicationContext());
        presenter = new DetailPresenter(writerSingleton, id);

        setFavButtonState();

    }

    /*
     * Helper method to fill all Top Layout views
     */
    private void fillTopDetailLayout() {

        String url;
        if(!getResources().getBoolean(R.bool.is_landscape)) {
            String backdropPath = intentThatStartedActivity.getStringExtra(PostersAdapter.KEY_BACKDROP_PATH);
            url = BASE_BACKDROP_IMAGE_URL + backdropPath;

        } else {
            String imagePath = intentThatStartedActivity.getStringExtra(PostersAdapter.KEY_IMAGE_PATH);
            url = PostersAdapter.BASE_IMAGE_URL + imagePath;

        }

        Picasso.with(this)
                .load(url)
                .into(thumbnailIv);

        thumbnailIv.setAlpha(0.35f);
        thumbnailIv.setContentDescription(getString(R.string.content_desc_poster) + title);

        title = intentThatStartedActivity.getStringExtra(PostersAdapter.KEY_TITLE);
        titleTv.setText(title);

        double rating = intentThatStartedActivity.getDoubleExtra(PostersAdapter.KEY_RATING, 0);
        ratingBar.setRating((float) rating);

    }

    // onClick method for "add to favourites" heart-shape button
    public void changeFavButtonState(View view) {

        if(favImageId == 0) favImageId = R.drawable.ic_favorite_border;

        // if movie was added to favourites
        if(favImageId == R.drawable.ic_favorite_border) {
            favButton.setImageResource(R.drawable.ic_favorite);
            favButton.setContentDescription(getString(R.string.unfav_button_content_desc));
            favImageId = R.drawable.ic_favorite;
            presenter.addMovieToFavourites(title);
            Snackbar.make(detailCoordinatorLayout, getString(R.string.snackbar_fav),
                    BaseTransientBottomBar.LENGTH_SHORT).show();
        }
        // else movie was deleted from favourites
        else {
            favButton.setImageResource(R.drawable.ic_favorite_border);
            favButton.setContentDescription(getString(R.string.fav_button_content_desc));
            favImageId = R.drawable.ic_favorite_border;
            presenter.deleteMovieFromFavourites();
            Snackbar.make(detailCoordinatorLayout, getString(R.string.snackbar_unfav),
                    BaseTransientBottomBar.LENGTH_SHORT).show();

        }

    }

    // helper method to set initial value of fav button when the activity is created
    private void setFavButtonState() {

        // check if movie is in favourites already
        if(presenter.isPresentInFavourites()) {
            favButton.setImageResource(R.drawable.ic_favorite);
            favImageId = R.drawable.ic_favorite;
        } else {
            favButton.setImageResource(R.drawable.ic_favorite_border);
            favImageId = R.drawable.ic_favorite_border;
        }
    }

}
