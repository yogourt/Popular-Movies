package com.example.jagoda.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

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

    //Number of columns in Grid Layout of Posters Recycler View
    private static final int COLUMNS_NUM = 3;

    private static final String API_KEY = "YOUR_API_KEY";

    //Key to access results from JSON String that contains some additional information
    private static final String RESULTS_JSON = "results";

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

        GridLayoutManager layoutManager = new GridLayoutManager(this, COLUMNS_NUM);
        postersRV.setLayoutManager(layoutManager);
        postersRV.setHasFixedSize(true);

        requestQueue = Volley.newRequestQueue(this);

        fetchMovies();

    }

    private void fetchMovies() {

        String url =  "https://api.themoviedb.org/3/movie/popular?api_key=" + API_KEY;
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
}
