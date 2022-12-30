package com.example.team21.ServerCall;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface StarWarService {
    @GET("people/")
    Call<PeopleResponse> getPeople(
            @Query("search") String searchInput);

    @GET("planets/")
    Call<PlanetsResponse> getPlanets(
            @Query("search") String searchInput);
}
