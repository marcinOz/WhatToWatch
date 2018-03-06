package pl.oziem.whattowatch.main

/**
 * Created by MarcinOz on 2018-03-06.
 * Copyright (C) 2017 OKE Poland Sp. z o.o. All rights reserved.
 */
interface MovieListContract {
  interface View {
    fun setText(result: String)
  }
  interface Presenter {
    fun getMovieById(movieId: Int)
  }
}