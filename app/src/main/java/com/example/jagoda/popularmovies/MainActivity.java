package com.example.jagoda.popularmovies;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.jagoda.popularmovies.data.Movie;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //Number of columns in Grid Layout of Posters Recycler View for portrait and landscape orientation
    private static final int COLUMN_NUM_PORT = 3;
    private static final int COLUMN_NUM_LAND = 5;

    private static final String API_KEY = "YOUR_API_KEY";

    //Key to access results from JSON String that contains some additional information
    private static final String RESULTS_JSON = "results";

    //Key to save sort order to Shared Preferences
    private static final String KEY_SORT_ORDER = "sort_order";
    //Values used for order: popular/top rated
    private static final int ORDER_POPULAR = 1;
    private static final int ORDER_TOP_RATED = 2;

    //Request Queue is field used by Volley library to make network requests.
    private RequestQueue requestQueue;

    PostersAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView postersRV = findViewById(R.id.posters_rv);

        adapter = new PostersAdapter(this);
        postersRV.setAdapter(adapter);

        //check screen orientation and make column number according to it
        int orientation = getResources().getConfiguration().orientation;
        int columnNum;
        if(orientation == Configuration.ORIENTATION_PORTRAIT) {
            columnNum = COLUMN_NUM_PORT;
        } else {
            columnNum = COLUMN_NUM_LAND;
        }
        GridLayoutManager layoutManager = new GridLayoutManager(this, columnNum);
        postersRV.setLayoutManager(layoutManager);
        postersRV.setHasFixedSize(true);

        requestQueue = Volley.newRequestQueue(this);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        int sortOrder = preferences.getInt(KEY_SORT_ORDER, ORDER_POPULAR);
        fetchMovies(sortOrder);

    }

    private void fetchMovies(int sortOrder) {

        String url;
        if(sortOrder == ORDER_POPULAR) {
            url = "https://api.themoviedb.org/3/movie/popular?api_key=" + API_KEY;
            Log.i("MainActivity", url);
        } else {
            url = "https://api.themoviedb.org/3/movie/top_rated?api_key=" + API_KEY;
            Log.i("MainActivity", url);
        }
        StringRequest request = new StringRequest(Request.Method.GET, url, onMoviesLoaded, onLoadError);
        requestQueue.add(request);
    }

    private final Response.Listener<String> onMoviesLoaded = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {

            /*
            After loading json, the response is parsed to JSON Object to extract results.
            Then it's parsed to Movies List using Gson library
             */
            try {
                JSONObject responseJson = new JSONObject(response);
                String results = responseJson.getString(RESULTS_JSON);

                List<Movie> movies = Arrays.asList(new Gson().fromJson(results, Movie[].class));

                Log.i("MainActivity", "Movies list created");
                adapter.setMovies(movies);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };

    private final Response.ErrorListener onLoadError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("MainActivity", error.toString());
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();

        if(itemId == R.id.action_popular) {
            fetchMovies(ORDER_POPULAR);
            PreferenceManager.getDefaultSharedPreferences(this).edit().
                    putInt(KEY_SORT_ORDER, ORDER_POPULAR).apply();
            return true;
        }

        if(itemId == R.id.action_top_rated) {
            fetchMovies(ORDER_TOP_RATED);
            PreferenceManager.getDefaultSharedPreferences(this).edit().
                    putInt(KEY_SORT_ORDER, ORDER_TOP_RATED).apply();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
