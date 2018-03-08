package com.example.jagoda.popularmovies.view;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.TextViewCompat;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jagoda.popularmovies.R;
import com.example.jagoda.popularmovies.model.MoviesSingleton;
import com.example.jagoda.popularmovies.model.Review;
import com.example.jagoda.popularmovies.presenter.ReviewsFragmentPresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewsFragment extends Fragment {

    public static final String KEY_MOVIE_ID = "movieId";

    ReviewsFragmentPresenter presenter;

    @BindView(R.id.reviews_linear_layout)
    LinearLayout reviewsLayout;

    public ReviewsFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MoviesSingleton singleton = MoviesSingleton.getInstance(getActivity().getApplicationContext());
        presenter = new ReviewsFragmentPresenter(this, singleton);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reviews, container, false);
        ButterKnife.bind(this, view);Bundle bundle = getArguments();

        if(bundle != null) {
            int movieId = bundle.getInt(KEY_MOVIE_ID);
            presenter.fetchReviews(movieId);
        }
        return view;
    }

    public synchronized void fillReviewsLayout(List<Review> reviews) {

        if(reviews.size() == 0) return;

        for(Review review: reviews) {

            TextView reviewTv = new TextView(this.getContext());
            reviewTv.setText(review.getContent());
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                reviewTv.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
            }
            TextViewCompat.setTextAppearance(reviewTv, R.style.DetailBottomTextStyle);
            reviewsLayout.addView(reviewTv);
            reviewsLayout.requestLayout();
        }

    }

}
