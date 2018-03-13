package pl.oziem.whattowatch.sharedpref

import pl.oziem.datasource.models.configuration.ImagesConfiguration

/**
 * Created by marcinoziem on 13/03/2018 WhatToWatch.
 */
interface SharedPreferenceMediator {
  fun saveImageConfiguration(imagesConfiguration: ImagesConfiguration)
  fun getImageBaseUrl(): String
  fun getImageSecureBaseUrl(): String
  fun getBackdropSizes(): List<String>
  fun getLogoSizes(): List<String>
  fun getPosterSizes(): List<String>
  fun getProfileSizes(): List<String>
  fun getStillSizes(): List<String>
}
