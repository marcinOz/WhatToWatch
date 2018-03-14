package pl.oziem.datasource.remote_config

import android.app.Activity
import io.reactivex.Completable

/**
* Created by MarcinOz on 2018-03-07 WhatToWatch.
*/
interface FirebaseRemoteConfigMediator {
  fun fetch(activity: Activity): Completable
  fun getTMDbApiKey(): String
}
