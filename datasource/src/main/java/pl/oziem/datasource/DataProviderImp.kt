package pl.oziem.datasource

import android.app.Activity
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import pl.oziem.datasource.firebase.FirebaseRemoteConfigHelper
import pl.oziem.datasource.models.Movie
import pl.oziem.datasource.services.ApiService

/**
 * Created by MarcinOz on 2018-03-02.
 * Copyright (C) 2017 OKE Poland Sp. z o.o. All rights reserved.
 */
class DataProviderImp(private val apiService: ApiService,
                      private val firebaseRemoteConfigHelper: FirebaseRemoteConfigHelper) : DataProvider {

  private fun <T> Single<T>.defaultThreads(): Single<T> =
    this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

  override fun fetchRemoteConfig(activity: Activity): Completable =
    firebaseRemoteConfigHelper.fetch(activity)

  override fun getMovieById(movieId: Int): Single<Movie> =
    apiService.getMovieById(movieId, firebaseRemoteConfigHelper.getTMDbApiKey())
      .defaultThreads()
}
