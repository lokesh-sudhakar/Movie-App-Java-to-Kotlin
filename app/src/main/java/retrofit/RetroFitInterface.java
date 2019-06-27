package retrofit;

import data.MovieResult;
import data.ReviewsData;
import data.VideoDatabase;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetroFitInterface {

    @GET("/3/movie/{category}")
    Call<MovieResult> getMovies(
            @Path("category") String category,
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );


    @GET("/3/movie/{movieId}/videos")
    Call<VideoDatabase> getTrailer(
            @Path("movieId") String movieId,
            @Query("api_key") String apiKey);

    @GET("/3/movie/{movieId}/reviews")
    Call<ReviewsData> getReviews(@Path("movieId") String movieId, @Query("api_key") String apiKey);
}
