package pl.oziem.datasource.dataprovider

import io.reactivex.Single
import pl.oziem.datasource.models.Language
import pl.oziem.datasource.models.configuration.Configuration
import pl.oziem.datasource.models.movie.MovieDetails
import pl.oziem.datasource.models.movie.MovieDiscoveryResponse

/**
* Created by MarcinOz on 2018-03-02 WhatToWatch.
*/
interface DataProvider {
  fun getMovieDetailsById(movieId: Int): Single<MovieDetails>
  suspend fun fetchRemoteConfig()
  fun getMovieDiscover(page: Int): Single<MovieDiscoveryResponse>
  suspend fun getConfiguration(): Configuration
  suspend fun getLanguages(): List<Language>
}
