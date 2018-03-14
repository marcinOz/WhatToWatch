package pl.oziem.whattowatch.main

import dagger.Module
import dagger.Provides
import pl.oziem.datasource.dataprovider.DataProvider


/** Created by MarcinOz on 2018-03-06 WhatToWatch.
*/
@Module
class MovieListModule {
  @Provides
  fun provideMainView(activity: MovieListActivity): MovieListContract.View = activity

  @Provides
  fun provideMainPresenter(movieListView: MovieListContract.View, dataProvider: DataProvider)
    : MovieListContract.Presenter = MovieListPresenter(movieListView, dataProvider)
}
