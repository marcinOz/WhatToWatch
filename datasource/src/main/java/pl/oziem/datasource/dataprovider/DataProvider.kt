package pl.oziem.datasource.dataprovider

import android.app.Activity
import io.reactivex.Completable
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
  fun fetchRemoteConfig(activity: Activity): Completable
  fun getMovieDiscover(page: Int): Single<MovieDiscoveryResponse>
  fun getConfiguration(): Single<Configuration>
  fun getLanguages(): Single<List<Language>>
}
