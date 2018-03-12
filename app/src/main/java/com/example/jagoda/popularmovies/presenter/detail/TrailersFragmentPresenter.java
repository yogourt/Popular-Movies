package com.example.jagoda.popularmovies.presenter.detail;


import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.jagoda.popularmovies.model.MoviesSingleton;
import com.example.jagoda.popularmovies.model.Video;
import com.example.jagoda.popularmovies.presenter.main.MainPresenter;
import com.example.jagoda.popularmovies.view.detail.TrailersFragment;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.jagoda.popularmovies.presenter.detail.DetailPresenter.RESULTS_JSON;

/*
 * Presenter for Trailers Fragment
 */
public class TrailersFragmentPresenter {


    public static final String PATH_VIDEOS_API_KEY = "/videos?api_key=";

    // this key is used to differentiate trailers from all videos taken from theMovieDb.org database
    // in onTrailersLoaded() method
    public static final String TYPE_TRAILER = "Trailer";

    private MoviesSingleton singleton;
    private TrailersFragment fragment;

    public TrailersFragmentPresenter(TrailersFragment fragment, MoviesSingleton singleton) {

        this.fragment = fragment;
        this.singleton = singleton;
    }

    /*
     * Method to fetch trailers from theMovieDb.org database. After that they are passed to Trailers
     * Fragment in a callback
     */
    public void fetchTrailers(int movieId) {
        String url = DetailPresenter.BASE_MOVIE_URL + movieId + PATH_VIDEOS_API_KEY + MainPresenter.API_KEY;
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

                fragment.fillTrailerLv(trailers);

                Log.i("TrailersFrgPresenter", "Trailers fetched");

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };



    private final Response.ErrorListener onLoadError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("TrailersFrgPresenter", error.toString());
        }
    };

}
