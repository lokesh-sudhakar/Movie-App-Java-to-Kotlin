package com.example.ui;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.movieretrofit.R;

public class MainActivity extends AppCompatActivity {

    private boolean mTwoPane;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(findViewById(R.id.linear_layout)!=null){
            mTwoPane=true;

            Toast.makeText(this,"on two pane ",Toast.LENGTH_SHORT);
        }else{
            mTwoPane=false;

        }

    }
}
