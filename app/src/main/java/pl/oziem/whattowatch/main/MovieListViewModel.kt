package pl.oziem.whattowatch.main

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.rxkotlin.subscribeBy
import pl.oziem.datasource.dataprovider.DataProvider
import pl.oziem.datasource.models.movie.Movie
import pl.oziem.datasource.models.view_state.*
import javax.inject.Inject

/**
 * Created by marcinoziem
 * on 13/06/2018 WhatToWatch.
 */
class MovieListViewModel @Inject constructor(private val dataProvider: DataProvider) : ViewModel() {

  val movieDiscover = MutableLiveData<ResourceState<List<Movie>>>()

  fun fetchMovieDiscover() {
    movieDiscover.postValue(LoadingState())
    dataProvider.getMovieDiscover().subscribeBy(
      onSuccess = { result ->
        if (result.totalResults == 0) movieDiscover.postValue(EmptyState())
        else result.movies?.apply {
          movieDiscover.postValue(PopulatedState(this))
        }
      },
      onError = { error -> movieDiscover.postValue(ErrorState(error.message)) }
    )
  }
}
