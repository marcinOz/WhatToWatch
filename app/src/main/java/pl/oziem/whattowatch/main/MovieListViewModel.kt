package pl.oziem.whattowatch.main

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import io.reactivex.disposables.CompositeDisposable
import pl.oziem.datasource.dataprovider.DataProvider
import pl.oziem.datasource.models.ResourceState
import pl.oziem.datasource.models.movie.Movie
import javax.inject.Inject

/**
 * Created by marcinoziem
 * on 13/06/2018 WhatToWatch.
 */
class MovieListViewModel @Inject constructor(private val dataProvider: DataProvider) : ViewModel() {

  val movieDiscover = MutableLiveData<ResourceState<List<Movie>>>()

  val movie: LiveData<PagedList<Movie>>
  private val compositeDisposable = CompositeDisposable()
  private val sourceFactory: MovieListDataSourceFactory

  init {
    sourceFactory = MovieListDataSourceFactory(dataProvider, compositeDisposable)
    val config = PagedList.Config.Builder()
      .setPageSize(2)
      .setEnablePlaceholders(false)
      .setInitialLoadSizeHint(1)
      .build()
    movie = LivePagedListBuilder<Int, Movie>(sourceFactory, config).build()
  }

  fun getLoadState(): LiveData<ResourceState<List<Movie>>> =
    Transformations.switchMap<MovieListDataSource, ResourceState<List<Movie>>>(
      sourceFactory.movieListDataSourceLiveData, { it.movieDiscover }
    )
}
