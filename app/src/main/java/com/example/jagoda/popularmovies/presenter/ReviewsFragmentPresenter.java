package com.example.jagoda.popularmovies.presenter;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.jagoda.popularmovies.model.MoviesSingleton;
import com.example.jagoda.popularmovies.model.Review;
import com.example.jagoda.popularmovies.view.ReviewsFragment;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class ReviewsFragmentPresenter {

    public static final String PATH_REVIEWS_API_KEY = "/reviews?api_key=";

    MoviesSingleton singleton;
    ReviewsFragment fragment;

    public ReviewsFragmentPresenter (ReviewsFragment fragment, MoviesSingleton singleton) {
        this.fragment = fragment;
        this.singleton = singleton;
    }

    public void fetchReviews(int movieId) {
        String url = DetailPresenter.BASE_MOVIE_URL + movieId + PATH_REVIEWS_API_KEY + MainPresenter.API_KEY;
        StringRequest request = new StringRequest(Request.Method.GET, url, onReviewsLoaded, onLoadError);
        singleton.addToRequestQueue(request);
    }

    private final Response.Listener<String> onReviewsLoaded = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {

            try {

                JSONObject responseJson = new JSONObject(response);
                String results = responseJson.getString(DetailPresenter.RESULTS_JSON);

                List<Review> reviews = Arrays.asList(new Gson().fromJson(results, Review[].class));

                fragment.fillReviewsLayout(reviews);

                Log.i("ReviewsFragPresenter", "Reviews fetched");

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };

    private final Response.ErrorListener onLoadError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("ReviewsFragPresenter", error.toString());
        }
    };
}
