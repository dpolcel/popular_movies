package com.example.polcel.popular_movies_part1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.polcel.popular_movies_part1.di.Injector;
import com.example.polcel.popular_movies_part1.models.Movie;
import com.example.polcel.popular_movies_part1.models.MoviesResult;
import com.example.polcel.popular_movies_part1.services.MoviesService;
import com.example.polcel.popular_movies_part1.utilities.GridAutofitLayoutManager;
import com.example.polcel.popular_movies_part1.utilities.NetworkUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.MovieClickListener {
    private static final String KEY_POPULAR_MOVIES = "is_popular_movies";
    private static final String KEY_MOVIE_DETAILS = "movie_details";
    private boolean getOnlyPopular = true;

    private MoviesAdapter mAdapter;

    @BindView(R.id.rv_movies) private RecyclerView mRecyclerViewMovies;
    @BindView(R.id.pb_loading_movies) private ProgressBar mProgressBarLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mAdapter = new MoviesAdapter(this);

        mRecyclerViewMovies.setLayoutManager(new GridAutofitLayoutManager(this, 500));
        mRecyclerViewMovies.setAdapter(mAdapter);

        if (savedInstanceState != null) {
            getOnlyPopular = savedInstanceState.getBoolean(KEY_POPULAR_MOVIES);
        }

        loadMovies(getOnlyPopular);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_POPULAR_MOVIES, getOnlyPopular);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_popular) {
            loadMovies(getOnlyPopular = true);
            return true;
        }

        if (item.getItemId() == R.id.action_top_rated) {
            loadMovies(getOnlyPopular = false);
            return true;
        }

        if (item.getItemId() == R.id.action_refresh) {
            loadMovies(getOnlyPopular);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_movies, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onMovieClickListener(Movie clickedMovie) {
        Intent intent = new Intent(this, MovieDetailsActivity.class);
        intent.putExtra(KEY_MOVIE_DETAILS, clickedMovie);
        startActivity(intent);
    }

    private void loadMovies(boolean getOnlyPopular) {
        if (NetworkUtils.isOnline(this)) {
            mProgressBarLoading.setVisibility(View.VISIBLE);

            MoviesService moviesService = Injector.provideMoviesService();
            moviesService.getMovies(getOnlyPopular ? "popular" : "top_rated").enqueue(new Callback<MoviesResult>() {
                @Override
                public void onResponse(Call<MoviesResult> call, Response<MoviesResult> response) {
                    if (response.body() != null) {
                        mAdapter.setMoviesData(response.body().getResults());
                    }
                    mProgressBarLoading.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onFailure(Call<MoviesResult> call, Throwable t) {
                    mProgressBarLoading.setVisibility(View.INVISIBLE);
                }
            });


        } else {
            Toast.makeText(this, R.string.no_internet_connection, Toast.LENGTH_LONG).show();
        }
    }
}
