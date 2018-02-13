package com.example.polcel.popular_movies_part1.utilities;

import android.util.Log;

import com.example.polcel.popular_movies_part1.models.Movie;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by polcel on 10/02/2018.
 */

public class MoviesAPIParser {

    public static ArrayList<Movie> parseMoviesFromJson(JSONArray moviesList) {
        ArrayList<Movie> moviesArrayList = new ArrayList<>();

        try {
            for (int i = 0; i < moviesList.length(); i++) {

                JSONObject jsonMovie = moviesList.getJSONObject(i);

                Movie movie = new Movie();
                movie.setVoteCount(jsonMovie.getInt("vote_count"));
                movie.setId(jsonMovie.getInt("id"));
                movie.setVoteAverage(jsonMovie.getDouble("vote_average"));
                movie.setTitle(jsonMovie.getString("title"));
                movie.setPopularity(jsonMovie.getDouble("popularity"));
                movie.setPosterPath(jsonMovie.getString("poster_path"));
                movie.setOriginalLanguage(jsonMovie.getString("original_language"));
                movie.setOriginalTitle(jsonMovie.getString("original_title"));
                //movie.setGenreIDS(jsonMovie.getJSONArray("genre_ids"));
                movie.setBackdropPath(jsonMovie.getString("backdrop_path"));
                movie.setAdult(jsonMovie.getBoolean("adult"));
                movie.setOverview(jsonMovie.getString("overview"));
                movie.setReleaseDate(jsonMovie.getString("release_date"));

                moviesArrayList.add(movie);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return moviesArrayList;
    }
}
