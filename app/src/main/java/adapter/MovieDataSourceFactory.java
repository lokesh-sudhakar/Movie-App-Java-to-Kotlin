package adapter;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.example.movieretrofit.MovieResult;
import com.example.movieretrofit.RetroFitInterface;

public class MovieDataSourceFactory extends DataSource.Factory {

     MovieDataSource movieDataSource;
    private String category ="popular";
    private MutableLiveData<MovieDataSource> mutableLiveData;

    public MovieDataSourceFactory() {

        mutableLiveData=new MutableLiveData<>();
    }

    @Override
    public DataSource create() {

        movieDataSource=new MovieDataSource(category);
        mutableLiveData.postValue(movieDataSource);
        return movieDataSource;
    }

    public void setCategory(String category){
        this.category=category;
        Log.d("showCategory in data source factory"+category,""+this.category);

      movieDataSource.invalidate();
//        Log.d("showCategory in data source factory"+movieDataSource.toString(),""+this.category);
    }
    public MutableLiveData<MovieDataSource> getMutableLiveData() {
        return mutableLiveData;
    }



}
