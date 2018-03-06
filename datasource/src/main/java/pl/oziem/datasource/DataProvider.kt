package pl.oziem.datasource

import io.reactivex.Single

/**
 * Created by MarcinOz on 2018-03-02.
 * Copyright (C) 2017 OKE Poland Sp. z o.o. All rights reserved.
 */
interface DataProvider {
  fun getMovieById(movieId: Int): Single<String>
}
