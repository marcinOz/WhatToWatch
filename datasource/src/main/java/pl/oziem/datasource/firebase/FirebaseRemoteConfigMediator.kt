package pl.oziem.datasource.firebase

import android.app.Activity
import io.reactivex.Completable

/**
 * Created by MarcinOz on 2018-03-07.
 * Copyright (C) 2017 OKE Poland Sp. z o.o. All rights reserved.
 */
interface FirebaseRemoteConfigMediator {
  fun fetch(activity: Activity): Completable
  fun getTMDbApiKey(): String
}
