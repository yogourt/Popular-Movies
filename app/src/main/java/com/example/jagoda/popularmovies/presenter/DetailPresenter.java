package com.example.jagoda.popularmovies.presenter;


import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.jagoda.popularmovies.model.MoviesSingleton;
import com.example.jagoda.popularmovies.model.Video;
import com.example.jagoda.popularmovies.model.Review;
import com.example.jagoda.popularmovies.view.DetailActivity;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class DetailPresenter {

    public static final String BASE_MOVIE_URL = "https://api.themoviedb.org/3/movie/";
    public static final String PATH_VIDEOS_API_KEY = "/videos?api_key=";

    public static final String TYPE_TRAILER = "Trailer";

    //Key to access results from JSON String that contains some additional information
    public static final String RESULTS_JSON = "results";

    private DetailActivity activity;
    private MoviesSingleton singleton;

    public DetailPresenter(DetailActivity activity, MoviesSingleton singleton) {
        this.activity = activity;
        this.singleton = singleton;
    }

    public void fetchTrailers(int movieId) {
        String url = BASE_MOVIE_URL + movieId + PATH_VIDEOS_API_KEY + MainPresenter.API_KEY;
        StringRequest request = new StringRequest(Request.Method.GET, url, onTrailersLoaded, onLoadError);
        singleton.addToRequestQueue(request);
    }



    private final Response.Listener<String> onTrailersLoaded = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {

            try {

                JSONObject responseJson = new JSONObject(response);
                String results = responseJson.getString(RESULTS_JSON);

                List<Video> videos = Arrays.asList(new Gson().fromJson(results, Video[].class));
                List<Video> trailers = new ArrayList<>();

                for(Video video: videos) {
                    if(video.getType().equals(TYPE_TRAILER)) {
                        trailers.add(video);
                    }
                }

               // activity.createTrailerLv(trailers);

                Log.i("DetailPresenter", "Trailers fetched");

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };



    private final Response.ErrorListener onLoadError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("DetailPresenter", error.toString());
        }
    };
}
