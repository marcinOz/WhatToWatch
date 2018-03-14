package pl.oziem.whattowatch

import io.reactivex.Single
import io.reactivex.SingleEmitter
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import pl.oziem.datasource.dataprovider.DataProvider
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

  private fun mockGetMovieDiscover(block: SingleEmitter<MovieDiscoveryResponse>.() -> Unit) {
    `when`(dataProvider.getMovieDiscover()).thenReturn(Single.create { e -> e.block() })
  }

  @Test
  fun getMovieDiscover_test_fail() {
    val errorMessage = "error message"
    mockGetMovieDiscover { onError(RuntimeException(errorMessage)) }

    presenter.getMovieDiscover()

    verify(view).showLoading()
    verify(view).showError(errorMessage)
  }

  @Test
  fun getMovieDiscover_test_success_with_empty() {
    mockGetMovieDiscover { onSuccess(MovieDiscoveryResponse()) }

    presenter.getMovieDiscover()

    verify(view).showLoading()
    verify(view).showEmptyMessage()
  }

  @Test
  fun getMovieDiscover_test_success_with_data() {
    val discoverResponse = MovieDiscoveryResponse(
      totalResults = Random().nextInt(999) + 1,
      movies = listOf(Movie())
    )
    mockGetMovieDiscover { onSuccess(discoverResponse) }

    presenter.getMovieDiscover()

    verify(view).showLoading()
    verify(view).populate(discoverResponse.movies!!)
  }
}
