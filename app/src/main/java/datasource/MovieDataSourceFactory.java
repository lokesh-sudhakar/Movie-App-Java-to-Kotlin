package datasource;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

public class MovieDataSourceFactory extends DataSource.Factory {

    private MovieDataSource movieDataSource;
    private String category = "popular";
    private Context context;
    private MutableLiveData<MovieDataSource> mutableLiveData;

    public MovieDataSourceFactory(Application application) {
        this.context = application;
        mutableLiveData = new MutableLiveData<>();
    }

    @Override
    public DataSource create() {
        movieDataSource = new MovieDataSource((Application) context,category);
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
