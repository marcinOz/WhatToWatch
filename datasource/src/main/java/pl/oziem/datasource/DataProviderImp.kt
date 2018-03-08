package pl.oziem.datasource

import android.app.Activity
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import pl.oziem.datasource.firebase.FirebaseRemoteConfigMediator
import pl.oziem.datasource.models.MovieDetails
import pl.oziem.datasource.services.ApiService

/**
 * Created by MarcinOz on 2018-03-02.
 * Copyright (C) 2017 OKE Poland Sp. z o.o. All rights reserved.
 */
class DataProviderImp(private val apiService: ApiService,
                      private val firebaseRemoteConfigMediator: FirebaseRemoteConfigMediator)
  : DataProvider {

  private fun <T> Single<T>.defaultThreads(): Single<T> =
    this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

  override fun fetchRemoteConfig(activity: Activity): Completable =
    firebaseRemoteConfigMediator.fetch(activity)

  override fun getMovieDetailsById(movieId: Int): Single<MovieDetails> =
    apiService.getMovieDetailsById(movieId, firebaseRemoteConfigMediator.getTMDbApiKey())
      .defaultThreads()
}
