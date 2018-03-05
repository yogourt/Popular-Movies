package com.example.jagoda.popularmovies.presenter;


import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.jagoda.popularmovies.model.Movie;
import com.example.jagoda.popularmovies.model.MoviesRepository;
import com.example.jagoda.popularmovies.view.MainActivity;
import com.example.jagoda.popularmovies.view.PostersAdapter;
import com.google.gson.Gson;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import static com.example.jagoda.popularmovies.view.MainActivity.ORDER_POPULAR;


public class MainPresenter {

    private static final String API_KEY = "YOUR_API_KEY";

    //Key to access results from JSON String that contains some additional information
    public static final String RESULTS_JSON = "results";

    private PostersAdapter adapter;
    private MoviesRepository repository;


    public MainPresenter(PostersAdapter adapter, MoviesRepository repository) {
        this.adapter = adapter;
        this.repository = repository;

    }


    public void setMoviesToAdapter(int sortOrder) {

        List<Movie> movies = repository.getMovies(sortOrder);
        Log.d("MainPresenter", "get movies from repo");
        if(movies != null) {
            adapter.setMovies(movies);

            Log.d("MainPresenter", "movies not null");
        } else {
            fetchMoviesAndSetToAdapter(sortOrder);

            Log.d("MainPresenter", "movies null");
        }
    }

    private void setMoviesToAdapter(List<Movie> movies) {
        adapter.setMovies(movies);
    }

    private void fetchMoviesAndSetToAdapter(int sortOrder) {
        String url;
        if (sortOrder == ORDER_POPULAR) {
            url = "https://api.themoviedb.org/3/movie/popular?api_key=" + API_KEY;
            StringRequest request = new StringRequest(Request.Method.GET, url,
                    onPopularMoviesLoaded, onLoadError);
            repository.addToRequestQueue(request);
        } else {
            url = "https://api.themoviedb.org/3/movie/top_rated?api_key=" + API_KEY;
            StringRequest request = new StringRequest(Request.Method.GET, url,
                    onTopRatedMoviesLoaded, onLoadError);
            repository.addToRequestQueue(request);
        }
    }

    private final Response.Listener<String> onPopularMoviesLoaded = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {

            /*
            After loading json, the response is parsed to JSON Object to extract results.
            Then it's parsed to Movies List using Gson library
             */
            try {
                JSONObject responseJson = new JSONObject(response);
                String results = responseJson.getString(RESULTS_JSON);

                List<Movie> moviesOrderPopular = Arrays.asList(new Gson().fromJson(results, Movie[].class));
                setMoviesToAdapter(moviesOrderPopular);
                repository.setMovies(moviesOrderPopular, MainActivity.ORDER_POPULAR);

                Log.i("MainPresenter", "Movies list created");

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };

    private final Response.Listener<String> onTopRatedMoviesLoaded = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {

            /*
            After loading json, the response is parsed to JSON Object to extract results.
            Then it's parsed to Movies List using Gson library
             */
            try {
                JSONObject responseJson = new JSONObject(response);
                String results = responseJson.getString(RESULTS_JSON);

                List<Movie> moviesOrderTopRated = Arrays.asList(new Gson().fromJson(results, Movie[].class));
                setMoviesToAdapter(moviesOrderTopRated);
                repository.setMovies(moviesOrderTopRated, MainActivity.ORDER_TOP_RATED);

                Log.i("MainPresenter", "Movies list created");

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };


    private final Response.ErrorListener onLoadError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("MainPresenter", error.toString());
            Log.d("MainPresenter", "Network Request error");
        }
    };
}


