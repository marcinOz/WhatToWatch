package pl.oziem.whattowatch.splash

import android.app.Activity
import io.reactivex.rxkotlin.subscribeBy
import pl.oziem.datasource.DataProvider

/** Created by marcinoziem on 12/03/2018 WhatToWatch.
 */
class SplashPresenter(private val view: SplashContract.View,
                      private val dataProvider: DataProvider) : SplashContract.Presenter {

  override fun fetchData(activity: Activity) {
    dataProvider.fetchRemoteConfig(activity).subscribeBy(
      onComplete = { getConfiguration() },
      onError = { error -> view.showError(error.message) }
    )
  }

  private fun getConfiguration() {
    dataProvider.getConfiguration().subscribeBy(
      onSuccess = { configuration ->
        //TODO: Save Configuration
        view.onDataFetched()
      },
      onError = { error -> view.showError(error.message) }
    )
  }
}
