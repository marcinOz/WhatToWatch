package pl.oziem.datasource.models.configuration

import com.google.gson.annotations.SerializedName

/**
* Created by marcinoziem on 11/03/2018 WhatToWatch.
*/
data class ImagesConfiguration(
  @SerializedName("base_url") val baseUrl: String,
  @SerializedName("secure_base_url") val secureBaseUrl: String,
  @SerializedName("backdrop_sizes") val backdropSizes: List<String>,
  @SerializedName("logo_sizes") val logoSizes: List<String>,
  @SerializedName("poster_sizes") val posterSizes: List<String>,
  @SerializedName("profile_sizes") val profileSizes: List<String>,
  @SerializedName("still_sizes") val stillSizes: List<String>
  )
