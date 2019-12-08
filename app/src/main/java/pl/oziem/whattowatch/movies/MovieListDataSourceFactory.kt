package pl.oziem.whattowatch.movies

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import io.reactivex.disposables.CompositeDisposable
import pl.oziem.datasource.dataprovider.DataProvider
import pl.oziem.datasource.models.movie.Movie

class MovieListDataSourceFactory(private val dataProvider: DataProvider,
                                 private val compositeDisposable: CompositeDisposable)
  : DataSource.Factory<Int, Movie>() {

  val movieListDataSourceLiveData = MutableLiveData<MovieListDataSource>()

  override fun create(): DataSource<Int, Movie> {
    val movieListDataSource = MovieListDataSource(dataProvider, compositeDisposable)
    movieListDataSourceLiveData.postValue(movieListDataSource)
    return movieListDataSource
  }
}
