package datasource;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import datasource.MovieDataSource;

public class MovieDataSourceFactory extends DataSource.Factory {

    private MovieDataSource movieDataSource;
    private String category = "popular";
    private MutableLiveData<MovieDataSource> mutableLiveData;

    public MovieDataSourceFactory() {
        mutableLiveData = new MutableLiveData<>();
    }

    @Override
    public DataSource create() {
        movieDataSource = new MovieDataSource(category);
        mutableLiveData.postValue(movieDataSource);
        return movieDataSource;
    }

    public void setCategory(String category) {
        this.category = category;
        movieDataSource.invalidate();
    }

    public MutableLiveData<MovieDataSource> getMutableLiveData() {
        return mutableLiveData;
    }
}
