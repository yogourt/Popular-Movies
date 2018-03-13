package com.example.jagoda.popularmovies.view.detail;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.jagoda.popularmovies.R;
import com.example.jagoda.popularmovies.contracts.TrailersContract;
import com.example.jagoda.popularmovies.model.MoviesSingleton;
import com.example.jagoda.popularmovies.model.Video;
import com.example.jagoda.popularmovies.presenter.detail.TrailersFragmentPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TrailersFragment extends Fragment implements TrailersContract.View {

    public static final String KEY_MOVIE_ID = "movieId";
    public static final String BASE_YOUTUBE_URL = "https://www.youtube.com/watch?v=";

    @BindView(R.id.trailers_list_view)
    ListView trailerLv;

    public TrailersFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_trailers, container, false);
        ButterKnife.bind(this, view);

        MoviesSingleton singleton = MoviesSingleton.getInstance(getActivity().getApplicationContext());
        TrailersContract.Presenter presenter = new TrailersFragmentPresenter(this, singleton);

        Bundle bundle = getArguments();
        if(bundle != null) {
            int movieId = bundle.getInt(KEY_MOVIE_ID);
            presenter.fetchTrailers(movieId);
        }

        return view;
    }

    /*
     * Method to create trailers labels and to set OnItemClickListener.
     * This is a callback method called by Trailers Fragment Presenter when trailers list is fetched.
     */
    @Override
    public synchronized void fillTrailerLv(final List<Video> trailers) {

        int trailerNum = trailers.size();
        if (trailerNum == 0) return;

        List<String> trailerLabels = new ArrayList<>();

        for (Video trailer: trailers) {
            String label = " " + trailer.getTitle();
            trailerLabels.add(label);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.trailer_list_item, trailerLabels);
        trailerLv.setAdapter(adapter);


        trailerLv.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Video trailer = trailers.get((int) id);
                String videoKey = trailer.getKey();
                String url = BASE_YOUTUBE_URL + videoKey;
                Intent intentToStartTrailer = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

                if (intentToStartTrailer.resolveActivity(getContext().getPackageManager()) != null) {
                    startActivity(intentToStartTrailer);
                }
            }
        });

    }
}
