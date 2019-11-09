package pl.oziem.datasource.models

import com.squareup.moshi.Json

/**
* Created by MarcinOz on 2018-03-07 WhatToWatch.
*/
data class SpokenLanguage(
  @Json(name = "iso_639_1") val iso: String,
  @Json(name = "name") val name: String
)
