package pl.oziem.whattowatch.splash

import dagger.Module
import dagger.Provides
import pl.oziem.datasource.DataProvider
import pl.oziem.whattowatch.sharedpref.SharedPreferenceMediator

/** Created by marcinoziem on 12/03/2018 WhatToWatch.
 */
@Module
class SplashModule {
  @Provides
  fun provideMainView(activity: SplashActivity): SplashContract.View = activity

  @Provides
  fun provideMainPresenter(movieListView: SplashContract.View,
                           dataProvider: DataProvider,
                           sharedPrefMediator: SharedPreferenceMediator)
    : SplashContract.Presenter = SplashPresenter(movieListView, dataProvider, sharedPrefMediator)
}
