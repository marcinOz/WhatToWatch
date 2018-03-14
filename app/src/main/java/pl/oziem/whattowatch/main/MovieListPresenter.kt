package pl.oziem.whattowatch.main

import android.os.Bundle
import io.reactivex.rxkotlin.subscribeBy
import pl.oziem.datasource.dataprovider.DataProvider
import pl.oziem.datasource.models.Movie
import pl.oziem.datasource.models.MovieDiscoveryResponse

/**
* Created by MarcinOz on 2018-03-06 WhatToWatch.
*/
class MovieListPresenter(private val view: MovieListContract.View,
                         private val dataProvider: DataProvider) : MovieListContract.Presenter {
  companion object {
    private const val MOVIE_DISCOVER_RESPONSE = "MOVIE_DISCOVER_RESPONSE"
  }

  private var movieDiscoveryResponse: MovieDiscoveryResponse? = null

  override fun saveInstanceState(bundle: Bundle?): Bundle? = bundle?.apply {
    movieDiscoveryResponse?.let { putParcelable(MOVIE_DISCOVER_RESPONSE, it) }
  }

  override fun readSavedInstanceState(bundle: Bundle?): List<Movie> {
    movieDiscoveryResponse = bundle?.getParcelable(MOVIE_DISCOVER_RESPONSE)
    return movieDiscoveryResponse?.movies ?: listOf()
  }

  override fun getMovieDiscover() {
    view.showLoading()
    dataProvider.getMovieDiscover().subscribeBy(
      onSuccess = { result ->
        movieDiscoveryResponse = result
        if (result.totalResults == 0) view.showEmptyMessage()
        else result.movies?.apply { view.populate(this) }
      },
      onError = { error -> view.showError(error.message) }
    )
  }
}
