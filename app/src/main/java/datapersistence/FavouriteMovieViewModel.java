package datapersistence;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class FavouriteMovieViewModel extends AndroidViewModel {

    private MovieRepository mMovieRespository;

    private LiveData<List<Movie>> mAllMovies;

    public FavouriteMovieViewModel(@NonNull Application application) {
        super(application);
        mMovieRespository = new MovieRepository(application);
        mAllMovies = mMovieRespository.getmAllMovies();
    }

    public LiveData<List<Movie>> getmAllMovies() {
        return mAllMovies;
    }

    public void deleteMovie(Movie movie) {
        mMovieRespository.delete(movie);
    }

    public void insert(Movie movie) {
        mMovieRespository.insert(movie);
    }
}
