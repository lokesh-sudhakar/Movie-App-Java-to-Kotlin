package com.example.movieretrofit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Movie;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
}
