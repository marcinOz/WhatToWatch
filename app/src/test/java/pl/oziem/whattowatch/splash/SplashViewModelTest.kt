package pl.oziem.whattowatch.splash

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
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
import pl.oziem.whattowatch.TestContextProvider
import pl.oziem.whattowatch.sharedpref.SharedPreferenceMediator
import pl.oziem.whattowatch.testutils.TestCoroutineRule

/** Created by marcinoziem on 12/03/2018 WhatToWatch.
 */
class SplashViewModelTest {

  @get:Rule
  val instantExecutorRule = InstantTaskExecutorRule() //Very very important

  @get:Rule
  val testCoroutineRule = TestCoroutineRule()

  @Mock
  private lateinit var dataProvider: DataProvider
  @Mock
  private lateinit var sharedPref: SharedPreferenceMediator
  @Mock
  lateinit var observer: Observer<ResourceState>
  private lateinit var viewModel: SplashViewModel

  @Before
  fun init() {
    MockitoAnnotations.initMocks(this)
  }

  private fun initViewModel() {
    viewModel = SplashViewModel(dataProvider, sharedPref, TestContextProvider())
      .apply { fetchedData.observeForever(observer) }
  }

  @Test
  fun fetchData_test_fetchFail() = testCoroutineRule.runBlockingTest {
    val runtimeException = RuntimeException("message")
    `when`(dataProvider.fetchRemoteConfig()).thenThrow(runtimeException)

    initViewModel()

    verify(observer).onChanged(ErrorState(runtimeException.message))
  }

  @Test
  fun fetchData_test_fetchSuccess_configurationAlreadySaved() = testCoroutineRule.runBlockingTest {
    `when`(dataProvider.fetchRemoteConfig()).thenReturn(Unit)
    `when`(sharedPref.hasImageConfigBeenSaved()).thenReturn(true)

    initViewModel()

    verify(observer).onChanged(PopulatedState)
  }

  @Test
  fun fetchData_test_fetchSuccess_configurationFail() = testCoroutineRule.runBlockingTest {
    val errorMessage = "error message"
    `when`(dataProvider.fetchRemoteConfig()).thenReturn(Unit)
    `when`(sharedPref.hasImageConfigBeenSaved()).thenReturn(false)
    `when`(dataProvider.getConfiguration()).thenThrow(RuntimeException(errorMessage))
    `when`(dataProvider.getLanguages()).thenReturn(emptyList())

    initViewModel()

    verify(observer).onChanged(ErrorState(errorMessage))
  }

  @Test
  fun fetchData_test_all_success() = testCoroutineRule.runBlockingTest {
    `when`(dataProvider.fetchRemoteConfig()).thenReturn(Unit)
    `when`(sharedPref.hasImageConfigBeenSaved()).thenReturn(false)
    `when`(dataProvider.getConfiguration()).thenReturn(Configuration())
    `when`(dataProvider.getLanguages()).thenReturn(emptyList())

    initViewModel()

    verify(observer).onChanged(PopulatedState)
  }
}
