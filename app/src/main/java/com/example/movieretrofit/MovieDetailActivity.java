package com.example.movieretrofit;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import data.MovieResult;
import datapersistence.Movie;

public class MovieDetailActivity extends AppCompatActivity {

    public static final String MOVIE = "movie";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
        movieDetailFragment.setMovie((MovieResult.Result) getIntent().getExtras().getSerializable(MOVIE));
        movieDetailFragment.setPersistence_movie((Movie) getIntent().getExtras().getSerializable("persistence_movie"));
        movieDetailFragment.getMovie();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.movie_details_container, movieDetailFragment).commit();
    }
}
