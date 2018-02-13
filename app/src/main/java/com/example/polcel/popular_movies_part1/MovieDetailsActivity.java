package com.example.polcel.popular_movies_part1;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.polcel.popular_movies_part1.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.Locale;

public class MovieDetailsActivity extends AppCompatActivity {
    private static final String KEY_MOVIE_DETAILS = "movie_details";

    private Movie mSelectedMovie;

    private TextView mMovieTitle;
    private ImageView mMoviePicture;
    private TextView mMovieYear;
    private TextView mMovieRating;
    private TextView mMovieOverview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mMovieTitle = findViewById(R.id.movie_details_tv_title);
        mMoviePicture = findViewById(R.id.movie_details_iv_movie_picture);
        mMovieYear = findViewById(R.id.movie_details_tv_release_date);
        mMovieRating = findViewById(R.id.movie_details_tv_movie_average_vote);
        mMovieOverview = findViewById(R.id.movie_details_tv_movie_overview);

        if (savedInstanceState != null) {
            if (savedInstanceState.getParcelable(KEY_MOVIE_DETAILS) != null) {
                mSelectedMovie = savedInstanceState.getParcelable(KEY_MOVIE_DETAILS);
            }
        }

        Intent movieIntent = getIntent();

        if (movieIntent.hasExtra(KEY_MOVIE_DETAILS)) {
            mSelectedMovie = movieIntent.getParcelableExtra(KEY_MOVIE_DETAILS);
        }

        if (mSelectedMovie != null) {
            mMovieTitle.setText(mSelectedMovie.getTitle());
            Picasso.with(this).load(mSelectedMovie.getPosterPath()).into(mMoviePicture);
            mMovieYear.setText(mSelectedMovie.getReleaseDate());

            String averageVote = String.format(Locale.getDefault(), "%.1f / 10", mSelectedMovie.getVoteAverage());

            mMovieRating.setText(averageVote);
            mMovieOverview.setText(mSelectedMovie.getOverview());
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(KEY_MOVIE_DETAILS, mSelectedMovie);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
