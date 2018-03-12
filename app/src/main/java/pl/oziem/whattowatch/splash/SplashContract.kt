package pl.oziem.whattowatch.splash

import android.app.Activity

/** Created by marcinoziem on 12/03/2018 WhatToWatch.
 */
interface SplashContract {
  interface View {
    fun onDataFetched()
    fun showError(message: String?)
  }
  interface Presenter {
    fun fetchData(activity: Activity)
  }
}
