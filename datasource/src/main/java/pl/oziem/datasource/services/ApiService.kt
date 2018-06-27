package pl.oziem.datasource.services

import io.reactivex.Single
import pl.oziem.datasource.models.Language
import pl.oziem.datasource.models.configuration.Configuration
import pl.oziem.datasource.models.movie.MovieDetails
import pl.oziem.datasource.models.movie.MovieDiscoveryResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
* Created by MarcinOz on 2018-03-02 WhatToWatch.
*/
interface ApiService {

  @GET("{version}/configuration")
  fun getConfiguration(@Path("version") version: Int,
                          @Query("api_key") apiKey: String): Single<Configuration>

  @GET("{version}/configuration/languages")
  fun getLanguages(@Path("version") version: Int,
                          @Query("api_key") apiKey: String): Single<List<Language>>

  @GET("{version}/movie/{id}")
  fun getMovieDetailsById(@Path("version") version: Int, @Path("id") movieId: Int,
                          @Query("api_key") apiKey: String): Single<MovieDetails>

  @GET("{version}/discover/movie")
  fun getMovieDiscover(@Path("version") version: Int,
                       @Query("api_key") apiKey: String,
                       @Query("page") page: Int,
                       @Query("include_adult") includeAdult: Boolean = false): Single<MovieDiscoveryResponse>
}
