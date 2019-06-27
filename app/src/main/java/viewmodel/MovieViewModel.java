package viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import data.MovieResult;
import datasource.MovieDataSource;
import datasource.MovieDataSourceFactory;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@SuppressWarnings("unchecked")
public class MovieViewModel extends AndroidViewModel {

    LiveData<MovieDataSource> movieDataSourceLiveData;
    private Executor executor;

    public String getCategory() {
        return category;
    }

    private String category;
    private MovieDataSourceFactory movieDataSourceFactory;
    public LiveData<PagedList<MovieResult.Result>> moviePagedList;

    public MovieViewModel(Application application) {
        super(application);

        movieDataSourceFactory = new MovieDataSourceFactory();
        movieDataSourceLiveData = movieDataSourceFactory.getMutableLiveData();
        PagedList.Config config = (new PagedList.Config.Builder())
                .setEnablePlaceholders(true)
                .setInitialLoadSizeHint(10)
                .setPageSize(20)
                .setPrefetchDistance(4)
                .build();

        executor = Executors.newFixedThreadPool(5);
        moviePagedList = (new LivePagedListBuilder<Long, MovieResult.Result>(movieDataSourceFactory, config))
                .setFetchExecutor(executor)
                .build();
    }

    public void setCategory(String category) {
        this.category = category;
        movieDataSourceFactory.setCategory(category);
    }

    public LiveData<PagedList<MovieResult.Result>> getMoviesPagedList() {
        return moviePagedList;
    }
}
