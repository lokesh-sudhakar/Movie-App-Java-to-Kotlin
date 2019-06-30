package datasource;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import java.util.ArrayList;
import java.util.Objects;

import data.MovieResult;
import retrofit.RetroFitInterface;
import retrofit.RetrofitInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import security.SecuredData;


public class MovieDataSource extends PageKeyedDataSource<Integer, MovieResult.Result> {

    private Context context;

    private static final int FIRST_PAGE = 1;
    private static String CATEGORY = "popular";
    private static String LANGUAGE = "en-US";
    private static int PAGE = 1;


    public MovieDataSource(Application application, String category) {
        this.context = application;
        CATEGORY = category;
        Log.d("showCategory in data source ", "" + this.CATEGORY);
    }


    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, MovieResult.Result> callback) {

        RetroFitInterface retroFitInterface = RetrofitInstance.getService();

        Call<MovieResult> call = retroFitInterface.getMovies(CATEGORY, SecuredData.API_KEY, LANGUAGE, PAGE);
        call.enqueue(new Callback<MovieResult>() {
            @Override
            public void onResponse(Call<MovieResult> call, Response<MovieResult> response) {
                ArrayList<MovieResult.Result> movies;

                if (response.body() != null && response.body().getResults() != null) {
                    movies = (ArrayList<MovieResult.Result>) response.body().getResults();

                    callback.onResult(movies, null, FIRST_PAGE + 1);

                }

            }

            @Override
            public void onFailure(Call<MovieResult> call, Throwable t) {
                t.printStackTrace();

            }
        });
    }

    @Override
    public void loadBefore(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, MovieResult.Result> callback) {

        RetroFitInterface retroFitInterface = RetrofitInstance.getService();


        Call<MovieResult> call = retroFitInterface.getMovies(CATEGORY, SecuredData.API_KEY, LANGUAGE, params.key);
        call.enqueue(new Callback<MovieResult>() {
            @Override
            public void onResponse(Call<MovieResult> call, Response<MovieResult> response) {

                Integer key = (params.key > 1) ? params.key - 1 : null;
                if (response.body() != null) {
                    callback.onResult(response.body().getResults(), key);
                }
            }

            @Override
            public void onFailure(Call<MovieResult> call, Throwable t) {
                Log.d("error", "retriving failed");
                t.printStackTrace();
            }
        });
    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, MovieResult.Result> callback) {
        RetroFitInterface retroFitInterface = RetrofitInstance.getService();

        Call<MovieResult> call = retroFitInterface.getMovies(CATEGORY,SecuredData.API_KEY, LANGUAGE, params.key);
        call.enqueue(new Callback<MovieResult>() {
            @Override
            public void onResponse(Call<MovieResult> call, Response<MovieResult> response) {
                long nextKey = (Objects.equals(params.key, response.body().getTotalResults())) ? null : params.key + 1;
                if (response.isSuccessful()) {
                    callback.onResult(response.body().getResults(), (int) nextKey);
                }
            }

            @Override
            public void onFailure(Call<MovieResult> call, Throwable t) {
                Log.d("error", "retriving failed");
                t.printStackTrace();
            }
        });
    }
}




