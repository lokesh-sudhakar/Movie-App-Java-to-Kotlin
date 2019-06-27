package com.example.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.movieretrofit.R;

import data.MovieResult;
import datapersistence.Movie;

public class MovieDetailActivity extends AppCompatActivity {

    public static final String MOVIE = "movie";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
        movieDetailFragment.setMovieResult((MovieResult.Result) getIntent().getExtras().getSerializable(MOVIE));
        movieDetailFragment.setFavouriteMovie((Movie) getIntent().getExtras().getSerializable("persistence_movie"));
        movieDetailFragment.getMovieResult();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.movie_details_container, movieDetailFragment).commit();
    }
}
