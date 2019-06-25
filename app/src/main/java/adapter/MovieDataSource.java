package adapter;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.paging.PageKeyedDataSource;

import com.example.movieretrofit.MainActivity;
import com.example.movieretrofit.MasterListFragment;
import com.example.movieretrofit.MovieResult;
import com.example.movieretrofit.R;
import com.example.movieretrofit.RetroFitInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieDataSource extends PageKeyedDataSource<Integer, MovieResult.Result> {




    public static final int PAGE_SIZE = 50;
    private static final int FIRST_PAGE = 1;
    private static final String SITE_NAME ="themoviedb";



    private  static String CATEGORY = "popular";


    private static String CATEGORY_TOP_RATED = "top_rated";
    private static final String BASE_URL = "https://api.themoviedb.org";
    private static String API_KEY = "c4fd0359f29736975ba764defb5f2878";
    private static String LANGUAGE = "en-US";
    private static int PAGE = 1;

    public MovieDataSource(String  category) {
        this.CATEGORY = category;
        Log.d("showCategory in data source ",""+this.CATEGORY);
    }


    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, MovieResult.Result> callback) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetroFitInterface retroFitInterface = retrofit.create(RetroFitInterface.class);

        Log.d("error","the category is "+CATEGORY);
        Call<MovieResult> call = retroFitInterface.getMovies(CATEGORY,API_KEY,LANGUAGE,PAGE);
        call.enqueue(new Callback<MovieResult>() {
            @Override
            public void onResponse(Call<MovieResult> call, Response<MovieResult> response) {
                ArrayList<MovieResult.Result> movies = new ArrayList<>();

                if(response.body() != null && response.body().getResults() != null) {
                    movies = (ArrayList<MovieResult.Result>) response.body().getResults();

                    callback.onResult(movies, null, FIRST_PAGE+1);
                }
//               progressDialog.dismiss();
//                callback.onResult(response.body().getResults(),null,FIRST_PAGE+1);
//                MovieResult movies = response.body();
//                List<MovieResult.Result> movieList = movies.getResults();
//                MasterListFragment masterListFragment = new MasterListFragment();
//                masterListFragment.setMovieList(movieList);

            }

            @Override
            public void onFailure(Call<MovieResult> call, Throwable t) {

                Log.d("error","the category is "+CATEGORY);
//                Toast.makeText(, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    @Override
    public void loadBefore(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, MovieResult.Result> callback) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetroFitInterface retroFitInterface = retrofit.create(RetroFitInterface.class);


        Call<MovieResult> call = retroFitInterface.getMovies(CATEGORY,API_KEY,LANGUAGE,params.key);
        call.enqueue(new Callback<MovieResult>() {
            @Override
            public void onResponse(Call<MovieResult> call, Response<MovieResult> response) {
//               progressDialog.dismiss();

                Integer key =  (params.key>1)? params.key-1 :null;
                if (response.body()!= null){
                    callback.onResult(response.body().getResults(),key);
                }

//                MovieResult movies = response.body();
//                List<MovieResult.Result> movieList = movies.getResults();
//                MasterListFragment masterListFragment = new MasterListFragment();
//                masterListFragment.setMovieList(movieList);

            }

            @Override
            public void onFailure(Call<MovieResult> call, Throwable t) {
                Log.d("error","retriving failed");
                t.printStackTrace();
//                Toast.makeText(this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, MovieResult.Result> callback) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetroFitInterface retroFitInterface = retrofit.create(RetroFitInterface.class);


        Call<MovieResult> call = retroFitInterface.getMovies(CATEGORY,API_KEY,LANGUAGE,params.key);
        call.enqueue(new Callback<MovieResult>() {
            @Override
            public void onResponse(Call<MovieResult> call, Response<MovieResult> response) {
                long nextKey = (params.key == response.body().getTotalResults()) ? null : params.key+1;
                if (response.isSuccessful()){
                    callback.onResult(response.body().getResults(),(int)nextKey);
                }

//                MovieResult movies = response.body();
//                List<MovieResult.Result> movieList = movies.getResults();
//                MasterListFragment masterListFragment = new MasterListFragment();
//                masterListFragment.setMovieList(movieList);

            }

            @Override
            public void onFailure(Call<MovieResult> call, Throwable t) {
                Log.d("error","retriving failed");
                t.printStackTrace();
//                Toast.makeText(this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
