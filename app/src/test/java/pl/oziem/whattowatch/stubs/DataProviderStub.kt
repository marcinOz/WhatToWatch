package pl.oziem.whattowatch.stubs

import android.app.Activity
import io.reactivex.Completable
import io.reactivex.Single
import pl.oziem.datasource.DataProvider
import pl.oziem.datasource.models.MovieDetails
import pl.oziem.datasource.models.MovieDiscoveryResponse

/**
 * Created by marcinoziem on 09/03/2018.
 */
class DataProviderStub: DataProvider {

  var getMovieDetailsById: Single<MovieDetails>? = null
  override fun getMovieDetailsById(movieId: Int): Single<MovieDetails> = getMovieDetailsById!!

  var fetchRemoteConfig: Completable? = null
  override fun fetchRemoteConfig(activity: Activity): Completable = fetchRemoteConfig!!

  var getMovieDiscover: Single<MovieDiscoveryResponse>? = null
  override fun getMovieDiscover(): Single<MovieDiscoveryResponse> = getMovieDiscover!!
}
