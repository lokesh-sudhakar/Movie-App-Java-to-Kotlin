package com.example.movieretrofit;


//import android.annotation.SuppressLint;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.List;

import data.MovieResult;
import datapersistence.Movie;
import datapersistence.MovieRoomDatabase;
import adapter.ReviewAdapter;
import data.ReviewsData;
import adapter.TrailerAdapter;
import data.VideoDatabase;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.RetroFitInterface;
import retrofit.RetrofitInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailFragment extends Fragment implements  TrailerAdapter.TrailerItemClickListener{


    private RecyclerView trailerRecyclerView;
    private TrailerAdapter trailerAdapter;
    private RecyclerView reviewRecyclerView;
    private ReviewAdapter reviewAdapter;
    private MovieRoomDatabase mDb;
    private MovieResult.Result movieResult;
    private Context context;

    public Movie getFavouriteMovie() {
        return favouriteMovie;
    }

    public void setFavouriteMovie(Movie favouriteMovie) {
        this.favouriteMovie = favouriteMovie;
    }

    private Movie favouriteMovie;
    MovieResult.Result movieInfo;

    private boolean isfavouriteselected;

    private static final String BASE_URL_FOR_BACKGROUND_PATH = "https://image.tmdb.org/t/p/w342/";
    private static final String YOUTUBE_BASE_URL = "https://www.youtube.com/watch?v=";

    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.card_view_trailer)
    CardView trailerCardView;
    @BindView(R.id.card_view_review)
    CardView reviewCardView;
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

    public MovieDetailFragment() {
    }

    public void setMovieResult(MovieResult.Result movieResult) {
        this.movieResult = movieResult;
    }

    public MovieResult.Result getMovieResult() {
        return movieResult;
    }

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
        movieInfo = new MovieResult.Result();

        if(favouriteMovie !=null) {
            movieInfo.setId(favouriteMovie.getId());
            movieInfo.setBackdropPath(favouriteMovie.getBackdropPath());
            movieInfo.setOverview(favouriteMovie.getOverview());
            movieInfo.setPosterPath(favouriteMovie.getPosterPath());
            movieInfo.setTitle(favouriteMovie.getTitle());
            movieInfo.setReleaseDate(favouriteMovie.getReleaseDate());
            movieInfo.setVoteAverage((double) favouriteMovie.getVoteAverage());
        }else{
            movieInfo = movieResult;
        }

        setUpToolbar();
        onChangePersistenceData();
        onClickFavourite(movieInfo);
        setDataTOViews();
        performNetworkCallToFetchTrailer(rootView);
        performNetworkCallToFetchReview(rootView);

        return rootView;
    }

    private void onChangePersistenceData() {
        LiveData<List<Movie>> moviesId = MovieRoomDatabase.getDatabase(context.getApplicationContext()).movieDao().getAllMovies();
        moviesId.observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> moviesData) {
                for(int index=0; index<moviesData.size(); index++){
                    if(((Integer) moviesData.get(index).getId()).equals((Integer) movieInfo.getId())){
                        isfavouriteselected = true;
                        fab.setImageResource(R.drawable.ic_favorite_black_24dp);
                    }
                }
            }
        });
    }

    private void onClickFavourite(final MovieResult.Result mMovie) {
        fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context,R.color.whiteBackground)));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final datapersistence.Movie movie = new datapersistence.Movie();
                movie.setId(mMovie.getId());
                movie.setBackdropPath(mMovie.getBackdropPath());
                movie.setOverview(mMovie.getOverview());
                movie.setPosterPath(mMovie.getPosterPath());
                movie.setTitle(mMovie.getTitle());
                movie.setReleaseDate(mMovie.getReleaseDate());
                movie.setVoteAverage(mMovie.getVoteAverage());

                if(isfavouriteselected){
                    isfavouriteselected =false;
                    mDb.movieDao().deleteMovie(movie);
                    fab.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.favourite_unselected));
                }else{
                    isfavouriteselected =true;
                    mDb.movieDao().insert(movie);
                    fab.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_favorite_black_24dp));
                }

            }
        });
    }

    private void setUpToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        toolbar.setTitle(movieInfo.getTitle());
        mDb = MovieRoomDatabase.getDatabase(context);
    }

    private void setDataTOViews() {
        movieTitleBelowPoster.setText(movieInfo.getTitle());
        String releaseYear = movieInfo.getReleaseDate().substring(0, 4);
        releaseYearView.setText(releaseYear);
        ratingBar.setRating(movieInfo.getVoteAverage()/2);
        summary.setText(movieInfo.getOverview());
        Picasso.Builder builder = new Picasso.Builder(context);
        builder.downloader(new OkHttp3Downloader(context));
        builder.build().load(BASE_URL_FOR_BACKGROUND_PATH + movieInfo.getBackdropPath())
                .into(moviePoster);
    }

    private void performNetworkCallToFetchReview(final View rootView) {

        RetroFitInterface retroFitInterface = RetrofitInstance.getService();
        Call<ReviewsData> call = retroFitInterface.getReviews(String.valueOf(movieInfo.getId()),getString(R.string.api_key));
        call.enqueue(new Callback<ReviewsData>() {
            @Override
            public void onResponse(Call<ReviewsData> call, Response<ReviewsData> response) {

                if(response.isSuccessful()) {

                    ReviewsData videoDatabase = response.body();

                    List<ReviewsData.Review> reviewsList = videoDatabase.getResults();
                    if(reviewsList.size()!=0){
                        reviewCardView.setVisibility(View.VISIBLE);
                    }
                    addReviewsToRecyclerView(rootView, reviewsList);
                }
            }
            @Override
            public void onFailure(Call<ReviewsData> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void performNetworkCallToFetchTrailer(final View view) {
        RetroFitInterface retroFitInterface = RetrofitInstance.getService();
        Call<VideoDatabase> call = retroFitInterface.getTrailer(String.valueOf(movieInfo.getId()), getString(R.string.api_key));
        call.enqueue(new Callback<VideoDatabase>() {
            @Override
            public void onResponse(Call<VideoDatabase> call, Response<VideoDatabase> response) {
                if(response.isSuccessful()) {
                    VideoDatabase videoDatabase = response.body();
                    List<VideoDatabase.Result> trailerList = videoDatabase.getResults();
                    if(trailerList.size()!=0){
                        trailerCardView.setVisibility(View.VISIBLE);
                    }
                    addTrailerToRecyclerView(view, trailerList);
                }
                else{
                    Toast.makeText(context, "Trailer did not load ", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<VideoDatabase> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    void addTrailerToRecyclerView(View view, List<VideoDatabase.Result> trailerList) {
        trailerRecyclerView = view.findViewById(R.id.trailer_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context,RecyclerView.HORIZONTAL,false);
        trailerRecyclerView.setLayoutManager(linearLayoutManager);
        trailerAdapter = new TrailerAdapter(context,trailerList,this);
        trailerRecyclerView.setAdapter(trailerAdapter);
    }

    void addReviewsToRecyclerView(View view, List<ReviewsData.Review> reviewList) {
        reviewRecyclerView = view.findViewById(R.id.review_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context,RecyclerView.VERTICAL,false);
        reviewRecyclerView.setLayoutManager(linearLayoutManager);
        reviewAdapter = new ReviewAdapter(context,reviewList);
        reviewRecyclerView.setAdapter(reviewAdapter);
    }

    @Override
    public void onListItemClick(int position, List<VideoDatabase.Result> trailerList) {
        String url = YOUTUBE_BASE_URL +trailerList.get(position).getKey();
        Intent implicit = new Intent(Intent.ACTION_VIEW);
        implicit.setData(Uri.parse(url));
        startActivity(implicit);
    }
}
