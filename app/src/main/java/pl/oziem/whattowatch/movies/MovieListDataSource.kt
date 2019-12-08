package pl.oziem.whattowatch.movies

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import pl.oziem.datasource.dataprovider.DataProvider
import pl.oziem.datasource.models.*
import pl.oziem.datasource.models.movie.Movie

class MovieListDataSource(private val dataProvider: DataProvider,
                          private val compositeDisposable: CompositeDisposable)
  : PageKeyedDataSource<Int, Movie>() {

  val movieDiscover = MutableLiveData<ResourceState>()

  override fun loadInitial(
    params: LoadInitialParams<Int>,
    callback: LoadInitialCallback<Int, Movie>
  ) {
    getMovieDiscover(INITIAL_PAGE) {
      callback.onResult(it, null, INITIAL_PAGE + 1)
    }
  }

  override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
    val after =
      if (params.key > MAX_PAGES) null
      else params.key + 1
    getMovieDiscover(params.key) { callback.onResult(it, after) }
  }

  override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
    val before =
      if (params.key == INITIAL_PAGE) null
      else params.key - 1
    getMovieDiscover(params.key) { callback.onResult(it, before) }
  }

  private fun getMovieDiscover(page: Int, callback: (List<Movie>) -> Unit) {
    movieDiscover.postValue(LoadingState)
    compositeDisposable.add(
      dataProvider.getMovieDiscover(page)
        .subscribeBy(
          onSuccess = { result ->
            if (result.totalResults == 0) {
              movieDiscover.postValue(EmptyState)
            } else {
              result.movies?.apply {
                movieDiscover.postValue(PopulatedState)
                callback(this)
              }
            }
          },
          onError = { error -> movieDiscover.postValue(ErrorState(error.message)) }
        )
    )
  }

  companion object {
    private const val INITIAL_PAGE = 1
    private const val MAX_PAGES = 1000
  }
}
