package pl.oziem.whattowatch.splash

import android.app.Activity
import io.reactivex.rxkotlin.subscribeBy
import pl.oziem.datasource.DataProvider
import pl.oziem.whattowatch.sharedpref.SharedPreferenceMediator

/** Created by marcinoziem on 12/03/2018 WhatToWatch.
 */
class SplashPresenter(private val view: SplashContract.View,
                      private val dataProvider: DataProvider,
                      private val sharedPrefMediator: SharedPreferenceMediator)
  : SplashContract.Presenter {

  override fun fetchData(activity: Activity) {
    dataProvider.fetchRemoteConfig(activity).subscribeBy(
      onComplete = { getConfiguration() },
      onError = { error -> view.showError(error.message) }
    )
  }

  private fun getConfiguration() {
    dataProvider.getConfiguration().subscribeBy(
      onSuccess = { configuration ->
        sharedPrefMediator.saveImageConfiguration(configuration.imagesConfig)
        view.onDataFetched()
      },
      onError = { error -> view.showError(error.message) }
    )
  }
}
