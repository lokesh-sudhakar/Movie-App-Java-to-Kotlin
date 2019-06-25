package com.example.movieretrofit;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Movie;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.List;

import adapter.CustomAdapter;
import adapter.MoviePageListadapter;
import adapter.MovieViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.provider.MediaStore.Video.VideoColumns.CATEGORY;

public class MasterListFragment extends Fragment implements MoviePageListadapter.ListItemClickListener{

    PagedList<MovieResult.Result> movies;
    MovieViewModel movieViewModel;
    RecyclerView recyclerView;
    MoviePageListadapter movieAdapter;
    CustomAdapter adapter;
    private static final String BASE_URL = "https://api.themoviedb.org";
    private static String CATEGORY = "popular";
    private static String CATEGORY_TOP_RATED = "top_rated";
    private static String API_KEY = "c4fd0359f29736975ba764defb5f2878";
    private static String LANGUAGE = "en-US";
    private static int PAGE = 1;
    private Context context;

    public List<MovieResult.Result> getMovieList() {
        return movieList;
    }

    public void setMovieList(List<MovieResult.Result> movieList) {
        this.movieList = movieList;
    }

    private List<MovieResult.Result> movieList;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    // Mandatory empty constructor
    public MasterListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    // Inflates the GridView of all AndroidMe images
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_master_list, container, false);
        movieViewModel= ViewModelProviders.of(this).get(MovieViewModel.class);

        //movieViewModel.setCategory("top_rated");
        movieViewModel.getMoviesPagedList().observe(this, new Observer<PagedList<MovieResult.Result>>() {
            @Override
            public void onChanged( PagedList<MovieResult.Result> moviesFromLiveData) {
                movies=moviesFromLiveData;
                showOnRecyclerView(rootView);
            }
        });
        return rootView;
    }

    private void privideViewModel(final View rootView,String category) {
        movieViewModel= ViewModelProviders.of(this).get(MovieViewModel.class);

        movieViewModel.setCategory(category);
        movieViewModel.getMoviesPagedList().observe(this, new Observer<PagedList<MovieResult.Result>>() {
            @Override
            public void onChanged( PagedList<MovieResult.Result> moviesFromLiveData) {
                movies=moviesFromLiveData;
                showOnRecyclerView(rootView);
            }
        });
    }


//    void generateDataList(View view, List<MovieResult.Result> movieList) {
//        recyclerView = view.findViewById(R.id.customRecyclerView);
//        recyclerView.setLayoutManager(new GridLayoutManager(context,2));
//        MovieViewModel movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
//        final MoviePageListadapter pageAdapter = new MoviePageListadapter(getContext());
////        adapter = new CustomAdapter(context,  movieList,this);
////        recyclerView.setAdapter(adapter);
//        movieViewModel.moviePagedList.observe(this, new Observer<PagedList<MovieResult.Result>>() {
//            @Override
//            public void onChanged(PagedList<MovieResult.Result> results) {
//
//                pageAdapter.submitList(results);
//            }
//        });
//
//    }

    private void showOnRecyclerView(View view) {

        recyclerView = view.findViewById(R.id.customRecyclerView);
        movieAdapter = new MoviePageListadapter(context,this);
        movieAdapter.submitList(movies);

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

            recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        } else {


            recyclerView.setLayoutManager(new GridLayoutManager(context, 4));


        }

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(movieAdapter);
        movieAdapter.notifyDataSetChanged();

    }
//
//    @Override
//    public void onListItemClick(int clickedItemIndex,List<MovieResult.Result> movieList) {
//
//        Bundle bundle = new Bundle();
//        bundle.putInt("item_position",clickedItemIndex);
//        Intent intent = new Intent(context, MovieDetailActivity.class);
//        intent.putExtra("data_list", (Serializable) movieList);
//        intent.putExtras(bundle);
//        startActivity(intent);
//        Toast.makeText(context, "the position clicked is"+clickedItemIndex+"fragment ", Toast.LENGTH_SHORT).show();
//    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.filter_movie, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_sort_by_rating:{
                Toast.makeText(context, "Sort by rating selected ", Toast.LENGTH_SHORT).show();
                movieViewModel.setCategory("top_rated");
                return true;
            }
            case R.id.action_sort_by_popularity:{
                Toast.makeText(context, "Sort by popularity selected ", Toast.LENGTH_SHORT).show();
                movieViewModel.setCategory("popular");
                return true;
            }
            case R.id.action_show_favourite :{
                Toast.makeText(context, "show favourites selected ", Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onListItemClick(int position) {
        Toast.makeText(context, "the position"+movies.get(position).getTitle()+" clicked is  "+position, Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent(context, MovieDetailActivity.class);
//        intent.putExtra("movies", (Serializable) movies);
//        startActivity(intent);
    }
}
