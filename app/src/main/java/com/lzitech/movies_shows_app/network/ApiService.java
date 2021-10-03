package com.lzitech.movies_shows_app.network;

import com.lzitech.movies_shows_app.responses.TVShowDetailsResponse;
import com.lzitech.movies_shows_app.responses.TVShowResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("most-popular")
    Call<TVShowResponse> getMostPopularTvShows(@Query("page") int page);

    @GET("show-details")
    Call<TVShowDetailsResponse> getTVShowDetails(@Query("q") long tvShowId);

}
