package com.example.polcel.popular_movies_part1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.polcel.popular_movies_part1.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by polcel on 10/02/2018.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder> {

    private ArrayList<Movie> mMovies;

    final private MovieClickListener mOnClickListener;

    public interface MovieClickListener {
        void onMovieClickListener(Movie clickedMovie);
    }

    MoviesAdapter(MovieClickListener listener) {
        mOnClickListener = listener;
    }

    public void setMoviesData(ArrayList<Movie> moviesList) {
        mMovies = moviesList;
        notifyDataSetChanged();
    }

    @Override
    public MoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MoviesViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_movie, parent, false));
    }

    @Override
    public void onBindViewHolder(MoviesViewHolder holder, int position) {
        Movie movie = mMovies.get(position);

        Picasso.with(holder.movieImage.getContext()).load(movie.getPosterPath()).into(holder.movieImage);
    }

    @Override
    public int getItemCount() {
        return mMovies != null ? mMovies.size() : 0;
    }

    class MoviesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final ImageView movieImage;
        final Context context;

        MoviesViewHolder(View itemView) {
            super(itemView);

            context = itemView.getContext();
            movieImage = itemView.findViewById(R.id.list_item_movie_image);
            movieImage.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();

            mOnClickListener.onMovieClickListener(mMovies.get(clickedPosition));
        }
    }
}
