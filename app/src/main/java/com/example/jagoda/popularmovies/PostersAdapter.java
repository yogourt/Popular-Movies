package com.example.jagoda.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.jagoda.popularmovies.data.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

/*
 * Adapter Class for Recycler View showing movies' posters in Main Activity
 */
public class PostersAdapter extends RecyclerView.Adapter<PostersAdapter.PosterViewHolder> {

    //base URL used to fetch poster's image in onBindViewHolder
    public static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/w185/";

    public static final String KEY_TITLE = "title";
    public static final String KEY_ORIGINAL_TITLE = "original_title";
    public static final String KEY_RELEASE_DATE = "release_date";
    public static final String KEY_OVERVIEW = "description";
    public static final String KEY_IMAGE_PATH = "image_path";
    public static final String KEY_RATING = "rating";

    private Context context;
    private List<Movie> movies;

    public PostersAdapter(Context context) {
        this.context = context;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    @Override
    public PosterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movies_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new PosterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PosterViewHolder holder, int position) {

        String relativeImagePath = movies.get(position).getPosterPath();
        String imageUrl = BASE_IMAGE_URL + relativeImagePath;
        Log.i("PostersAdapter", imageUrl);

        Picasso.with(context)
                .load(imageUrl)
                .into(holder.posterIv);

    }

    @Override
    public int getItemCount() {
        if(movies != null) return movies.size();
        else return 0;
    }


    class PosterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //Image View to hold the poster
        ImageView posterIv;

        public PosterViewHolder(View itemView) {
            super(itemView);
            posterIv = itemView.findViewById(R.id.poster_image_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Intent intentForDetailActivity = new Intent(context, DetailActivity.class);
            Movie clickedMovie = movies.get(position);
            intentForDetailActivity.putExtra(KEY_TITLE, clickedMovie.getTitle());
            intentForDetailActivity.putExtra(KEY_ORIGINAL_TITLE, clickedMovie.getOriginalTitle());
            intentForDetailActivity.putExtra(KEY_RELEASE_DATE, clickedMovie.getReleaseDate());
            intentForDetailActivity.putExtra(KEY_IMAGE_PATH, clickedMovie.getPosterPath());
            intentForDetailActivity.putExtra(KEY_OVERVIEW, clickedMovie.getOverview());
            intentForDetailActivity.putExtra(KEY_RATING, clickedMovie.getRating());
            context.startActivity(intentForDetailActivity);
        }
    }
}
