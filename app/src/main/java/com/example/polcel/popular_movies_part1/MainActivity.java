package com.example.polcel.popular_movies_part1;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.polcel.popular_movies_part1.models.Movie;
import com.example.polcel.popular_movies_part1.utilities.GridAutofitLayoutManager;
import com.example.polcel.popular_movies_part1.utilities.MoviesAPIParser;
import com.example.polcel.popular_movies_part1.utilities.NetworkUtils;

import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.MovieClickListener {
    private static final String KEY_POPULAR_MOVIES = "is_popular_movies";
    private static final String KEY_MOVIE_DETAILS = "movie_details";
    private boolean getOnlyPopular = true;

    private RecyclerView mRecyclerViewMovies;
    private ProgressBar mProgressBarLoading;
    private MoviesAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerViewMovies = findViewById(R.id.rv_movies);
        mProgressBarLoading = findViewById(R.id.pb_loading_movies);

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

    class MoviesTask extends AsyncTask<Boolean, Void, ArrayList<Movie>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBarLoading.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<Movie> doInBackground(Boolean... params) {
            if (params.length == 0) {
                return null;
            }

            boolean filter = params[0];

            URL moviesRequestURL = NetworkUtils.buildUrl(filter);
            ArrayList<Movie> moviesArrayList = new ArrayList<>();

            try {
                String jsonMoviesResponse = NetworkUtils.getResponseFromHttpUrl(moviesRequestURL);

                JSONObject movies;
                if (!TextUtils.isEmpty(jsonMoviesResponse)) {
                    movies = new JSONObject(jsonMoviesResponse);

                    moviesArrayList = MoviesAPIParser.parseMoviesFromJson(movies.getJSONArray("results"));
                }

                return moviesArrayList;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> result) {
            if (result != null) {
                if (result.size() > 0) {
                    mAdapter.setMoviesData(result);
                }
            }
            mProgressBarLoading.setVisibility(View.INVISIBLE);
        }
    }

    private void loadMovies(boolean getOnlyPopular) {
        if (NetworkUtils.isOnline(this)) {
            new MoviesTask().execute(getOnlyPopular);
        } else {
            Toast.makeText(this, R.string.no_internet_connection, Toast.LENGTH_LONG).show();
        }
    }
}
