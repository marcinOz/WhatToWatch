package pl.oziem.whattowatch.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import pl.oziem.commons.CoroutineContextProvider
import pl.oziem.datasource.dataprovider.DataProvider
import pl.oziem.datasource.models.ErrorState
import pl.oziem.datasource.models.LoadingState
import pl.oziem.datasource.models.PopulatedState
import pl.oziem.whattowatch.sharedpref.SharedPreferenceMediator
import javax.inject.Inject

class SplashViewModel @Inject constructor(
  private val dataProvider: DataProvider,
  private val sharedPrefMediator: SharedPreferenceMediator,
  contextProvider: CoroutineContextProvider
) : ViewModel() {

  val fetchedData = liveData(contextProvider.IO) {
    emit(LoadingState)
    try {
      dataProvider.fetchRemoteConfig()

      if (sharedPrefMediator.hasImageConfigBeenSaved()) {
        emit(PopulatedState)
        return@liveData
      }

      val config = viewModelScope.async { dataProvider.getConfiguration() }
      val languages = viewModelScope.async { dataProvider.getLanguages() }

      sharedPrefMediator.saveImageConfiguration(config.await().imagesConfig)
      sharedPrefMediator.saveLanguageConfiguration(languages.await())
      emit(PopulatedState)
    } catch (e: Throwable) {
      emit(ErrorState(e.message))
    }
  }
}
