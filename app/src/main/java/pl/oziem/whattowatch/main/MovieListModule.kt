package pl.oziem.whattowatch.main

import dagger.Module
import dagger.Provides
import pl.oziem.datasource.DataProvider


/**
 * Created by MarcinOz on 2018-03-06.
 * Copyright (C) 2017 OKE Poland Sp. z o.o. All rights reserved.
 */
@Module
class MovieListModule {
  @Provides
  fun provideMainView(activity: MovieListActivity): MovieListContract.View {
    return activity
  }

  @Provides
  fun provideMainPresenter(movieListView: MovieListContract.View, dataProvider: DataProvider): MovieListContract.Presenter {
    return MovieListPresenter(movieListView, dataProvider)
  }
}
