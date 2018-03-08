package pl.oziem.whattowatch.main

import android.app.Activity
import android.util.Log
import io.reactivex.rxkotlin.subscribeBy
import pl.oziem.datasource.DataProvider

/**
 * Created by MarcinOz on 2018-03-06.
 * Copyright (C) 2017 OKE Poland Sp. z o.o. All rights reserved.
 */
class MovieListPresenter(private val view: MovieListContract.View,
                         private val dataProvider: DataProvider) : MovieListContract.Presenter {
  companion object {
      const val TAG = "MovieListPresenter"
  }

  override fun initDownloadData(activity: Activity) {
    dataProvider.fetchRemoteConfig(activity).subscribeBy (
      onComplete = { getMovieDetailsById(2) },
      onError = { view.setText("server error") }
    )
  }

  override fun getMovieDetailsById(movieId: Int) {
    dataProvider.getMovieDetailsById(movieId).subscribeBy(
      onSuccess = { result -> view.setText(result.toString())},
      onError = { error -> Log.d(TAG, error.toString())}
    )
  }
}
