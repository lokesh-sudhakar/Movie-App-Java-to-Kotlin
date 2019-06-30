package com.example.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieretrofit.R;

import java.util.List;
import java.util.Objects;

import adapter.FavouriteMoviesAdapter;
import adapter.MoviePageListadapter;
import data.MovieResult;
import datapersistence.Movie;
import datapersistence.MovieRoomDatabase;
import viewmodel.MovieViewModel;

public class MasterListFragment extends Fragment implements MoviePageListadapter.ListItemClickListener, FavouriteMoviesAdapter.ListItemClickListener {

    private static final String MOVIE_FROM_LIST_FRAGMENT = "movie_from list_fragment";
    private PagedList<MovieResult.Result> movies;
    private MovieViewModel movieViewModel;
    private RecyclerView favRecyclerView;
    private RecyclerView movieRecyclerView;
    private MoviePageListadapter movieAdapter;
    private MovieRoomDatabase mDb;
    private FavouriteMoviesAdapter adapter;
    private Context context;
    private MovieDetailFragment movieDetailFragment;
    private boolean mTwoPane;
    private int scrollItemPosition;
    ProgressDialog progressDialog;
    FragmentManager fragmentManager;

    OnPosterClickListner onPosterClickListner;

    public interface OnPosterClickListner {
        void onClickPoster(MovieResult.Result moviePoster);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        try {
            onPosterClickListner = (OnPosterClickListner) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
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

        progressDialog = new ProgressDialog(context);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading...");

        movieViewModel.getMoviesPagedList().observe(this, new Observer<PagedList<MovieResult.Result>>() {
            @Override
            public void onChanged(PagedList<MovieResult.Result> moviesFromLiveData) {
                movies = moviesFromLiveData;
                progressDialog.dismiss();
                showMoviesOnRecyclerView(rootView);
            }
        });
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mTwoPane = false;
        } else {
            mTwoPane = true;
            Log.d("twopane ", "working");
            movieDetailFragment = new MovieDetailFragment();
            if (mTwoPane){
                movieViewModel.getFirstMovie().observe(this, new Observer<MovieResult.Result>() {
                    @Override
                    public void onChanged(MovieResult.Result result) {
                        movieDetailFragment = new MovieDetailFragment();
                        movieDetailFragment.setMovieResult(result);
                        fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.movie_details_container, movieDetailFragment).commit();
                    }
                });
            }
        }



        addFavouriteMoviesToRecyclerView(rootView);



        mDb = MovieRoomDatabase.getDatabase(context);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mTwoPane){
        movieViewModel.getFirstMovie().observe(this, new Observer<MovieResult.Result>() {
            @Override
            public void onChanged(MovieResult.Result result) {
                Log.d("movie reached on changed","yes"+result.toString());

                movieDetailFragment.setMovieResult(result);
                getFragmentManager().beginTransaction().replace(R.id.movie_details_container, movieDetailFragment).commit();
            }
        });
        }
    }

    private void addFavouriteMoviesToRecyclerView(View rootView) {
        favRecyclerView = rootView.findViewById(R.id.favourite_movie_recyclerView);
        adapter = new FavouriteMoviesAdapter(context, this);
        favRecyclerView.setAdapter(adapter);
        favRecyclerView.setLayoutManager(new GridLayoutManager(context, 2));
    }

    private void showMoviesOnRecyclerView(View view) {

        movieRecyclerView = view.findViewById(R.id.customRecyclerView);
        movieAdapter = new MoviePageListadapter(getActivity(), this,movieViewModel);
        movieAdapter.submitList(movies);
        GridLayoutManager mGridlayoutManager = new GridLayoutManager(context, 2);
        scrollItemPosition = mGridlayoutManager.findFirstVisibleItemPosition();
        mGridlayoutManager.scrollToPosition(scrollItemPosition);
        movieRecyclerView.setLayoutManager(mGridlayoutManager);
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
                progressDialog.show();
                movieViewModel.setCategory("top_rated");
                movieViewModel.setFirstMovieLoaded(false);

                movieRecyclerView.setVisibility(View.VISIBLE);
                favRecyclerView.setVisibility(View.GONE);
                return true;
            }
            case R.id.action_sort_by_popularity: {
                progressDialog.show();
                movieViewModel.setCategory("popular");
                movieViewModel.setFirstMovieLoaded(false);
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
    public void onFavouritePosterClick(int position, List<datapersistence.Movie> dataList) {
        Movie movie = dataList.get(position);
        if (mTwoPane) {
            Log.d("error in movies while in two pane", "" + movie.getId());
            movieDetailFragment = new MovieDetailFragment();
            movieDetailFragment.setFavouriteMovie(movie);
            FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.movie_details_container, movieDetailFragment).commit();
        }else{
        Bundle bundle = new Bundle();
        bundle.putSerializable("persistence_movie", movie);
        Intent intent = new Intent(context, MovieDetailActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);}
    }

    @Override
    public void onListItemClick(int position) {
        MovieResult.Result movie = movies.get(position);
        if (mTwoPane) {
            Log.d("error in movies while in two pane", "" + movie.getId());
            movieDetailFragment = new MovieDetailFragment();
            movieDetailFragment.setMovieResult(movie);
            FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.movie_details_container, movieDetailFragment).commit();
        } else {
            Log.d("error in movies in single pane", "" + movie.getId());
            Bundle bundle = new Bundle();
            bundle.putSerializable(MOVIE_FROM_LIST_FRAGMENT, movie);
            Intent intent = new Intent(context, MovieDetailActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}
