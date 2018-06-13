package pl.oziem.whattowatch

import android.app.Activity
import io.reactivex.Completable
import io.reactivex.CompletableEmitter
import io.reactivex.Single
import io.reactivex.SingleEmitter
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import pl.oziem.datasource.dataprovider.DataProvider
import pl.oziem.datasource.models.configuration.Configuration
import pl.oziem.whattowatch.sharedpref.SharedPreferenceMediator
import pl.oziem.whattowatch.splash.SplashContract
import pl.oziem.whattowatch.splash.SplashPresenter

/** Created by marcinoziem on 12/03/2018 WhatToWatch.
 */
class SplashPresenterTest {

  @Mock
  private lateinit var dataProvider: DataProvider
  @Mock
  private lateinit var view: SplashContract.View
  @Mock
  private lateinit var sharedPref: SharedPreferenceMediator
  @Mock
  private lateinit var activity: Activity
  private lateinit var presenter: SplashPresenter

  @Before
  fun init() {
    MockitoAnnotations.initMocks(this)
    presenter = SplashPresenter(view, dataProvider, sharedPref)
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
    mockFetchRemoteConfig { onError(RuntimeException()) }

    presenter.fetchData(activity)

    verify(view).showError(null)
  }

  @Test
  fun fetchData_test_fetchSuccess_configurationAlreadySaved() {
    mockFetchRemoteConfig { onComplete() }
    `when`(sharedPref.hasImageConfigBeenSaved()).thenReturn(true)

    presenter.fetchData(activity)

    verify(view).onDataFetched()
  }

  @Test
  fun fetchData_test_fetchSuccess_configurationFail() {
    val errorMessage = "error message"
    mockFetchRemoteConfig { onComplete() }
    `when`(sharedPref.hasImageConfigBeenSaved()).thenReturn(false)
    mockGetConfiguration { onError(RuntimeException(errorMessage)) }

    presenter.fetchData(activity)

    verify(view).showError(errorMessage)
  }

  @Test
  fun fetchData_test_all_success() {
    mockFetchRemoteConfig { onComplete() }
    `when`(sharedPref.hasImageConfigBeenSaved()).thenReturn(false)
    mockGetConfiguration { onSuccess(Configuration()) }

    presenter.fetchData(activity)

    verify(view).onDataFetched()
  }
}
