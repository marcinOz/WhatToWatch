package pl.oziem.whattowatch.main

import android.app.Activity
import android.os.Bundle
import pl.oziem.datasource.models.Movie

/**
* Created by MarcinOz on 2018-03-06 WhatToWatch.
*/
interface MovieListContract {
  interface View {
    fun showLoading(show: Boolean = true)
    fun showError(message: String? = null)
    fun populate(movies: List<Movie>)
    fun showEmptyMessage()
  }
  interface Presenter {
    fun saveInstanceState(bundle: Bundle?): Bundle?
    fun readSavedInstanceState(bundle: Bundle?): List<Movie>
    fun getMovieDiscover()
    fun initDownloadData(activity: Activity)
  }
}
