package pl.oziem.whattowatch.main

import android.arch.lifecycle.LiveData
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
class MovieListViewModel @Inject constructor(dataProvider: DataProvider) : ViewModel() {

  val pagedListData: LiveData<PagedList<Movie>>
  private val compositeDisposable = CompositeDisposable()
  private val sourceFactory: MovieListDataSourceFactory

  init {
    sourceFactory = MovieListDataSourceFactory(dataProvider, compositeDisposable)
    val config = PagedList.Config.Builder()
      .setPageSize(PAGE_SIZE)
      .setEnablePlaceholders(false)
      .build()
    pagedListData = LivePagedListBuilder<Int, Movie>(sourceFactory, config).build()
  }

  fun getLoadState(): LiveData<ResourceState> =
    Transformations.switchMap<MovieListDataSource, ResourceState>(
      sourceFactory.movieListDataSourceLiveData
    ) { it.movieDiscover }

  companion object {
      const val PAGE_SIZE = 2
  }
}
