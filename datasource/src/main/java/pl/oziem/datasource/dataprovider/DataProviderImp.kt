package pl.oziem.datasource.dataprovider

import android.app.Activity
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import pl.oziem.datasource.models.MovieDetails
import pl.oziem.datasource.models.MovieDiscoveryResponse
import pl.oziem.datasource.models.configuration.Configuration
import pl.oziem.datasource.remote_config.FirebaseRemoteConfigMediator
import pl.oziem.datasource.services.ApiService

/**
* Created by MarcinOz on 2018-03-02 WhatToWatch.
*/
class DataProviderImp(private val apiService: ApiService,
                      private val firebaseRemoteConfigMediator: FirebaseRemoteConfigMediator)
  : DataProvider {

  companion object {
      private const val API_VERSION = 3
  }

  private fun <T> Single<T>.defaultThreads(): Single<T> =
    this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

  override fun fetchRemoteConfig(activity: Activity): Completable =
    firebaseRemoteConfigMediator.fetch(activity)

  override fun getConfiguration(): Single<Configuration> =
    apiService.getConfiguration(API_VERSION, firebaseRemoteConfigMediator.getTMDbApiKey())
      .defaultThreads()

  override fun getMovieDetailsById(movieId: Int): Single<MovieDetails> =
    apiService.getMovieDetailsById(API_VERSION, movieId, firebaseRemoteConfigMediator.getTMDbApiKey())
      .defaultThreads()

  override fun getMovieDiscover(): Single<MovieDiscoveryResponse> =
    apiService.getMovieDiscover(API_VERSION, firebaseRemoteConfigMediator.getTMDbApiKey())
      .defaultThreads()
}
