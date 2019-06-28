package com.example.ui;


import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieretrofit.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.List;

import adapter.ReviewAdapter;
import adapter.TrailerAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import data.MovieResult;
import data.ReviewsData;
import data.VideoDatabase;
import datapersistence.Movie;
import datapersistence.MovieRoomDatabase;
import retrofit.RetroFitInterface;
import retrofit.RetrofitInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailFragment extends Fragment implements TrailerAdapter.TrailerItemClickListener {


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
    private MovieResult.Result movie;
    private boolean isfavouriteselected;
    private List<VideoDatabase.Result> trailerList;


    private static final String BASE_URL_FOR_BACKGROUND_PATH = "https://image.tmdb.org/t/p/w342/";
    private static final String YOUTUBE_BASE_URL = "https://www.youtube.com/watch?v=";
    private String trailerKey;
    private boolean mTwoPane;


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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.movie_detail_fragment, container, false);
        ButterKnife.bind(this, rootView);
        if (savedInstanceState != null) {
            movie = (MovieResult.Result) savedInstanceState.getSerializable("movie");
        }

        movie = new MovieResult.Result();
        mDb = MovieRoomDatabase.getDatabase(context);
        if (favouriteMovie != null) {
            movie.setId(favouriteMovie.getId());
            movie.setBackdropPath(favouriteMovie.getBackdropPath());
            movie.setOverview(favouriteMovie.getOverview());
            movie.setPosterPath(favouriteMovie.getPosterPath());
            movie.setTitle(favouriteMovie.getTitle());
            movie.setReleaseDate(favouriteMovie.getReleaseDate());
            movie.setVoteAverage((double) favouriteMovie.getVoteAverage());
        } else {
            movie = movieResult;
        }

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mTwoPane = false;
            setUpToolbar(savedInstanceState);
            if (savedInstanceState == null) {
                onChangePersistenceData();
                onClickFavourite(movie);
                setDataTOViews(savedInstanceState);
                performNetworkCallToFetchTrailer(rootView);
                performNetworkCallToFetchReview(rootView);
            }
        } else {
            mTwoPane = true;
            onChangePersistenceData();
            onClickFavourite(movie);
            setDataTOViews(savedInstanceState);
            performNetworkCallToFetchTrailer(rootView);
            performNetworkCallToFetchReview(rootView);
        }
        return rootView;
    }

    private void onChangePersistenceData() {
        LiveData<List<Movie>> moviesId = MovieRoomDatabase.getDatabase(context.getApplicationContext()).movieDao().getAllMovies();
        moviesId.observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> moviesData) {
                for (int index = 0; index < moviesData.size(); index++) {
                    if (((Integer) moviesData.get(index).getId()).equals((Integer) movie.getId())) {
                        isfavouriteselected = true;
                        fab.setImageResource(R.drawable.ic_favorite_black_24dp);
                    }
                }
            }
        });
    }

    private void onClickFavourite(final MovieResult.Result mMovie) {
        fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.whiteBackground)));
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

                if (isfavouriteselected) {
                    isfavouriteselected = false;
                    mDb.movieDao().deleteMovie(movie);
                    fab.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.favourite_unselected));
                } else {
                    isfavouriteselected = true;
                    mDb.movieDao().insert(movie);
                    fab.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_favorite_black_24dp));
                }

            }
        });
    }

    private void setUpToolbar(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            if (!mTwoPane) {
                toolbar.setTitle(movie.getTitle());
                ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
                ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getActivity().onBackPressed();
                    }
                });
            }
        }
    }


    private void setDataTOViews(Bundle savedInstanceState) {

            movie = (MovieResult.Result) savedInstanceState.getSerializable("movie");
            movieTitleBelowPoster.setText(movie.getTitle());
            String releaseYear = movie.getReleaseDate().substring(0, 4);
            releaseYearView.setText(releaseYear);
            ratingBar.setRating(movie.getVoteAverage() / 2);
            summary.setText(movie.getOverview());
            Picasso.Builder builder = new Picasso.Builder(context);
            builder.downloader(new OkHttp3Downloader(context));
            builder.build().load(BASE_URL_FOR_BACKGROUND_PATH + movie.getBackdropPath())
                    .into(moviePoster);

    }

    private void performNetworkCallToFetchReview(final View rootView) {

        RetroFitInterface retroFitInterface = RetrofitInstance.getService();
        Call<ReviewsData> call = retroFitInterface.getReviews(String.valueOf(movie.getId()), getString(R.string.api_key));
        call.enqueue(new Callback<ReviewsData>() {
            @Override
            public void onResponse(Call<ReviewsData> call, Response<ReviewsData> response) {

                if (response.isSuccessful()) {

                    ReviewsData videoDatabase = response.body();

                    List<ReviewsData.Review> reviewsList = videoDatabase.getResults();
                    if (reviewsList.size() != 0) {
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
        Call<VideoDatabase> call = retroFitInterface.getTrailer(String.valueOf(movie.getId()), getString(R.string.api_key));
        call.enqueue(new Callback<VideoDatabase>() {
            @Override
            public void onResponse(Call<VideoDatabase> call, Response<VideoDatabase> response) {
                if (response.isSuccessful()) {
                    VideoDatabase videoDatabase = response.body();
                    trailerList = videoDatabase.getResults();
                    if (trailerList.size() != 0) {
                        trailerCardView.setVisibility(View.VISIBLE);
                        trailerKey = trailerList.get(0).getKey();
                    }
                    addTrailerToRecyclerView(view, trailerList);
                } else {
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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        trailerRecyclerView.setLayoutManager(linearLayoutManager);
        trailerAdapter = new TrailerAdapter(context, trailerList, this);
        trailerRecyclerView.setAdapter(trailerAdapter);
    }

    void addReviewsToRecyclerView(View view, List<ReviewsData.Review> reviewList) {
        reviewRecyclerView = view.findViewById(R.id.review_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        reviewRecyclerView.setLayoutManager(linearLayoutManager);
        reviewAdapter = new ReviewAdapter(context, reviewList);
        reviewRecyclerView.setAdapter(reviewAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_scrolling, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.share_button){
            String url = YOUTUBE_BASE_URL + trailerKey;

            if (url.equals("https://www.youtube.com/watch?v=null")) {
                url = movie.getTitle();
            }

            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_TEXT, url);
            Intent chooser = Intent.createChooser(share, "Share using");

            if (share.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivity(chooser);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClick(int position, List<VideoDatabase.Result> trailerList) {
        String url = YOUTUBE_BASE_URL + trailerList.get(position).getKey();
        Intent implicit = new Intent(Intent.ACTION_VIEW);
        implicit.setData(Uri.parse(url));
        startActivity(implicit);
    }

    @Override
    public void onSaveInstanceState(Bundle currentState) {
        currentState.putSerializable("movie", movie);
    }
}
