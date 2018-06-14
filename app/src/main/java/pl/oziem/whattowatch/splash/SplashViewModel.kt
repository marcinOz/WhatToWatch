package pl.oziem.whattowatch.splash

import android.app.Activity
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.rxkotlin.Singles
import io.reactivex.rxkotlin.subscribeBy
import pl.oziem.datasource.dataprovider.DataProvider
import pl.oziem.datasource.models.ErrorState
import pl.oziem.datasource.models.LoadingState
import pl.oziem.datasource.models.PopulatedState
import pl.oziem.datasource.models.ResourceState
import pl.oziem.whattowatch.sharedpref.SharedPreferenceMediator
import javax.inject.Inject

/**
 * Created by marcinoziem
 * on 14/06/2018 WhatToWatch.
 */
class SplashViewModel @Inject constructor(private val dataProvider: DataProvider,
                                          private val sharedPrefMediator: SharedPreferenceMediator)
  : ViewModel() {

  val fetchedData = MutableLiveData<ResourceState<Unit>>()

  fun fetchData(activity: Activity) {
    fetchedData.postValue(LoadingState())
    dataProvider.fetchRemoteConfig(activity).subscribeBy(
      onComplete = { getConfiguration() },
      onError = { error -> fetchedData.postValue(ErrorState(error.message)) }
    )
  }

  private fun getConfiguration() {
    if (sharedPrefMediator.hasImageConfigBeenSaved()) {
      fetchedData.postValue(PopulatedState(Unit))
      return
    }
    Singles.zip(dataProvider.getConfiguration(), dataProvider.getLanguages())
      .subscribeBy(
        onSuccess = { configurationAndLanguages ->
          sharedPrefMediator.saveImageConfiguration(configurationAndLanguages.first.imagesConfig)
          sharedPrefMediator.saveLanguageConfiguration(configurationAndLanguages.second)
          fetchedData.postValue(PopulatedState(Unit))
        },
        onError = { error -> fetchedData.postValue(ErrorState(error.message)) }
      )
  }
}
