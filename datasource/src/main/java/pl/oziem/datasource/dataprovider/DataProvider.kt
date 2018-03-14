package pl.oziem.datasource.dataprovider

import android.app.Activity
import io.reactivex.Completable
import io.reactivex.Single
import pl.oziem.datasource.models.MovieDetails
import pl.oziem.datasource.models.MovieDiscoveryResponse
import pl.oziem.datasource.models.configuration.Configuration

/**
* Created by MarcinOz on 2018-03-02 WhatToWatch.
*/
interface DataProvider {
  fun getMovieDetailsById(movieId: Int): Single<MovieDetails>
  fun fetchRemoteConfig(activity: Activity): Completable
  fun getMovieDiscover(): Single<MovieDiscoveryResponse>
  fun getConfiguration(): Single<Configuration>
}
