package pl.oziem.datasource

import android.content.Context
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

/** Created by marcinoziem on 14/03/2018 WhatToWatch.
 */
object OkHttpInterceptors {

  fun loggingInterceptor() = HttpLoggingInterceptor().apply {
    level = if (BuildConfig.DEBUG)
      HttpLoggingInterceptor.Level.BODY
    else
      HttpLoggingInterceptor.Level.NONE
  }

  fun rewriteCacheControlInterceptor(context: Context) = Interceptor {
    val originalResponse = it.proceed(it.request())
    if (NetworkUtils.isNetworkAvailable(context)) {
      val maxAge = TimeUnit.MINUTES.toSeconds(10) // read from cache for 10 minute
      originalResponse.newBuilder()
        .header("Cache-Control", "public, max-age=$maxAge")
        .build()
    } else {
      val maxStale = TimeUnit.DAYS.toSeconds(7) // tolerate 1-weeks stale
      originalResponse.newBuilder()
        .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
        .build()
    }
  }
}
