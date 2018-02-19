package com.example.polcel.popular_movies_part1.services;

import com.example.polcel.popular_movies_part1.models.MoviesResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by polcel on 17/02/2018.
 */

public interface MoviesService {
    @GET("movie/{movieIndex}")
    Call<MoviesResult> getMovies(@Path("movieIndex") String moviesIndex);
}
