package com.example.movieretrofit;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieDetailActivity extends AppCompatActivity {

    private static final String BASE_URL = "https://api.themoviedb.org";
    private static String CATEGORY = "popular";
    private static String API_KEY = "c4fd0359f29736975ba764defb5f2878";
    private static String LANGUAGE = "en-US";
    private static int PAGE = 1;
    private List<MovieResult.Result> movieList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);


            MovieDetailFragment movieDetailFragment = new MovieDetailFragment();

            movieDetailFragment.setDataList((List<MovieResult.Result>) getIntent().getSerializableExtra("data_list"));
            movieDetailFragment.setPosition(getIntent().getIntExtra("item_position", 0));
            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction().add(R.id.movie_details_container, movieDetailFragment).commit();

    }

}
