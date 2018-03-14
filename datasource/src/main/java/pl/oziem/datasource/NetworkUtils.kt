package pl.oziem.datasource

import android.content.Context
import android.net.ConnectivityManager

/** Created by marcinoziem on 14/03/2018 WhatToWatch.
 */
object NetworkUtils {
  fun isNetworkAvailable(context: Context) =
    (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).let {
      val activeNetworkInfo = it.activeNetworkInfo
      activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}
