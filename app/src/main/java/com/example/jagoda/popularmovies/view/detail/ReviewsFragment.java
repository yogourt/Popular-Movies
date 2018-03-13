package com.example.jagoda.popularmovies.view.detail;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.jagoda.popularmovies.R;
import com.example.jagoda.popularmovies.contracts.ReviewsContract;
import com.example.jagoda.popularmovies.model.MoviesSingleton;
import com.example.jagoda.popularmovies.model.Review;
import com.example.jagoda.popularmovies.presenter.detail.ReviewsFragmentPresenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewsFragment extends Fragment implements ReviewsContract.View{

    public static final String KEY_MOVIE_ID = "movieId";

    ReviewsContract.Presenter presenter;

    @BindView(R.id.reviews_list_view)
    ListView reviewsLv;

    private Context context;

    public ReviewsFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MoviesSingleton singleton = MoviesSingleton.getInstance(getActivity().getApplicationContext());
        presenter = new ReviewsFragmentPresenter(this, singleton);
        context = getContext();
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

    /*
     * Method to create reviews list, that contain review content and author.
     * This is a callback method called by Reviews Fragment Presenter when reviews list is fetched.
     */
    @Override
    public void fillReviewsLv(List<Review> reviews) {

        if(reviews.size() == 0) return;

        String KEY_AUTHOR = "author";
        String KEY_CONTENT = "content";
        List<Map<String, String>> data = new ArrayList<>();

        for(Review review: reviews) {
            Map<String,String> reviewData = new ArrayMap<>();
            reviewData.put(KEY_AUTHOR, "~" + review.getAuthor());
            reviewData.put(KEY_CONTENT, review.getContent());
            data.add(reviewData);
        }
        String[] from = {KEY_AUTHOR, KEY_CONTENT};
        int[] to = {R.id.author_text_view, R.id.content_text_view};

        SimpleAdapter adapter = new SimpleAdapter(context, data,
                R.layout.review_list_item, from, to);
        reviewsLv.setAdapter(adapter);

    }

}
