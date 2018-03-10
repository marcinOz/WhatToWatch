package pl.oziem.whattowatch.main

import android.app.Activity
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
    view.showLoading()
    dataProvider.fetchRemoteConfig(activity).subscribeBy(
      onComplete = { getMovieDiscover() },
      onError = { view.showError("server error") }
    )
  }

  override fun getMovieDiscover() {
    dataProvider.getMovieDiscover().subscribeBy(
      onSuccess = { result ->
        if (result.totalResults == 0) view.showEmptyMessage()
        else view.populate(result)
      },
      onError = { error -> view.showError(error.message ?: "server error") }
    )
  }
}
