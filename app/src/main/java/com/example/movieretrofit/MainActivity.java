package com.example.movieretrofit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Movie;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import adapter.CustomAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Query;

public class MainActivity extends AppCompatActivity implements CustomAdapter.ListItemClickListener{

    private static String CATEGORY = "popular";
    private static String CATEGORY_TOP_RATED = "top_rated";
    private static final String BASE_URL = "https://api.themoviedb.org";
    private static String API_KEY = "c4fd0359f29736975ba764defb5f2878";
    private static String LANGUAGE = "en-US";
    private static int PAGE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        retrofitExtractionFirstTime(CATEGORY);
    }

    @Override
    public void onListItemClick(int clickedItemIndex,List<MovieResult.Result> dataList) {
        Toast.makeText(this, "the position clicked is"+clickedItemIndex+"in main activity", Toast.LENGTH_SHORT).show();
//        Bundle bundle = new Bundle();
//        bundle.putInt("item_position",clickedItemIndex);
//
//        // Attach the Bundle to an intent
//        final Intent intent = new Intent(this, MovieDetailActivity.class);
//        intent.putExtra("data_list", (Serializable) dataList);
//        intent.putExtras(bundle);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//
//        inflater.inflate(R.menu.filter_movie, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        switch (item.getItemId()) {
//            case R.id.action_sort_by_rating:{
//                Toast.makeText(this, "Sort by rating selected ", Toast.LENGTH_SHORT).show();
//                retrofitExtraction(CATEGORY_TOP_RATED);
//                return true;
//            }
//            case R.id.action_sort_by_popularity:{
//                Toast.makeText(this, "Sort by popularity selected ", Toast.LENGTH_SHORT).show();
//                retrofitExtraction(CATEGORY);
//                return true;
//            }
//            case R.id.action_show_favourite :{
//                Toast.makeText(this, "show favourites selected ", Toast.LENGTH_SHORT).show();
//                return true;
//            }
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//    private void retrofitExtraction(String category) {
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        RetroFitInterface retroFitInterface = retrofit.create(RetroFitInterface.class);
//
//
//        Call<MovieResult> call = retroFitInterface.getMovies(category ,API_KEY,LANGUAGE,PAGE);
//        call.enqueue(new Callback<MovieResult>() {
//            @Override
//            public void onResponse(Call<MovieResult> call, Response<MovieResult> response) {
////               progressDialog.dismiss();
//                MovieResult movies = response.body();
//                List<MovieResult.Result> movieList = movies.getResults();
//                MasterListFragment masterListFragment = new MasterListFragment();
//                masterListFragment.setMovieList(movieList);
//                FragmentManager fragmentManager = getSupportFragmentManager();
//                fragmentManager.beginTransaction().replace(R.id.movie_list_container, masterListFragment).commit();
//            }
//
//            @Override
//            public void onFailure(Call<MovieResult> call, Throwable t) {
//                t.printStackTrace();
//                Toast.makeText(MainActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//    private void retrofitExtractionFirstTime(String category) {
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        RetroFitInterface retroFitInterface = retrofit.create(RetroFitInterface.class);
//
//
//        Call<MovieResult> call = retroFitInterface.getMovies(category ,API_KEY,LANGUAGE,PAGE);
//        call.enqueue(new Callback<MovieResult>() {
//            @Override
//            public void onResponse(Call<MovieResult> call, Response<MovieResult> response) {
////               progressDialog.dismiss();
//                MovieResult movies = response.body();
//                List<MovieResult.Result> movieList = movies.getResults();
//                MasterListFragment masterListFragment = new MasterListFragment();
//                masterListFragment.setMovieList(movieList);
//                FragmentManager fragmentManager = getSupportFragmentManager();
//                fragmentManager.beginTransaction().add(R.id.movie_list_container, masterListFragment).commit();
//            }
//
//            @Override
//            public void onFailure(Call<MovieResult> call, Throwable t) {
//                t.printStackTrace();
//                Toast.makeText(MainActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
}
