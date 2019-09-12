package khlafawi.com.movietest.data;

import khlafawi.com.movietest.data.remote.MoviesResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Service {

    @GET("latest")
    Call<MoviesResponse> getLatestMovies(@Query("api_key") String apiKey);

    @GET("popular")
    Call<MoviesResponse> getPopularMovies(@Query("api_key") String apiKey, @Query("page") int pageIndex);

    @GET("top_rated")
    Call<MoviesResponse> getTopRatedMovies(@Query("api_key") String apiKey, @Query("page") int pageIndex);

    @GET("now_playing")
    Call<MoviesResponse> getNowPlayingNowMovies(@Query("api_key") String apiKey, @Query("page") int pageIndex);

}
