package pl.oziem.whattowatch

import android.app.Activity
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import pl.oziem.datasource.models.MovieDiscoveryResponse
import pl.oziem.whattowatch.main.MovieListContract
import pl.oziem.whattowatch.main.MovieListPresenter
import pl.oziem.whattowatch.stubs.DataProviderStub
import java.util.*

class MovieListPresenterTest {

  private val dataProvider = DataProviderStub()
  private lateinit var presenter: MovieListPresenter
  @Mock
  private lateinit var view: MovieListContract.View
  @Mock
  private lateinit var activity: Activity

  @Before
  fun init() {
    MockitoAnnotations.initMocks(this)
    presenter = MovieListPresenter(view, dataProvider)
  }

  @Test
  fun initDownloadData_test_fail() {
    `when`(dataProvider.fetchRemoteConfig(any()))
      .thenReturn(Completable.create { e -> e.onError(RuntimeException()) })

    presenter.initDownloadData(activity)

    verify(view).showLoading()
    verify(view).showError(anyString())
  }

  @Test
  fun initDownloadData_test_fetchSuccess_discovery_fail() {
    `when`(dataProvider.fetchRemoteConfig(any()))
      .thenReturn(Completable.create { e -> e.onComplete() })
    `when`(dataProvider.getMovieDiscover())
      .thenReturn(Single.create { e -> e.onError(RuntimeException()) })

    presenter.initDownloadData(activity)

    verify(view).showLoading()
    verify(view).showError(anyString())
  }

  @Test
  fun initDownloadData_test_all_success_with_empty() {
    `when`(dataProvider.fetchRemoteConfig(any()))
      .thenReturn(Completable.create { e -> e.onComplete() })

    val discoverResponse = mock(MovieDiscoveryResponse::class.java)
    `when`(discoverResponse.totalResults)
      .thenReturn(0)
    `when`(dataProvider.getMovieDiscover())
      .thenReturn(Single.just(discoverResponse))

    presenter.initDownloadData(activity)

    verify(view).showLoading()
    verify(view).showEmptyMessage()
  }

  @Test
  fun initDownloadData_test_all_success_with_data() {
    `when`(dataProvider.fetchRemoteConfig(any()))
      .thenReturn(Completable.create { e -> e.onComplete() })

    val discoverResponse = mock(MovieDiscoveryResponse::class.java)
    `when`(discoverResponse.totalResults)
      .thenReturn(Random().nextInt(999) + 1)
    `when`(dataProvider.getMovieDiscover())
      .thenReturn(Single.just(discoverResponse))

    presenter.initDownloadData(activity)

    verify(view).showLoading()
    verify(view).populate(discoverResponse)
  }
}
