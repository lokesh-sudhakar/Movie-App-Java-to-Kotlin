package com.example.movieretrofit;


//import android.annotation.SuppressLint;
import androidx.appcompat.widget.Toolbar;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.Guideline;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

import adapter.MoviePageListadapter;
import adapter.MovieViewModel;
import adapter.TrailerAdapter;
import adapter.VideoDatabase;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieDetailFragment extends Fragment implements  TrailerAdapter.TrailerItemClickListener{
    private static final String BASE_URL = "https://api.themoviedb.org";
    private static String CATEGORY = "popular";
    private static String API_KEY = "c4fd0359f29736975ba764defb5f2878";
    private static String LANGUAGE = "en-US";
    private static int PAGE = 1;

    RecyclerView trailerRecyclerView;
    TrailerAdapter trailerAdapter;


    Guideline guideline2;
    @BindView(R.id.movie_poster)
    ImageView moviePoster;
    @BindView(R.id.release_year_view)
    TextView releaseYearView;
    @BindView(R.id.movie_title_below_poster)
    TextView movieTitleBelowPoster;
    @BindView(R.id.rating_bar)
    RatingBar ratingBar;
    @BindView(R.id.summary)
    TextView summary;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.app_bar)
    AppBarLayout appBar;
    @BindView(R.id.Synopsis)
    TextView Synopsis;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    public MovieDetailFragment() {
    }

    public List<MovieResult.Result> getDataList() {
        return dataList;
    }

    public void setDataList(List<MovieResult.Result> dataList) {
        this.dataList = dataList;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    private List<MovieResult.Result> dataList;
    private int position;

    public MovieResult.Result getMovie() {
        return movie;
    }

    public void setMovie(MovieResult.Result movie) {
        this.movie = movie;
    }

    private MovieResult.Result movie;

    private Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.movie_detail_fragment, container, false);
        ButterKnife.bind(this, rootView);

        if (savedInstanceState != null) {
            setDataList((List<MovieResult.Result>) savedInstanceState.getSerializable("data_list"));
            setPosition(savedInstanceState.getInt("item_position", 0));
        }

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        toolbar.setTitle(movie.getTitle());
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });




        MovieResult.Result movie = getMovie();
        movieTitleBelowPoster.setText(movie.getTitle());
        releaseYearView.setText(movie.getReleaseDate().substring(0, 4));
        ratingBar.setRating(movie.getVoteAverage());
        summary.setText(movie.getOverview());
        Picasso.Builder builder = new Picasso.Builder(context);
        builder.downloader(new OkHttp3Downloader(context));
        builder.build().load("https://image.tmdb.org/t/p/w342/" + movie.getBackdropPath())
                .into(moviePoster);
        retrofitExtractionFirstTime(rootView);

        return rootView;
    }

    private void retrofitExtractionFirstTime(final View view) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetroFitInterface retroFitInterface = retrofit.create(RetroFitInterface.class);


        Call<VideoDatabase> call = retroFitInterface.getTrailer(""+movie.getId(),API_KEY);
        call.enqueue(new Callback<VideoDatabase>() {
            @Override
            public void onResponse(Call<VideoDatabase> call, Response<VideoDatabase> response) {
//               progressDialog.dismiss();
                if(response.isSuccessful()) {
                    Toast.makeText(context, "Trailer load in on Respose", Toast.LENGTH_SHORT).show();
                    VideoDatabase videoDatabase = response.body();
                    List<VideoDatabase.Result> trailerList = videoDatabase.getResults();
                    generateDataList(view, trailerList);
                    Log.d("trailerId",""+videoDatabase.getResults().get(0).getKey());
                }
                else{
                    Toast.makeText(context, "Trailer did not load in on Respose", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<VideoDatabase> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(context, "Trailer did not load", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void generateDataList(View view, List<VideoDatabase.Result> trailerList) {
        trailerRecyclerView = view.findViewById(R.id.trailer_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context,RecyclerView.HORIZONTAL,false);
        trailerRecyclerView.setLayoutManager(linearLayoutManager);
        trailerAdapter = new TrailerAdapter(context,trailerList,this);
        trailerRecyclerView.setAdapter(trailerAdapter);

    }

    @Override
    public void onSaveInstanceState(Bundle currentState) {
        currentState.putSerializable("data_list", (Serializable) dataList);
        currentState.putInt("item_position", position);
    }

    @Override
    public void onListItemClick(int clickedItemIndex, List<VideoDatabase.Result> trailerList) {
        Toast.makeText(context, "You Clicked Trailer"+clickedItemIndex, Toast.LENGTH_SHORT).show();
    }
}
