package viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import data.MovieResult;
import datasource.MovieDataSource;
import datasource.MovieDataSourceFactory;

@SuppressWarnings("unchecked")
public class MovieViewModel extends AndroidViewModel {

    LiveData<MovieDataSource> movieDataSourceLiveData;
    private MutableLiveData<MovieResult.Result> firstMovie = new MutableLiveData<>();
    private Executor executor;
    private boolean firstMovieLoaded =false;
    public boolean isFirstMovieLoaded() {
        return firstMovieLoaded;
    }

    public void setFirstMovieLoaded(boolean firstMovieLoaded) {
        this.firstMovieLoaded = firstMovieLoaded;
    }



    public String getCategory() {
        return category;
    }

    private String category;
    private MovieDataSourceFactory movieDataSourceFactory;
    public LiveData<PagedList<MovieResult.Result>> moviePagedList;

    public MutableLiveData<MovieResult.Result> getFirstMovie() {
        return firstMovie;
    }

    public void setFirstMovie(MovieResult.Result firstMovie) {
        Log.d("movie loaded in on bind view","movie" +firstMovie.toString());
        this.firstMovie.postValue(firstMovie);
//        this.firstMovie = firstMovie;
    }


    public MovieViewModel(Application application) {
        super(application);

        movieDataSourceFactory = new MovieDataSourceFactory(application);
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
