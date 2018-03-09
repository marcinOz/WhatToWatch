package pl.oziem.whattowatch.main

import android.app.Activity
import pl.oziem.datasource.models.MovieDiscoveryResponse

/**
 * Created by MarcinOz on 2018-03-06.
 * Copyright (C) 2017 OKE Poland Sp. z o.o. All rights reserved.
 */
interface MovieListContract {
  interface View {
    fun showLoading(show: Boolean = true)
    fun showError(message: String)
    fun populate(movieDiscoveryResponse: MovieDiscoveryResponse)
    fun showEmptyMessage()
    fun setText(result: String)
  }
  interface Presenter {
    fun getMovieDiscover()
    fun initDownloadData(activity: Activity)
  }
}
