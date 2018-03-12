package pl.oziem.whattowatch

import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import pl.oziem.datasource.DataProvider
import pl.oziem.datasource.models.Movie
import pl.oziem.datasource.models.MovieDiscoveryResponse
import pl.oziem.whattowatch.main.MovieListContract
import pl.oziem.whattowatch.main.MovieListPresenter
import java.util.*

class MovieListPresenterTest {

  @Mock
  private lateinit var dataProvider: DataProvider
  @Mock
  private lateinit var view: MovieListContract.View
  private lateinit var presenter: MovieListPresenter

  @Before
  fun init() {
    MockitoAnnotations.initMocks(this)
    presenter = MovieListPresenter(view, dataProvider)
  }

  @Test
  fun initDownloadData_test_discovery_fail() {
    val errorMessage = "error message"
    `when`(dataProvider.getMovieDiscover())
      .thenReturn(Single.create { e -> e.onError(RuntimeException(errorMessage)) })

    presenter.getMovieDiscover()

    verify(view).showLoading()
    verify(view).showError(errorMessage)
  }

  @Test
  fun initDownloadData_test_success_with_empty() {
    val discoverResponse = MovieDiscoveryResponse()
    `when`(dataProvider.getMovieDiscover())
      .thenReturn(Single.just(discoverResponse))

    presenter.getMovieDiscover()

    verify(view).showLoading()
    verify(view).showEmptyMessage()
  }

  @Test
  fun initDownloadData_test_success_with_data() {
    val discoverResponse = MovieDiscoveryResponse(
      totalResults = Random().nextInt(999) + 1,
      movies = listOf(Movie()))
    `when`(dataProvider.getMovieDiscover())
      .thenReturn(Single.just(discoverResponse))

    presenter.getMovieDiscover()

    verify(view).showLoading()
    verify(view).populate(discoverResponse.movies!!)
  }
}
