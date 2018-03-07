package pl.oziem.datasource

import android.app.Activity
import io.reactivex.Completable
import io.reactivex.Single
import pl.oziem.datasource.models.Movie

/**
 * Created by MarcinOz on 2018-03-02.
 * Copyright (C) 2017 OKE Poland Sp. z o.o. All rights reserved.
 */
interface DataProvider {
  fun getMovieById(movieId: Int): Single<Movie>
  fun fetchRemoteConfig(activity: Activity): Completable
}
