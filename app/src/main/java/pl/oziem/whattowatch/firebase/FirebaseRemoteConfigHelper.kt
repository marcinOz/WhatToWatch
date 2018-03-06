package pl.oziem.whattowatch.firebase

import android.app.Activity
import android.widget.Toast
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import io.reactivex.Completable
import pl.oziem.whattowatch.BuildConfig
import java.util.concurrent.TimeUnit


/**
 * Created by Marcin Oziemski on 06.03.2018.
 * Copyright (C) 2017 OKE Poland Sp. z o.o. All rights reserved.
 */
class FirebaseRemoteConfigHelper {

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

  fun fetch(activity: Activity): Completable =
    Completable.create { emitter ->
      firebaseRemoteConfig.fetch(CACHE_EXPIRATION_SEC)
        .addOnCompleteListener(activity) { task ->
          if (task.isSuccessful) {
            Toast.makeText(activity, "Fetch Succeeded", Toast.LENGTH_SHORT).show()

            // After config data is successfully fetched, it must be activated before newly fetched
            // values are returned.
            firebaseRemoteConfig.activateFetched()
            emitter.onComplete()
          } else {
            Toast.makeText(activity, "Fetch Failed", Toast.LENGTH_SHORT).show()
            emitter.onError(RuntimeException("Firebase RemoteConfig Fetch Failed"))
          }
        }
    }

  fun getTMDbApiKey(): String = firebaseRemoteConfig.getString(TMDB_API_KEY, "")
}
