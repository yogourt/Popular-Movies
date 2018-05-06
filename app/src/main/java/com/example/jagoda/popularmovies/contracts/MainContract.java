package com.example.jagoda.popularmovies.contracts;


import com.example.jagoda.popularmovies.model.Movie;

import java.util.List;

public interface MainContract {

    interface View {
        void showMessageNoFavMovies();
    }

    interface Adapter {
        void setMovies(List<Movie> movies);
        void addToMovies(Movie movie);

    }

    interface Presenter {
        void setMoviesToAdapter(int sortOrder);

    }
}
