package com.example.jagoda.popularmovies.presenter.detail;


import com.example.jagoda.popularmovies.contracts.DetailContract;
import com.example.jagoda.popularmovies.model.data.DatabaseSingleton;

/*
 * Presenter for top Detail Activity
 */
public class DetailPresenter implements DetailContract.Presenter {

    public static final String BASE_MOVIE_URL = "https://api.themoviedb.org/3/movie/";

    //Key to access results from JSON String that contains some additional information
    public static final String RESULTS_JSON = "results";

    private DatabaseSingleton singleton;
    private int movieId;

    public DetailPresenter(DatabaseSingleton writerSingleton, int movieId) {
        this.singleton = writerSingleton;
        this.movieId = movieId;
    }

    @Override
    public void deleteMovieFromFavourites() {
        singleton.deleteMovieFromDb(movieId);
    }

    @Override
    public void addMovieToFavourites(String title) {
        singleton.addMovieToDb(title, movieId);
    }

    @Override
    public boolean isPresentInFavourites() {
        return singleton.isPresentInDb(movieId);
    }


}
