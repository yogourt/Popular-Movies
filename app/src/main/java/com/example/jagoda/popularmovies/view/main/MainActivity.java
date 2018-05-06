package com.example.jagoda.popularmovies.view.main;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.jagoda.popularmovies.R;
import com.example.jagoda.popularmovies.contracts.MainContract;
import com.example.jagoda.popularmovies.model.MoviesSingleton;
import com.example.jagoda.popularmovies.model.data.DatabaseSingleton;
import com.example.jagoda.popularmovies.presenter.main.MainPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainContract.View{

    //Number of columns in Grid Layout of Posters Recycler View for portrait and landscape orientation
    public static final int COLUMN_NUM_PORT = 2;
    public static final int COLUMN_NUM_LAND = 4;

    //Key to save sort order to Shared Preferences
    public static final String KEY_SORT_ORDER = "sort_order";
    //Values used for order: popular/top rated/favourites in Shared Preferences
    public static final int ORDER_POPULAR = 1;
    public static final int ORDER_TOP_RATED = 2;
    public static final int ORDER_FAVOURITES = 3;


    private PostersAdapter adapter;
    private MainPresenter presenter;
    private MoviesSingleton moviesSingleton;
    private DatabaseSingleton databaseSingleton;

    @BindView(R.id.posters_rv)
    RecyclerView postersRV;
    @BindView(R.id.message_text_view)
    TextView noFavMoviesTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        moviesSingleton = MoviesSingleton.getInstance(getApplicationContext());
        databaseSingleton = DatabaseSingleton.getInstance(getApplicationContext());
    }

    @Override
    protected void onStart() {
        super.onStart();
        setPostersRvProperties();

        presenter = new MainPresenter(this, adapter, moviesSingleton, databaseSingleton);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        int sortOrder = preferences.getInt(KEY_SORT_ORDER, ORDER_POPULAR);

        presenter.setMoviesToAdapter(sortOrder);
    }

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
            showPostersRv();
            presenter.setMoviesToAdapter(ORDER_POPULAR);
            PreferenceManager.getDefaultSharedPreferences(this).edit().
                    putInt(KEY_SORT_ORDER, ORDER_POPULAR).apply();
            return true;
        }

        if(itemId == R.id.action_top_rated) {
            showPostersRv();
            presenter.setMoviesToAdapter(ORDER_TOP_RATED);
            PreferenceManager.getDefaultSharedPreferences(this).edit().
                    putInt(KEY_SORT_ORDER, ORDER_TOP_RATED).apply();
            return true;
        }

        if(itemId == R.id.action_favourites) {
            presenter.setMoviesToAdapter(ORDER_FAVOURITES);
            PreferenceManager.getDefaultSharedPreferences(this).edit().
                    putInt(KEY_SORT_ORDER, ORDER_FAVOURITES).apply();
        }

        return super.onOptionsItemSelected(item);
    }

    private void setPostersRvProperties() {
        adapter = new PostersAdapter(this);
        postersRV.setAdapter(adapter);

        postersRV.setLayoutManager(createLayoutManager());
        postersRV.setHasFixedSize(true);
    }

    private RecyclerView.LayoutManager createLayoutManager() {
        //check screen orientation and make column number according to it
        int orientation = getResources().getConfiguration().orientation;
        int columnNum;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            columnNum = COLUMN_NUM_PORT;
        } else {
            columnNum = COLUMN_NUM_LAND;
        }
        return new GridLayoutManager(this, columnNum);
    }

    @Override
    public void showMessageNoFavMovies() {
        postersRV.setVisibility(View.GONE);
        noFavMoviesTv.setVisibility(View.VISIBLE);
    }

    public void showPostersRv() {
        postersRV.setVisibility(View.VISIBLE);
        noFavMoviesTv.setVisibility(View.GONE);
    }
}
