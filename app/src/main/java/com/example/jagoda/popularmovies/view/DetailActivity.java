package com.example.jagoda.popularmovies.view;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.jagoda.popularmovies.R;
import com.example.jagoda.popularmovies.model.MoviesSingleton;
import com.example.jagoda.popularmovies.model.Review;
import com.example.jagoda.popularmovies.presenter.DetailPresenter;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {


    public static final String BASE_YOUTUBE_URL = "https://www.youtube.com/watch?v=";

    @BindView(R.id.title_text_view)
    TextView titleTv;
    @BindView(R.id.original_title_text_view)
    TextView originalTitleTv;
    @BindView(R.id.poster_thumbnail_image_view)
    ImageView posterIv;
    @BindView(R.id.rating_bar)
    RatingBar ratingBar;
    @BindView(R.id.detail_view_pager)
    ViewPager detailViewPager;

    private ArrayAdapter<String> adapter;
    private List<String> trailerLabels;
    DetailPagerAdapter pagerAdapter;

    Intent intentThatStartedActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        intentThatStartedActivity = getIntent();

        fillTopDetailLayout();

        String releaseDate = intentThatStartedActivity.getStringExtra(PostersAdapter.KEY_RELEASE_DATE);
        String overview = intentThatStartedActivity.getStringExtra(PostersAdapter.KEY_OVERVIEW);
        int id = intentThatStartedActivity.getIntExtra(PostersAdapter.KEY_ID, -1);

        android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
        pagerAdapter = new DetailPagerAdapter(manager, overview, releaseDate, id);
        detailViewPager.setAdapter(pagerAdapter);


        MoviesSingleton singleton = MoviesSingleton.getInstance(getApplicationContext());
        DetailPresenter presenter = new DetailPresenter(this, singleton);
        //presenter.fetchTrailers(id);

    }

    /*
     * Helper method to fill all Top Layout views
     */
    private void fillTopDetailLayout() {
        String title = intentThatStartedActivity.getStringExtra(PostersAdapter.KEY_TITLE);
        titleTv.setText(title);

        String originalTitle = intentThatStartedActivity.getStringExtra(PostersAdapter.KEY_ORIGINAL_TITLE);
        if( !originalTitle.equals(title) ) {
            originalTitleTv.setText(getString(R.string.original_title_label) + " " + originalTitle);
        }

        String imagePath = intentThatStartedActivity.getStringExtra(PostersAdapter.KEY_IMAGE_PATH);
        String url = PostersAdapter.BASE_IMAGE_URL + imagePath;

        Picasso.with(this)
                .load(url)
                .into(posterIv);

        posterIv.setContentDescription(getString(R.string.content_desc_poster) + title);

        double rating = intentThatStartedActivity.getDoubleExtra(PostersAdapter.KEY_RATING, 0);
        ratingBar.setRating((float) rating);
    }

    public void fillReviewsLayout(List<Review> reviews) {

        ReviewsFragment reviewsFragment = (ReviewsFragment) pagerAdapter.getItem(1);
        reviewsFragment.fillReviewsLayout(reviews);
    }
    /*
     * method to create appropriate number of trailers labels, to call setTrailerLvHeight and
     * to set OnItemClickListener. This is a callback method called by DetailPresenter instance when
     * trailers list is fetched

    public synchronized void createTrailerLv(final List<Video> trailers) {

        int trailerNum = trailers.size();
        if(trailerNum == 0) return;

        List<String> trailerLabels = new ArrayList<>();

        for(int i = 0; i < trailerNum; i++) {
            String label = " Trailer " + String.valueOf(i + 1);
            trailerLabels.add(label);
        }
        adapter = new ArrayAdapter<>(this, R.layout.trailer_list_item, trailerLabels);
        trailerLv.setAdapter(adapter);

        setTrailerLvHeight(trailerNum);

        trailerLv.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Video trailer = trailers.get((int)id);
                String videoKey = trailer.getKey();
                String url = BASE_YOUTUBE_URL + videoKey;
                Intent intentToStartTrailer = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

                if(intentToStartTrailer.resolveActivity(getPackageManager()) != null) {
                    startActivity(intentToStartTrailer);
                }
            }
        });

    }

    /*
     * method to calculate appropriate height for Trailers List View

    private void setTrailerLvHeight(int trailerNum) {

        View listItem = adapter.getView(0, null, trailerLv);
        listItem.measure(0, 0);
        int itemHeight = listItem.getMeasuredHeight();

        ViewGroup.LayoutParams params = trailerLv.getLayoutParams();
        params.height = (itemHeight + 5) * trailerNum;
        trailerLv.setLayoutParams(params);
        trailerLv.requestLayout();
    }
    */



}
