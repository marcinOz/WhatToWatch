package pl.oziem.datasource.services

import io.reactivex.Single
import pl.oziem.datasource.models.MovieDetails
import pl.oziem.datasource.models.MovieDiscoveryResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by MarcinOz on 2018-03-02.
 * Copyright (C) 2017 OKE Poland Sp. z o.o. All rights reserved.
 */
interface ApiService {

  @GET("{version}/movie/{id}")
  fun getMovieDetailsById(@Path("version") version: Int, @Path("id") movieId: Int,
                          @Query("api_key") apiKey: String): Single<MovieDetails>

  @GET("{version}/discover/movie")
  fun getMovieDiscover(@Path("version") version: Int,
                       @Query("api_key") apiKey: String): Single<MovieDiscoveryResponse>
}
