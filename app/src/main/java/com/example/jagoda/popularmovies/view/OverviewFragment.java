package com.example.jagoda.popularmovies.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jagoda.popularmovies.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class OverviewFragment extends Fragment {

    public static final String KEY_OVERVIEW = "overview";
    public static final String KEY_RELEASE_DATE = "release_date";

    @BindView(R.id.overview_text_view)
    TextView overviewTv;
    @BindView(R.id.release_date_text_view)
    TextView releaseDateTv;

    public OverviewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_overview, container, false);
        ButterKnife.bind(this, view);

        Bundle bundle = this.getArguments();
        if(bundle != null) {
            String overview = bundle.getString(KEY_OVERVIEW);
            overviewTv.setText(overview);
            String releaseDate = bundle.getString(KEY_RELEASE_DATE);
            releaseDateTv.setText(releaseDate);
        }

        return view;
    }

}
