package pl.oziem.whattowatch

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import android.arch.paging.PagedList
import io.reactivex.Single
import io.reactivex.SingleEmitter
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.*
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
  @Mock
  lateinit var stateObserver: Observer<ResourceState>
  @Mock
  lateinit var pagingListObserver: Observer<PagedList<Movie>>

  @Before
  fun init() {
    MockitoAnnotations.initMocks(this)
  }

  private fun mockGetMovieDiscover(block: SingleEmitter<MovieDiscoveryResponse>.() -> Unit) {
    `when`(dataProvider.getMovieDiscover(ArgumentMatchers.anyInt())).thenReturn(Single.create { e -> e.block() })
  }

  private fun initializeViewModel() = MovieListViewModel(dataProvider)
    .apply {
      getLoadState().observeForever(stateObserver)
      pagedListData.observeForever(pagingListObserver)
    }

  @Test
  fun fetchMovieDiscover_test_fail() {
    val errorMessage = "error message"
    mockGetMovieDiscover { onError(RuntimeException(errorMessage)) }

    initializeViewModel()

    verify(stateObserver).onChanged(LoadingState)
    verify(stateObserver).onChanged(ErrorState(errorMessage))
  }

  @Test
  fun fetchMovieDiscover_test_success_with_empty() {
    mockGetMovieDiscover { onSuccess(MovieDiscoveryResponse()) }

    initializeViewModel()

    verify(stateObserver).onChanged(LoadingState)
    verify(stateObserver).onChanged(EmptyState)
  }

  @Test
  fun fetchMovieDiscover_test_success_with_data() {
    val discoverResponse = MovieDiscoveryResponse(
      totalResults = Random().nextInt(999) + 1,
      movies = listOf(Movie())
    )
    mockGetMovieDiscover { onSuccess(discoverResponse) }

    initializeViewModel()

    verify(stateObserver).onChanged(LoadingState)
    verify(stateObserver).onChanged(PopulatedState)
  }

  @Test
  fun fetchMovieDiscover_test_next_page_success_with_data() {
    val discoverResponse = MovieDiscoveryResponse(
      totalResults = Random().nextInt(999) + 1,
      movies = listOf(Movie())
    )
    mockGetMovieDiscover { onSuccess(discoverResponse) }

    var pagedList: PagedList<Movie>? = null
    MovieListViewModel(dataProvider)
      .apply {
        getLoadState().observeForever(stateObserver)
        pagedListData.observeForever { pagedList = it }
      }

    pagedList?.loadAround(1)

    verify(stateObserver, times(1 + MovieListViewModel.PAGE_SIZE))
      .onChanged(LoadingState)
    verify(stateObserver, times(1 + MovieListViewModel.PAGE_SIZE))
      .onChanged(PopulatedState)
  }

  @Test
  fun fetchMovieDiscover_test_next_page_success_with_empty() {
    val discoverResponse = MovieDiscoveryResponse(
      totalResults = Random().nextInt(999) + 1,
      movies = listOf(Movie())
    )
    mockGetMovieDiscover { onSuccess(discoverResponse) }

    var pagedList: PagedList<Movie>? = null
    MovieListViewModel(dataProvider)
      .apply {
        getLoadState().observeForever(stateObserver)
        pagedListData.observeForever { pagedList = it }
      }

    mockGetMovieDiscover { onSuccess(MovieDiscoveryResponse()) }

    pagedList?.loadAround(1)

    verify(stateObserver, times(2)).onChanged(LoadingState)
    verify(stateObserver).onChanged(PopulatedState)
    verify(stateObserver).onChanged(EmptyState)
  }
}
