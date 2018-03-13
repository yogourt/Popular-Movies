package com.example.jagoda.popularmovies.contracts;


import com.example.jagoda.popularmovies.model.Review;

import java.util.List;

public interface ReviewsContract {

    interface View {
        void fillReviewsLv(List<Review> reviews);

    }

    interface Presenter {
        void fetchReviews(int movieId);
    }
}
