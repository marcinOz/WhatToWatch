package pl.oziem.whattowatch.splash

import android.app.Activity
import io.reactivex.rxkotlin.Singles
import io.reactivex.rxkotlin.subscribeBy
import pl.oziem.datasource.dataprovider.DataProvider
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
    if (sharedPrefMediator.hasImageConfigBeenSaved()) {
      view.onDataFetched()
      return
    }
    Singles.zip(dataProvider.getConfiguration(), dataProvider.getLanguages())
      .subscribeBy(
      onSuccess = { configurationAndLanguages ->
        sharedPrefMediator.saveImageConfiguration(configurationAndLanguages.first.imagesConfig)
        sharedPrefMediator.saveLanguageConfiguration(configurationAndLanguages.second)
        view.onDataFetched()
      },
      onError = { error -> view.showError(error.message) }
    )
  }
}