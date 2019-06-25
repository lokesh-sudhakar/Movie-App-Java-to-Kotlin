package com.example.movieretrofit;


//import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Guideline;
import androidx.fragment.app.Fragment;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieDetailFragment extends Fragment {
    private static final String BASE_URL = "https://api.themoviedb.org";
    private static String CATEGORY = "popular";
    private static String API_KEY = "c4fd0359f29736975ba764defb5f2878";
    private static String LANGUAGE = "en-US";
    private static int PAGE = 1;




    Guideline guideline2;
    @BindView(R.id.movie_poster)
    ImageView moviePoster;
    @BindView(R.id.movie_title_on_poster)
    TextView movieTitleOnPoster;
    @BindView(R.id.release_year_view)
    TextView releaseYearView;
    @BindView(R.id.movie_title_below_poster)
    TextView movieTitleBelowPoster;
    @BindView(R.id.rating_bar)
    RatingBar ratingBar;
    @BindView(R.id.summary)
    TextView summary;

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

        if(savedInstanceState!=null){
            setDataList((List<MovieResult.Result>) savedInstanceState.getSerializable("data_list"));
            setPosition(savedInstanceState.getInt("item_position", 0));
        }
//        movieTitleOnPoster = (TextView) rootView.findViewById(R.id.movie_title_on_poster);
//        movieTitleBelowPoster = (TextView) rootView.findViewById(R.id.movie_title_below_poster);
//        releaseYearView = (TextView) rootView.findViewById(R.id.release_year_view);
//        ratingBar = (RatingBar) rootView.findViewById(R.id.rating_bar);
//        summary = (TextView) rootView.findViewById(R.id.release_year_view);
//        moviePoster = (ImageView) rootView.findViewById(R.id.movie_poster);
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        RetroFitInterface retroFitInterface = retrofit.create(RetroFitInterface.class);
//
//
//        Call<MovieResult> call = retroFitInterface.getMovies(CATEGORY, API_KEY, LANGUAGE, PAGE);
//        call.enqueue(new Callback<MovieResult>() {
//            @Override
//            public void onResponse(Call<MovieResult> call, Response<MovieResult> response) {
//
//                MovieResult movies = response.body();
//                List<MovieResult.Result> movieList = movies.getResults();
//                // Toast.makeText(context, "Something went right...Please try later!", Toast.LENGTH_SHORT).show();
//                setDataList(movieList);
//
//                setPosition(0);
                movieTitleOnPoster.setText(getDataList().get(getPosition()).getTitle());
                movieTitleBelowPoster.setText(getDataList().get(getPosition()).getTitle());
                releaseYearView.setText(getDataList().get(getPosition()).getReleaseDate().substring(0,4));
                ratingBar.setRating(getDataList().get(getPosition()).getVoteAverage());
                summary.setText(getDataList().get(getPosition()).getOverview());
                Picasso.Builder builder = new Picasso.Builder(context);
                builder.downloader(new OkHttp3Downloader(context));
                builder.build().load("https://image.tmdb.org/t/p/w185/" + getDataList().get(getPosition()).getBackdropPath())
                        .into(moviePoster);
//                Toast.makeText(context, "result movie is " + getDataList().get(getPosition()).getTitle(), Toast.LENGTH_SHORT).show();

//            }
//            @Override
//            public void onFailure(Call<MovieResult> call, Throwable t) {
//                t.printStackTrace();
//                Toast.makeText(context, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
//            }
//        });

//        MovieResult.Result movie = getDataList().get(getPosition());
//        movieTitleOnPoster.setText(getDataList().get(getPosition()).getTitle());
//        movieTitleBelowPoster.setText(getDataList().get(getPosition()).getTitle());
//        releaseYearView.setText(getDataList().get(getPosition()).getReleaseDate());
//        ratingBar.setRating(getDataList().get(getPosition()).getVoteAverage());
//        summary.setText(getDataList().get(getPosition()).getOverview());

//        Picasso.Builder builder = new Picasso.Builder(context);
//        builder.downloader(new OkHttp3Downloader(context));
//        builder.build().load("https://image.tmdb.org/t/p/w185/" + movie.getPosterPath())
//                .into(moviePoster);

        return rootView;
    }
    @Override
    public void onSaveInstanceState(Bundle currentState) {
        currentState.putSerializable("data_list", (Serializable) dataList);
        currentState.putInt("item_position",position);
    }
}
