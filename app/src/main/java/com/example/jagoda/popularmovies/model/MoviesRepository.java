package com.example.jagoda.popularmovies.model;


import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.jagoda.popularmovies.view.MainActivity;
import java.util.List;

/* This is Singleton class for movies repository that allows caching data on orientation change
 * and that preserves RequestQueue Singleton object for network requests.
*/
public class MoviesRepository {


    private static MoviesRepository instance;

    //Request Queue is field used by Volley library to make network requests.
    private RequestQueue requestQueue;
    private List<Movie> moviesOrderPopular;
    private List<Movie> moviesOrderTopRated;

    Context appContext;

    //private constructor can be called only inside this class
    private MoviesRepository(Context appContext) {
        this.appContext = appContext;
        requestQueue = getRequestQueue();
    }

    public static synchronized MoviesRepository getInstance(Context appContext) {
        if (instance == null) {
            instance = new MoviesRepository(appContext);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            requestQueue = Volley.newRequestQueue(appContext.getApplicationContext());
        }
        return requestQueue;
    }

    public void addToRequestQueue(Request request) {
        getRequestQueue().add(request);
        Log.d("MoviesRepository", "New request in requestQueue");
    }


    public List<Movie> getMovies(int sortOrder) {

        if (sortOrder == MainActivity.ORDER_POPULAR) {
            return moviesOrderPopular;
        } else {
            return moviesOrderTopRated;
        }
    }

    public  void setMovies(List<Movie> movies, int sortOrder) {

        if (sortOrder == MainActivity.ORDER_POPULAR) {
            moviesOrderPopular = movies;
        } else {
            moviesOrderTopRated = movies;
        }
    }

}

