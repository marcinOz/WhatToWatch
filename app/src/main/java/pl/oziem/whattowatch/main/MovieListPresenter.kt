package pl.oziem.whattowatch.main

import android.util.Log
import io.reactivex.rxkotlin.subscribeBy
import pl.oziem.datasource.DataProvider

/**
 * Created by MarcinOz on 2018-03-06.
 * Copyright (C) 2017 OKE Poland Sp. z o.o. All rights reserved.
 */
class MovieListPresenter(val view: MovieListContract.View,
                         private val dataProvider: DataProvider) : MovieListContract.Presenter {
  companion object {
      const val TAG = "MovieListPresenter"
  }
  override fun getMovieById(movieId: Int) {
    dataProvider.getMovieById(movieId).subscribeBy(
      onSuccess = { result -> view.setText(result)},
      onError = { error -> Log.d(TAG, error.toString())}
    )
  }
}
