package pl.oziem.datasource.models.configuration

import com.squareup.moshi.Json

/** Created by marcinoziem on 11/03/2018 WhatToWatch.
 */
data class ImagesConfiguration(
  @Json(name = "base_url") val baseUrl: String = "",
  @Json(name = "secure_base_url") val secureBaseUrl: String = "",
  @Json(name = "backdrop_sizes") val backdropSizes: List<String> = listOf(),
  @Json(name = "logo_sizes") val logoSizes: List<String> = listOf(),
  @Json(name = "poster_sizes") val posterSizes: List<String> = listOf(),
  @Json(name = "profile_sizes") val profileSizes: List<String> = listOf(),
  @Json(name = "still_sizes") val stillSizes: List<String> = listOf()
)
