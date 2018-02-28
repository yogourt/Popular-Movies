package com.example.jagoda.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.title_text_view)
    TextView titleTv;
    @BindView(R.id.original_title_text_view)
    TextView originalTitleTv;
    @BindView(R.id.release_date_text_view)
    TextView releaseDateTv;
    @BindView(R.id.overview_text_view)
    TextView overviewTv;
    @BindView(R.id.poster_thumbnail_image_view)
    ImageView posterIv;
    @BindView(R.id.rating_bar)
    RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        Intent intentThatStartedActivity = getIntent();
        String title = intentThatStartedActivity.getStringExtra(PostersAdapter.KEY_TITLE);
        titleTv.setText(title);

        String originalTitle = intentThatStartedActivity.getStringExtra(PostersAdapter.KEY_ORIGINAL_TITLE);
        originalTitleTv.setText(originalTitle);

        String releaseDate = intentThatStartedActivity.getStringExtra(PostersAdapter.KEY_RELEASE_DATE);
        releaseDateTv.setText(releaseDate);

        String overview = intentThatStartedActivity.getStringExtra(PostersAdapter.KEY_OVERVIEW);
        overviewTv.setText(overview);

        String imagePath = intentThatStartedActivity.getStringExtra(PostersAdapter.KEY_IMAGE_PATH);
        String url = PostersAdapter.BASE_IMAGE_URL + imagePath;

        Picasso.with(this)
                .load(url)
                .into(posterIv);

        double rating = intentThatStartedActivity.getDoubleExtra(PostersAdapter.KEY_RATING, 0);
        ratingBar.setRating((float)rating);
    }
}
