package pl.oziem.whattowatch.main

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.PageKeyedDataSource
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import pl.oziem.datasource.dataprovider.DataProvider
import pl.oziem.datasource.models.*
import pl.oziem.datasource.models.movie.Movie

/**
 * Created by marcinoziem
 * on 26/06/2018 WhatToWatch.
 */
class MovieListDataSource(private val dataProvider: DataProvider,
                          private val compositeDisposable: CompositeDisposable)
  : PageKeyedDataSource<Int, Movie>() {

  val movieDiscover = MutableLiveData<ResourceState<List<Movie>>>()

  override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Movie>) {
    getMovieDiscover(1, { callback.onResult(it, null, 2) })
  }

  override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
    val after = if (params.key > 1000) null else params.key + 1
    getMovieDiscover(params.key, { callback.onResult(it, after) })
  }

  override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
    val before = if (params.key == 1) null else params.key - 1
    getMovieDiscover(params.key, { callback.onResult(it, before) })
  }

  private fun getMovieDiscover(page: Int, callback: (List<Movie>) -> Unit) {
    movieDiscover.postValue(LoadingState())
    compositeDisposable.add(dataProvider.getMovieDiscover(page).subscribeBy(
      onSuccess = { result ->
        if (result.totalResults == 0) movieDiscover.postValue(EmptyState())
        else result.movies?.apply {
          movieDiscover.postValue(PopulatedState(this))
          callback(this)
        }
      },
      onError = { error -> movieDiscover.postValue(ErrorState(error.message)) }
    ))
  }
}
