package com.example.polcel.popular_movies_part1.models;

import java.util.List;

/**
 * Created by polcel on 19/02/2018.
 */

public class MoviesResult {

    public List<Movie> getResults() {
        return results;
    }

    public void setResults(List<Movie> results) {
        this.results = results;
    }

    private List<Movie> results;

}
