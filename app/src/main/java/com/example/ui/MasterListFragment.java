package com.example.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import data.MovieResult;
import datapersistence.Movie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.movieretrofit.R;

import java.util.List;

import adapter.FavouriteMoviesAdapter;
import adapter.MoviePageListadapter;
import viewmodel.MovieViewModel;
import datapersistence.MovieRoomDatabase;

public class MasterListFragment extends Fragment implements MoviePageListadapter.ListItemClickListener, FavouriteMoviesAdapter.ListItemClickListener {


    private PagedList<MovieResult.Result> movies;
    private MovieViewModel movieViewModel;
    private RecyclerView favRecyclerView;
    private RecyclerView movieRecyclerView;
    private MoviePageListadapter movieAdapter;
    private MovieRoomDatabase mDb;
    private FavouriteMoviesAdapter adapter;
    private Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public MasterListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_master_list, container, false);

        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);


        movieViewModel.getMoviesPagedList().observe(this, new Observer<PagedList<MovieResult.Result>>() {
            @Override
            public void onChanged(PagedList<MovieResult.Result> moviesFromLiveData) {
                movies = moviesFromLiveData;
                showOnRecyclerView(rootView);
            }
        });


        favRecyclerView = rootView.findViewById(R.id.favourite_movie_recyclerView);
        adapter = new FavouriteMoviesAdapter(context, this);
        favRecyclerView.setAdapter(adapter);
        favRecyclerView.setLayoutManager(new GridLayoutManager(context, 2));

        mDb = MovieRoomDatabase.getDatabase(context);

        return rootView;
    }

    private void showOnRecyclerView(View view) {

        movieRecyclerView = view.findViewById(R.id.customRecyclerView);
        movieAdapter = new MoviePageListadapter(context, this);
        movieAdapter.submitList(movies);

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

            movieRecyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        } else {


            movieRecyclerView.setLayoutManager(new GridLayoutManager(context, 4));


        }

        movieRecyclerView.setItemAnimator(new DefaultItemAnimator());
        movieRecyclerView.setAdapter(movieAdapter);
        movieAdapter.notifyDataSetChanged();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.filter_movie, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort_by_rating: {
                movieViewModel.setCategory("top_rated");
                movieRecyclerView.setVisibility(View.VISIBLE);
                favRecyclerView.setVisibility(View.GONE);
                return true;
            }
            case R.id.action_sort_by_popularity: {
                movieViewModel.setCategory("popular");
                movieRecyclerView.setVisibility(View.VISIBLE);
                favRecyclerView.setVisibility(View.GONE);
                return true;
            }
            case R.id.action_show_favourite: {
                LiveData<List<Movie>> favMovie = MovieRoomDatabase.getDatabase(context.getApplicationContext()).movieDao().getAllMovies();
                favMovie.observe(this, new Observer<List<datapersistence.Movie>>() {
                    @Override
                    public void onChanged(List<datapersistence.Movie> movies) {
                        movieRecyclerView.setVisibility(View.GONE);
                        favRecyclerView.setVisibility(View.VISIBLE);
                        adapter.setMovies(movies);
                        adapter.notifyDataSetChanged();
                    }
                });
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFavouriteItemClick(int position, List<datapersistence.Movie> dataList) {
        Movie movie = dataList.get(position);
        Bundle bundle = new Bundle();
        bundle.putSerializable("persistence_movie", movie);
        Intent intent = new Intent(context, MovieDetailActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onListItemClick(int position) {

        MovieResult.Result movie = movies.get(position);
        Bundle bundle = new Bundle();
        bundle.putSerializable("movie", movie);
        Intent intent = new Intent(context, MovieDetailActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
