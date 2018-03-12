package pl.oziem.whattowatch

import android.app.Activity
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import pl.oziem.datasource.DataProvider
import pl.oziem.datasource.models.configuration.Configuration
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
  private lateinit var activity: Activity
  private lateinit var presenter: SplashPresenter

  @Before
  fun init() {
    MockitoAnnotations.initMocks(this)
    presenter = SplashPresenter(view, dataProvider)
  }

  @Test
  fun initDownloadData_test_fail() {
    Mockito.`when`(dataProvider.fetchRemoteConfig(activity))
      .thenReturn(Completable.create { e -> e.onError(RuntimeException()) })

    presenter.fetchData(activity)

    Mockito.verify(view).showError(null)
  }

  @Test
  fun initDownloadData_test_fetchSuccess_configuration_fail() {
    val errorMessage = "error message"
    Mockito.`when`(dataProvider.fetchRemoteConfig(activity))
      .thenReturn(Completable.create { e -> e.onComplete() })
    Mockito.`when`(dataProvider.getConfiguration())
      .thenReturn(Single.create { e -> e.onError(RuntimeException(errorMessage)) })

    presenter.fetchData(activity)

    Mockito.verify(view).showError(errorMessage)
  }

  @Test
  fun initDownloadData_test_all_success() {
    Mockito.`when`(dataProvider.fetchRemoteConfig(activity))
      .thenReturn(Completable.create { e -> e.onComplete() })

    Mockito.`when`(dataProvider.getConfiguration())
      .thenReturn(Single.just(Configuration()))

    presenter.fetchData(activity)

    Mockito.verify(view).onDataFetched()
  }
}
