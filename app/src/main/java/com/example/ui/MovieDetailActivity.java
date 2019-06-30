package com.example.ui;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.movieretrofit.R;

import data.MovieResult;
import datapersistence.Movie;

public class MovieDetailActivity extends AppCompatActivity {

    public static final String MOVIE_FROM_LIST_FRAGMENT = "movie_from list_fragment";
    public static final String PERSISTENCE_MOVIE = "persistence_movie";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            setContentView(R.layout.activity_main);
        }else{
        MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
        movieDetailFragment.setMovieResult((MovieResult.Result) getIntent().getExtras().getSerializable(MOVIE_FROM_LIST_FRAGMENT));
        movieDetailFragment.setFavouriteMovie((Movie) getIntent().getExtras().getSerializable(PERSISTENCE_MOVIE));
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.movie_details_container, movieDetailFragment).commit();}
    }


}
