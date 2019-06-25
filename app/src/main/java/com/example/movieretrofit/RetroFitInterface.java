package com.example.movieretrofit;

import adapter.VideoDatabase;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetroFitInterface {

    @GET ("/3/movie/{category}")
    Call<MovieResult> getMovies(
        @Path("category") String category,
        @Query("api_key") String apiKey,
        @Query("language") String language,
        @Query("page") int page
        );
//   https://api.themoviedb.org/3/movie/254/videos?api_key=c4fd0359f29736975ba764defb5f2878&language=en-US
    @GET ("/3/movie/{movieId}/videos")
    Call <VideoDatabase> getTrailer(
            @Path("movieId") String movieId,
            @Query("api_key") String apiKey);

    @GET("https://api.themoviedb.org/3/movie/254/videos?api_key=c4fd0359f29736975ba764defb5f2878&language=en-US")
    Call <VideoDatabase> getAllTrailer();
}
