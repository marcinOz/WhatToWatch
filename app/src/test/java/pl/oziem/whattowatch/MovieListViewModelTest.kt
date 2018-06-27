package pl.oziem.whattowatch

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
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
import pl.oziem.datasource.models.*
import pl.oziem.datasource.models.movie.Movie
import pl.oziem.datasource.models.movie.MovieDiscoveryResponse
import pl.oziem.whattowatch.main.MovieListViewModel
import java.util.*

class MovieListViewModelTest {

  @Rule
  @JvmField
  val instantExecutorRule = InstantTaskExecutorRule() //Very very important

  @Mock
  private lateinit var dataProvider: DataProvider
  @Mock lateinit var observer: Observer<ResourceState<List<Movie>>>
  private lateinit var viewModel: MovieListViewModel

  @Before
  fun init() {
    MockitoAnnotations.initMocks(this)
    viewModel = MovieListViewModel(dataProvider)
      .apply { movieDiscover.observeForever(observer) }
  }

  private fun mockGetMovieDiscover(block: SingleEmitter<MovieDiscoveryResponse>.() -> Unit) {
    `when`(dataProvider.getMovieDiscover()).thenReturn(Single.create { e -> e.block() })
  }

  @Test
  fun fetchMovieDiscover_test_fail() {
    val errorMessage = "error message"
    mockGetMovieDiscover { onError(RuntimeException(errorMessage)) }

    viewModel.fetchMovieDiscover()

    verify(observer).onChanged(LoadingState())
    verify(observer).onChanged(ErrorState(errorMessage))
  }

  @Test
  fun fetchMovieDiscover_test_success_with_empty() {
    mockGetMovieDiscover { onSuccess(MovieDiscoveryResponse()) }

    viewModel.fetchMovieDiscover()

    verify(observer).onChanged(LoadingState())
    verify(observer).onChanged(EmptyState())
  }

  @Test
  fun fetchMovieDiscover_test_success_with_data() {
    val discoverResponse = MovieDiscoveryResponse(
      totalResults = Random().nextInt(999) + 1,
      movies = listOf(Movie())
    )
    mockGetMovieDiscover { onSuccess(discoverResponse) }

    viewModel.fetchMovieDiscover()

    verify(observer).onChanged(LoadingState())
    verify(observer).onChanged(PopulatedState(discoverResponse.movies!!))
  }
}
