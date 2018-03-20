package pl.oziem.datasource.remote_config

import android.app.Activity
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import io.reactivex.Completable
import pl.oziem.datasource.BuildConfig
import pl.oziem.datasource.R
import java.util.concurrent.TimeUnit


/** Created by Marcin Oziemski on 06.03.2018 WhatToWatch.
 */
class FirebaseRemoteConfigMediatorImp : FirebaseRemoteConfigMediator {

  companion object {
    const val TMDB_API_KEY = "themoviedb_api_key"
    val CACHE_EXPIRATION_SEC = TimeUnit.DAYS.toSeconds(1)
  }

  private val firebaseRemoteConfig: FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()

  init {
    val configSettings = FirebaseRemoteConfigSettings.Builder()
      .setDeveloperModeEnabled(BuildConfig.DEBUG)
      .build()
    firebaseRemoteConfig.setConfigSettings(configSettings)
  }

  override fun fetch(activity: Activity): Completable =
    Completable.create { emitter ->
      firebaseRemoteConfig.fetch(CACHE_EXPIRATION_SEC)
        .addOnCompleteListener(activity) { task ->
          if (task.isSuccessful) {
            // After config data is successfully fetched, it must be activated before newly fetched
            // values are returned.
            firebaseRemoteConfig.activateFetched()
            if (getTMDbApiKey().isNotEmpty()) {
              emitter.onComplete()
              return@addOnCompleteListener
            }
          }
          emitter.onError(RuntimeException(activity.getString(R.string.remote_config_fetch_failed)))
        }
    }

  override fun getTMDbApiKey(): String = firebaseRemoteConfig.getString(TMDB_API_KEY)
}
