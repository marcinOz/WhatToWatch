package pl.oziem.whattowatch.sharedpref

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import pl.oziem.datasource.models.configuration.ImagesConfiguration

/** Created by marcinoziem on 12/03/2018 WhatToWatch.
 */
class SharedPreferenceMediatorImp(val context: Context) : SharedPreferenceMediator {
  companion object {
    private const val KEY = "pl.oziem.whattowatch.key"
    private const val IMAGE_BASE_URL = "IMAGE_BASE_URL"
    private const val IMAGE_SECURE_BASE_URL = "IMAGE_SECURE_BASE_URL"
    private const val BACKDROP_SIZES = "BACKDROP_SIZES"
    private const val LOGO_SIZES = "LOGO_SIZES"
    private const val POSTER_SIZES = "POSTER_SIZES"
    private const val PROFILE_SIZES = "PROFILE_SIZES"
    private const val STILL_SIZES = "STILL_SIZES"
  }

  private fun getSharedPref() = context.getSharedPreferences(KEY, Context.MODE_PRIVATE)

  @SuppressLint("CommitPrefEdits")
  private inline fun applySharedPrefChanges(action: SharedPreferences.Editor.() -> Unit) {
    with(getSharedPref().edit()) {
      action()
      apply()
    }
  }

  override fun saveImageConfiguration(imagesConfiguration: ImagesConfiguration) =
    applySharedPrefChanges {
      putString(IMAGE_BASE_URL, imagesConfiguration.baseUrl)
      putString(IMAGE_SECURE_BASE_URL, imagesConfiguration.secureBaseUrl)
      putStringSet(BACKDROP_SIZES, imagesConfiguration.backdropSizes.toSet())
      putStringSet(LOGO_SIZES, imagesConfiguration.logoSizes.toSet())
      putStringSet(POSTER_SIZES, imagesConfiguration.posterSizes.toSet())
      putStringSet(PROFILE_SIZES, imagesConfiguration.profileSizes.toSet())
      putStringSet(STILL_SIZES, imagesConfiguration.stillSizes.toSet())
    }

  override fun getImageBaseUrl(): String = getSharedPref().getString(IMAGE_BASE_URL, "")
  override fun getImageSecureBaseUrl(): String = getSharedPref().getString(IMAGE_SECURE_BASE_URL, "")
  override fun getBackdropSizes() = getSharedPref().getStringSet(BACKDROP_SIZES, mutableSetOf()).toList()
  override fun getLogoSizes() = getSharedPref().getStringSet(LOGO_SIZES, mutableSetOf()).toList()
  override fun getPosterSizes() = getSharedPref().getStringSet(POSTER_SIZES, mutableSetOf()).toList()
  override fun getProfileSizes() = getSharedPref().getStringSet(PROFILE_SIZES, mutableSetOf()).toList()
  override fun getStillSizes() = getSharedPref().getStringSet(STILL_SIZES, mutableSetOf()).toList()
}
