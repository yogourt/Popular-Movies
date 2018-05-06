package com.example.jagoda.popularmovies.presenter.main;


import android.database.Cursor;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.jagoda.popularmovies.contracts.MainContract;
import com.example.jagoda.popularmovies.model.Movie;
import com.example.jagoda.popularmovies.model.MoviesSingleton;
import com.example.jagoda.popularmovies.model.data.DatabaseSingleton;
import com.example.jagoda.popularmovies.presenter.detail.DetailPresenter;
import com.example.jagoda.popularmovies.view.main.MainActivity;
import com.example.jagoda.popularmovies.view.main.PostersAdapter;
import com.google.gson.Gson;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import static com.example.jagoda.popularmovies.view.main.MainActivity.ORDER_FAVOURITES;
import static com.example.jagoda.popularmovies.view.main.MainActivity.ORDER_POPULAR;
import static com.example.jagoda.popularmovies.view.main.MainActivity.ORDER_TOP_RATED;

/*
 * Presenter for Main Activity
 */
public class MainPresenter implements MainContract.Presenter {

    public static final String API_KEY = "YOUR_API_KEY";

    // Key to access results from JSON String that contains some additional information
    public static final String RESULTS_JSON = "results";

    private MainContract.View activity;
    private MainContract.Adapter adapter;
    private MoviesSingleton moviesSingleton;
    private DatabaseSingleton databaseSingleton;


    public MainPresenter(MainActivity activity, PostersAdapter adapter, MoviesSingleton moviesSingleton,
                         DatabaseSingleton databaseSingleton) {
        this.activity = activity;
        this.adapter = adapter;
        this.moviesSingleton = moviesSingleton;
        this.databaseSingleton = databaseSingleton;

    }


    /*
     * Method that sets list of movies to posters adapter according to sort order chosen by user.
     * If sort order is favourites then method uses app's SQLite database and connects to it
     * by databaseSingleton.
     * If sort order is popular/top rated then method is trying to get movies list cached in
     * moviesSingleton. It it's the first time method is called in the app lifetime then
     * there is no movies cached and fetchMoviesAndSetToAdapter() is called where
     * the network request is made.
     */
    @Override
    public void setMoviesToAdapter(int sortOrder) {

        if(sortOrder == ORDER_FAVOURITES) {
            adapter.setMovies(null);
            Cursor cursor = databaseSingleton.fetchAllFavourites();

            if(cursor == null || cursor.getCount() == 0) {
                activity.showMessageNoFavMovies();
                return;
            }

            // for every movie in cursor add it to posters adapter in method fetchMovie which
            // is making network request to theMovieDb.org database
            for(int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                fetchMovie(cursor.getInt(0));
            }

            return;
        }

        List<Movie> movies = moviesSingleton.getMovies(sortOrder);
        Log.d("MainPresenter", "get movies from repo");
        if(movies != null) {
            adapter.setMovies(movies);

            Log.d("MainPresenter", "movies in repo not null");
        } else {
            fetchMoviesAndSetToAdapter(sortOrder);

            Log.d("MainPresenter", "movies in repo null");
        }
    }

    /*
     * This method is called only once for each sort order in app lifecycle, because movies list
     * are cached in moviesSingleton.
     * In this method there is made network request to theMovieDb.org database with Volley library.
     * RequestQueue singleton is field in moviesSingleton class, that's why addToRequestQueue() is
     * moviesSingleton's method.
     */
    private void fetchMoviesAndSetToAdapter(int sortOrder) {
        String url;
        if (sortOrder == ORDER_POPULAR) {
            url = "https://api.themoviedb.org/3/movie/popular?api_key=" + API_KEY;
            StringRequest request = new StringRequest(Request.Method.GET, url,
                    onPopularMoviesLoaded, onLoadError);
            moviesSingleton.addToRequestQueue(request);
        } else if(sortOrder == ORDER_TOP_RATED) {
            url = "https://api.themoviedb.org/3/movie/top_rated?api_key=" + API_KEY;
            StringRequest request = new StringRequest(Request.Method.GET, url,
                    onTopRatedMoviesLoaded, onLoadError);
            moviesSingleton.addToRequestQueue(request);
        }
    }

    /*
     * This method is called only for sort order favourites. It is making request for only one
     * movie's data in theMovieDb.org database. When data is fetched, in onMovieLoaded method,
     * each movie is added for posters adapter respectively.
     */
    private void fetchMovie(int id) {

        String url = DetailPresenter.BASE_MOVIE_URL + String.valueOf(id) +
                "?api_key=" + MainPresenter.API_KEY;

        StringRequest request = new StringRequest(Request.Method.GET, url, onMovieLoaded, onLoadError);
        moviesSingleton.addToRequestQueue(request);

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
                adapter.setMovies(moviesOrderPopular);
                moviesSingleton.setMovies(moviesOrderPopular, MainActivity.ORDER_POPULAR);

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
                adapter.setMovies(moviesOrderTopRated);
                moviesSingleton.setMovies(moviesOrderTopRated, MainActivity.ORDER_TOP_RATED);

                Log.i("MainPresenter", "Movies list created");

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };

    private final Response.Listener<String> onMovieLoaded = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Movie movie = new Gson().fromJson(response, Movie.class);
            adapter.addToMovies(movie);
        }
    };

    private final Response.ErrorListener onLoadError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("MainPresenter", error.toString());
        }
    };
}


