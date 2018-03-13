package com.example.jagoda.popularmovies.contracts;


public interface DetailContract {

    interface View {


    }

    interface Presenter {
        void deleteMovieFromFavourites();
        void addMovieToFavourites(String title);
        boolean isPresentInFavourites();

    }

}
