package pl.oziem.whattowatch

import android.app.Activity
import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import io.reactivex.Completable
import io.reactivex.CompletableEmitter
import io.reactivex.Single
import io.reactivex.SingleEmitter
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import pl.oziem.datasource.dataprovider.DataProvider
import pl.oziem.datasource.models.ErrorState
import pl.oziem.datasource.models.PopulatedState
import pl.oziem.datasource.models.ResourceState
import pl.oziem.datasource.models.configuration.Configuration
import pl.oziem.whattowatch.sharedpref.SharedPreferenceMediator
import pl.oziem.whattowatch.splash.SplashViewModel

/** Created by marcinoziem on 12/03/2018 WhatToWatch.
 */
class SplashPresenterTest {

  @Rule
  @JvmField
  val instantExecutorRule = InstantTaskExecutorRule() //Very very important

  @Mock
  private lateinit var dataProvider: DataProvider
  @Mock
  private lateinit var sharedPref: SharedPreferenceMediator
  @Mock
  private lateinit var activity: Activity
  @Mock
  lateinit var observer: Observer<ResourceState>
  private lateinit var viewModel: SplashViewModel

  @Before
  fun init() {
    MockitoAnnotations.initMocks(this)
    viewModel = SplashViewModel(dataProvider, sharedPref)
      .apply { fetchedData.observeForever(observer) }
  }

  private fun mockFetchRemoteConfig(block: CompletableEmitter.() -> Unit) {
    `when`(dataProvider.fetchRemoteConfig(activity))
      .thenReturn(Completable.create { e -> e.block() })
  }

  private fun mockGetConfiguration(block: SingleEmitter<Configuration>.() -> Unit) {
    `when`(dataProvider.getConfiguration()).thenReturn(Single.create { e -> e.block() })
    `when`(dataProvider.getLanguages()).thenReturn(Single.create { e -> e.onSuccess(emptyList()) })
  }

  @Test
  fun fetchData_test_fetchFail() {
    val runtimeException = RuntimeException()
    mockFetchRemoteConfig { onError(runtimeException) }

    viewModel.fetchData(activity)

    verify(observer).onChanged(ErrorState(runtimeException.message))
  }

  @Test
  fun fetchData_test_fetchSuccess_configurationAlreadySaved() {
    mockFetchRemoteConfig { onComplete() }
    `when`(sharedPref.hasImageConfigBeenSaved()).thenReturn(true)

    viewModel.fetchData(activity)

    verify(observer).onChanged(PopulatedState)
  }

  @Test
  fun fetchData_test_fetchSuccess_configurationFail() {
    val errorMessage = "error message"
    mockFetchRemoteConfig { onComplete() }
    `when`(sharedPref.hasImageConfigBeenSaved()).thenReturn(false)
    mockGetConfiguration { onError(RuntimeException(errorMessage)) }

    viewModel.fetchData(activity)

    verify(observer).onChanged(ErrorState(errorMessage))
  }

  @Test
  fun fetchData_test_all_success() {
    mockFetchRemoteConfig { onComplete() }
    `when`(sharedPref.hasImageConfigBeenSaved()).thenReturn(false)
    mockGetConfiguration { onSuccess(Configuration()) }

    viewModel.fetchData(activity)

    verify(observer).onChanged(PopulatedState)
  }
}
