package com.example.jagoda.popularmovies.contracts;


import com.example.jagoda.popularmovies.model.Video;

import java.util.List;

public interface TrailersContract {

    interface View {
        void fillTrailerLv(List<Video> videos);
    }

    interface Presenter {
        void fetchTrailers(int movieId);
    }

}
