package com.example.jagoda.popularmovies;

import android.content.Context;
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
    private static final String BASE_IMAGE_URL = " http://image.tmdb.org/t/p/w185/";

    Context context;
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


    class PosterViewHolder extends RecyclerView.ViewHolder {

        //Image View to hold the poster
        ImageView posterIv;

        public PosterViewHolder(View itemView) {
            super(itemView);
            posterIv = itemView.findViewById(R.id.poster_image_view);
        }
    }
}
