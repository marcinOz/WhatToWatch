package pl.oziem.datasource

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import pl.oziem.datasource.services.ApiService

/**
 * Created by MarcinOz on 2018-03-02.
 * Copyright (C) 2017 OKE Poland Sp. z o.o. All rights reserved.
 */
class DataProviderImp(val apiService: ApiService) : DataProvider {

  companion object {
    const val API_KEY = "db31875b822b2c06943f308311f83cbd"
  }

  private fun <T> Single<T>.defaultThreads(): Single<T> =
    this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

  override fun getMovieById(movieId: Int): Single<String> =
    apiService.getMovieById(movieId, API_KEY)
      .onErrorReturn { t ->
        if (t.message?.contains("40") == true) {
          return@onErrorReturn "40X"
        }
        return@onErrorReturn ""
      }.defaultThreads()
}
