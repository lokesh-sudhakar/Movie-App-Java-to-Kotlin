package com.example.movieretrofit;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import adapter.CustomAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.provider.MediaStore.Video.VideoColumns.CATEGORY;

public class MasterListFragment extends Fragment implements CustomAdapter.ListItemClickListener{


    RecyclerView recyclerView;
    CustomAdapter adapter;
    private static final String BASE_URL = "https://api.themoviedb.org";
    ProgressDialog progressDialog;
    private static String CATEGORY = "popular";
    private static String API_KEY = "c4fd0359f29736975ba764defb5f2878";
    private static String LANGUAGE = "en-US";
    private static int PAGE = 1;
    private Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    // Mandatory empty constructor
    public MasterListFragment() {
    }

    // Inflates the GridView of all AndroidMe images
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_master_list, container, false);
        Retrofit retrofit = new retrofit2.Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetroFitInterface retroFitInterface = retrofit.create(RetroFitInterface.class);


        Call<MovieResult> call = retroFitInterface.getMovies(CATEGORY,API_KEY,LANGUAGE,PAGE);
        call.enqueue(new Callback<MovieResult>() {
            @Override
            public void onResponse(Call<MovieResult> call, Response<MovieResult> response) {
//               progressDialog.dismiss();
                MovieResult movies = response.body();
                List<MovieResult.Result> movieList = movies.getResults();
                generateDataList(rootView,movieList);
            }

            @Override
            public void onFailure(Call<MovieResult> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(context, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
        return rootView;
    }
    private void generateDataList(View view, List<MovieResult.Result> movieList) {
        recyclerView = view.findViewById(R.id.customRecyclerView);
        adapter = new CustomAdapter(context,  movieList,this);
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(new GridLayoutManager(context,2));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Toast.makeText(context, "the position clicked is"+clickedItemIndex, Toast.LENGTH_SHORT).show();
    }
}
